package javax.microedition.lcdui;

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.Font;

public class Alert extends Screen {
    public static final int FOREVER = -2;
    private int timeout = 2000; // Default 2 seconds

    public Alert(String title) {
        this(title, null, null, null);
    }

    public Alert(String title, String alertText, Image alertImage, AlertType alertType) {
        super();
        setTitle(title);
        
        if (alertImage != null) {
            _swingPanel.add(new JLabel(new ImageIcon(alertImage.getBufferedImage())));
        }

        if (alertText != null) {
            JLabel textLabel = new JLabel("<html><body style='width: 200px'>" + alertText + "</body></html>");
            textLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
            _swingPanel.add(textLabel);
        }
    }

    public void setTimeout(int time) {
        this.timeout = time;
    }

    public int getTimeout() {
        return timeout;
    }
}
