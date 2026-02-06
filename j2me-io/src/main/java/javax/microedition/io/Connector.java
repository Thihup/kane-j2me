package javax.microedition.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL; // This import is not strictly needed but good for reference

// J2ME Generic Connection Framework (GCF) Stub
public class Connector {
    
    // Access modes
    public static final int READ = 1;
    public static final int WRITE = 2;
    public static final int READ_WRITE = 3;

    public static Connection open(String name) throws IOException {
        System.out.println(">>> GCF: Game tried to open connection: " + name);

        if (name.startsWith("resource:///")) {
            String resourcePath = name.substring("resource:///".length());
            // J2ME resource paths do not start with a leading slash typically
            if (resourcePath.startsWith("/")) {
                resourcePath = resourcePath.substring(1);
            }
            
            System.out.println("DEBUG: GCF: Attempting to load resource: " + resourcePath);
            // Use the application's ClassLoader to find the resource
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            InputStream is = null;
            if (cl != null) {
                is = cl.getResourceAsStream(resourcePath);
            } else {
                // Fallback to the system class loader if context is null
                is = ClassLoader.getSystemClassLoader().getResourceAsStream(resourcePath);
            }

            if (is != null) {
                System.out.println("DEBUG: GCF: Resource '" + resourcePath + "' found. Returning InputStreamConnection.");
                return new InputStreamConnection(is); // Return our custom InputStreamConnection
            } else {
                System.err.println("ERROR: GCF: Resource '" + resourcePath + "' not found.");
                throw new IOException("Resource not found: " + resourcePath);
            }
        }
        // We don't support other connections yet, so fail.
        throw new IOException("Unsupported connection type: " + name);
    }
    
    public static Connection open(String name, int mode) throws IOException {
        return open(name);
    }
    
    public static Connection open(String name, int mode, boolean timeouts) throws IOException {
        return open(name);
    }
}