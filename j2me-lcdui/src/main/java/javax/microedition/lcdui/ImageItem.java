package javax.microedition.lcdui;

public class ImageItem extends Item {
    public static final int LAYOUT_DEFAULT = 0;
    public static final int LAYOUT_LEFT = 1;
    public static final int LAYOUT_RIGHT = 2;
    public static final int LAYOUT_CENTER = 3;
    public static final int LAYOUT_NEWLINE_BEFORE = 0x100;
    public static final int LAYOUT_NEWLINE_AFTER = 0x200;
    
    private Image image;
    private int layout;
    private String altText;
    
    public ImageItem(String label, Image img, int layout, String altText) {
        super(label);
        this.image = img;
        this.layout = layout;
        this.altText = altText;
    }
    
    public Image getImage() { return image; }
    public void setImage(Image img) { this.image = img; }
    public String getAltText() { return altText; }
    public void setAltText(String text) { this.altText = text; }
    public int getLayout() { return layout; }
    public void setLayout(int layout) { this.layout = layout; }

    @Override
    public javax.swing.JComponent getSwingComponent() {
        // Simple placeholder
        return new javax.swing.JLabel(image != null ? new javax.swing.ImageIcon(image.getBufferedImage()) : null);
    }
}
