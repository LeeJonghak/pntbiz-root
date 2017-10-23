package api.geofencing.service;

import core.api.geofencing.dao.GeofencingRedisDao;
import core.common.geofencing.domain.Geofencing;
import framework.geofence.Geofence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jhlee on 2017-04-15.
 */
@Service
public class GeofencingRedisServiceImpl implements GeofencingRedisService {

    @Autowired
    private GeofencingRedisDao geofencingRedisDao;

    @Override
    public Map<Object, Object> getGeofencingInfo(String fcNum) {
        return geofencingRedisDao.getGeofenceInfo(fcNum);
    }

    @Override
    public List<Map<Object, Object>> getGeofencingList(String uuid) {
        List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
        Set<?> set = geofencingRedisDao.getGeofenceList(uuid);
        int idx = 0;
        for(Object fcNum : set) {
            String strFcNum = fcNum.toString();
            Map<Object, Object> geofencing = geofencingRedisDao.getGeofenceInfo(strFcNum);
            List<Object> latlngs = geofencingRedisDao.getGeofenceLatlng(strFcNum);
            geofencing.put("latlngs", latlngs);
            list.add(idx, geofencing);
            idx++;
         }
        return list;
    }

    /**
     *
     * @param SUUID 업체 UUID
     * @param floor 층
     * @param lat   위도
     * @param lng   경도
     * @return 지오펜스영역인 fence list
     */
     public List<Geofencing> checkGeofence(String SUUID, String floor, Double lat, Double lng) {
         List<Geofencing> res = new ArrayList<Geofencing>();
         List<Map<Object, Object>> list = getGeofencingList(SUUID);
         for (Map<Object, Object> geofencing : list) {
//             Geofencing fence = (Geofencing) geofencing;
//             Map<String, PresenceRequestParam> fence = new HashMap<String, PresenceRequestParam>();
//             fence.put("floor", (String) geofencing.get("floor"));
//             fence.put("fcNum", (Long) geofencing.get("fcNum"));
//             fence.put("fcName", (String) geofencing.get("fcName"));
//             fence.put("evtEnter", (Integer) geofencing.get("evtEnter"));
//             fence.put("evtLeave", (Integer) geofencing.get("evtLeave"));
//             fence.put("evtStay", (Integer) geofencing.get("evtStay"));
//             fence.put("numEnter", (Integer) geofencing.get("numEnter"));
//             fence.put("numLeave", (Integer) geofencing.get("numLeave"));
//             fence.put("numStay", (Integer) geofencing.get("numStay"));
//             fence.put("field1", (String) geofencing.get("field1"));
//             fence.put("field2", (String) geofencing.get("field2"));
//             fence.put("field3", (String) geofencing.get("field3"));
//             fence.put("field4", (String) geofencing.get("field4"));
//             fence.put("field5", (String) geofencing.get("field5"));
//             if (!fence.get("floor").equals(floor)) continue;
             Geofencing fence = new Geofencing();
             fence.setFloor(geofencing.get("floor").toString());
             fence.setFcNum(Long.parseLong(geofencing.get("fcNum").toString()));
             fence.setFcName(geofencing.get("fcName").toString());
//             fence.setEvtEnter(Integer.parseInt(geofencing.get("evtEnter").toString()));
//             fence.setEvtLeave(Integer.parseInt(geofencing.get("evtLeave").toString()));
//             fence.setEvtStay(Integer.parseInt(geofencing.get("evtStay").toString()));
             fence.setEvtEnter(5);
             fence.setEvtLeave(5);
             fence.setEvtStay(10);
             fence.setNumEnter(Integer.parseInt(geofencing.get("numEnter").toString()));
             fence.setNumLeave(Integer.parseInt(geofencing.get("numLeave").toString()));
             fence.setNumStay(Integer.parseInt(geofencing.get("numStay").toString()));
             fence.setField1(geofencing.get("field1").toString());
             fence.setField2(geofencing.get("field2").toString());
             fence.setField3(geofencing.get("field3").toString());
             fence.setField4(geofencing.get("field4").toString());
             fence.setField5(geofencing.get("field5").toString());

             if (!fence.getFloor().equals(floor)) continue;
//             List<Map<String, Double>> bounds = new ArrayList<Map<String, Double>>();
             List<Geofence.LatLng> bounds = new ArrayList<Geofence.LatLng>();
             for (Object latlngs : (List<Object>) geofencing.get("latlngs")) {
                 String[] latlngArr = latlngs.toString().split("\\|");
                 Geofence.LatLng latlng = new Geofence.LatLng();
                 latlng.setLat(Double.parseDouble(latlngArr[0]));
                 latlng.setLng(Double.parseDouble(latlngArr[1]));
//                 Map<String, Double> latlng = new HashMap<String, Double>();
//                 latlng.put("lat", Double.parseDouble(latlngArr[0]));
//                 latlng.put("lng", Double.parseDouble(latlngArr[1]));
                 bounds.add(latlng);
             }
             if (Geofence.containLatlng(bounds, lat, lng)) {
                 res.add(fence);
             }
         }
         return res;
    }

    public String getGeofenceZoneTagPrefix() {
        return geofencingRedisDao.GEOFENCE_ZONE_TAG_PREFIX;
    }

    public void setGeofenceZoneTag(String key, Map<Object, Object> zoneData) {
        geofencingRedisDao.setGeofenceZoneTag(key, zoneData);
    }
    public Map<Object, Object> getGeofenceZoneTag(String key) {
        return geofencingRedisDao.getGeofenceZoneTag(key);
    }
    public void deleteGeofenceZoneTag(String key) {
        geofencingRedisDao.deleteGeofenceZoneTag(key);
    }
}
