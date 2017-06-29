package dk.tv2.bulldog.backend.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

/**
 * @author hjep
 */
public class Configuration {

    private static Properties prop;

    public static void initilizeTest() {
        prop = new Properties();
    }
    
    public static void initilizeContext(ServletContext context) {
        prop = new Properties();
        String propFileName = context.getRealPath("/WEB-INF/bulldog.properties");
        try (InputStream resourceAsStream = new FileInputStream(propFileName)) {
            if (resourceAsStream != null) {
                prop.load(resourceAsStream);
            } else {
                throw new FileNotFoundException("Property file '" + propFileName + "' not found in the classpath");
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * 
     * @param key
     * @return 
     */
    public static boolean contains(String key) {
        return prop.contains(key);
    }

    /**
     *
     * @param key
     * @return
     */
    public static String getProperty(String key) {
        return prop.getProperty(key);
    }

    /**
     *
     * @param key
     * @return
     */
    public static void setProperty(String key, String value) {
        prop.setProperty(key, value);
    }

    public static boolean isUserSignedIn(HttpServletRequest request) {
        return true;
    }

}
