package api.map.service;

import core.api.log.domain.PresenceLog;
import core.api.map.dao.FloorAreaDao;
import core.api.map.dao.FloorDao;
import core.api.map.domain.Floor;
import core.api.map.domain.FloorAreaConfig;
import core.api.map.domain.FloorAreaInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Math.ceil;

@Service
public class FloorAreaServiceImpl implements FloorAreaService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private FloorDao floorDao;

    @Autowired
    private FloorAreaDao floorAreaDao;

    @Override
    public FloorAreaInfo getAreaInfo(PresenceLog presenceLog) throws Exception {

        Floor floor = getFloor(presenceLog);
        FloorAreaConfig floorAreaConfig = getFloorAreaConfig(floor);

        Double swLat = floor.getSwLat();
        Double swLng = floor.getSwLng();
        Double neLat= floor.getNeLat();
        Double neLng= floor.getNeLng();

        Double posLat = presenceLog.getLat();
        Double posLng = presenceLog.getLng();

        isValidPostion(swLat, swLng, neLat, neLng, posLat, posLng);

        Integer axisX =  ((Double)ceil(getDistance(swLat, swLng, swLat, posLng) / floorAreaConfig.getWidth())).intValue();
        Integer axisY =  ((Double)ceil(getDistance(posLat, neLng, neLat, neLng) / floorAreaConfig.getHeight())).intValue();

        return getFloorAreaInfo(floorAreaConfig, axisX, axisY);
    }

    private void isValidPostion(Double swLat, Double swLng, Double neLat, Double neLng, Double posLat, Double posLng) throws Exception {
        if ( posLat < swLat || posLat > neLat || posLng < swLng || posLng > neLng) {
            throw new Exception("FLOOR_POSITION_NOT_IN_AREA");
        }
    }

    private FloorAreaInfo getFloorAreaInfo(FloorAreaConfig floorAreaConfig, Integer axisX, Integer axisY) {
        FloorAreaInfo floorAreainfo = new FloorAreaInfo();

        List<FloorAreaInfo> floorAreaInfos = floorAreaDao.getFloorAresInfoList(floorAreaConfig);

        for (FloorAreaInfo fi : floorAreaInfos ) {
            if (fi.getAxisX().equals(axisX) && fi.getAxisY().equals(axisY)) {
                floorAreainfo = fi;
            }
        }
        return floorAreainfo;
    }

    private Double getDistance(Double lat1, Double lng1, Double lat2, Double lng2) {
        return 6371 * 1000 * Math.acos( Math.cos( Math.toRadians(lat1) ) * Math.cos( Math.toRadians( lat2 ) ) * Math.cos( Math.toRadians( lng2 ) - Math.toRadians(lng1) ) + Math.sin( Math.toRadians(lat1) ) * Math.sin( Math.toRadians( lat2 ) ) );
    }

    private FloorAreaConfig getFloorAreaConfig(Floor floor) throws Exception {
        List<FloorAreaConfig> floorAreaConfigs = floorAreaDao.getFloorAreaConfigList(floor.getComNum());

        FloorAreaConfig floorAreaConfig = null;
        for ( FloorAreaConfig f : floorAreaConfigs ) {
            if (f.getComNum().equals(floor.getComNum()) && f.getFloorNum().equals(floor.getFloorNum())) {
                floorAreaConfig = f;
                break;
            }
        }

        if (floorAreaConfig == null) {
            throw new Exception("FLOOR_AREA_CONFIG_NOT_EXIST");
        }
        return floorAreaConfig;
    }

    private Floor getFloor(PresenceLog presenceLog) throws Exception {
        Floor floor = new Floor();

        floor.setUUID(presenceLog.getSUUID());
        floor.setFloor(presenceLog.getFloor());

        List<?> floors = floorDao.getFloorList(floor);
        if (floors.size() > 0)
            floor = (Floor) floors.get(0);
        else
            throw new Exception("FLOOR_INFO_IS_NOT EXIST");

        return floor;
    }
}
