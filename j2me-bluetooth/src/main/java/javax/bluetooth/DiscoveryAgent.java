package javax.bluetooth;

import java.io.IOException;

// JSR-82 Bluetooth API Stub
public class DiscoveryAgent {
    public boolean startInquiry(int accessCode, DiscoveryListener listener) throws BluetoothStateException {
        System.out.println(">>> BT: Game started device inquiry.");
        // Immediately tell the listener that the inquiry is complete.
        listener.inquiryCompleted(DiscoveryListener.INQUIRY_COMPLETED);
        return true;
    }
}
