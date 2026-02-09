package javax.wireless.messaging;

import java.io.IOException;

public interface MessageListener {
    void notifyIncomingMessage(MessageConnection conn);
}
