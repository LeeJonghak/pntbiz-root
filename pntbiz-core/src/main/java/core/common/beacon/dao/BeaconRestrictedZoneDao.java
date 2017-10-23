package core.common.beacon.dao;

import core.common.beacon.domain.BeaconRestrictedZone;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nsyun on 17. 8. 17..
 */
@Repository
public class BeaconRestrictedZoneDao extends BaseDao {

	public Integer getBeaconRestrictedZoneCount(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		return (Integer) this.select("getBeaconRestrictedZoneCount", beaconRestrictedZone);
	}

	public List<BeaconRestrictedZone> getBeaconRestrictedZoneList(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		return (List<BeaconRestrictedZone>) this.list("getBeaconRestrictedZoneList", beaconRestrictedZone);
	}

	public void insertBeaconRestrictedZone(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		insert("insertBeaconRestrictedZone", beaconRestrictedZone);
	}

	public BeaconRestrictedZone getBeaconRestrictedZoneInfo(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		return (BeaconRestrictedZone) select("getBeaconRestrictedZoneInfo", beaconRestrictedZone);
	}

	public void updateBeaconRestrictedZone(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		update("updateBeaconRestrictedZone", beaconRestrictedZone);
	}

	public void deleteBeaconRestrictedZone(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
		delete("deleteBeaconRestrictedZone", beaconRestrictedZone);
	}



}
