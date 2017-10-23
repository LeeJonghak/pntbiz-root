package api.presence.service;

import core.common.presence.bo.domain.ScannerPresenceRedis;
import framework.util.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.api.log.domain.PresenceBeaconLog;
import core.api.log.domain.PresenceLog;
import core.api.presence.dao.PresenceRedisDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jhlee on 2017-04-15.
 */
@Service
public class PresenceRedisServiceImpl implements PresenceRedisService {

    @Autowired
    private PresenceRedisDao presenceRedisDao;

    @Override
    public void setPresenceBeacon(String key, PresenceLog presenceLog) {
        presenceRedisDao.setPresenceBeacon(key, presenceLog);
    }
    @Override
    public void setPresenceDevice(String key, PresenceBeaconLog presenceBeaconLog) {
        presenceRedisDao.setPresenceDevice(key, presenceBeaconLog);
    }
    @Override
    public void deletePresenceBeacon(String key) {
    	presenceRedisDao.deletepresenceBeacon(key);
    }
    @Override
    public void deletePresenceDevice(String key) {
    	presenceRedisDao.deletepresenceDevice(key);
    }

    @Override
    public List<PresenceLog> getPresenceBeaconList(String keys) {
        List<PresenceLog> list = new ArrayList<PresenceLog>();
        Set<Object> redisList =  presenceRedisDao.getPresenceBeaconList(keys);
        for(Object key : redisList) {
            String prekey = key.toString();
            Map<Object, Object> pre = presenceRedisDao.hgetall(prekey);
            PresenceLog presence = new PresenceLog();
            presence.setUUID(pre.get("UUID").toString());
            presence.setMajorVer(Integer.parseInt(pre.get("majorVer").toString()));
            presence.setMinorVer(Integer.parseInt(pre.get("minorVer").toString()));
            presence.setLat(Double.parseDouble(pre.get("lat").toString()));
            presence.setLng(Double.parseDouble(pre.get("lng").toString()));
            presence.setFloor(pre.get("floor").toString());
            try {
            	presence.setBattery(Integer.parseInt(pre.get("battery").toString()));
            } catch(Exception e) {
            	presence.setBattery(0);
            }
            presence.setLastTime(Long.parseLong(pre.get("lastTime").toString()));
            presence.setRegionCode(pre.get("regionCode").toString());
            presence.setZoneCode(pre.get("zoneCode").toString());
            presence.setBuildingCode(pre.get("buildingCode").toString());
            presence.setFloorCode(pre.get("floorCode").toString());
            presence.setAreaCode(pre.get("areaCode").toString());
            presence.setFcNum(Long.parseLong(pre.get("fcNum").toString()));
            presence.setFcName(pre.get("fcName").toString());
            list.add(presence);
        }
        return list;
    }

    @Override
    public PresenceLog getPresenceBeacon(String key) {
        Map<Object, Object> redisObject = presenceRedisDao.getPresenceBeacon(key);

        return (redisObject.size() == 0) ? null : new ObjectMapper().convertValue(redisObject, PresenceLog.class);
    }

    public ScannerPresenceRedis getScannerPresenceRedis(String key) {
        String result = (String) presenceRedisDao.getRedisPresenceData(key);
        return (result == null) ? null : JsonUtils.readValue(result, ScannerPresenceRedis.class);
    }

    public void setScannerPresenceRedis(ScannerPresenceRedis scannerPresenceRedis) {
        presenceRedisDao.setRedisPresenceData(
                scannerPresenceRedis.getSUUID() + "_"
                        + scannerPresenceRedis.getUUID() + "_"
                        + scannerPresenceRedis.getMajorVer() + "_"
                        + scannerPresenceRedis.getMinorVer(),
                JsonUtils.writeValue(scannerPresenceRedis)
        );
    }
}
