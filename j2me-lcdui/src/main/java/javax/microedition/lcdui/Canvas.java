package javax.microedition.lcdui;

import javax.swing.JPanel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

// CRITICAL CHANGE: We now extend JPanel!
public abstract class Canvas extends Displayable {
    
    // We create a "Panel" bridge to handle the Swing painting
    public final JPanel _swingPanel;

    @Override
    public javax.swing.JPanel getSwingPanel() {
        return _swingPanel;
    }

    protected Canvas() {
        super();
        // This inner class listens to the OS repaint requests
        _swingPanel = new JPanel() {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                super.paintComponent(g);
                // clear screen black
                g.setColor(java.awt.Color.BLACK);
                g.fillRect(0,0, getWidth(), getHeight());

                // Call the GAME'S paint method with our wrapper
                Canvas.this.paint(new javax.microedition.lcdui.Graphics(g));
            }
        };

        // Keyboard Input Hook
        _swingPanel.setFocusable(true);
        _swingPanel.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            
            public void keyPressed(KeyEvent e) {
                // Map PC keys to Phone keys
                int gameKey = mapKey(e.getKeyCode());
                if (gameKey != 0) Canvas.this.keyPressed(gameKey);
            }

            public void keyReleased(KeyEvent e) {
                int gameKey = mapKey(e.getKeyCode());
                if (gameKey != 0) Canvas.this.keyReleased(gameKey);
            }
        });
    }

    // Simple PC -> J2ME Key Mapper
    private int mapKey(int pcKey) {
        switch(pcKey) {
            case KeyEvent.VK_UP: return -1; // UP
            case KeyEvent.VK_DOWN: return -2; // DOWN
            case KeyEvent.VK_LEFT: return -3; // LEFT
            case KeyEvent.VK_RIGHT: return -4; // RIGHT
            case KeyEvent.VK_ENTER: return -5; // FIRE/OK
            case KeyEvent.VK_ESCAPE: return -7; // SOFT2 (Right soft key, often 'Back')
            case KeyEvent.VK_1: return 49;
            case KeyEvent.VK_2: return 50;
            case KeyEvent.VK_3: return 51;
            case KeyEvent.VK_4: return 52;
            case KeyEvent.VK_5: return 53;
            case KeyEvent.VK_6: return 54;
            case KeyEvent.VK_7: return 55;
            case KeyEvent.VK_8: return 56;
            case KeyEvent.VK_9: return 57;
            case KeyEvent.VK_0: return 48;
            case KeyEvent.VK_A: return 9;  // Game A
            case KeyEvent.VK_S: return 10; // Game B
            case KeyEvent.VK_D: return 11; // Game C
            case KeyEvent.VK_F: return 12; // Game D
        }
        return 0;
    }

    // Abstract method the game implements
    protected abstract void paint(Graphics g);

    // Game lifecycle inputs
    protected void keyPressed(int keyCode) {}
    protected void keyReleased(int keyCode) {}

    // Trigger a repaint of the Swing panel
    public void repaint() {
        _swingPanel.repaint();
    }
    
    public void repaint(int x, int y, int w, int h) {
        _swingPanel.repaint(x, y, w, h);
    }

    public void serviceRepaints() {
        // Force immediate update (optional)
        _swingPanel.paintImmediately(0, 0, _swingPanel.getWidth(), _swingPanel.getHeight());
    }
    
    public void setFullScreenMode(boolean mode) {}

    // Key constants... (Keep these from your previous file)
    public static final int KEY_NUM0 = 48;
    // ... (Paste the rest of the key constants here if you want)
    public static final int UP = 1;
    public static final int DOWN = 6;
    public static final int LEFT = 2;
    public static final int RIGHT = 5;
    public static final int FIRE = 8;
    
    public int getGameAction(int keyCode) {
        if (keyCode == -1) return UP;
        if (keyCode == -2) return DOWN;
        if (keyCode == -3) return LEFT;
        if (keyCode == -4) return RIGHT;
        if (keyCode == -5) return FIRE;
        return 0;
    }
}