package api.presence.bo.handler;

import api.presence.bo.domain.PresenceDataPackage;
import api.presence.bo.service.PresenceServiceDelegator;

/**
 * Created by ucjung on 2017-06-05.
 */
public interface PresenceTaskEventHandler {
    public void doEvent(PresenceDataPackage presenceDataPackage, PresenceServiceDelegator serviceDelegator);
}
