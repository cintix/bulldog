/*
 */
package dk.tv2.bulldog.backend.db.managers;

import dk.tv2.bulldog.backend.db.EntityManager;
import dk.tv2.bulldog.backend.db.annotations.InjectConnection;
import dk.tv2.bulldog.backend.db.entities.ClientMapping;
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
public class ClientMappingManager extends ClientMapping {

    private static final Logger logger = Logger.getLogger(ClientMappingManager.class.getName());
    private final DataSource dataSource = DataSourceManager.getInstance(Configuration.getProperty("datasource"));

    @InjectConnection
    private final Connection cachedConnection = null;

    private final static String SELECT_ALL_CLIENT_MAPPING_MAPPING = "SELECT * FROM client_mapping".intern();
    private final static String SELECT_CLIENT_MAPPING = "SELECT * FROM client_mapping where id = ? ".intern();
    private final static String UPDATE_CLIENT_MAPPING = "UPDATE client_mapping set actions = ?, name = ? , path = ? , url = ?, updated_at = now() pattern = ? where id = ? ".intern();
    private final static String DELETE_CLIENT_MAPPING = "DELETE FROM client_mapping where id = ? ".intern();
    private final static String DELETE_ALL_CLINET_CLIENT_MAPPING = "DELETE FROM client_mapping where client_id = ? ".intern();
    private final static String INSERT_CLIENT_MAPPING = "INSERT INTO client_mapping (client_id, actions, name, path, url, pattern) values (?,?,?,?,?,?) RETURNING id".intern();

    @Override
    public List<ClientMapping> loadAll() {
        List<ClientMapping> clients = new LinkedList<>();
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CLIENT_MAPPING_MAPPING);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                clients.add(readEntity(resultSet));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientMappingManager.loadAll() threw an exception", e);

        }
        return clients;
    }

    @Override
    public boolean create() {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLIENT_MAPPING);
            preparedStatement.setInt(1, getClientId());
            preparedStatement.setInt(2, getActions());
            preparedStatement.setString(3, getName());
            preparedStatement.setString(4, getPath());
            preparedStatement.setString(5, getUrl());
            preparedStatement.setString(6, getPattern());
           
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
                setUpdated(new Date());
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientMappingManager.create() threw an exception", e);

        }
        return false;
    }

    @Override
    public boolean update() {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CLIENT_MAPPING);
            preparedStatement.setInt(1, getActions());
            preparedStatement.setString(2, getName());
            preparedStatement.setString(3, getPath());
            preparedStatement.setString(4, getUrl());
            preparedStatement.setString(5, getPattern());
            int resultCount = preparedStatement.executeUpdate();

            return resultCount > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientMappingManager.update() threw an exception", e);

        }
        return false;
    }

    @Override
    public boolean delete() {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CLIENT_MAPPING);
            preparedStatement.setInt(1, getId());
            int resultCount = preparedStatement.executeUpdate();

            return resultCount > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientMappingManager.delete() threw an exception", e);

        }
        return false;
    }

    @Override
    public boolean deleteAllFromClient(int clientId) {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_ALL_CLINET_CLIENT_MAPPING);
            preparedStatement.setInt(1, clientId);
            int resultCount = preparedStatement.executeUpdate();

            return resultCount > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientMappingManager.deleteAllFromClient(" + clientId + ") threw an exception", e);

        }
        return false;

    }

    @Override
    public ClientMapping load() {
        return load(getId());
    }

    @Override
    public ClientMapping load(int id) {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CLIENT_MAPPING);
            preparedStatement.setInt(0, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                return readEntity(resultSet);
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientMappingManager.load() threw an exception", e);

        }
        return null;
    }

    private ClientMapping readEntity(ResultSet resultSet) throws SQLException {
        ClientMapping clientMapping = EntityManager.create(ClientMapping.class);
        clientMapping.setId(resultSet.getInt("id"));
        clientMapping.setClientId(resultSet.getInt("client_id"));
        clientMapping.setActions(resultSet.getInt("actions"));
        clientMapping.setName(resultSet.getString("name"));
        clientMapping.setPattern(resultSet.getString("pattern"));
        clientMapping.setPath(resultSet.getString("path"));
        clientMapping.setUrl(resultSet.getString("url"));
        clientMapping.setCreated(new Date(resultSet.getTimestamp("created_at").getTime()));
        clientMapping.setUpdated(new Date(resultSet.getTimestamp("updated_at").getTime()));
        return clientMapping;
    }

}
