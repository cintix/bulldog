/*
 */
package dk.tv2.bulldog.backend.db.managers;

import dk.tv2.bulldog.backend.db.EntityManager;
import dk.tv2.bulldog.backend.db.annotations.InjectConnection;
import dk.tv2.bulldog.backend.db.entities.ClientResponse;
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
public class ClientResponseManager extends ClientResponse {

    private static final Logger logger = Logger.getLogger(ClientResponseManager.class.getName());
    private final DataSource dataSource = DataSourceManager.getInstance(Configuration.getProperty("datasource"));

    @InjectConnection
    private final Connection cachedConnection = null;

    private final static String SELECT_ALL_CLIENTS = "SELECT * FROM response".intern();
    private final static String SELECT_CLIENT = "SELECT * FROM response where client_id = ? ".intern();
    private final static String DELETE_CLIENT = "DELETE FROM client_response where now() < (created_at::timestamp + interval '7 day');".intern();
    private final static String INSERT_CLIENT = "INSERT INTO client_response (client_mapping_id, client_id, filename, created_at, response_code, response) values (?,?,?, NOW(), ?, ?)".intern();

    @Override
    public List<ClientResponse> loadAll() {
        List<ClientResponse> clientResponses = new LinkedList<>();
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_CLIENTS);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                clientResponses.add(readEntity(resultSet));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientResponseManager.loadAll() threw an exception", e);

        }
        return clientResponses;
    }

    @Override
    public List<ClientResponse> loadAllFromClient(int clientId) {
        List<ClientResponse> clientResponses = new LinkedList<>();
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(SELECT_CLIENT);
            preparedStatement.setInt(1, clientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                clientResponses.add(readEntity(resultSet));
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientResponseManager.loadAllFromClient() threw an exception", e);

        }
        return clientResponses;
    }

    @Override
    public boolean create() {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CLIENT);
            preparedStatement.setInt(1, getClientMappingId());
            preparedStatement.setInt(2, getClientId());
            preparedStatement.setString(3, getFilename());
            preparedStatement.setInt(4, getResponseCode());
            preparedStatement.setString(5, getResponse());

            int executeUpdate = preparedStatement.executeUpdate();
            setCreated(new Date());
            return executeUpdate > 0;

        } catch (SQLException e) {
            if (!e.getMessage().contains("duplicate key")) {
                logger.log(Level.SEVERE, "ClientResponseManager.create() threw an exception", e);
            }

        }
        return false;
    }

    @Override
    public boolean delete() {
        try (Connection connection = (cachedConnection != null && !cachedConnection.isClosed()) ? cachedConnection : dataSource.getConnection()) {

            PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CLIENT);
            int resultCount = preparedStatement.executeUpdate();

            return resultCount > 0;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "ClientResponseManager.delete() threw an exception", e);

        }
        return false;
    }

    private ClientResponse readEntity(ResultSet resultSet) throws SQLException {
        ClientResponse clientResponse = EntityManager.create(ClientResponse.class);
        clientResponse.setClientMappingId(resultSet.getInt("mapping_id"));
        clientResponse.setClientId(resultSet.getInt("client_id"));
        clientResponse.setResponseCode(resultSet.getInt("response_code"));
        clientResponse.setResponse(resultSet.getString("response"));
        clientResponse.setClientName(resultSet.getString("client_name"));
        clientResponse.setMappingName(resultSet.getString("mapping_name"));
        clientResponse.setCreated(new Date(resultSet.getTimestamp("created_at").getTime()));
        return clientResponse;
    }

}
