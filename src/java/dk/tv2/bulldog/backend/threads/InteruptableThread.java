/*
 */
package dk.tv2.bulldog.backend.threads;

/**
 *
 * @author migo
 */
public abstract class InteruptableThread extends Thread {
    
    private volatile boolean running = true;

    public final boolean isRunning() {
        return running;
    }

    public final void setRunning(boolean running) {
        this.running = running;
    }
    
}