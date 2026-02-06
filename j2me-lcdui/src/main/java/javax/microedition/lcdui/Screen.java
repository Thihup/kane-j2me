package javax.microedition.lcdui;

import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Dimension;

public abstract class Screen extends Displayable {
    protected final JPanel _swingPanel;

    protected Screen() {
        _swingPanel = new JPanel();
        _swingPanel.setLayout(new BoxLayout(_swingPanel, BoxLayout.Y_AXIS));
        _swingPanel.setPreferredSize(new Dimension(240, 320));
        _swingPanel.setBackground(Color.WHITE);
        _swingPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    public JPanel getSwingPanel() {
        return _swingPanel;
    }
}
