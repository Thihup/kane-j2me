package javax.microedition.lcdui;

import javax.microedition.midlet.MIDlet;
import javax.swing.JFrame;
import javax.swing.JFrame;

public class Display {
    
    // The "Hardware" Screen manager
    private static Display instance;
    private Displayable currentDisplayable;
    private JFrame window;

    // Private constructor (Standard Singleton pattern)
    private Display() {}

    // STATIC ENTRY POINT: This is what the game calls first.
    // "Display.getDisplay(this)"
    public static Display getDisplay(MIDlet m) {
        if (instance == null) {
            instance = new Display();
        }
        return instance;
    }

    // SCREEN SWITCHING: The game calls this to show a menu or the game canvas.
    public void setCurrent(Displayable nextDisplayable) {
        System.out.println("DEBUG: Switching screen to " + nextDisplayable);
        this.currentDisplayable = nextDisplayable;

        // Lazy initialization of the window
        if (window == null) {
            window = new JFrame("J2ME Emulator");
            window.setSize(255, 360); // 240x320 + window borders
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setVisible(true);
        }

        // If this is a Canvas (which we hacked to have a _swingPanel)
        if (nextDisplayable instanceof Canvas) {
            Canvas c = (Canvas) nextDisplayable;
            window.setContentPane(c._swingPanel);
            c._swingPanel.requestFocus(); // Give it keyboard focus
            window.revalidate();
        }
    }

    public Displayable getCurrent() {
        return currentDisplayable;
    }

    // VIBRATION: Games often call this on goals/hits
    public boolean vibrate(int duration) {
        System.out.println("DEBUG: Vibrate for " + duration + "ms");
        return true; 
    }

    // THREADING: Games use this to queue logic on the UI thread
    public void callSerially(Runnable r) {
        // For this simple emulator, running it immediately is usually fine.
        // In a complex one, you'd send this to the Event Dispatch Thread (EDT).
        r.run(); 
    }
    
    // Some games check color depth
    public boolean isColor() { return true; }
    public int numColors() { return 65536; }
}