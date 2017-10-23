package api.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 *  Floor 및 Geofence 연동 테스트를 위한 Dummy Controller
 */
@RestController
public class ApiDummyController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // Floor In 더미 API
    @RequestMapping(value="/mock/floor/in", method=RequestMethod.POST)
    @ResponseBody
    public Object floorIn(
            @RequestBody Map<String, Object> requestJson
    ) throws Exception {

        logger.info("[FLOOR] [IN] requested ------------------------------------------");
        logger.info("SUUID : {}\n{}", requestJson);
        logger.info("-----------------------------------------------------------------\n");

        return getResult(requestJson);
    }

    // Floor Out 더미 API
    @RequestMapping(value="/mock/floor/out", method=RequestMethod.POST)
    @ResponseBody
    public Object floorOut(
            @RequestBody Map<String, Object> requestJson
    ) throws Exception {

        logger.info("[FLOOR] [OUT] requested -----------------------------------------");
        logger.info("SUUID : {}\n{}", requestJson);
        logger.info("-----------------------------------------------------------------\n");

        return getResult(requestJson);
    }

    // Floor Out 더미 API
    @RequestMapping(value="/mock/floor/stay", method=RequestMethod.POST)
    @ResponseBody
    public Object floorStay(
            @RequestBody Map<String, Object> requestJson
    ) throws Exception {

        logger.info("[FLOOR] [OUT] requested -----------------------------------------");
        logger.info("{}", requestJson);
        logger.info("-----------------------------------------------------------------\n");

        return getResult(requestJson);
    }

    // Geofence In 더미 API
    @RequestMapping(value="/mock/geofence/in", method=RequestMethod.POST)
    @ResponseBody
    public Object geofenceIn(
            @RequestBody Map<String, Object> requestJson
    ) throws Exception {

        logger.info("[GEOFENCE] [IN] requested ---------------------------------------");
        logger.info("SUUID : {}\n{}", requestJson);
        logger.info("-----------------------------------------------------------------\n");

        return getResult(requestJson);
    }

    // Geofence Out 더미 API
    @RequestMapping(value="/mock/geofence/out", method=RequestMethod.POST)
    @ResponseBody
    public Object geofenceOut(
            @RequestBody Map<String, Object> requestJson
    ) throws Exception {

        logger.info("[GEOFENCE] [OUT] requested --------------------------------------");
        logger.info("SUUID : {}\n{}", requestJson);
        logger.info("-----------------------------------------------------------------\n");

        return getResult(requestJson);
    }

    // Geofence Stay 더미 API
    @RequestMapping(value="/mock/geofence/stay", method=RequestMethod.POST)
    @ResponseBody
    public Object geofenceStay(
            @RequestBody Map<String, Object> requestJson
    ) throws Exception {

        logger.info("[GEOFENCE] [STAY] requested -------------------------------------");
        logger.info("SUUID : {}\n{}", requestJson);
        logger.info("-----------------------------------------------------------------\n");

        return getResult(requestJson);
    }

    // Restriction In 더미 API
    @RequestMapping(value="/mock/restiction/in", method=RequestMethod.POST)
    @ResponseBody
    public Object retictedZoneIn(
            @RequestBody Map<String, Object> requestJson
    ) throws Exception {

        logger.info("[Restriction Zone] [IN] requested ---------------------------------");
        logger.info("SUUID : {}\n{}", requestJson);
        logger.info("-----------------------------------------------------------------\n");

        return getResult(requestJson);
    }

    // Restriction Out 더미 API
    @RequestMapping(value="/mock/restiction/out", method=RequestMethod.POST)
    @ResponseBody
    public Object retictedZoneOut(
            @RequestBody Map<String, Object> requestJson
    ) throws Exception {

        logger.info("[Restriction Zone] [Out] requested --------------------------------");
        logger.info("SUUID : {}\n{}", requestJson);
        logger.info("-----------------------------------------------------------------\n");

        return getResult(requestJson);
    }

    // 위치 측위 결과 Interface
    @RequestMapping(value="/mock/location/change", method=RequestMethod.POST)
    @ResponseBody
    public Object locationChangeing(
            @RequestBody Map<String, Object> requestJson
    ) throws Exception {

        logger.info("[Location] [Change] requested -----------------------------------");
        logger.info("SUUID : {}\n{}", requestJson);
        logger.info("-----------------------------------------------------------------\n");

        return getResult(requestJson);
    }

    private Map<String, Object> getResult(@RequestBody Map<String, Object> requestJson) {
        Map<String, Object> res = new HashMap<>();
        res.put("result", "success");
        res.put("code", "0");
        res.put("body", requestJson);
        return res;
    }
}