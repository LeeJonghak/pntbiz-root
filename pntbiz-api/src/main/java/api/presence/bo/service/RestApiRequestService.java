package api.presence.bo.service;

import api.presence.bo.domain.PresenceDataPackage;
import core.common.presence.bo.domain.ZoneInOutState;
import core.common.enums.InterfaceCommandType;

/**
 * Created by ucjung on 2017-06-26.
 */
public interface RestApiRequestService {
    void send(InterfaceCommandType command, PresenceDataPackage presenceVo, ZoneInOutState zoneInOutState);

    void send(InterfaceCommandType command, PresenceDataPackage presenceVo);
}
