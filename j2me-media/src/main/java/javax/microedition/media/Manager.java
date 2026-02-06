package javax.microedition.media;

import java.io.InputStream;
import java.io.IOException;

public class Manager {
    
    // The main method games use to load audio
    public static Player createPlayer(InputStream stream, String type) throws IOException, MediaException {
        System.out.println(">>> AUDIO: Game requested audio player for type: " + type);
        
        if (type.equalsIgnoreCase("audio/midi")) {
            return new MidiPlayer(stream);
        }

        // Fallback for other audio types (e.g., wav, amr) - return a silent player
        System.out.println(">>> AUDIO: Unsupported audio type '" + type + "'. Using silent player.");
        return new Player() {
            public void realize() {}
            public void prefetch() {}
            public void start() { System.out.println(">>> AUDIO: Dummy Player Started (Silent)"); }
            public void stop() {}
            public void deallocate() {}
            public void close() {}
            public long setMediaTime(long now) { return 0; }
            public long getMediaTime() { return 0; }
            public void setLoopCount(int count) {}
            public int getState() { return Player.STARTED; }
            public void addPlayerListener(PlayerListener l) {}
            public void removePlayerListener(PlayerListener l) {}
            public Control getControl(String s) { return null; }
            public Control[] getControls() { return new Control[0]; }
        };
    }

    public static String[] getSupportedContentTypes(String protocol) {
        return new String[] { "audio/midi", "audio/wav" };
    }
}
