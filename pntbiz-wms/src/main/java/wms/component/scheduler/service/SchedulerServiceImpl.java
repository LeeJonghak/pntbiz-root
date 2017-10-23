package wms.component.scheduler.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import wms.component.scheduler.dao.SchedulerDao;
import core.wms.monitoring.dao.MonitoringDao;

@Service
public class SchedulerServiceImpl implements SchedulerService {

    @Autowired
    private SchedulerDao dao;
    @Autowired
    private MonitoringDao redisDao;

    @Override
    public void deletePresenceLogSchedule() throws DataAccessException {
        dao.deletePresenceLogSchedule();
    }


    @Override
    public int selectComNum() throws DataAccessException {
        return dao.selectComNum();
    }

    @Override
    public void deleteFloorCode(Integer comNum) throws DataAccessException {
        dao.deleteFloorCode(comNum);
    }

    @Override
    public void insertFloorCode(Integer comNum) throws DataAccessException {
        dao.insertFloorCode(comNum);
    }

    @Override
    public List<Map<String, Object>> selectBeaconList() throws DataAccessException{
        return dao.selectBeaconList();
    }


    @Override
    public void updateRtlBeaconStat(Map<String, Object> paramMap) throws DataAccessException {
        StringBuffer sb = new StringBuffer();
        sb.append("PRESENCE_BEACON_")
          .append(paramMap.get("UUID"))
          .append("_")
          .append(paramMap.get("UUID"))
          .append("_")
          .append(paramMap.get("majorVer"))
          .append("_")
          .append(paramMap.get("minorVer"));

        String tagId = (String)paramMap.get("tagId");
        String existYn = (String)paramMap.get("existYn");
        Map<Object, Object> rtnInfo  = redisDao.getRedisInfo(sb.toString());
        if(rtnInfo != null && rtnInfo.get("lastTime") != null){
            rtnInfo.put("tagId", tagId);

            if(existYn.equals("U")){
                dao.updateRtlBeaconStatus(rtnInfo);
            }else{
                dao.insertRtlBeaconStatus(rtnInfo);
            }
        }

    }
}
