package api.beacon.service;


import api.beacon.domain.BeaconExternalRequestParam;
import core.common.beacon.domain.BeaconExternal;
import core.common.beacon.domain.BeaconExternalLog;
import core.common.beacon.domain.BeaconExternalWidthRestrictedZone;
import core.common.beacon.domain.BeaconRestrictedZoneLog;
import core.common.enums.AssignUnassignType;

import java.util.List;

/**
 * Created by nsyun on 17. 7. 20..
 */

public interface BeaconExternalService {

	void assign(Integer beaconNum, BeaconExternalRequestParam requestParam);

	void assign(Long beaconNum, BeaconExternalRequestParam requestParam);

	void unassign(Integer beaconNum);

	void unassign(Long beaconNum);

	public BeaconExternal info(Integer beaconNum);

	public BeaconExternal info(Long beaconNum);

	public List<BeaconExternalWidthRestrictedZone> list(String UUID, String floor);

	public List<BeaconExternalLog> listExternalLog(Integer beaconNum);

	public List<BeaconExternalLog> listExternalLog(Integer beaconNum, AssignUnassignType type);

	public List<BeaconExternalLog> listExternalLog(Long beaconNum);

	public List<BeaconExternalLog> listExternalLog(Long beaconNum, AssignUnassignType type);

	public List<BeaconRestrictedZoneLog> listRestrictedZoneLog(Integer beaconNum);

	public List<BeaconRestrictedZoneLog> listRestrictedZoneLog(Integer beaconNum, AssignUnassignType type);

	public List<BeaconRestrictedZoneLog> listRestrictedZoneLog(Long beaconNum);

	public List<BeaconRestrictedZoneLog> listRestrictedZoneLog(Long beaconNum, AssignUnassignType type);

}
