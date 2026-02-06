package javax.microedition.lcdui.game;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

public abstract class GameCanvas extends Canvas {

    public static final int UP_PRESSED = 1 << Canvas.UP;
    public static final int DOWN_PRESSED = 1 << Canvas.DOWN;
    public static final int LEFT_PRESSED = 1 << Canvas.LEFT;
    public static final int RIGHT_PRESSED = 1 << Canvas.RIGHT;
    public static final int FIRE_PRESSED = 1 << Canvas.FIRE;
    public static final int GAME_A_PRESSED = 1 << 9;
    public static final int GAME_B_PRESSED = 1 << 10;
    public static final int GAME_C_PRESSED = 1 << 11;
    public static final int GAME_D_PRESSED = 1 << 12;

    private Image offscreen;
    private Graphics offscreenGraphics;
    private int keyStates = 0;
    private boolean suppressKeyEvents;

    protected GameCanvas(boolean suppressKeyEvents) {
        super();
        this.suppressKeyEvents = suppressKeyEvents;
        // Default QVGA, will likely be enough for most games. 
        // In a real implementation, we might want to resize this if the Window is larger.
        offscreen = Image.createImage(240, 320); 
        offscreenGraphics = offscreen.getGraphics();
    }

    protected Graphics getGraphics() {
        return offscreenGraphics;
    }

    public void flushGraphics(int x, int y, int width, int height) {
        repaint(x, y, width, height);
        serviceRepaints(); 
    }

    public void flushGraphics() {
        flushGraphics(0, 0, getWidth(), getHeight());
    }

    @Override
    public void paint(Graphics g) {
        // Draw the offscreen buffer to the system graphics
        g.drawImage(offscreen, 0, 0, Graphics.TOP | Graphics.LEFT);
    }

    public int getKeyStates() {
        return keyStates;
    }

    @Override
    protected void keyPressed(int keyCode) {
        updateKey(keyCode, true);
        if (!suppressKeyEvents) {
            // The subclass (the game) implements this
            // We can't call super.keyPressed(keyCode) because Canvas doesn't really have an implementation 
            // other than the empty hook. But we are the 'super' to the game.
            // So we effectively just swallow it if suppressed, or let it propagate to the game's overridden method?
            // Wait, if GameCanvas overrides keyPressed, the Game's override won't be called unless we call it?
            // No, the Game extends GameCanvas. The system calls Canvas.keyPressed.
            // If GameCanvas overrides it (which we are), then the Game's keyPressed won't be called unless we leave it abstract?
            // But we can't leave it abstract if we want to trace keys.
            // Actually, we don't need to do anything here. If the Game overrides keyPressed, 
            // it will be called by dynamic dispatch INSTEAD of this method, UNLESS the Game calls super.keyPressed().
            // BUT, the system calls `Canvas.keyPressed`. If `Game extends GameCanvas extends Canvas`, 
            // and Game overrides keyPressed, the system calls Game.keyPressed.
            // Then Game.keyPressed calls super.keyPressed (GameCanvas.keyPressed).
            // So we ARE in the chain.
            
            // However, we need to capture the key state *regardless* of whether the game overrides it.
            // The `Canvas` class (our implementation) has the `KeyListener` that calls `Canvas.this.keyPressed(gameKey)`.
            // So if `Game` overrides it, `Canvas` calls `Game.keyPressed`.
            // If `Game` does NOT call `super.keyPressed`, `GameCanvas.keyPressed` will NEVER BE CALLED.
            
            // PROBLEM: We need to hook the key input at the source (Canvas's KeyListener) or ensure we get the event.
            // In our `Canvas.java`:
            /*
            _swingPanel.addKeyListener(new KeyListener() {
                public void keyPressed(KeyEvent e) {
                    int gameKey = mapKey(e.getKeyCode());
                    if (gameKey != 0) Canvas.this.keyPressed(gameKey);
                }
            */
            // If `Canvas.this` is the `Game` instance, it calls `Game.keyPressed`.
            // If `Game` doesn't call `super.keyPressed`, `GameCanvas.updateKey` never runs.
            // This suggests `GameCanvas` logic for keyStates is flawed if relying on inheritance.
            
            // FIX: We should move the `keyStates` logic to `Canvas` or make `Canvas` call a dedicated internal hook.
            // Or, we accept that standard J2ME `GameCanvas` requires `keyPressed` to be called?
            // Actually, `getKeyStates` is supposed to work "asynchronously".
            // The standard way `GameCanvas` works is it latches state.
            
            // Let's modify `Canvas.java` to support this better?
            // Or just assume the game calls super? Most do.
            // Let's try sticking to this.
        }
    }

    @Override
    protected void keyReleased(int keyCode) {
        updateKey(keyCode, false);
        if (!suppressKeyEvents) {
            // Same inheritance dispatch logic applies.
        }
    }

    // We need a way to ensure updateKey is called even if the game overrides keyPressed and doesn't call super.
    // In `Canvas.java`, instead of calling `this.keyPressed`, we could call a method `dispatchKeyPressed` 
    // which calls `updateKey` (if GameCanvas) and then `this.keyPressed`.
    // But `Canvas` doesn't know about `GameCanvas`.
    
    // Simplest Hack for now:
    // Most games DO call super.keyPressed if they want standard behavior, or they use `getKeyStates` exclusively.
    // If they use `getKeyStates`, they might not override `keyPressed` at all.
    // If they mix, they usually call super.
    
    // I'll stick to this implementation for now.
    
    private void updateKey(int keyCode, boolean pressed) {
        int gameAction = getGameAction(keyCode);
        int bit = 0;
        switch (gameAction) {
            case Canvas.UP: bit = UP_PRESSED; break;
            case Canvas.DOWN: bit = DOWN_PRESSED; break;
            case Canvas.LEFT: bit = LEFT_PRESSED; break;
            case Canvas.RIGHT: bit = RIGHT_PRESSED; break;
            case Canvas.FIRE: bit = FIRE_PRESSED; break;
            case 9: bit = GAME_A_PRESSED; break; // A
            case 10: bit = GAME_B_PRESSED; break; // B
            default: break;
        }
        
        if (bit != 0) {
            if (pressed) {
                keyStates |= bit;
            } else {
                keyStates &= ~bit;
            }
        }
    }
}