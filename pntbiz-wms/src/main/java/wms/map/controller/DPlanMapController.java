package wms.map.controller;


import core.wms.map.domain.Area;
import core.wms.map.domain.AreaLatlng;
import core.wms.sync.domain.Sync;
import framework.Security;
import framework.web.util.JsonUtil;
import framework.web.util.QueryParam;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import wms.component.auth.LoginDetail;
import wms.map.service.AreaService;
import wms.service.SyncService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 동관리 컨트롤러
 * Created by nohsoo on 2015-11-27
 */
@Controller
public class DPlanMapController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private AreaService areaService;

    @Autowired
    private SyncService syncService;

    @RequestMapping(value="/map/dplan.do", method = RequestMethod.GET)
    public String BeaconStatusMap() {

        return "/map/dplan";
    }

    @RequestMapping(value="/map/areaform.ajax.do", method = RequestMethod.GET)
    public String areaform(ModelMap map, @RequestParam(value = "areaNum", required = false)Integer areaNum) {

        if(areaNum!=null) {
            Area areaInfo = areaService.getAreaInfo(QueryParam.create("areaNum", areaNum).build());
            map.addAttribute("areaInfo", areaInfo);
        }

        return "/map/ajax.areaform";
    }

    @RequestMapping(value = "/map/area/list.do", method = RequestMethod.GET)
    @ResponseBody
    public String areaList(@RequestParam(value = "floor", required = true)String floor) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            List<?> list = areaService.getAreaList(QueryParam.create("comNum", loginDetail.getCompanyNumber()).put("floor", floor).build());
            info.put("result", "1");
            info.put("list", list);
        } catch(Exception e) {
            info.put("result", "2");
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    /**
     * 동 등록
     * @param areaName 동이름
     * @param floor 층
     * @param latlngJson 동영역 좌표 정보
     */
    @RequestMapping(value = "/map/area/reg.do", method = RequestMethod.POST)
    @ResponseBody
    public String areaReg(@RequestParam(value = "areaName", required = true)String areaName,
                          @RequestParam(value = "floor", required = true)String floor,
                          @RequestParam(value = "latlngJson", required = true)String latlngJson) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {

            LoginDetail loginDetail = Security.getLoginDetail();

            JSONArray jsonArray = new JSONArray(latlngJson);
            List<AreaLatlng> latlngList = new ArrayList<AreaLatlng>();
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                double lat = jsonObject.getDouble("lat");
                double lng = jsonObject.getDouble("lng");
                int orderSeq = jsonObject.getInt("orderSeq");
                AreaLatlng areaLatlng = new AreaLatlng();
                areaLatlng.setLat(lat);
                areaLatlng.setLng(lng);
                areaLatlng.setOrderSeq(orderSeq);
                latlngList.add(areaLatlng);
            }

            Area area = new Area();
            area.setAreaName(areaName);
            area.setFloor(floor);
            area.setComNum(loginDetail.getCompanyNumber());
            area.setAreaLatlngs(latlngList);
            areaService.registerArea(area, latlngList);

            info.put("result", "1");
            info.put("area", area);

        } catch(Exception e) {
            info.put("result", "2");
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value = "/map/area/mod.do", method = RequestMethod.POST)
    @ResponseBody
    public String areaMod(@RequestParam(value = "areaNum", required = true)Integer areaNum,
                          @RequestParam(value = "areaName", required = true)String areaName,
                          @RequestParam(value = "latlngJson", required = true)String latlngJson) throws IOException {

        Map<String, String> info = new HashMap<String, String>();
        try {

            JSONArray jsonArray = new JSONArray(latlngJson);
            List<AreaLatlng> latlngList = new ArrayList<AreaLatlng>();
            for(int i=0; i<jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                double lat = jsonObject.getDouble("lat");
                double lng = jsonObject.getDouble("lng");
                int orderSeq = jsonObject.getInt("orderSeq");
                AreaLatlng areaLatlng = new AreaLatlng();
                areaLatlng.setLat(lat);
                areaLatlng.setLng(lng);
                areaLatlng.setOrderSeq(orderSeq);
                latlngList.add(areaLatlng);
            }

            Area areaInfo = areaService.getAreaInfo(QueryParam.create("areaNum", areaNum).build());
            areaInfo.setAreaName(areaName);
            areaService.modifyArea(areaInfo, latlngList);

            info.put("result", "1");

        } catch(Exception e) {
            info.put("result", "2");
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value = "/map/area/del.do", method = RequestMethod.POST)
    @ResponseBody
    public String areaDel(@RequestParam(value = "areaNum", required = true)Integer areaNum) throws IOException {
        Map<String, String> info = new HashMap<String, String>();
        try {

            areaService.removeArea(QueryParam.create("areaNum", areaNum).build());

            info.put("result", "1");

        } catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }


    @RequestMapping(value = "/map/area/nodeUpdateBatch.do", method = RequestMethod.POST)
    @ResponseBody
    public String nodeUpdateBatch(@RequestParam(value = "areaName", required = true)String areaName,
                                  @RequestParam(value = "areaNum", required = true)Integer areaNum,
                                  @RequestParam(value = "nodeNumArray[]", required = false)List<Long> nodeNumArray) throws IOException {

        Map<String, String> info = new HashMap<String, String>();
        try {

            areaService.updateNodeUpdate(nodeNumArray, areaNum, areaName);


            LoginDetail loginDetail = Security.getLoginDetail();
            Sync sync = new Sync();
            sync.setComNum(loginDetail.getCompanyNumber());
            sync.setSyncType("NODE");
            syncService.updateSync(sync);

            info.put("result", "1");

        } catch(Exception e) {
            logger.error(e.getMessage());
            info.put("result", "2");
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }
}


