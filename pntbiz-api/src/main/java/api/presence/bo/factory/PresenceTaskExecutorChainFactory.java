package api.presence.bo.factory;

import api.presence.bo.domain.PresenceDataPackage;
import api.presence.bo.PresenceTaskExecutor;

/**
 * Created by ucjung on 2017-06-26.
 */
public interface PresenceTaskExecutorChainFactory {
    PresenceTaskExecutor getTaskExecutorChain(PresenceDataPackage dataPackage);
}
