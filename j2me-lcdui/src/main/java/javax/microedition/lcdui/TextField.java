package javax.microedition.lcdui;

public class TextField extends Item {
    public static final int ANY = 0;
    public static final int EMAILADDR = 1;
    public static final int NUMERIC = 2;
    public static final int PHONENUMBER = 3;
    public static final int URL = 4;
    public static final int DECIMAL = 5;
    
    public static final int PASSWORD = 65536;
    public static final int UNEDITABLE = 131072;
    public static final int SENSITIVE = 262144;
    public static final int NON_PREDICTIVE = 524288;
    public static final int INITIAL_CAPS_WORD = 1048576;
    public static final int INITIAL_CAPS_SENTENCE = 2097152;

    private String text;
    private int maxSize;
    private int constraints;
    
    private final javax.swing.JPanel panel;
    private final javax.swing.JTextField swingField;

    public TextField(String label, String text, int maxSize, int constraints) {
        super(label);
        this.text = text;
        this.maxSize = maxSize;
        this.constraints = constraints;
        
        panel = new javax.swing.JPanel(new java.awt.BorderLayout());
        if (label != null) {
            panel.add(new javax.swing.JLabel(label), java.awt.BorderLayout.NORTH);
        }
        
        swingField = new javax.swing.JTextField(text);
        panel.add(swingField, java.awt.BorderLayout.CENTER);
    }

    public String getString() {
        return swingField.getText();
    }

    public void setString(String text) {
        swingField.setText(text);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return maxSize;
    }

    @Override
    public javax.swing.JComponent getSwingComponent() {
        return panel;
    }
}
