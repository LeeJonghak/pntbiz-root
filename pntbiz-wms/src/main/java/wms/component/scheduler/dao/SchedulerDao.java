package wms.component.scheduler.dao;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class SchedulerDao extends BaseDao {

    public void deletePresenceLogSchedule() throws DataAccessException {
        delete("deletePresenceLogSchedule");
    }


    /*
     * LGD에서만 쓰는 schedualer
     * 추후 분리
     */
    public int selectComNum() throws DataAccessException {
        return (Integer)select("selectComNum");
    }
    public void deleteFloorCode(Integer comNum) throws DataAccessException {
        delete("deleteFloorCodeScheduler", comNum);
    }
    public void insertFloorCode(Integer comNum) throws DataAccessException {
        update("insertFloorCodeScheduler", comNum);
    }


    public List<Map<String, Object>> selectBeaconList() throws DataAccessException {
        return (List<Map<String, Object>>) list("selectBeaconList");
    }

    public int selectChkExistBeaconStatus(String value) throws DataAccessException {
        return (int) select("selectChkExistBeaconStatus", value);
    }

    public void insertRtlBeaconStatus(Map<Object, Object> paramMap) throws DataAccessException {
        insert("insertRtlBeaconStatus", paramMap);
    }

    public void updateRtlBeaconStatus(Map<Object, Object> paramMap) throws DataAccessException {
        insert("updateRtlBeaconStatus", paramMap);
    }
}