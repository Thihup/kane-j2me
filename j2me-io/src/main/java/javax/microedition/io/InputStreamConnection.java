package javax.microedition.io;

import java.io.InputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class InputStreamConnection implements InputConnection {
    private final InputStream is;
    private boolean open = true;

    public InputStreamConnection(InputStream is) {
        this.is = is;
    }

    @Override
    public InputStream openInputStream() throws IOException {
        if (!open) throw new IOException("Connection closed");
        return is;
    }

    @Override
    public DataInputStream openDataInputStream() throws IOException {
        if (!open) throw new IOException("Connection closed");
        return new DataInputStream(is);
    }

    @Override
    public void close() throws IOException {
        if (open) {
            is.close();
            open = false;
        }
    }
}
