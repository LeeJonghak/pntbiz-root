package api.presence.bo.factory;

import api.presence.bo.PresenceTaskExecutor;
import api.presence.bo.domain.PresenceDataPackage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

/**
 * Presnece 처리를 위한 Task Chain를 생성하여 반환하는 Class
 *
 * Created by ucjung on 2017-06-08.
 */
@Service
public class PresenceTaskExecutorChainFactoryImpl implements PresenceTaskExecutorChainFactory {
    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public PresenceTaskExecutor getTaskExecutorChain(PresenceDataPackage dataPackage) {
        return (dataPackage.getMode().equals("B"))
                ? (PresenceTaskExecutor) applicationContext.getBean("beaconPresenceTaskExecutorChain")
                : (PresenceTaskExecutor) applicationContext.getBean("scannerPresenceTaskExecutorChain");
    }


}
