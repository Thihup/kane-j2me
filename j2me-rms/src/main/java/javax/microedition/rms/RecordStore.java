package javax.microedition.rms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * In-Memory RMS (Record Management System) Implementation.
 * J2ME games use this for saving data like game state, settings, etc.
 * 
 * This is a simplified, non-persistent version. Data is lost when the emulator closes.
 * It uses a static HashMap to simulate multiple record stores.
 */
public class RecordStore {

    private static final Map<String, RecordStore> recordStores = new HashMap<>();

    private final String recordStoreName;
    private int version;
    private final List<byte[]> records;
    private int nextRecordId = 1;

    private RecordStore(String recordStoreName) {
        this.recordStoreName = recordStoreName;
        this.records = new ArrayList<>();
        // J2ME record IDs are 1-based, so add a null placeholder at index 0
        this.records.add(null); 
    }

    // --- Static Methods ---

    public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary) throws RecordStoreException {
        System.out.println(">>> RMS: openRecordStore('" + recordStoreName + "')");
        
        synchronized (recordStores) {
            RecordStore rs = recordStores.get(recordStoreName);
            if (rs == null) {
                if (createIfNecessary) {
                    System.out.println(">>> RMS: Creating new RecordStore: " + recordStoreName);
                    rs = new RecordStore(recordStoreName);
                    recordStores.put(recordStoreName, rs);
                } else {
                    throw new RecordStoreNotFoundException("RecordStore '" + recordStoreName + "' not found.");
                }
            }
            return rs;
        }
    }
    
    public static void deleteRecordStore(String recordStoreName) throws RecordStoreException {
        System.out.println(">>> RMS: deleteRecordStore('" + recordStoreName + "')");
        synchronized (recordStores) {
            if (recordStores.remove(recordStoreName) == null) {
                throw new RecordStoreNotFoundException("RecordStore '" + recordStoreName + "' not found.");
            }
        }
    }

    // --- Instance Methods ---

    public int addRecord(byte[] data, int offset, int numBytes) throws RecordStoreException {
        byte[] newRecord = new byte[numBytes];
        System.arraycopy(data, offset, newRecord, 0, numBytes);
        
        synchronized (this) {
            records.add(newRecord);
            int recordId = nextRecordId++;
            System.out.println(">>> RMS ["+recordStoreName+"]: addRecord() -> ID: " + recordId + ", Size: " + numBytes);
            return recordId;
        }
    }

    public byte[] getRecord(int recordId) throws RecordStoreException {
        System.out.println(">>> RMS ["+recordStoreName+"]: getRecord(" + recordId + ")");
        synchronized (this) {
            if (recordId <= 0 || recordId >= records.size() || records.get(recordId) == null) {
                throw new InvalidRecordIDException("Record ID " + recordId + " is invalid.");
            }
            return records.get(recordId);
        }
    }
    
    public void setRecord(int recordId, byte[] newData, int offset, int numBytes) throws RecordStoreException {
        System.out.println(">>> RMS ["+recordStoreName+"]: setRecord(" + recordId + ")");
        synchronized (this) {
            if (recordId <= 0 || recordId >= records.size() || records.get(recordId) == null) {
                throw new InvalidRecordIDException("Record ID " + recordId + " is invalid.");
            }
            byte[] newRecord = new byte[numBytes];
            System.arraycopy(newData, offset, newRecord, 0, numBytes);
            records.set(recordId, newRecord);
        }
    }

    public int getNumRecords() throws RecordStoreException {
        // Subtract the null placeholder at index 0
        int count = records.size() - 1;
        System.out.println(">>> RMS ["+recordStoreName+"]: getNumRecords() -> " + count);
        return count;
    }

    public int getVersion() throws RecordStoreException {
        return version;
    }

    public void closeRecordStore() throws RecordStoreException {
        // In-memory implementation doesn't need to do anything here.
        System.out.println(">>> RMS ["+recordStoreName+"]: closeRecordStore()");
    }
    
    // Simple placeholder exceptions, J2ME has more specific ones.
    public static class RecordStoreException extends Exception {
        public RecordStoreException(String message) { super(message); }
    }
    public static class RecordStoreNotFoundException extends RecordStoreException {
        public RecordStoreNotFoundException(String message) { super(message); }
    }
    public static class InvalidRecordIDException extends RecordStoreException {
        public InvalidRecordIDException(String message) { super(message); }
    }
}
