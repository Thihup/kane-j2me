package javax.microedition.lcdui;

public class Graphics {
    // We hold the REAL Java graphics object here
    private java.awt.Graphics target;
    private Font currentFont = Font.getDefaultFont();

    public Graphics(java.awt.Graphics g) {
        this.target = g;
        target.setFont(currentFont._getAwtFont());
    }

    // Anchor constants
    public static final int HCENTER = 1;
    public static final int VCENTER = 2;
    public static final int LEFT    = 4;
    public static final int RIGHT   = 8;
    public static final int TOP     = 16;
    public static final int BOTTOM  = 32;
    public static final int BASELINE= 64;
    public static final int SOLID   = 0;
    public static final int DOTTED  = 1;

    // --- MAPPING METHODS ---
    // The game calls these. We forward them to the real Java AWT.
    
    public void setColor(int rgb) {
        target.setColor(new java.awt.Color(rgb));
    }

    public void setColor(int r, int g, int b) {
        target.setColor(new java.awt.Color(r, g, b));
    }

    private int translateX = 0;
    private int translateY = 0;

    public void translate(int x, int y) {
        this.translateX += x;
        this.translateY += y;
        target.translate(x, y);
    }

    public int getTranslateX() { return translateX; }
    public int getTranslateY() { return translateY; }

    public void setGrayScale(int value) {
        int v = value & 0xFF;
        target.setColor(new java.awt.Color(v, v, v));
    }
    public int getGrayScale() {
        java.awt.Color c = target.getColor();
        return (c.getRed() + c.getGreen() + c.getBlue()) / 3;
    }

    private int strokeStyle = SOLID;
    public void setStrokeStyle(int style) { this.strokeStyle = style; }
    public int getStrokeStyle() { return strokeStyle; }

    public void fillRect(int x, int y, int width, int height) {
        target.fillRect(x, y, width, height);
    }
    
    public void drawRect(int x, int y, int width, int height) {
        target.drawRect(x, y, width, height);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        target.drawLine(x1, y1, x2, y2);
    }

    public void fillArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        target.fillArc(x, y, width, height, startAngle, arcAngle);
    }
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        target.drawArc(x, y, width, height, startAngle, arcAngle);
    }
    public void fillRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        target.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        target.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    public void copyArea(int x_src, int y_src, int width, int height, int x_dest, int y_dest, int anchor) {
        // Simple implementation
        target.copyArea(x_src, y_src, width, height, x_dest - x_src, y_dest - y_src);
    }

    public void drawChar(char character, int x, int y, int anchor) {
        drawString(String.valueOf(character), x, y, anchor);
    }
    public void drawChars(char[] data, int offset, int length, int x, int y, int anchor) {
        drawString(new String(data, offset, length), x, y, anchor);
    }
    public void drawSubstring(String str, int offset, int len, int x, int y, int anchor) {
        drawString(str.substring(offset, offset + len), x, y, anchor);
    }
    
    public void setClip(int x, int y, int width, int height) {
        target.setClip(x, y, width, height);
    }

    public void clipRect(int x, int y, int width, int height) {
        target.clipRect(x, y, width, height);
    }

    public int getClipX() { return target.getClipBounds().x; }
    public int getClipY() { return target.getClipBounds().y; }
    public int getClipWidth() { return target.getClipBounds().width; }
    public int getClipHeight() { return target.getClipBounds().height; }

    public void drawString(String str, int x, int y, int anchor) {
        
        int drawX = x;
        int drawY = y;

        if ((anchor & HCENTER) != 0) {
            drawX -= currentFont.stringWidth(str) / 2;
        } else if ((anchor & RIGHT) != 0) {
            drawX -= currentFont.stringWidth(str);
        }

        if ((anchor & VCENTER) != 0) {
            drawY += currentFont.getHeight() / 2;
        } else if ((anchor & BOTTOM) != 0) {
            // In AWT, drawString y is baseline. 
            // In J2ME, BOTTOM anchor means y is the bottom edge.
            drawY -= (currentFont.getHeight() - currentFont.getBaselinePosition());
        } else if ((anchor & TOP) != 0) {
             drawY += currentFont.getBaselinePosition();
        }
        
        target.drawString(str, drawX, drawY);
    }

    public void drawImage(Image img, int x, int y, int anchor) {
        if (img == null) return;
        // Handle J2ME's anchor constants for positioning
        if ((anchor & VCENTER) != 0) {
            y -= img.getHeight() / 2;
        }
        if ((anchor & HCENTER) != 0) {
            x -= img.getWidth() / 2;
        }
        
        // Use the internal BufferedImage from our Image wrapper
        target.drawImage(img.getBufferedImage(), x, y, null);
    }

    /**
     * Draws a series of ARGB pixels from the specified array onto the Graphics.
     */
    public void drawRGB(int[] rgbData, int offset, int scanlength, int x, int y, int width, int height, boolean processAlpha) {
        // Create a BufferedImage from the RGB data
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(width, height, 
            processAlpha ? java.awt.image.BufferedImage.TYPE_INT_ARGB : java.awt.image.BufferedImage.TYPE_INT_RGB);
        
        // Set the RGB data into the image
        img.setRGB(0, 0, width, height, rgbData, offset, scanlength);
        
        // Draw the image onto the target graphics
        target.drawImage(img, x, y, null);
    }
    
    // Transform constants
    public static final int TRANS_NONE = 0;
    public static final int TRANS_ROT90 = 5;
    public static final int TRANS_ROT180 = 3;
    public static final int TRANS_ROT270 = 6;
    public static final int TRANS_MIRROR = 2;
    public static final int TRANS_MIRROR_ROT90 = 7;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT270 = 4;

    public void drawRegion(Image src, int x_src, int y_src, int width, int height, int transform, int x_dest, int y_dest, int anchor) {
        if (src == null || src.getBufferedImage() == null || width <= 0 || height <= 0) {
            return;
        }

        java.awt.Graphics2D g2d = (java.awt.Graphics2D) target.create();
        java.awt.image.BufferedImage subImage = src.getBufferedImage().getSubimage(x_src, y_src, width, height);

        // Handle J2ME's anchor constants for positioning
        if ((anchor & VCENTER) != 0) {
            y_dest -= height / 2;
        }
        if ((anchor & HCENTER) != 0) {
            x_dest -= width / 2;
        }
        if ((anchor & RIGHT) != 0) {
            x_dest -= width;
        }
        if ((anchor & BOTTOM) != 0) {
            y_dest -= height;
        }

        java.awt.geom.AffineTransform originalTransform = g2d.getTransform();
        
        try {
            g2d.translate(x_dest, y_dest);

            switch (transform) {
                case TRANS_NONE:
                    break;
                case TRANS_ROT90:
                    g2d.rotate(Math.PI / 2);
                    g2d.translate(0, -width);
                    break;
                case TRANS_ROT180:
                    g2d.rotate(Math.PI);
                    g2d.translate(-width, -height);
                    break;
                case TRANS_ROT270:
                    g2d.rotate(3 * Math.PI / 2);
                    g2d.translate(-height, 0);
                    break;
                case TRANS_MIRROR:
                    g2d.scale(-1, 1);
                    g2d.translate(-width, 0);
                    break;
                case TRANS_MIRROR_ROT90:
                    g2d.rotate(Math.PI / 2);
                    g2d.scale(-1, 1);
                    break;
                case TRANS_MIRROR_ROT180:
                    g2d.rotate(Math.PI);
                    g2d.scale(-1, 1);
                    g2d.translate(0, -height);
                    break;
                case TRANS_MIRROR_ROT270:
                    g2d.rotate(3 * Math.PI / 2);
                    g2d.scale(-1, 1);
                    g2d.translate(-height, -width);
                    break;
                default:
                    // No other transformations are standard
                    break;
            }
            g2d.drawImage(subImage, 0, 0, null);

        } finally {
            g2d.setTransform(originalTransform);
            g2d.dispose();
        }
    }

    public Font getFont() { 
        return currentFont; 
    }
    
    public void setFont(Font font) { 
        if (font != null) {
            this.currentFont = font;
            target.setFont(font._getAwtFont());
        }
    }
}
