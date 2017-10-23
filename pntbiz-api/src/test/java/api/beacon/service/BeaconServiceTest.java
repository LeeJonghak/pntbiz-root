package api.beacon.service;

import api.map.service.FloorCodeService;
import common.JUnitTestBase;
import core.api.beacon.domain.BeaconContents;
import core.api.map.domain.FloorCode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhlee on 2017-08-10.
 */
public class BeaconServiceTest extends JUnitTestBase {
    @Autowired
    BeaconService beaconService;
    @Autowired
    FloorCodeService floorCodeService;

    @Test
    public void getBeaconFloorCodeList() {
        String UUID = "44790BA4-7EB3-4095-9E14-4B43AE67512B";
//        String conType = "CPN";
        String conType = "PRE";
        List<FloorCode> floorCodeDepthList = new ArrayList<FloorCode>();
        FloorCode flevel0 = new FloorCode();
        flevel0.setNodeId("PJ");
        flevel0.setLevelNo(0);
        floorCodeDepthList.add(flevel0);
        FloorCode flevel1 = new FloorCode();
        flevel1.setNodeId("FP7");
        flevel1.setLevelNo(1);
        floorCodeDepthList.add(flevel1);
        FloorCode flevel2 = new FloorCode();
//        flevel2.setNodeId("1012");
//        flevel2.setLevelNo(2);
//        floorCodeDepthList.add(flevel2);
//        FloorCode flevel3 = new FloorCode();
//        flevel3.setNodeId("10121");
//        flevel3.setLevelNo(3);
//        flevel3.setNodeId("10122");
//        flevel3.setLevelNo(3);
//        floorCodeDepthList.add(flevel3);
        FloorCode floorCode = new FloorCode();
        floorCode.setUUID(UUID);
        List<FloorCode> floorCodeChildNodeList = floorCodeService.getFloorCodeChildNodeList(floorCode, floorCodeDepthList);
        List<FloorCode> floorCodeLastChildNodeList = floorCodeService.getFloorCodeLastChildNodeList(floorCodeChildNodeList);
        List<BeaconContents> beaconList = beaconService.getBeaconFloorCodeListByField(UUID, conType, floorCodeLastChildNodeList);
        logger.info("beaconList : ", beaconList);
    }
}
