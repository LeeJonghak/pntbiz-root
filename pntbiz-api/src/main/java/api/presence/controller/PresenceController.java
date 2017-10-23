package api.presence.controller;

import api.common.service.CommonService;
import api.presence.bo.PresenceBo;
import api.presence.service.PresenceSetmapService;
import core.api.common.domain.Company;
import core.api.presence.domain.PresenceSetmap;
import framework.exception.PresenceException;
import framework.web.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PresenceController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommonService commonService;

    @Autowired
    private PresenceSetmapService presenceSetmapService;

    @Autowired
    private PresenceBo presenceBo;

    // 실시간 위치 전송
    @RequestMapping(value="/presence/send/{SUUID}", method=RequestMethod.POST)
    @ResponseBody
    public Object presencePost(
            @RequestBody String requestJson,
            @PathVariable(value="SUUID", required = true) String SUUID
    ) throws Exception {

        Map<String, Object> res = new HashMap<>();

        try{
            presenceBo.execute(requestJson);
            res.put("result", "success");
            res.put("code", "0");
        } catch (PresenceException ex) {
            res.put("result", ex.getMessage());
            res.put("code", ex.getErrorCode());
        }

        return res;
    }

    // 실시간 위치 전송
    @RequestMapping(value="/presence/send/{SUUID}", method=RequestMethod.POST, consumes="application/json")
    @ResponseBody
    public Object presenceJsonPost(
            @RequestBody Map<String, Object> requestJson,
            @PathVariable(value="SUUID", required = true) String SUUID
    ) throws Exception {

        Map<String, Object> res = new HashMap<>();
        try{
            presenceBo.execute(requestJson);
            res.put("result", "success");
            res.put("code", "0");
        } catch (PresenceException ex) {
            res.put("result", ex.getMessage());
            res.put("code", ex.getErrorCode());
        }

        return res;
    }



    // 프레즌스 설정 맵 정보
    @RequestMapping(value="/presence/setmap/info/{UUID}", method=RequestMethod.GET)
    @ResponseBody
    public Object setMapInfo(HttpServletRequest request, Company company, PresenceSetmap presenceSetmap,
                             @PathVariable("UUID") String UUID)
            throws IOException, ServletException {
        Map<String, Object> res = new HashMap<String, Object>();
        UUID = StringUtil.NVL(UUID, "");

        commonService.checkAuthorized(UUID);

        // 필수 값 체크
        if("".equals(UUID)) {
            res.put("result", "error");
            res.put("code", "200");
        } else {
            company.setUUID(UUID);
            try {
                Company companyInfo = commonService.getCompanyInfoByUUID(company);
                presenceSetmap.setComNum(companyInfo.getComNum());
                presenceSetmap.setMapType("S"); // 스캐너 맵타입
                PresenceSetmap presenceSetmapInfo = presenceSetmapService.getPresenceSetmapInfo(presenceSetmap);

                Map<String, Object> setmap = new HashMap<String, Object>();
                setmap.put("lat", companyInfo.getLat());
                setmap.put("lng", companyInfo.getLng());
                setmap.put("initZoom", presenceSetmapInfo.getInitZoom());
                setmap.put("initFloor", presenceSetmapInfo.getInitFloor());
                setmap.put("checkTimeInterval", presenceSetmapInfo.getCheckTimeInterval());
                setmap.put("removeTimeInterval", presenceSetmapInfo.getRemoveTimeInterval());
                setmap.put("moveTimeInterval", presenceSetmapInfo.getMoveTimeInterval());
                setmap.put("moveUnit", presenceSetmapInfo.getMoveUnit());

                res.put("result", "success");
                res.put("code", "0");
                res.put("data", setmap);
                logger.info("setmap info : {}", setmap);
            } catch(Exception e) {
                res.put("result", "error");
                res.put("code", "303");
            }
        }
        return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
    }
}