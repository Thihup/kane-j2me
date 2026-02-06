package javax.bluetooth;

// JSR-82 Bluetooth API Stub
public interface DiscoveryListener {
    void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod);
    void inquiryCompleted(int discType);
    void servicesDiscovered(int transID, ServiceRecord[] servRecord);
    void serviceSearchCompleted(int transID, int respCode);

    // Constants from the API
    public static final int INQUIRY_COMPLETED = 0;
    public static final int INQUIRY_TERMINATED = 5;
    public static final int INQUIRY_ERROR = 7;
    
    public static final int SERVICE_SEARCH_COMPLETED = 1;
    public static final int SERVICE_SEARCH_TERMINATED = 2;
    public static final int SERVICE_SEARCH_ERROR = 3;
    public static final int SERVICE_SEARCH_NO_RECORDS = 4;
    public static final int SERVICE_SEARCH_DEVICE_NOT_REACHABLE = 6;
}
