package framework.redis;

import api.presence.bo.service.LostBeaconSignalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * Beacon 신호가 수신되지 않아 알림용 Redis Key가 Expire 될 경우에 대한 Redis Event Listener
 * Created by ucjung on 2017-06-26.
 */
@Component
public class RedisKeyExpireMessageListener implements MessageListener {
    public final String PRESENCE_BEACON_NOTIFY_PREFIX = "NOTIFY_PRESENCE_BEACON_";
    public final String PRESENCE_BEACON_PREFIX = "PRESENCE_BEACON_";

    @Autowired
    private LostBeaconSignalService lostBeaconSignalService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onMessage(Message message, byte[] channel) {
        logger.info("Message Received at Listener Start \n- channel : {}\n- message : {}", new String(channel), message.toString());
        if (message.toString().startsWith(PRESENCE_BEACON_NOTIFY_PREFIX))
            lostBeaconSignalService.excute(message.toString());
        logger.info("Message Received at Listener End \n- channel : {}\n- message : {}", new String(channel), message.toString());
    }
}
