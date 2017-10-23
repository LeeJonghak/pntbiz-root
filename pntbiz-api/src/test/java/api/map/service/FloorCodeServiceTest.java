package api.map.service;

import api.presence.service.PresenceService;
import com.google.gson.Gson;
import common.JUnitTestBase;
import core.api.map.domain.FloorCode;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.util.List;

/**
 * Created by jhlee on 2017-08-03.
 */
public class FloorCodeServiceTest extends JUnitTestBase {

    @Autowired
    FloorService floorService;
    @Autowired
    FloorCodeService floorCodeService;
    @Autowired
    PresenceService presenceService;

    private Gson gson = new Gson();

    @Test
    public void getFloorCodeInfoTest() throws ParseException {
        String UUID = "44790BA4-7EB3-4095-9E14-4B43AE67512B";
        String nodeId = "PJ";
        FloorCode floorCode = new FloorCode();
        floorCode.setUUID(UUID);
        floorCode.setNodeId(nodeId);
//        floorCode.setFloorCodeNum(278);
        FloorCode floorCodeInfo = floorCodeService.getFloorCodeInfo(floorCode);
        logger.info("floorCode :", floorCodeInfo);
    }

    @Test
    public void getFloorCodeListTest() {
        String UUID = "44790BA4-7EB3-4095-9E14-4B43AE67512B";
        String upperNodeId = "FP7";
        FloorCode floorCode = new FloorCode();
        floorCode.setUUID(UUID);
        floorCode.setUpperNodeId(upperNodeId);
        List<FloorCode> floorCodeList = floorCodeService.getFloorCodeList(floorCode);
        logger.info("floorCodeList :", floorCodeList);
    }

    @Test
    public void getFloorCodeListHierarchyTest() {
        String UUID = "44790BA4-7EB3-4095-9E14-4B43AE67512B";
        FloorCode floorCode = new FloorCode();
        floorCode.setUUID(UUID);
//        floorCode.setLevelNo(1);
        List<FloorCode> floorCodeList = floorCodeService.getFloorCodeHierarchyList(floorCode);
        String json = gson.toJson(floorCodeList);
        logger.debug("floorCodeList : ", json);
    }

}