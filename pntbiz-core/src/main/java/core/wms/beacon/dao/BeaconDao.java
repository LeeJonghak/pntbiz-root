package core.wms.beacon.dao;

import java.util.HashMap;
import java.util.List;

import core.common.beacon.domain.BeaconRestrictedZone;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import core.wms.beacon.domain.Beacon;
import core.wms.beacon.domain.BeaconGroup;
import core.wms.beacon.domain.BeaconGroupMapping;
import core.wms.beacon.domain.BeaconGroupSearchParam;
import core.wms.beacon.domain.BeaconSearchParam;
import core.wms.beacon.domain.BeaconState;
import core.wms.beacon.domain.BeaconStateSearchParam;
import framework.db.dao.BaseDao;

/**
 */
@Repository
public class BeaconDao extends BaseDao {

    public long insertBeacon(Beacon beacon) {
        this.insert("insertBeacon", beacon);
        return beacon.getBeaconNum();
    }

    public List<?> getBeaconList(BeaconSearchParam paramVo) {
        List<?> list = this.list("getBeaconList", paramVo);
        return list;
    }

    public Integer getBeaconCount(BeaconSearchParam paramVo) {
        Integer count = (Integer)this.select("getBeaconCount", paramVo);
        return count;
    }

    public Beacon getBeacon(HashMap<String, Object> param) {
        Beacon beacon = (Beacon)this.select("getBeacon", param);
        return beacon;
    }

    public void modifyBeacon(Beacon vo) {
        this.update("modifyBeacon", vo);
    }

    public void updateBeaconBlankImage(Beacon vo) {
        this.update("updateBeaconBlankFile", vo);
    }

    public void deleteBeacon(HashMap<String, Object> param) {
        this.delete("deleteBeacon", param);
    }

    public Integer getBeaconStateCount(BeaconStateSearchParam param) {
        Integer count = (Integer)this.select("getBeaconStateCount", param);
        return count;
    }

    public List<?> getBeaconStateList(BeaconStateSearchParam param) {
        List<?> list = this.list("getBeaconStateList", param);
        return list;
    }

    @SuppressWarnings("unchecked")
	public List<BeaconState> getLogBeaconStateList(HashMap<String, Object> param) {
    	List<BeaconState> list = (List<BeaconState>) this.list("getLogBeaconStateList", param);
        return list;
    }

    @SuppressWarnings("unchecked")
	public List<BeaconState> getChartLogLossBeaconStateList(HashMap<String, Object> param) {
    	List<BeaconState> list = (List<BeaconState>) this.list("getChartLogLossBeaconStateList", param);
        return list;
    }

    @SuppressWarnings("unchecked")
	public List<Beacon> getLogLossBeaconStateList(HashMap<String, Object> param) {
    	List<Beacon> list = (List<Beacon>) this.list("getLogLossBeaconStateList", param);
        return list;
    }

    public void modifyBeaconState(BeaconState beaconState) {
        this.update("modifyBeaconState", beaconState);
    }

    public Integer getBeaconGroupCount(BeaconGroupSearchParam param) throws DataAccessException {
    	Integer cnt = 0;
        cnt = (Integer) select("getBeaconGroupCount", param);
        return cnt;
    }

    public List<?> getBeaconGroupList(BeaconGroupSearchParam param) throws DataAccessException {
        List<?> list = null;
        list = (List<?>) list("getBeaconGroupList", param);
        return list;
    }

    public BeaconGroup getBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException {
        return (BeaconGroup) select("getBeaconGroup", beaconGroup);
    }

    public void insertBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException {
        insert("insertBeaconGroup", beaconGroup);
    }

    public void updateBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException {
        update("updateBeaconGroup", beaconGroup);
    }

    public void deleteBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException {
        delete("deleteBeaconGroup", beaconGroup);
    }

    public void insertBeaconGroupMapping(BeaconGroupMapping beaconGroupMapping) throws DataAccessException {
        insert("insertBeaconGroupMapping", beaconGroupMapping);
    }

    public void updateBeaconGroupMapping(BeaconGroupMapping beaconGroupMapping) throws DataAccessException {
        insert("updateBeaconGroupMapping", beaconGroupMapping);
    }

    public void deleteBeaconGroupMapping(BeaconGroupMapping beaconGroupMapping) throws DataAccessException {
        delete("deleteBeaconGroupMapping", beaconGroupMapping);
    }

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

    public void updateAllBeaconRestrictedZoneForPermitted(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
        update("updateAllBeaconRestrictedZoneForPermitted", beaconRestrictedZone);
    }

    public void deleteBeaconRestrictedZone(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException {
        delete("deleteBeaconRestrictedZone", beaconRestrictedZone);
    }

}
