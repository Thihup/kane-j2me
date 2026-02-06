package javax.microedition.media;

public interface Player extends Controllable {
    // State constants
    int UNREALIZED = 100;
    int REALIZED = 200;
    int PREFETCHED = 300;
    int STARTED = 400;
    int CLOSED = 0;

    // Standard lifecycle methods
    void realize() throws MediaException;
    void prefetch() throws MediaException;
    void start() throws MediaException;
    void stop() throws MediaException;
    void deallocate();
    void close();

    // Playback control
    long setMediaTime(long now) throws MediaException;
    long getMediaTime();
    void setLoopCount(int count);
    int getState();
    
    // Listeners (can be empty for now)
    void addPlayerListener(PlayerListener playerListener);
    void removePlayerListener(PlayerListener playerListener);
}
