package javax.microedition.lcdui;

import java.util.ArrayList;
import java.util.List;

public abstract class Displayable {
    private String title;
    private CommandListener listener;
    private List<Command> commands = new ArrayList<>();

    public String getTitle() { return title; }
    
    public void setTitle(String s) { 
        this.title = s; 
        System.out.println("DEBUG: Title set to " + s);
    }

    public boolean isShown() { return true; } // Pretend we are always visible

    public void addCommand(Command cmd) {
        commands.add(cmd);
    }

    public void removeCommand(Command cmd) {
        commands.remove(cmd);
    }

    public void setCommandListener(CommandListener l) {
        this.listener = l;
    }

    // CRITICAL: Games calculate scaling based on these.
    // Do not return 0 or the game math might fail.
    public int getWidth() { return 240; } 
    public int getHeight() { return 320; }
    
    // Returns the Swing component used to render this Displayable
    public javax.swing.JPanel getSwingPanel() {
        return null;
    }

    // Stub for size change events
    protected void sizeChanged(int w, int h) {}
}
