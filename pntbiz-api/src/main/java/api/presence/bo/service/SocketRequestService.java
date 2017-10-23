package api.presence.bo.service;

import api.presence.bo.domain.PresenceDataPackage;
import core.common.presence.bo.domain.ZoneInOutState;
import core.common.enums.InterfaceCommandType;
import core.common.enums.SocketCommandType;

/**
 * Created by ucjung on 2017-06-26.
 */
public interface SocketRequestService {
    void send(SocketCommandType command, PresenceDataPackage presenceVo);

    void send(SocketCommandType command, InterfaceCommandType interfaceCommandType, PresenceDataPackage presenceVo, ZoneInOutState zoneInOutState);

    void send(SocketCommandType command, InterfaceCommandType interfaceCommandType, PresenceDataPackage presenceVo);
}
