package javax.microedition.lcdui;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.BorderLayout;

public class TextBox extends Screen {
    private final JTextArea textArea;
    private int maxSize;
    private int constraints;

    public TextBox(String title, String text, int maxSize, int constraints) {
        super();
        setTitle(title);
        this.maxSize = maxSize;
        this.constraints = constraints;

        textArea = new JTextArea(text);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        _swingPanel.setLayout(new BorderLayout());
        _swingPanel.add(new JScrollPane(textArea), BorderLayout.CENTER);
    }

    public String getString() {
        return textArea.getText();
    }

    public void setString(String text) {
        textArea.setText(text);
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int setMaxSize(int maxSize) {
        this.maxSize = maxSize;
        return maxSize;
    }
}
