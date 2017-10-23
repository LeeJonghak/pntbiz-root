package api.presence.bo.strategy;

import api.presence.bo.domain.PresenceDataPackage;
import api.presence.bo.service.PresenceServiceDelegator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by ucjung on 2017-06-05.
 */
public abstract class AbstractPresenceStrategy {

    final protected Logger logger = LoggerFactory.getLogger(this.getClass());

    protected PresenceDataPackage presenceVo;
    protected PresenceServiceDelegator serviceDelegator;

    protected void setInitData(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        this.presenceVo = presenceVo;
        this.serviceDelegator = serviceDelegator;
    }

    protected void setPresenceDataPackage(PresenceDataPackage presenceVo) {

    }

    protected void doStrategy(PresenceDataPackage presenceVo) {

    }

    final public void execute(PresenceDataPackage presenceVo, PresenceServiceDelegator serviceDelegator) {
        setInitData(presenceVo, serviceDelegator);
        doStrategy(presenceVo);
        setPresenceDataPackage(presenceVo);
    }
}
