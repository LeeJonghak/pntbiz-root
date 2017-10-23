package core.common.beacon.dao;

import core.common.beacon.domain.BeaconExternalLog;
import framework.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by nsyun on 17. 8. 18..
 */
@Repository
public class BeaconExternalLogDao extends BaseDao {

	public void insertBeaconExternalLog(BeaconExternalLog vo) {
		insert("insertBeaconExternalLog", vo);
	}

	public List<BeaconExternalLog> listBeaconExternalLog(BeaconExternalLog param) {

		return (List<BeaconExternalLog>)list("listBeaconExternalLog", param);

	}
}
