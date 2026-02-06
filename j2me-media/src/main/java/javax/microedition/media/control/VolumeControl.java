package javax.microedition.media.control;

import javax.microedition.media.Control;

public interface VolumeControl extends Control {
    void setLevel(int level);
    int getLevel();
    boolean isMuted();
    void setMute(boolean mute);
}
