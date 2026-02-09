package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public class Sprite extends Layer {
    public static final int TRANS_NONE = 0;
    public static final int TRANS_ROT90 = 5;
    public static final int TRANS_ROT180 = 3;
    public static final int TRANS_ROT270 = 6;
    public static final int TRANS_MIRROR = 2;
    public static final int TRANS_MIRROR_ROT90 = 7;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT270 = 4;

    private Image image;
    private int frameWidth, frameHeight;
    private int currentFrame;

    public Sprite(Image image) {
        super(0, 0, image.getWidth(), image.getHeight(), true);
        this.image = image;
        this.frameWidth = image.getWidth();
        this.frameHeight = image.getHeight();
    }

    public Sprite(Image image, int frameWidth, int frameHeight) {
        super(0, 0, frameWidth, frameHeight, true);
        this.image = image;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
    }
    
    public Sprite(Sprite s) {
        super(s.getX(), s.getY(), s.getWidth(), s.getHeight(), s.isVisible());
        this.image = s.image;
        this.frameWidth = s.frameWidth;
        this.frameHeight = s.frameHeight;
        this.currentFrame = s.currentFrame;
        // Copy other state if needed (ref pixel, transform, etc.)
    }

    public void setFrame(int sequenceIndex) {
        this.currentFrame = sequenceIndex;
    }

    public final int getFrame() {
        return currentFrame;
    }

    public void setFrameSequence(int[] sequence) {
        // Stub: In a real implementation we would map sequence indices to frame indices
    }

    public void setTransform(int transform) {
        // Not implemented
    }

    public final boolean collidesWith(Sprite other, boolean pixelLevel) {
        return false; // Stub
    }

    public final boolean collidesWith(TiledLayer other, boolean pixelLevel) {
        return false; // Stub
    }

    public final boolean collidesWith(Image image, int x, int y, boolean pixelLevel) {
        return false; // Stub
    }

    @Override
    public void paint(Graphics g) {
        if (isVisible()) {
            g.drawImage(image, getX(), getY(), Graphics.TOP | Graphics.LEFT);
        }
    }
}
