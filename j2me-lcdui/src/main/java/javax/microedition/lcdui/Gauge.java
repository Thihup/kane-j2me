package javax.microedition.lcdui;

public class Gauge extends Item {
    public static final int CONTINUOUS_IDLE = 0;
    public static final int INCREMENTAL_IDLE = 1;
    public static final int CONTINUOUS_RUNNING = 2;
    public static final int INCREMENTAL_UPDATING = 3;

    private int value;
    private int maxValue;
    private boolean interactive;

    public Gauge(String label, boolean interactive, int maxValue, int initialValue) {
        super(label);
        this.interactive = interactive;
        this.maxValue = maxValue;
        this.value = initialValue;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    @Override
    public javax.swing.JComponent getSwingComponent() {
        return new javax.swing.JProgressBar(0, maxValue);
    }
}
