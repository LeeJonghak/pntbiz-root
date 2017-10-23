package framework.activemq;

import api.common.service.ActivemqService;
import org.springframework.stereotype.Component;

import javax.jms.*;
import java.io.IOException;

/**
 * Created by jhlee on 2017-08-29.
 */
@Component
public class ActivemqMessageListener<T> implements MessageListener {

    private ActivemqService activemqService;

    public ActivemqMessageListener(ActivemqService activemqService) {
        this.activemqService = activemqService;
    }

    @Override
    public void onMessage( final Message message )
    {
        try {
            activemqService.receiveMessage(message);
        } catch (JMSException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        activemqService.sendMessage();
    }
}
