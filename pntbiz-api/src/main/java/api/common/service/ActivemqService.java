package api.common.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import java.io.IOException;

/**
 * Created by jhlee on 2017-08-29.
 */
public interface ActivemqService {
    void receiveMessage(Message message) throws JMSException, IOException;
    void sendMessage();
}
