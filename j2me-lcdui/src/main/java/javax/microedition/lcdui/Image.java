package javax.microedition.lcdui;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.nio.Buffer;

public class Image {
    
    // The REAL image data
    public final BufferedImage _awtImage;

    // Constructor for internal use
    private Image(BufferedImage img) {
        this._awtImage = img;
    }

    // 1. Loading from the JAR (The method games use most)
    public static Image createImage(String name) throws IOException {
        System.out.println("DEBUG: Loading image -> " + name);
        try {
            // Load the PNG from inside the JAR
            BufferedImage b = ImageIO.read(Image.class.getResourceAsStream(name));
            if (b == null) throw new IOException("Image not found: " + name);
            return new Image(b);
        } catch (Exception e) {
            // If it fails, return a pink placeholder so the game doesn't crash
            System.err.println("ERROR loading " + name + ": " + e.getMessage());
            BufferedImage errorImg = new BufferedImage(20, 20, BufferedImage.TYPE_INT_RGB);
            java.awt.Graphics g = errorImg.getGraphics();
            g.setColor(java.awt.Color.MAGENTA);
            g.fillRect(0,0,20,20);
            return new Image(errorImg);
        }
    }

    // 2. Creating a blank mutable image (for double-buffering)
    public static Image createImage(int width, int height) {
        BufferedImage b = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        return new Image(b);
    }
    
    // 3. Creating an image from a byte array (downloaded images)
    public static Image createImage(byte[] imageData, int imageOffset, int imageLength) {
        try {
            java.io.ByteArrayInputStream bis = new java.io.ByteArrayInputStream(imageData, imageOffset, imageLength);
            return new Image(ImageIO.read(bis));
        } catch (IOException e) {
            return createImage(10, 10); // Return stub on fail
        }
    }

    // FIX FOR YOUR ERROR:
    // We create a J2ME Graphics wrapper around the AWT Graphics
    public Graphics getGraphics() {
        return new Graphics(_awtImage.createGraphics());
    }

    public int getWidth() { return _awtImage.getWidth(); }
    public int getHeight() { return _awtImage.getHeight(); }
    
    // Direct access for our Graphics.java to draw this
    public BufferedImage getMe() { return _awtImage; }

    public BufferedImage getBufferedImage() {
        return _awtImage;
    }
}