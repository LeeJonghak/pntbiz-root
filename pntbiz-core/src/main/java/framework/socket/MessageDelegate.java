package framework.socket;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by ucjung on 2017-04-19.
 */
public interface MessageDelegate {
    void handleMessage(String message);

    void handleMessage(Map message);

    void handleMessage(byte[] message);

    void handleMessage(Serializable message);
}
