package api.geofencing.controller;

import api.map.service.FloorCodeService;
import core.api.common.domain.Company;
import core.api.common.domain.Sync;
import api.common.service.CommonService;
import core.api.contents.domain.Contents;
import api.contents.service.ContentsService;
import core.api.geofencing.domain.CodeAction;
import core.common.geofencing.domain.Geofencing;
import core.api.geofencing.domain.GeofencingZone;
import api.geofencing.service.GeofencingService;

import core.api.map.domain.FloorCode;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;
import org.springframework.web.servlet.HandlerMapping;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 */
@Controller
public class GeofencingController {

    @Autowired
    private GeofencingService geofencingService;

    @Autowired
    private ContentsService contentsService;

    @Autowired
    private FloorCodeService floorCodeService;

    @Autowired
    private CommonService commonService;

    private ArrayList<HashMap<String, Object>> convertGeofencingList(List<?> list, HashMap<String, ArrayList<Contents>> geofencingContentList, HashMap<String, ArrayList<CodeAction>> geofencingActionMap) {
        HashMap<Long, HashMap<String, Object>> geofencings = new HashMap<Long, HashMap<String, Object>>();
        for(Object obj: list) {
            GeofencingZone geofencing = (GeofencingZone)obj;

            HashMap<String, Object> latlng = new HashMap<String, Object>();
            latlng.put("lat", geofencing.getLat());
            latlng.put("lng", geofencing.getLng());
            latlng.put("orderSeq", geofencing.getOrderSeq());
            latlng.put("radius", geofencing.getRadius());
            latlng.put("fcNum", geofencing.getFcNum());

            if(!geofencings.containsKey(geofencing.getFcNum())) {
                HashMap<String, Object> fencing = new HashMap<String, Object>();
                fencing.put("fcNum", geofencing.getFcNum());
                fencing.put("comNum", geofencing.getComNum());
                fencing.put("fcType", geofencing.getFcType());
                fencing.put("fcShape", geofencing.getFcShape());
                fencing.put("fcName", geofencing.getFcName());
                fencing.put("floor", geofencing.getFloor());
                fencing.put("evtEnter", geofencing.getEvtEnter());
                fencing.put("evtLeave", geofencing.getEvtLeave());
                fencing.put("evtStay", geofencing.getEvtStay());
                fencing.put("userID", geofencing.getUserID());
                fencing.put("numEnter", geofencing.getNumEnter());
                fencing.put("numLeave", geofencing.getNumLeave());
                fencing.put("numStay", geofencing.getNumStay());
                fencing.put("modDate", geofencing.getModDate());
                fencing.put("regDate", geofencing.getRegDate());
                fencing.put("zoneType", geofencing.getZoneType());
                fencing.put("field1", geofencing.getField1());
                fencing.put("field2", geofencing.getField2());
                fencing.put("field3", geofencing.getField3());
                fencing.put("field4", geofencing.getField4());
                fencing.put("field5", geofencing.getField5());
                fencing.put("latlngs", new ArrayList<HashMap<String, Object>>());

                ArrayList<Contents> contentsList = geofencingContentList.get(String.valueOf(geofencing.getFcNum()));
                ArrayList<CodeAction> codeActionList = geofencingActionMap.get(String.valueOf(geofencing.getFcNum()));
                ArrayList<HashMap<String, Object>> conList = new ArrayList<HashMap<String, Object>>();
                if(contentsList!=null && contentsList.size()>0) {
                    for(Contents content: contentsList) {
                        String refSubType = content.getRefSubType();

                        HashMap<String, Object> con = new HashMap<String, Object>();

                        con.put("conNum", content.getConNum());
                        con.put("userID", content.getUserID());
                        con.put("acNum", content.getAcNum());
                        con.put("regDate", content.getRegDate());
                        con.put("modDate", content.getModDate());
                        con.put("acName", content.getAcName());
                        con.put("comNum", content.getComNum());
                        con.put("conDesc", content.getConDesc());
                        con.put("conName", content.getConName());
                        con.put("conType", content.getConType());
                        con.put("sDate", content.getsDate());
                        con.put("eDate", content.geteDate());
                        con.put("evtNum", content.getEvtNum());
                        con.put("expFlag", content.getExpFlag());
                        con.put("rssi", content.getRssi());

                        con.put("imgSrc1", content.getImgSrc1());
                        con.put("imgSrc2", content.getImgSrc2());
                        con.put("imgSrc3", content.getImgSrc3());
                        con.put("imgSrc4", content.getImgSrc4());
                        con.put("imgSrc5", content.getImgSrc5());
                        con.put("soundURL1", content.getSoundURL1());
                        con.put("soundURL2", content.getSoundURL2());
                        con.put("soundURL3", content.getSoundURL3());
                        con.put("url1", content.getUrl1());
                        con.put("url2", content.getUrl2());
                        con.put("url3", content.getUrl3());

                        con.put("soundSrc1", content.getSoundSrc1());
                        con.put("soundSrc2", content.getSoundSrc2());
                        con.put("soundSrc3", content.getSoundSrc3());

                        con.put("text1", content.getText1());
                        con.put("text2", content.getText2());
                        con.put("text3", content.getText3());

                        con.put("imgURL1", content.getImgURL1());
                        con.put("imgURL2", content.getImgURL2());
                        con.put("imgURL3", content.getImgURL3());
                        con.put("imgURL4", content.getImgURL4());
                        con.put("imgURL5", content.getImgURL5());

                        con.put("refNum", content.getRefNum());
                        con.put("refType", content.getRefType());
                        con.put("refSubType", content.getRefSubType());

                        //con.put("codeAction", null);
                        fencing.put("contents", con);

                        con.put("code", "");
                        con.put("codeNum", "");
                        con.put("codeName", "");
                        con.put("codeType", "");

                        if(codeActionList!=null && codeActionList.size()>0) {
                            for(CodeAction codeAction: codeActionList) {
                                if(StringUtils.equals(codeAction.getRefSubType(), refSubType)) {

                                    //con.put("codeAction", codeAction);
                                    /**
                                     * Code Action 값을 contents 정보에 병합
                                     * @author nohsoo 2015-03-11
                                     */
                                    con.put("code", codeAction.getCode());
                                    con.put("codeNum", codeAction.getCodeNum());
                                    con.put("codeName", codeAction.getCodeName());
                                    con.put("codeType", codeAction.getCodeType());
                                }
                            }
                        }
                        conList.add(con);
                    }
                }


                fencing.put("contents", conList);
                geofencings.put(geofencing.getFcNum(), fencing);
            }

            @SuppressWarnings("unchecked")
            ArrayList<HashMap<String, Object>> latlngs =
                    (ArrayList<HashMap<String, Object>>) geofencings.get(geofencing.getFcNum()).get("latlngs");
            latlngs.add(latlng);
        }
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for(HashMap<String, Object> item: geofencings.values()) {
            data.add(item);
        }

        return data;
    }

    @RequestMapping(value = "/geofencing/list/{UUID}", method = RequestMethod.GET)
    @ResponseBody
    public Object geofencingList(@PathVariable(value = "UUID")String UUID) throws IOException, ParseException {
        commonService.checkAuthorized(UUID);
        Map<String, Object> res = new HashMap<String, Object>();
        List<?> list = geofencingService.getGeofencingList(UUID);
        res.put("result", "success");
        res.put("code", "0");
        res.put("data", list);
        return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
    }

    @RequestMapping(value = "/geofencing/info/{UUID}", method = RequestMethod.GET)
    @ResponseBody
    public Object geofencingInfo(@PathVariable(value = "UUID")String UUID) throws IOException, ParseException {

        commonService.checkAuthorized(UUID);

        Map<String, Object> res = new HashMap<String, Object>();

        /**
         * 지오펜스 컨텐츠 목록 조회
         */
        List<?> contentsList = geofencingService.getGeofencingContentsList(UUID, "");
        HashMap<String, ArrayList<Contents>> geofencingContentMap = new HashMap<String, ArrayList<Contents>>();
        for(Object item: contentsList) {
            Contents contents = (Contents)item;
            Integer fcNum = contents.getRefNum();
            if(!geofencingContentMap.containsKey(String.valueOf(fcNum))) {
                geofencingContentMap.put(String.valueOf(fcNum), new ArrayList<Contents>());
            }
            geofencingContentMap.get(String.valueOf(fcNum)).add(contents);
        }

        /**
         * 액션코드, 목록 조회
         */
        List<?> actionList = geofencingService.getGeofencingActionList(UUID, "");
        HashMap<String, ArrayList<CodeAction>> geofencingActionMap = new HashMap<String, ArrayList<CodeAction>>();
        for(Object item: actionList) {
            CodeAction codeAction = (CodeAction)item;
            Integer fcNum = codeAction.getRefNum();
            if(!geofencingActionMap.containsKey(String.valueOf(fcNum))) {
                geofencingActionMap.put(String.valueOf(fcNum), new ArrayList<CodeAction>());
            }
            geofencingActionMap.get(String.valueOf(fcNum)).add(codeAction);
        }


        /**
         * 지오펜스 목록 조회
         */
//        List<?> list = geofencingService.getGeofencingListByAll(UUID);

        List<GeofencingZone> list = geofencingService.getGeofencingFloorCodeList(UUID, null);
        ArrayList<HashMap<String, Object>> data = this.convertGeofencingList(list, geofencingContentMap, geofencingActionMap);

        res.put("result", "success");
        res.put("code", "0");
        res.put("data", data);
        return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
    }

    @RequestMapping(value = "/geofencing/info/{UUID}/**", method = RequestMethod.GET)
    @ResponseBody
    public Object geofencingFloorCodeInfo(HttpServletRequest request,
                                          @PathVariable(value = "UUID")String UUID
    ) throws ParseException {

        commonService.checkAuthorized(UUID);

        Map<String, Object> res = new HashMap<String, Object>();
        List<GeofencingZone> list = new ArrayList<GeofencingZone>();
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
        List<FloorCode> floorCodeDepthList = new ArrayList<FloorCode>();
        List<FloorCode> floorCodeChildNodeList =  new ArrayList<FloorCode>();
        List<FloorCode> floorCodeLastChildNodeList =  new ArrayList<FloorCode>();

        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
        int pathSize = 4; // /geofencing/info/uuid
        String[] param = path.split("/");
        int paramSize = param.length;
        int level = 0;
        boolean isLastNode = true;

        if(UUID.equals("") || paramSize <= pathSize) {
            res.put("result", "error");
            res.put("code", "200");
        } else {
            for(int i=pathSize; i<paramSize; i++) {
                FloorCode floorCode = new FloorCode();
                floorCode.setNodeId(param[i]);
                floorCode.setLevelNo(level);
                floorCodeDepthList.add(floorCode);
                level++;
            }
            if(paramSize > pathSize) {
                FloorCode floorCode = new FloorCode();
                floorCode.setUUID(UUID);
                floorCodeChildNodeList = floorCodeService.getFloorCodeChildNodeList(floorCode, floorCodeDepthList);
                floorCodeLastChildNodeList = floorCodeService.getFloorCodeLastChildNodeList(floorCodeChildNodeList);
                if(floorCodeLastChildNodeList.size() == 0) isLastNode = false;
            }
            if(isLastNode == true) {
                /**
                 * 지오펜스 컨텐츠 목록 조회
                 */
                List<?> contentsList = geofencingService.getGeofencingContentsList(UUID, "");
                HashMap<String, ArrayList<Contents>> geofencingContentMap = new HashMap<String, ArrayList<Contents>>();
                for (Object item : contentsList) {
                    Contents contents = (Contents) item;
                    Integer fcNum = contents.getRefNum();
                    if (!geofencingContentMap.containsKey(String.valueOf(fcNum))) {
                        geofencingContentMap.put(String.valueOf(fcNum), new ArrayList<Contents>());
                    }
                    geofencingContentMap.get(String.valueOf(fcNum)).add(contents);
                }

                /**
                 * 액션코드, 목록 조회
                 */
                List<?> actionList = geofencingService.getGeofencingActionList(UUID, "");
                HashMap<String, ArrayList<CodeAction>> geofencingActionMap = new HashMap<String, ArrayList<CodeAction>>();
                for (Object item : actionList) {
                    CodeAction codeAction = (CodeAction) item;
                    Integer fcNum = codeAction.getRefNum();
                    if (!geofencingActionMap.containsKey(String.valueOf(fcNum))) {
                        geofencingActionMap.put(String.valueOf(fcNum), new ArrayList<CodeAction>());
                    }
                    geofencingActionMap.get(String.valueOf(fcNum)).add(codeAction);
                }

                /**
                 * 지오펜스 목록 조회
                 */
                list = geofencingService.getGeofencingFloorCodeList(UUID, floorCodeLastChildNodeList);
                data = this.convertGeofencingList(list, geofencingContentMap, geofencingActionMap);
            }
            res.put("result", "success");
            res.put("code", "0");
            res.put("data", data);
        }
        return res;
    }

//    @RequestMapping(value = "/geofencing/info/{UUID}/{fcNum}", method = RequestMethod.PUT)
//    @ResponseBody
//    @Deprecated
//    public Object geofencingModify(HttpServletRequest request,
//                                   @PathVariable(value = "UUID")String UUID,
//                                   @PathVariable(value = "fcNum")Long fcNum
//    ) throws IOException, ParseException {
//
//        commonService.checkAuthorized(UUID);
//
//        Map<String, Object> res = new HashMap<String, Object>();
//
//        UUID = StringUtil.NVL(UUID, "");
//        String str = JsonUtil.getJson(request);
//        JsonNode node = JsonUtil.toNode(str);
//
//        String fcName = (node.has("fcName") == true) ? node.get("fcName").getTextValue() : null;
//        String floor = (node.has("floor") == true) ? node.get("floor").getTextValue() : null;
//        Integer evtEnter = (node.has("evtEnter") == true) ? node.get("evtEnter").getIntValue() : null;
//        Integer evtLeave = (node.has("evtLeave") == true) ? node.get("evtLeave").getIntValue() : null;
//        Integer evtStay = (node.has("evtStay") == true) ? node.get("evtStay").getIntValue() : null;
//        Integer numEnter = (node.has("numEnter") == true) ? node.get("numEnter").getIntValue() : null;
//        Integer numLeave = (node.has("numLeave") == true) ? node.get("numLeave").getIntValue() : null;
//        Integer numStay = (node.has("numStay") == true) ? node.get("numStay").getIntValue() : null;
//        String field1 = (node.has("field1") == true) ? node.get("field1").getTextValue() : null;
//        String field2 = (node.has("field2") == true) ? node.get("field2").getTextValue() : null;
//        String field3 = (node.has("field3") == true) ? node.get("field3").getTextValue() : null;
//        String field4 = (node.has("field4") == true) ? node.get("field4").getTextValue() : null;
//        String field5 = (node.has("field5") == true) ? node.get("field5").getTextValue() : null;
//
//        if("".equals(UUID) || (fcNum == null || fcNum == 0)) {
//            res.put("result", "error");
//            res.put("code", "200");
//        } else {
//            try {
//                Geofencing geofencing = new Geofencing();
//                geofencing.setFcNum(fcNum);
//                geofencing.setFcName(fcName);;
//                geofencing.setFloor(floor);
//                geofencing.setEvtEnter(evtEnter);
//                geofencing.setEvtLeave(evtLeave);
//                geofencing.setEvtStay(evtStay);
//                geofencing.setNumEnter(numEnter);
//                geofencing.setNumLeave(numLeave);
//                geofencing.setNumStay(numStay);
//                geofencing.setField1(field1);
//                geofencing.setField2(field2);
//                geofencing.setField3(field3);
//                geofencing.setField4(field4);
//                geofencing.setField5(field5);
//
//                Geofencing geofencingInfo = geofencingService.getGeofencing(geofencing);
//
//                Company company = new Company();
//                company.setUUID(UUID);
//                Company companyInfo = commonService.getCompanyInfoByUUID(company);
//
//                if(geofencingInfo.getComNum().equals(companyInfo.getComNum())) {
//
//                    geofencingService.modifyGeofencing(geofencing);
//
//                    Sync sync = new Sync();
//                    sync.setComNum(companyInfo.getComNum());
//                    sync.setSyncType("GEOFENCING");
//                    commonService.updateSync(sync);
//                    res.put("result", "success");
//                    res.put("code", "0");
//                } else {
//                    res.put("result", "error");
//                    res.put("code", "301");
//                }
//
//            } catch(Exception e) {
//                res.put("result", "error");
//                res.put("code", "301");
//            }
//        }
//        return res;
////      JsonUtil jsonUtil = new JsonUtil(res);
////      String json = jsonUtil.toJson();
////      return json;
//    }

//    @RequestMapping(value = "/geofencing/info/{UUID}/{fcNum}", method = RequestMethod.GET)
//    @ResponseBody
//    @Deprecated
//    public Object geofencingInfo(@PathVariable(value = "UUID")String UUID,
//                                 @PathVariable(value = "fcNum")Long fcNum) throws IOException, ParseException {
//
//        commonService.checkAuthorized(UUID);
//
//        Map<String, Object> res = new HashMap<String, Object>();
//
//        /**
//         * 지오펜스 컨텐츠 목록 조회
//         */
//        Contents contentParam = new Contents();
//        contentParam.setUUID(UUID);
//        List<?> contentsList = geofencingService.getGeofencingContentsList(UUID, fcNum);
//        HashMap<String, ArrayList<Contents>> geofencingContentMap = new HashMap<String, ArrayList<Contents>>();
//        for(Object item: contentsList) {
//            Contents contents = (Contents)item;
//            if(!geofencingContentMap.containsKey(fcNum)) {
//                geofencingContentMap.put(String.valueOf(fcNum), new ArrayList<Contents>());
//            }
//            geofencingContentMap.get(String.valueOf(fcNum)).add(contents);
//        }
//
//        /**
//         * 액션코드, 목록 조회
//         */
//        List<?> actionList = geofencingService.getGeofencingActionList(UUID, fcNum);
//        HashMap<String, ArrayList<CodeAction>> geofencingActionMap = new HashMap<String, ArrayList<CodeAction>>();
//        for(Object item: actionList) {
//            CodeAction codeAction = (CodeAction)item;
//            if(!geofencingActionMap.containsKey(fcNum)) {
//                geofencingActionMap.put(String.valueOf(fcNum), new ArrayList<CodeAction>());
//            }
//            geofencingActionMap.get(String.valueOf(fcNum)).add(codeAction);
//        }
//
//
//        /**
//         * 지오펜스 목록 조회
//         */
//        GeofencingZone geofencingZone = new GeofencingZone();
//        geofencingZone.setFcNum(fcNum);
//        List<?> list = geofencingService.getGeofencingZone(geofencingZone);
//        ArrayList<HashMap<String, Object>> data = this.convertGeofencingList(list, geofencingContentMap, geofencingActionMap);
//
//        res.put("result", "success");
//        res.put("code", "0");
//        res.put("data", (data != null && data.size() > 0)? data.get(0): null);
//        return res;
////      JsonUtil jsonUtil = new JsonUtil(res);
////      String json = jsonUtil.toJson();
////      return json;
//    }
}
