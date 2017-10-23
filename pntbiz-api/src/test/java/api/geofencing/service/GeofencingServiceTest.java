package api.geofencing.service;

import common.JUnitTestBase;
import core.api.geofencing.domain.GeofencingZone;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * Created by jhlee on 2017-08-21.
 */
public class GeofencingServiceTest extends JUnitTestBase {

    @Autowired
    GeofencingService geofencingService;

    @Test
    public void getGeofencingFloorCodeListTest() throws ParseException {
        String UUID = "44790BA4-7EB3-4095-9E14-4B43AE67512B";
        // 지오펜스의 노드 갯수만큼 list가 나오면 된다.
        List<GeofencingZone> list = geofencingService.getGeofencingFloorCodeList(UUID, null);
    }
}
