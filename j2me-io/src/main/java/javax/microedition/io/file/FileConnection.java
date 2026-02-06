package javax.microedition.io.file;

import javax.microedition.io.StreamConnection;
import java.io.IOException;

public interface FileConnection extends StreamConnection {
    long fileSize() throws IOException;
    boolean exists();
    boolean isDirectory();
    void delete() throws IOException;
    void create() throws IOException;
    void mkdir() throws IOException;
    long lastModified();
    boolean canRead();
    boolean canWrite();
    void setReadable(boolean readable);
    void setWritable(boolean writable);
}
