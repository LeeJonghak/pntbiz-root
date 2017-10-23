package framework.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

/**
 * Created by ucjung on 2017-04-19.
 */
public class RedisKeyExpireMessageListener implements MessageListener {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onMessage(Message message, byte[] bytes) {
        System.out.println("Redis Pub/Sub Message =======> " + message);
        logger.info("Redis Pub/Sub Message =======> {}", message);
    }
}
