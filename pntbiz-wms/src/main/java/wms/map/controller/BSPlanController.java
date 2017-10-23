package wms.map.controller;

import core.wms.beacon.domain.Beacon;
import wms.beacon.service.BeaconService;
import framework.web.util.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by nohsoo on 15. 7. 6.
 */
@Controller
public class BSPlanController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BeaconService beaconService;

    @RequestMapping(value="/map/bsplan.do", method = RequestMethod.GET)
    public String BeaconStatusMap() {

        return "/map/bsplan";
    }

    @RequestMapping(value = "/map/beacon/state.do", method = RequestMethod.POST)
    @ResponseBody
    public String beaconStatus(@RequestParam(value = "beaconNum", required = true)Long beaconNum,
                               @RequestParam(value = "state", required = false)String state,
                               @RequestParam(value = "reason", required = false)String reason) throws IOException {

        Map<String, String> info = new HashMap<String, String>();
        try {

            Beacon beacon = beaconService.getBeacon(beaconNum);
            if (beacon == null) {
                info.put("result", "3");
            } else {
                Beacon vo = new Beacon();
                vo.setBeaconNum(beaconNum);
                vo.setState(state);
                vo.setStateReason(reason);
                beaconService.modifyBeacon(vo);
                info.put("result", "1");
            }
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

}
