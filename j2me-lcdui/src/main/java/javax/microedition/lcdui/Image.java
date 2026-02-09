package javax.microedition.lcdui;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;

public class Image {
    
    // The REAL image data
    public final BufferedImage _awtImage;
    public static Module gameModule;

    // Constructor for internal use
    private Image(BufferedImage img) {
        this._awtImage = img;
    }

    // 1. Loading from the JAR (The method games use most)
    public static Image createImage(String name) throws IOException {
        System.out.println("DEBUG: Loading image -> " + name);
        try {
            InputStream stream = gameModule.getResourceAsStream(name);
            // Load the PNG from inside the JAR
            BufferedImage b = ImageIO.read(stream);
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

    public void getRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height) {
        _awtImage.getRGB(x, y, width, height, rgbData, offset, scanlength);
    }

    public static Image createImage(java.io.InputStream stream) throws IOException {
        try {
            BufferedImage b = ImageIO.read(stream);
            if (b == null) throw new IOException("Image could not be decoded from stream");
            return new Image(b);
        } catch (Exception e) {
             throw new IOException("Error reading image from stream: " + e.getMessage());
        }
    }

    public static Image createRGBImage(int[] rgb, int width, int height, boolean processAlpha) {
        BufferedImage img = new BufferedImage(width, height, 
            processAlpha ? BufferedImage.TYPE_INT_ARGB : BufferedImage.TYPE_INT_RGB);
        img.setRGB(0, 0, width, height, rgb, 0, width);
        return new Image(img);
    }
    
    public static Image createImage(Image source) {
        // Create a copy (immutable by default unless standard says otherwise, but usually immutable from immutable)
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source._awtImage.getType());
        java.awt.Graphics g = b.getGraphics();
        g.drawImage(source._awtImage, 0, 0, null);
        g.dispose();
        return new Image(b);
    }
    
    public static Image createImage(Image image, int x, int y, int width, int height, int transform) {
        // Just use Graphics.drawRegion-like logic or subimage
        // For simplicity, we create a new image and draw the region into it
        
        // Handle transform dimensions swap
        int w = width;
        int h = height;
        if ((transform & 4) != 0) { // Check if rotated 90/270 (bit 4 or similar logic? No, let's use the constants)
             if (transform == Graphics.TRANS_ROT90 || transform == Graphics.TRANS_ROT270 
                 || transform == Graphics.TRANS_MIRROR_ROT90 || transform == Graphics.TRANS_MIRROR_ROT270) {
                 w = height;
                 h = width;
             }
        }
        
        BufferedImage b = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics g = new Graphics(b.createGraphics());
        g.drawRegion(image, x, y, width, height, transform, 0, 0, Graphics.TOP | Graphics.LEFT);
        
        return new Image(b);
    }

    public boolean isMutable() {
        // We don't track this perfectly, but usually images created with (w, h) are mutable.
        // For now, return true or track it in constructor.
        return true; 
    }
}