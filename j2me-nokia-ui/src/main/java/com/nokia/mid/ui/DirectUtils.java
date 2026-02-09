package com.nokia.mid.ui;

import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Graphics;

public class DirectUtils {
    public static DirectGraphics getDirectGraphics(Graphics g) {
        return new DirectGraphicsImpl(g);
    }
    
    public static Image createImage(int width, int height, int color) {
        Image img = Image.createImage(width, height);
        Graphics g = img.getGraphics();
        g.setColor(color);
        g.fillRect(0, 0, width, height);
        return img;
    }

    private static class DirectGraphicsImpl implements DirectGraphics {
        private Graphics g;

        public DirectGraphicsImpl(Graphics g) {
            this.g = g;
        }

        public void drawImage(Image img, int x, int y, int anchor, int manipulation) {
            // Map Nokia manipulation to J2ME transform
            int transform = 0;
            
            // This is a rough mapping, Nokia's constants are bitmasks/degrees
            if (manipulation == ROTATE_90) transform = Graphics.TRANS_ROT90;
            else if (manipulation == ROTATE_180) transform = Graphics.TRANS_ROT180;
            else if (manipulation == ROTATE_270) transform = Graphics.TRANS_ROT270;
            // else if (manipulation == FLIP_HORIZONTAL) ... 
            
            // Use drawRegion for transformation
            g.drawRegion(img, 0, 0, img.getWidth(), img.getHeight(), transform, x, y, anchor);
        }

        public void drawPixels(int[] pixels, boolean transparency, int offset, int scanlength, int x, int y, int width, int height, int manipulation, int format) {
            // Simplified: ignoring manipulation for now
            g.drawRGB(pixels, offset, scanlength, x, y, width, height, transparency);
        }

        public void drawPolygon(int[] xPoints, int xOffset, int[] yPoints, int yOffset, int nPoints, int argbColor) {
             g.setColor(argbColor);
             // Stub: graphics doesn't support drawPolygon yet in our impl
        }

        public void fillPolygon(int[] xPoints, int xOffset, int[] yPoints, int yOffset, int nPoints, int argbColor) {
             g.setColor(argbColor);
             // Stub
        }
    }
}
