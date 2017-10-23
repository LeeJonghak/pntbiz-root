package wms.component.scheduler.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

public interface SchedulerService {
    public void deletePresenceLogSchedule() throws DataAccessException;

    public int selectComNum() throws DataAccessException;
    public void deleteFloorCode(Integer comNum) throws DataAccessException;
    public void insertFloorCode(Integer comNum) throws DataAccessException;

    public List<Map<String, Object>> selectBeaconList() throws DataAccessException;
    public void updateRtlBeaconStat(Map<String, Object> paramMap) throws DataAccessException;
}