package javax.microedition.lcdui;

import javax.swing.JComponent;

public abstract class Item {
    private String label;

    protected Item(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    // Returns the Swing component for this J2ME item
    public abstract JComponent getSwingComponent();
}
