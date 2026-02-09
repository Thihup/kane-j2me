package javax.microedition.lcdui;

public class Font {
    public static final int STYLE_PLAIN = 0;
    public static final int STYLE_BOLD = 1;
    public static final int STYLE_ITALIC = 2;
    public static final int STYLE_UNDERLINED = 4;
    
    public static final int SIZE_SMALL = 8;
    public static final int SIZE_MEDIUM = 0;
    public static final int SIZE_LARGE = 16;
    
    public static final int FACE_SYSTEM = 0;
    public static final int FACE_MONOSPACE = 32;
    public static final int FACE_PROPORTIONAL = 64;

    private final java.awt.Font _awtFont;
    private final java.awt.FontMetrics _metrics;
    private final int face, style, size;

    private Font(java.awt.Font font, int face, int style, int size) {
        this._awtFont = font;
        this.face = face;
        this.style = style;
        this.size = size;
        // Create a temporary canvas to get metrics
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        this._metrics = img.createGraphics().getFontMetrics(font);
    }

    public static Font getFont(int face, int style, int size) {
        int awtStyle = java.awt.Font.PLAIN;
        if ((style & STYLE_BOLD) != 0) awtStyle |= java.awt.Font.BOLD;
        if ((style & STYLE_ITALIC) != 0) awtStyle |= java.awt.Font.ITALIC;
        
        int awtSize = 12; // Medium
        if (size == SIZE_SMALL) awtSize = 10;
        if (size == SIZE_LARGE) awtSize = 16;
        
        String name = "SansSerif";
        if (face == FACE_MONOSPACE) name = "Monospaced";
        
        return new Font(new java.awt.Font(name, awtStyle, awtSize), face, style, size);
    }
    
    public int getStyle() { return style; }
    public int getSize() { return size; }
    public int getFace() { return face; }
    
    public static Font getDefaultFont() {
        return getFont(FACE_SYSTEM, STYLE_PLAIN, SIZE_MEDIUM);
    }

    public int getHeight() {
        return _metrics.getHeight();
    }
    
    public int getBaselinePosition() {
        return _metrics.getAscent();
    }
    
    public int stringWidth(String str) {
        if (str == null) return 0;
        return _metrics.stringWidth(str);
    }
    
    public int substringWidth(String str, int offset, int len) {
        return stringWidth(str.substring(offset, offset + len));
    }
    
    public int charWidth(char ch) {
        return _metrics.charWidth(ch);
    }
    
    public int charsWidth(char[] ch, int offset, int length) {
        return _metrics.charsWidth(ch, offset, length);
    }
    
    // Internal accessor
    public java.awt.Font _getAwtFont() {
        return _awtFont;
    }
}
