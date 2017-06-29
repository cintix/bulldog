package dk.tv2.bulldog.backend.io;

import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent.Kind;

/**
 *
 * @author migo
 */
public enum Actions {

    CREATED(1),
    UPDATED(2),
    DELTETED(4);

    private final int value;

    private Actions(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static boolean contains(int actions, Actions action) {
        return (actions & action.getValue()) == action.getValue();
    }

    public static int getActions(Actions... actions) {
        int allActionsValue = 0;
        for (Actions action : actions) {
            allActionsValue = allActionsValue | action.value;
        }
        return allActionsValue;
    }

    public Kind actionKind() {
        if (value == 1) {
            return StandardWatchEventKinds.ENTRY_CREATE;
        }
        if (value == 2) {
            return StandardWatchEventKinds.ENTRY_MODIFY;
        }
        if (value == 4) {
            return StandardWatchEventKinds.ENTRY_DELETE;
        }

        return StandardWatchEventKinds.OVERFLOW;
    }

}
