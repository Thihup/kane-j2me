package javax.microedition.media;

import javax.sound.midi.*;
import java.io.InputStream;
import java.io.IOException;

public class MidiPlayer implements Player {
    private Sequencer sequencer;
    private int state = UNREALIZED;

    public MidiPlayer(InputStream inputStream) throws MediaException, IOException {
        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            // The input stream needs to be buffered to support mark/reset
            if (!inputStream.markSupported()) {
                inputStream = new java.io.BufferedInputStream(inputStream);
            }
            Sequence sequence = MidiSystem.getSequence(inputStream);
            sequencer.setSequence(sequence);
            state = REALIZED;
        } catch (MidiUnavailableException | InvalidMidiDataException e) {
            throw new MediaException(e.getMessage());
        }
    }

    public void realize() throws MediaException {
        // Already realized in constructor
    }

    public void prefetch() throws MediaException {
        // In this implementation, prefetching is a no-op.
        // The data is already loaded.
        state = PREFETCHED;
    }

    public void start() throws MediaException {
        if (sequencer != null) {
            sequencer.start();
            state = STARTED;
            System.out.println(">>> AUDIO: MIDI Player started.");
        }
    }

    public void stop() throws MediaException {
        if (sequencer != null) {
            sequencer.stop();
            state = PREFETCHED; // Or REALIZED, depending on interpretation
            System.out.println(">>> AUDIO: MIDI Player stopped.");
        }
    }

    public void deallocate() {
        close();
    }

    public void close() {
        if (sequencer != null && sequencer.isOpen()) {
            sequencer.close();
        }
        state = CLOSED;
        System.out.println(">>> AUDIO: MIDI Player closed.");
    }

    public long setMediaTime(long now) throws MediaException {
        if (sequencer != null) {
            sequencer.setTickPosition(now / 1000); // J2ME is microseconds, MIDI is ticks
            return getMediaTime();
        }
        return 0;
    }

    public long getMediaTime() {
        if (sequencer != null) {
            return sequencer.getTickPosition() * 1000;
        }
        return 0;
    }

    public void setLoopCount(int count) {
        if (sequencer != null) {
            if (count == -1) {
                sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
            } else {
                sequencer.setLoopCount(count - 1); // J2ME is # of loops, sequencer is # of repeats
            }
        }
    }

    public int getState() {
        return state;
    }

    public void addPlayerListener(PlayerListener playerListener) {
        // Stub: Not implemented
    }

    public void removePlayerListener(PlayerListener playerListener) {
        // Stub: Not implemented
    }
    
    public Control getControl(String controlType) {
        // Stub: Not implemented, would be used for VolumeControl, etc.
        return null;
    }

    public Control[] getControls() {
        // Stub: Not implemented
        return new Control[0];
    }
}
