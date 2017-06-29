package dk.tv2.bulldog.backend.db;

import java.io.PrintWriter;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author migo
 */
public class DatasourceMock implements DataSource {

    private static Connection connection = null;

    public DatasourceMock() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return getLocalConnection();
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return getLocalConnection();
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getLoginTimeout() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     *
     * @return
     */
    private Connection getLocalConnection() throws SQLException {

        if (connection == null) {
            connection = new Connection(DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/teletext?user=teletextuser&password=teletext"));
        } else if (!connection.isIsFree() || connection.isClosed()) {
            connection = new Connection(DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/teletext?user=teletextuser&password=teletext"));
        }
        return connection;
    }

}
