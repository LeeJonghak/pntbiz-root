package core.common.beacon.dao;

import core.common.beacon.domain.BeaconExternal;
import core.common.beacon.domain.BeaconExternalWidthRestrictedZone;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nsyun on 17. 8. 17..
 */
@Repository
public class BeaconExternalDao extends BaseDao {

	public BeaconExternal getBeaconExternalInfo(BeaconExternal param) throws DataAccessException {
		return (BeaconExternal) select("getBeaconExternalInfo", param);
	}

	public void updateBeaconExternalInfo(BeaconExternal param) throws DataAccessException {
		update("updateBeaconExternalInfo", param);
	}

	public List<BeaconExternal> getBeaconExternalList(BeaconExternal param) throws DataAccessException {
		return (List<BeaconExternal>) list("getBeaconExternalList", param);
	}

	public List<BeaconExternalWidthRestrictedZone> getBeaconExternalWidthRestrictedZoneList(BeaconExternal param) throws DataAccessException {
		return (List<BeaconExternalWidthRestrictedZone>) list("getBeaconExternalWidthRestrictedZoneList", param);
	}
}
