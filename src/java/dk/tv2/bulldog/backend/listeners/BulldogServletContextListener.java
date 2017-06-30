/*
 */
package dk.tv2.bulldog.backend.listeners;

import dk.tv2.bulldog.backend.threads.WatcherThread;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author migo
 */
public class BulldogServletContextListener implements ServletContextListener {

    private WatcherThread watcherThread;
    

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("Registering layouts and imports classes");
        dk.tv2.bulldog.backend.io.Configuration.initilizeContext(sce.getServletContext());

        System.out.println("Trying to start bulldog-backend [Watcher - THREAD]");
        watcherThread = new WatcherThread();
        watcherThread.setName("bulldog-backend - Watcher");
        //watcherThread.start();        

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("Stopping bulldog-backend");
        if (watcherThread != null) {
            try {
                System.out.println("Trying to close bulldog-backend [Watcher - THREAD]");
                watcherThread.setRunning(false);
                watcherThread.interrupt();
            } catch (Exception e) {
            }
        }

    }
}
