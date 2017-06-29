/*
 */
package dk.tv2.bulldog.backend.db.managers;

import dk.tv2.bulldog.backend.db.EntityManager;
import dk.tv2.bulldog.backend.db.annotations.InjectConnection;
import dk.tv2.bulldog.backend.db.entities.Client;
import dk.tv2.bulldog.backend.io.Configuration;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author migo
 */
public class ClientManager extends Client {

    private static final Logger logger = Logger.getLogger(ClientManager.class.getName());
    private final DataSource dataSource = DataSourceManager.getInstance(Configuration.getProperty("datasource"));

    @InjectConnection
    private final Connection cachedConnection = null;

    private final static String SELECT_ALL_CLIENTS = "SELECT * FROM client".intern();
    private final static String SELECT_CLIENT = "SELECT * FROM client where id = ? ".intern();
    private final static String UPDATE_CLIENT = "UPDATE client set name = ? , description = ? where id = ? ".intern();
    private final static String DELETE_CLIENT = "DELETE FROM client where id = ? ".intern();
    private final static String INSERT_CLIENT = "INSERT INTO client (name, description) values (?,?) RETURNING id".intern();

    @Override
    public List<Client> loadAll() {
        List<Client> clients = new LinkedList<>();
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CLIENTS);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                clients.add(readEntity(resultSet));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientManager.loadAll() threw an exception", e);

        }
        return clients;
    }

    @Override
    public boolean create() {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLIENT);
            preparedStatement.setString(1, getName());
            preparedStatement.setString(2, getDescription());

            boolean useGeneratedKeys = false;
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet == null) {
                resultSet = preparedStatement.getGeneratedKeys();
                useGeneratedKeys = true;
            }

            if (resultSet != null && resultSet.next()) {
                if (useGeneratedKeys) {
                    setId(resultSet.getInt(1));
                } else {
                    setId(resultSet.getInt("id"));
                }

                setCreated(new Date());
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            if (!e.getMessage().contains("duplicate key")) {
                logger.log(Level.SEVERE, "ClientManager.create() threw an exception", e);
            }

        }
        return false;
    }

    @Override
    public boolean update() {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT);
            preparedStatement.setString(1, getName());
            preparedStatement.setString(2, getDescription());
            preparedStatement.setInt(3, getId());
            int resultCount = preparedStatement.executeUpdate();

            return resultCount > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientManager.update() threw an exception", e);

        }
        return false;
    }

    @Override
    public boolean delete() {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CLIENT);
            preparedStatement.setInt(1, getId());
            int resultCount = preparedStatement.executeUpdate();

            return resultCount > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientManager.delete() threw an exception", e);

        }
        return false;
    }

    @Override
    public Client load() {
        return load(getId());
    }

    @Override
    public Client load(int id) {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CLIENT);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return readEntity(resultSet);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientManager.load() threw an exception", e);

        }
        return null;
    }

    private Client readEntity(ResultSet resultSet) throws SQLException {
        Client client = EntityManager.create(Client.class);
        client.setId(resultSet.getInt("id"));
        client.setName(resultSet.getString("name"));
        client.setDescription(resultSet.getString("description"));
        client.setCreated(new Date(resultSet.getTimestamp("created_at").getTime()));
        return client;
    }

}
