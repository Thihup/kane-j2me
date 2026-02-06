package javax.microedition.lcdui;

import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Font;

public class StringItem extends Item {
    private String text;
    private final JPanel container;
    private final JLabel textLabel;

    public StringItem(String label, String text) {
        super(label);
        this.text = text;

        container = new JPanel(new BorderLayout());
        container.setOpaque(false);

        if (label != null) {
            JLabel labelComp = new JLabel(label);
            labelComp.setFont(new Font("SansSerif", Font.BOLD, 12));
            container.add(labelComp, BorderLayout.NORTH);
        }

        textLabel = new JLabel(text);
        textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        container.add(textLabel, BorderLayout.CENTER);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        textLabel.setText(text);
    }

    @Override
    public javax.swing.JComponent getSwingComponent() {
        return container;
    }
}
