package javax.microedition.midlet;

/**
 * Signals that a requested MIDlet state change failed.
 */
public class MIDletStateChangeException extends Exception {
    
    /**
     * Constructs an exception with no specified detail message.
     */
    public MIDletStateChangeException() {
        super();
    }

    /**
     * Constructs an exception with the specified detail message.
     * @param s the detail message
     */
    public MIDletStateChangeException(String s) {
        super(s);
    }
}
