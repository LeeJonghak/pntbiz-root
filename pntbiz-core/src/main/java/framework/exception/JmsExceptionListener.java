package framework.exception;

import javax.jms.JMSException;
import javax.jms.ExceptionListener;

/**
 * Created by jhlee on 2017-08-24.
 */
public class JmsExceptionListener implements ExceptionListener {
    public void onException(final JMSException e) {
        e.printStackTrace();
    }
}
