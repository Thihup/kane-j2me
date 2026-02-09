package com.nokia.mid.ui;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;

public abstract class FullCanvas extends Canvas {
    protected FullCanvas() {
        super();
        setFullScreenMode(true);
    }
}
