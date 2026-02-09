package javax.microedition.io;

import java.io.IOException;

public interface HttpConnection extends ContentConnection {
    String GET = "GET";
    String POST = "POST";
    String HEAD = "HEAD";
    
    int HTTP_OK = 200;
    int HTTP_MOVED_TEMP = 302;
    int HTTP_MOVED_PERM = 301;
    int HTTP_TEMP_REDIRECT = 307;
    int HTTP_NOT_FOUND = 404;

    String getURL();
    String getProtocol();
    String getHost();
    String getFile();
    String getRef();
    String getQuery();
    int getPort();
    
    void setRequestMethod(String method) throws IOException;
    String getRequestMethod();
    
    void setRequestProperty(String key, String value) throws IOException;
    String getRequestProperty(String key);
    
    int getResponseCode() throws IOException;
    String getResponseMessage() throws IOException;
    
    long getExpiration() throws IOException;
    long getDate() throws IOException;
    long getLastModified() throws IOException;
    
    String getHeaderField(String name) throws IOException;
    String getHeaderField(int n) throws IOException;
    String getHeaderFieldKey(int n) throws IOException;
}
