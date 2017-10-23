package framework.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;

/**
 * Created by ucjung on 2017-04-19.
 */
public class DefaultMessageDelegate implements MessageDelegate  {
    static  private Logger logger = LoggerFactory.getLogger(DefaultMessageDelegate.class);

    @Override
    public void handleMessage(String message) {
        logger.info(message);
    }

    @Override
    public void handleMessage(Map message) {
        logger.info(message.toString());

    }

    @Override
    public void handleMessage(byte[] message) {
        logger.info(message.toString());
    }

    @Override
    public void handleMessage(Serializable message) {
        logger.info(message.toString());
    }
}
