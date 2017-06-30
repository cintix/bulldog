package dk.tv2.bulldog.backend.threads;

import dk.tv2.bulldog.backend.db.EntityManager;
import dk.tv2.bulldog.backend.db.entities.ClientMapping;
import dk.tv2.bulldog.backend.io.Actions;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author migo
 */
public class WatcherThread extends InteruptableThread {

    private static WatchService watcher;
    private static Map<WatchKey, ClientMapping> keys;

    public WatcherThread() {
        keys = new LinkedHashMap<>();
        try {
            watcher = FileSystems.getDefault().newWatchService();
            
            List<ClientMapping> clientMappings = EntityManager.create(ClientMapping.class).loadAll();
            for (ClientMapping cm : clientMappings) {
                register(cm);
            }
            
        } catch (IOException ex) {
            Logger.getLogger(WatcherThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void watch() {
        for (;;) {

            // wait for key to be signaled
            WatchKey key;
            try {
                key = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            ClientMapping mapping = keys.get(key);
            Path dir = new File(mapping.getPath()).toPath();

            if (dir == null) {
                System.err.println("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                WatchEvent.Kind<?> kind = event.kind();

                if (kind == OVERFLOW) {
                    continue;
                }

                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path filename = ev.context();
                Path localFileAndPath = dir.resolve(filename);

                if (kind == ENTRY_MODIFY) {
                    System.out.println("MODIFICATION of " + localFileAndPath.toString());
                }

                /**
                 * LOGIC -
                 */
                int actions = mapping.getActions();
                
                if (Actions.contains(actions, Actions.CREATED) && kind == ENTRY_CREATE) {
                    System.out.println("CREATION of " + localFileAndPath.toString());
                    
                    /**
                     * if localFileAndPath.name matches pattern then post
                     */
                    
                    
                    // Actions.CREATED.name();                    
                    // HTTP POST
                    
                }
                
                
                if (Actions.contains(actions, Actions.UPDATED) && kind == ENTRY_MODIFY) {
                    System.out.println("MODIFICATION of " + localFileAndPath.toString());
                    // Actions.UPDATED.name();                    
                    // HTTP PUT
                    
                }

            }

            boolean valid = key.reset();
            if (!valid) {
                break;
            }

            if (!isRunning()) {
                break;
            }

        }
    }

    public static void remove(ClientMapping mapping) throws IOException {
        for (WatchKey key : keys.keySet()) {
            ClientMapping clientMapping = keys.get(key);
            if (clientMapping.getId() == mapping.getId()) {
                key.cancel();
            }
            keys.remove(key);
        }

    }

    /**
     * Register the given directory with the WatchService
     */
    public static void register(ClientMapping mapping) throws IOException {

        for (WatchKey key : keys.keySet()) {
            ClientMapping clientMapping = keys.get(key);
            if (clientMapping.getId() == mapping.getId()) {
                key.cancel();
            }
            keys.remove(key);
        }

        Path dir = new File(mapping.getPath()).toPath();
        List<Kind> registeredActions = new LinkedList<>();

        int mappingActions = mapping.getActions();
        for (Actions action : Actions.values()) {
            if (Actions.contains(mappingActions, action)) {
                registeredActions.add(action.actionKind());
            }
        }

        Kind[] arrayActions = new Kind[registeredActions.size()];
        arrayActions = registeredActions.toArray(arrayActions);
        WatchKey key = dir.register(watcher, arrayActions);

        keys.put(key, mapping);
    }

}
