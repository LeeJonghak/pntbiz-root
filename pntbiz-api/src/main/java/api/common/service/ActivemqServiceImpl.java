package api.common.service;

import org.springframework.stereotype.Service;

import javax.jms.Message;

/**
 * Created by jhlee on 2017-08-29.
 */
@Service
public class ActivemqServiceImpl implements ActivemqService {
    @Override
    public void receiveMessage(Message message) {
        System.out.println("org method");
    }

    @Override
    public void sendMessage() {

    }
}
