package dk.tv2.bulldog.backend.db.managers;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 *
 * @author migo
 */
public class DataSourceManager {

    private static final Logger logger = Logger.getLogger(DataSourceManager.class.getName());

    private static Map<String, DataSource> dataSources;
    private static final String CONTEXT_LOOKUP = "java:comp/env/";

    /**
     * Get a Instance of a DataSource
     *
     * @param name
     *
     * @return {@link javax.sql.DataSource}
     */
    public static DataSource getInstance(String name) {
        if (dataSources == null) {
            dataSources = new HashMap<>();
        }

        if (dataSources.containsKey(name)) {
            return dataSources.get(name);
        } else {
            try {
                Context ctx = new InitialContext();
                dataSources.put(name, (DataSource) ctx.lookup(CONTEXT_LOOKUP + name));
            } catch (NamingException ex) {
                logger.log(Level.SEVERE, MessageFormat.format("getInstance() Failed on {0}", name));
                ex.printStackTrace();
            }
        }
        return dataSources.get(name);
    }

    /**
     * Add a DataSource to the Manager
     *
     * @param name name of the datasource
     * @param ds   {@link javax.sql.DataSource}
     */
    public static void addDataSource(String name, DataSource ds) {
        if (dataSources == null) {
            dataSources = new HashMap<>();
        }
        dataSources.put(name, ds);
    }

    /**
     * Remove a datasource from the manager
     * @param name 
     */
    public static void removeDataSource(String name) {
        if (dataSources != null && dataSources.containsKey(name)) {
            dataSources.remove(name);
        }
    }

}
