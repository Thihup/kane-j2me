package javax.bluetooth;

// JSR-82 Bluetooth API Stub
public class LocalDevice {

    private static LocalDevice instance;

    private LocalDevice() {
        // private constructor
    }

    public static LocalDevice getLocalDevice() throws BluetoothStateException {
        if (instance == null) {
            System.out.println(">>> BT: Game requested local device for the first time.");
            // We don't have a real bluetooth device, so we could throw an exception,
            // but it might be better to return a dummy device.
            // Let's throw for now, and see how the game handles it.
            throw new BluetoothStateException("Bluetooth not available");
        }
        return instance;
    }

    public DiscoveryAgent getDiscoveryAgent() {
        // Return a dummy discovery agent
        return new DiscoveryAgent();
    }
}
