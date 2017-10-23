package core.api.beacon.dao;

import core.api.beacon.domain.Beacon;
import core.api.beacon.domain.BeaconContents;
import core.api.beacon.domain.BeaconState;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class BeaconDao extends BaseDao {
	
	public Beacon getBeaconInfo(Beacon beacon) throws DataAccessException {
		return (Beacon) select("getBeaconInfo", beacon);
	}

    public Beacon getBeaconInfoByMacAddr(String macAddr) throws DataAccessException {
        return (Beacon) select("getBeaconInfoMacAddress", macAddr);
    }

	public void insertBeaconState(BeaconState beaconState) throws DataAccessException {
		insert("insertBeaconState", beaconState);
	}

    public List<?> getBeaconList(HashMap<String, Object> param) {
        List<?> list = this.list("getBeaconList", param);
        return list;
    }
    
    public List<?> getBeaconListByAll(HashMap<String, Object> param) {
        List<?> list = this.list("getBeaconListByAll", param);
        return list;
    }

    public List<?> getBeaconContentsList(HashMap<String, Object> param) {
        return this.list("getBeaconContentsList", param);
    }

    public List<?> getBeaconActionList(HashMap<String, Object> param) {
        return this.list("getBeaconActionList", param);
    }

    public List<BeaconContents> getBeaconFloorCodeList(HashMap<String, Object> param) {
        List<BeaconContents> list = new ArrayList<BeaconContents>();
        list = (List<BeaconContents>) this.list("getBeaconFloorCodeList", param);
        return list;
    }

    public List<BeaconContents> getBeaconFloorCodeListByField(HashMap<String, Object> param) {
        List<BeaconContents> list = new ArrayList<BeaconContents>();
        list = (List<BeaconContents>) this.list("getBeaconFloorCodeListByField", param);
        return list;
    }

    public void updateScannerBeaconState(Beacon beacon) {
        update("updateScannerBeaconState", beacon);
    }

}