package com.nokia.mid.sound;

public class Sound {
    public static final int FORMAT_TONE = 1;
    public static final int FORMAT_WAV = 2;
    
    public Sound(byte[] data, int type) {
        // Stub
    }
    
    public void play(int loop) {
        // Stub
    }
    
    public void stop() {
        // Stub
    }
    
    public void release() {
        // Stub
    }
    
    public void setGain(int gain) {
        // Stub
    }
    
    public void setSoundListener(SoundListener listener) {
        // Stub
    }
    
    public static int getConcurrentSoundCount(int type) {
        return 1;
    }
}
