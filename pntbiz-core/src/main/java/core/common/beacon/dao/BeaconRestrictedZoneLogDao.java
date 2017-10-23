package core.common.beacon.dao;

import core.common.beacon.domain.BeaconExternalLog;
import core.common.beacon.domain.BeaconRestrictedZoneLog;
import framework.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nsyun on 17. 8. 18..
 */
@Repository
public class BeaconRestrictedZoneLogDao extends BaseDao {

	public void insertBeaconRestrictedZoneLog(BeaconRestrictedZoneLog vo) {
		insert("insertBeaconRestrictedZoneLog", vo);
	}

	public List<BeaconRestrictedZoneLog> listBeaconRestrictedZoneLog(BeaconRestrictedZoneLog param) {
		return (List<BeaconRestrictedZoneLog>)list("listBeaconRestrictedZoneLog", param);
	}
}
