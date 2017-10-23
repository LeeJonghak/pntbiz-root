package wms.maptool.controller;

import org.springframework.ui.Model;
import wms.component.auth.LoginDetail;
import framework.web.util.JsonUtil;
import framework.Security;
import wms.maptool.service.MapToolService;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Controller
public class MapToolController {

    @Autowired
    private MapToolService mapToolService;

    @RequestMapping(value = "/maptool/map.do", method = RequestMethod.GET)
    public String maptoolView(Model model) {

		HashMap<String, String> poiCateList = mapToolService.getPOICateList();
		model.addAttribute("poiCateList", poiCateList);

        return "/maptool/map";
    }


    @RequestMapping(value = "/maptool/beacon/reg.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String beaconReg(@RequestParam("UUID") String uuid,
                            @RequestParam("majorVer") Integer majorVer,
                            @RequestParam("minorVer") Integer minorVer,
                            @RequestParam("beaconName") String beaconName,
                            @RequestParam("beaconType") String beaconType,
                            @RequestParam("beaconDesc") String beaconDesc,
                            @RequestParam("lat") String lat,
                            @RequestParam("lng") String lng,
                            @RequestParam("floor") String floor,
                            @RequestParam("txPower") String txPower,
                            @RequestParam(value = "beaconGroupNum", required = false) Integer beaconGroupNum) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {

            LoginDetail loginDetail = Security.getLoginDetail();

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("comNum", loginDetail.getCompanyNumber());
            param.put("UUID", uuid);
            param.put("majorVer", majorVer);
            param.put("minorVer", minorVer);
            param.put("beaconName", beaconName);
            param.put("beaconType", beaconType);
            param.put("beaconDesc", beaconDesc);
            param.put("lat", lat);
            param.put("lng", lng);
            param.put("floor", floor);
            param.put("txPower", txPower);

            param.put("field1", "");
            param.put("field2", "");
            param.put("field3", "");
            param.put("field4", "");
            param.put("field5", "");
            mapToolService.registerBeacon(param);

            if (beaconGroupNum != null && beaconGroupNum > 0) {
                Map<String, Object> mappingParam = new HashMap<String, Object>();
                mappingParam.put("beaconGroupNum", beaconGroupNum);
                mappingParam.put("beaconNum", param.get("beaconNum"));
                mapToolService.insertBeaconGroupMapping(mappingParam);
            }

            Map<String, Object> beaconParam = new HashMap<String, Object>();
            beaconParam.put("beaconNum", param.get("beaconNum"));
            Map<String, Object> beaconInfo = mapToolService.getBeaconInfo(param);

            Map<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put("comNum", loginDetail.getCompanyNumber());
            mapParam.put("beaconNum", beaconInfo.get("beaconNum"));
            List<?> mapList = mapToolService.getBeaconGroupMappingList(mapParam);
            List<String> groupNames = new ArrayList<String>();
            for (Object obj : mapList) {
                Map<String, Object> groupInfo = (Map<String, Object>) obj;
                groupNames.add(String.valueOf(groupInfo.get("beaconGroupName")));
            }
            beaconInfo.put("groupMapping", mapList);
            beaconInfo.put("groupNames", StringUtils.join(groupNames, ","));

            if (beaconInfo != null) {
                info.put("result", "1");
                info.put("data", beaconInfo);
            } else {
                info.put("result", "3");
            }

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }


    @RequestMapping(value = "/maptool/beacon/mod.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String beaconMod2(@RequestParam(value = "beaconNum") Integer beaconNum,
                            @RequestParam(value = "UUID") String uuid,
                            @RequestParam(value = "majorVer") Integer majorVer,
                            @RequestParam(value = "minorVer") Integer minorVer,
                            @RequestParam(value = "beaconName") String beaconName,
                            @RequestParam(value = "beaconType") String beaconType,
                            @RequestParam(value = "beaconDesc", defaultValue = "") String beaconDesc,
                            @RequestParam(value = "lat") Double lat,
                            @RequestParam(value = "lng") Double lng,
                            @RequestParam(value = "txPower", defaultValue = "0") String txPower,
                            @RequestParam(value = "beaconGroupNum", required = false) Integer beaconGroupNum) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {

            LoginDetail loginDetail = Security.getLoginDetail();

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("beaconNum", beaconNum);
            Map<String, Object> beaconInfo = mapToolService.getBeaconInfo(param);
            if (beaconInfo != null) {
                beaconInfo.put("UUID", uuid);
                beaconInfo.put("majorVer", majorVer);
                beaconInfo.put("minorVer", minorVer);
                beaconInfo.put("beaconName", beaconName);
                beaconInfo.put("beaconType", beaconType);
                beaconInfo.put("beaconDesc", beaconDesc);
                beaconInfo.put("lat", lat);
                beaconInfo.put("lng", lng);
                beaconInfo.put("txPower", txPower);
                mapToolService.modifyBeacon(beaconInfo);

                if (beaconGroupNum != null) {
                    Map<String, Object> mappingParam = new HashMap<String, Object>();
                    mappingParam.put("beaconNum", param.get("beaconNum"));
                    mapToolService.deleteBeaconGroupMapping(mappingParam);

                    if (beaconGroupNum > 0) {
                        mappingParam.put("beaconGroupNum", beaconGroupNum);
                        mapToolService.insertBeaconGroupMapping(mappingParam);
                    }
                }

                Map<String, Object> mapParam = new HashMap<String, Object>();
                mapParam.put("comNum", loginDetail.getCompanyNumber());
                mapParam.put("beaconNum", beaconInfo.get("beaconNum"));
                List<?> mapList = mapToolService.getBeaconGroupMappingList(mapParam);
                List<String> groupNames = new ArrayList<String>();
                for (Object obj : mapList) {
                    Map<String, Object> groupInfo = (Map<String, Object>) obj;
                    groupNames.add(String.valueOf(groupInfo.get("beaconGroupName")));
                }
                beaconInfo.put("groupMapping", mapList);
                beaconInfo.put("groupNames", StringUtils.join(groupNames, ","));

                info.put("result", "1");
                info.put("data", beaconInfo);
            } else {
                info.put("result", "3");
            }
        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value = "/maptool/beacon/del.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String beaconDel(@RequestParam("beaconNum") Integer beaconNum) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("beaconNum", beaconNum);
            mapToolService.deleteBeacon(param);

            info.put("result", "1");


        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }


    @RequestMapping(value = "/maptool/scanner/reg.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String scannerReg(@RequestParam("macAddr") String macAddr,
                             @RequestParam("majorVer") String majorVer,
                             @RequestParam("scannerName") String scannerName,
                             @RequestParam("sid") String sid,
                             @RequestParam("lat") String lat,
                             @RequestParam("lng") String lng,
                             @RequestParam("rssi") String rssi,
                             @RequestParam("srssi") String srssi,
                             @RequestParam("mrssi") String mrssi,
                             @RequestParam("drssi") String drssi,
                             @RequestParam("exMeter") String exMeter,
                             @RequestParam("calPoint") String calPoint,
                             @RequestParam("maxSig") String maxSig,
                             @RequestParam("maxBuf") String maxBuf,
                             @RequestParam("fwVer") String fwVer,
                             @RequestParam("floor") String floor) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {

            LoginDetail loginDetail = Security.getLoginDetail();

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("comNum", loginDetail.getCompanyNumber());
            param.put("macAddr", macAddr);
            param.put("majorVer", majorVer);
            param.put("scannerName", scannerName);
            param.put("sid", sid);
            param.put("lat", lat);
            param.put("lng", lng);
            param.put("rssi", rssi);
            param.put("srssi", srssi);
            param.put("mrssi", mrssi);
            param.put("drssi", drssi);
            param.put("exMeter", exMeter);
            param.put("calPoint", calPoint);
            param.put("maxSig", maxSig);
            param.put("maxBuf", maxBuf);
            param.put("fwVer", fwVer);
            param.put("floor", floor);


            mapToolService.registerScanner(param);

            Map<String, Object> beaconParam = new HashMap<String, Object>();
            beaconParam.put("scannerNum", param.get("scannerNum"));
            Map<String, Object> scannerInfo = mapToolService.getScannerInfo(param);
            if (scannerInfo != null) {
                info.put("result", "1");
                info.put("data", scannerInfo);
            } else {
                info.put("result", "3");
            }

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value = "/maptool/scanner/mod.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String scannerMod(@RequestParam("scannerNum") Integer scannerNum,
                             @RequestParam("macAddr") String macAddr,
                             @RequestParam("majorVer") String majorVer,
                             @RequestParam("scannerName") String scannerName,
                             @RequestParam("sid") String sid,
                             @RequestParam("lat") String lat,
                             @RequestParam("lng") String lng,
                             @RequestParam("rssi") String rssi,
                             @RequestParam("srssi") String srssi,
                             @RequestParam("mrssi") String mrssi,
                             @RequestParam("exMeter") String exMeter,
                             @RequestParam("calPoint") String calPoint,
                             @RequestParam("maxSig") String maxSig,
                             @RequestParam("maxBuf") String maxBuf,
                             @RequestParam("fwVer") String fwVer) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("scannerNum", scannerNum);
            Map<String, Object> scannerInfo = mapToolService.getScannerInfo(param);
            if (scannerInfo != null) {
                scannerInfo.put("macAddr", macAddr);
                scannerInfo.put("majorVer", majorVer);
                scannerInfo.put("scannerName", scannerName);
                scannerInfo.put("sid", sid);
                scannerInfo.put("lat", lat);
                scannerInfo.put("lng", lng);
                scannerInfo.put("rssi", rssi);
                scannerInfo.put("srssi", srssi);
                scannerInfo.put("mrssi", mrssi);
                scannerInfo.put("exMeter", exMeter);
                scannerInfo.put("calPoint", calPoint);
                scannerInfo.put("maxSig", maxSig);
                scannerInfo.put("maxBuf", maxBuf);
                scannerInfo.put("fwVer", fwVer);
                mapToolService.modifyScanner(scannerInfo);

                info.put("result", "1");
                info.put("data", scannerInfo);
            } else {
                info.put("result", "3");
            }
        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value = "/maptool/scanner/del.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String scannerDel(@RequestParam("scannerNum") Integer scannerNum) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("scannerNum", scannerNum);
            mapToolService.deleteScanner(param);

            info.put("result", "1");

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }

    @RequestMapping(value = "/maptool/geofence/reg.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String geofenceReg(@RequestParam(value = "latlngs") String latlngsStr,
                              @RequestParam(value = "floor") String floor,
                              @RequestParam(value = "fcName") String fcName,
                              @RequestParam(value = "fcShape") String fcShape,
                              @RequestParam(value = "evtEnter", defaultValue = "0") Integer evtEnter,
                              @RequestParam(value = "numEnter", defaultValue = "-1") Integer numEnter,
                              @RequestParam(value = "evtLeave", defaultValue = "0") Integer evtLeave,
                              @RequestParam(value = "numLeave", defaultValue = "-1") Integer numLeave,
                              @RequestParam(value = "evtStay", defaultValue = "0") Integer evtStay,
                              @RequestParam(value = "numStay", defaultValue = "-1") Integer numStay,
							  @RequestParam(value = "field1", required = false) String field1,
							  @RequestParam(value = "field2", required = false) String field2,
							  @RequestParam(value = "field3", required = false) String field3,
							  @RequestParam(value = "field4", required = false) String field4,
							  @RequestParam(value = "field5", required = false) String field5,
                              @RequestParam(value = "fcGroupNum", required = false) Integer fcGroupNum) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {

            LoginDetail loginDetail = Security.getLoginDetail();

            Map<String, Object> geofenceParam = new HashMap<String, Object>();
            geofenceParam.put("comNum", loginDetail.getCompanyNumber());
            geofenceParam.put("userID", loginDetail.getUsername());
            geofenceParam.put("fcName", fcName);
            geofenceParam.put("floor", floor);
            geofenceParam.put("fcType", "G");
            geofenceParam.put("fcShape", fcShape);
            geofenceParam.put("evtEnter", evtEnter);
            geofenceParam.put("evtStay", evtStay);
            geofenceParam.put("evtLeave", evtLeave);
            geofenceParam.put("numEnter", numEnter);
            geofenceParam.put("numStay", numStay);
            geofenceParam.put("numLeave", numLeave);

            geofenceParam.put("field1", field1);
            geofenceParam.put("field2", field2);
            geofenceParam.put("field3", field3);
            geofenceParam.put("field4", field4);
            geofenceParam.put("field5", field5);

            /**
             * 펜스 좌표정보 객체 정보
             */
            JSONArray jsonLatlngs = new JSONArray(latlngsStr);
            List<Map<String, Object>> latlngs = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < jsonLatlngs.length(); i++) {
                JSONObject object = jsonLatlngs.getJSONObject(i);
                Map<String, Object> latlngParam = new HashMap<String, Object>();
                if (object.has("lat")) {
                    latlngParam.put("lat", object.getDouble("lat"));
                }
                if (object.has("lng")) {
                    latlngParam.put("lng", object.getDouble("lng"));
                }
                if (object.has("orderSeq")) {
                    latlngParam.put("orderSeq", object.getDouble("orderSeq"));
                }
                latlngParam.put("radius", 0);

                latlngs.add(latlngParam);
            }
            geofenceParam.put("latlngs", latlngs);
            mapToolService.registerGeofence(geofenceParam);

            if (fcGroupNum != null && fcGroupNum > 0) {
                Map<String, Object> mappingParam = new HashMap<String, Object>();
                mappingParam.put("fcGroupNum", fcGroupNum);
                mappingParam.put("fcNum", geofenceParam.get("fcNum"));
                mapToolService.insertGeofencingGroupMapping(mappingParam);
            }

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("fcNum", geofenceParam.get("fcNum"));
            Map<String, Object> geofenceInfo = mapToolService.getGeofenceInfo(param);

            Map<String, Object> mapParam = new HashMap<String, Object>();
            mapParam.put("comNum", loginDetail.getCompanyNumber());
            mapParam.put("fcNum", geofenceInfo.get("fcNum"));
            List<?> mapList = mapToolService.getGeofencingGroupMappingList(mapParam);
            List<String> groupNames = new ArrayList<String>();
            for (Object obj : mapList) {
                Map<String, Object> groupInfo = (Map<String, Object>) obj;
                groupNames.add(String.valueOf(groupInfo.get("fcGroupName")));
            }
            geofenceInfo.put("groupMapping", mapList);
            geofenceInfo.put("groupNames", StringUtils.join(groupNames, ","));

            if (geofenceInfo != null) {
                info.put("result", "1");
                info.put("data", geofenceInfo);
            } else {
                info.put("result", "3");
            }

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value = "/maptool/geofence/mod.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String geofenceMod(@RequestParam(value = "fcNum", required = true) Integer fcNum,
                              @RequestParam(value = "latlngs", required = false) String latlngsStr,
                              @RequestParam(value = "fcName", required = false) String fcName,
                              @RequestParam(value = "fcShape", required = false) String fcShape,
                              @RequestParam(value = "evtEnter", required = false) Integer evtEnter,
                              @RequestParam(value = "numEnter", required = false) Integer numEnter,
                              @RequestParam(value = "evtLeave", required = false) Integer evtLeave,
                              @RequestParam(value = "numLeave", required = false) Integer numLeave,
                              @RequestParam(value = "field1", required = false) String field1,
							  @RequestParam(value = "field2", required = false) String field2,
							  @RequestParam(value = "field3", required = false) String field3,
							  @RequestParam(value = "field4", required = false) String field4,
							  @RequestParam(value = "field5", required = false) String field5,
							  @RequestParam(value = "evtStay", required = false) Integer evtStay,
                              @RequestParam(value = "numStay", required = false) Integer numStay,
                              @RequestParam(value = "fcGroupNum", required = false) Integer fcGroupNum) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {

            LoginDetail loginDetail = Security.getLoginDetail();

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("fcNum", fcNum);
            Map<String, Object> geofenceInfo = mapToolService.getGeofenceInfo(param);
            if (geofenceInfo != null) {
                if (fcName != null) {
                    geofenceInfo.put("fcName", fcName);
                }
                if (evtEnter != null) {
                    geofenceInfo.put("evtEnter", evtEnter);
                }
                if (evtLeave != null) {
                    geofenceInfo.put("evtLeave", evtLeave);
                }
                if (evtStay != null) {
                    geofenceInfo.put("evtStay", evtStay);
                }
                if (numEnter != null) {
                    geofenceInfo.put("numEnter", numEnter);
                }
                if (numLeave != null) {
                    geofenceInfo.put("numLeave", numLeave);
                }
                if (numStay != null) {
                    geofenceInfo.put("numStay", numStay);
                }
				if (field1 != null) {
					geofenceInfo.put("field1", field1);
				}
				if (field2 != null) {
					geofenceInfo.put("field2", field2);
				}
				if (field3 != null) {
					geofenceInfo.put("field3", field3);
				}
				if (field4 != null) {
					geofenceInfo.put("field4", field4);
				}
				if (field5 != null) {
					geofenceInfo.put("field5", field5);
				}

                if (latlngsStr != null) {
                    JSONArray jsonLatlngs = new JSONArray(latlngsStr);
                    List<Map<String, Object>> latlngs = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < jsonLatlngs.length(); i++) {
                        JSONObject object = jsonLatlngs.getJSONObject(i);
                        Map<String, Object> latlng = new HashMap<String, Object>();
                        latlng.put("fcNum", fcNum);
                        if (object.has("lat")) {
                            latlng.put("lat", object.getDouble("lat"));
                        }
                        if (object.has("lng")) {
                            latlng.put("lng", object.getDouble("lng"));
                        }
                        latlng.put("orderSeq", 0);
                        latlng.put("radius", 0);
                        latlngs.add(latlng);
                    }
                    geofenceInfo.put("latlngs", latlngs);
                }

                mapToolService.modifyGeofence(geofenceInfo);

                if (fcGroupNum != null) {
                    Map<String, Object> mappingParam = new HashMap<String, Object>();
                    mappingParam.put("fcNum", param.get("fcNum"));
                    mapToolService.deleteGeofencingGroupMapping(mappingParam);

                    if (fcGroupNum > 0) {
                        mappingParam.put("fcGroupNum", fcGroupNum);
                        mapToolService.insertGeofencingGroupMapping(mappingParam);
                    }
                }

                Map<String, Object> mapParam = new HashMap<String, Object>();
                mapParam.put("comNum", loginDetail.getCompanyNumber());
                mapParam.put("fcNum", geofenceInfo.get("fcNum"));
                List<?> mapList = mapToolService.getGeofencingGroupMappingList(mapParam);
                List<String> groupNames = new ArrayList<String>();
                for (Object obj : mapList) {
                    Map<String, Object> groupInfo = (Map<String, Object>) obj;
                    groupNames.add(String.valueOf(groupInfo.get("fcGroupName")));
                }
                geofenceInfo.put("groupMapping", mapList);
                geofenceInfo.put("groupNames", StringUtils.join(groupNames, ","));

                info.put("result", "1");
                info.put("data", geofenceInfo);
            } else {
                info.put("result", "3");
            }
        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }

    @RequestMapping(value = "/maptool/geofence/del.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String geofenceDel(@RequestParam("fcNum") Integer fcNum) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("fcNum", fcNum);
            mapToolService.deleteGeofence(param);

            info.put("result", "1");

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }

    @RequestMapping(value = "/maptool/area/reg.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String areaReg(@RequestParam(value = "latlngs", required = true) String latlngsStr,
                          @RequestParam(value = "floor", required = true) String floor,
                          @RequestParam(value = "areaName", required = true) String areaName) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {

            LoginDetail loginDetail = Security.getLoginDetail();

            Map<String, Object> areaParam = new HashMap<String, Object>();
            areaParam.put("comNum", loginDetail.getCompanyNumber());
            areaParam.put("areaName", areaName);
            areaParam.put("floor", floor);


            /**
             * 펜스 좌표정보 객체 정보
             */
            JSONArray jsonLatlngs = new JSONArray(latlngsStr);
            List<Map<String, Object>> latlngs = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < jsonLatlngs.length(); i++) {
                JSONObject object = jsonLatlngs.getJSONObject(i);
                Map<String, Object> latlngParam = new HashMap<String, Object>();
                if (object.has("lat")) {
                    latlngParam.put("lat", object.getDouble("lat"));
                }
                if (object.has("lng")) {
                    latlngParam.put("lng", object.getDouble("lng"));
                }
                if (object.has("orderSeq")) {
                    latlngParam.put("orderSeq", object.getInt("orderSeq"));
                }
                latlngs.add(latlngParam);

            }
            areaParam.put("latlngs", latlngs);
            mapToolService.registerArea(areaParam);

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("areaNum", areaParam.get("areaNum"));
            Map<String, Object> areaInfo = mapToolService.getAreaInfo(param);
            if (areaInfo != null) {
                info.put("result", "1");
                info.put("data", areaInfo);
            } else {
                info.put("result", "3");
            }

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value = "/maptool/area/mod.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String areaReg(@RequestParam(value = "areaNum", required = true) Integer areaNum,
                          @RequestParam(value = "latlngs", required = true) String latlngsStr,
                          @RequestParam(value = "areaName", required = true) String areaName) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("areaNum", areaNum);
            Map<String, Object> areaInfo = mapToolService.getAreaInfo(param);
            if (areaInfo != null) {

                if (areaName != null) {
                    areaInfo.put("areaName", areaName);
                }
                if (latlngsStr != null) {
                    JSONArray jsonLatlngs = new JSONArray(latlngsStr);
                    List<Map<String, Object>> latlngs = new ArrayList<Map<String, Object>>();
                    for (int i = 0; i < jsonLatlngs.length(); i++) {
                        JSONObject object = jsonLatlngs.getJSONObject(i);
                        Map<String, Object> latlng = new HashMap<String, Object>();
                        latlng.put("areaNum", areaNum);
                        if (object.has("lat")) {
                            latlng.put("lat", object.getDouble("lat"));
                        }
                        if (object.has("lng")) {
                            latlng.put("lng", object.getDouble("lng"));
                        }
                        if (object.has("orderSeq")) {
                            latlng.put("orderSeq", object.getInt("orderSeq"));
                        }

                        latlngs.add(latlng);
                    }
                    areaInfo.put("latlngs", latlngs);
                }

                mapToolService.modifyArea(areaInfo);

                info.put("result", "1");
                info.put("data", areaInfo);

            } else {
                info.put("result", "3");
            }

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }

    @RequestMapping(value = "/maptool/area/del.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String areaDel(@RequestParam("areaNum") Integer areaNum) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("areaNum", areaNum);
            mapToolService.deleteArea(param);

            info.put("result", "1");

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }


    @RequestMapping(value = "/maptool/node/reg.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String nodeReg(@RequestParam(value = "floor", required = true) String floor,
                          @RequestParam(value = "nodeName", defaultValue = "") String nodeName,
                          @RequestParam(value = "latlngs", required = true) String latlngsStr,
                          @RequestParam(value = "type", defaultValue = "B") String type) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {
            //values(#{comNum}, #{nodeID}, #{floor}, #{nodeName}, #{lat}, #{lng}
            LoginDetail loginDetail = Security.getLoginDetail();


            List<Map<String, Object>> resultData = new ArrayList<Map<String, Object>>();

            JSONArray jsonLatlngs = new JSONArray(latlngsStr);
            List<Map<String, Object>> latlngs = new ArrayList<Map<String, Object>>();
            for (int i = 0; i < jsonLatlngs.length(); i++) {
                JSONObject object = jsonLatlngs.getJSONObject(i);
                Map<String, Object> latlngParam = new HashMap<String, Object>();

                Map<String, Object> param = new HashMap<String, Object>();
                param.put("comNum", loginDetail.getCompanyNumber());
                param.put("nodeName", nodeName);
                param.put("floor", floor);
                param.put("lat", object.getDouble("lat"));
                param.put("lng", object.getDouble("lng"));
                param.put("type", type);
                mapToolService.insertNode(param);


                Map<String, Object> infoParam = new HashMap<String, Object>();
                infoParam.put("comNum", loginDetail.getCompanyNumber());
                infoParam.put("floor", floor);
                infoParam.put("nodeID", param.get("nodeID"));
                Map<String, Object> nodeInfo = mapToolService.getNodeInfo(infoParam);
                resultData.add(nodeInfo);
            }

            info.put("result", "1");
            info.put("data", resultData);

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value = "/maptool/node/mod.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String nodeMod(@RequestParam(value = "nodeNum", required = true) Integer nodeNum,
                          @RequestParam(value = "lat") Double lat,
                          @RequestParam(value = "lng") Double lng,
						  @RequestParam(value = "nodeName", required = false) String nodeName,
						  @RequestParam(value = "nodeID", required = false) String nodeID,
						  @RequestParam(value = "jointName", required = false) String jointName,
						  @RequestParam(value = "cate", required = false) String cate,
                          @RequestParam(value = "areaNum", required = false, defaultValue = "0") Integer areaNum,
                          @RequestParam(value = "areaName", required = false, defaultValue = "") String areaName) throws IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        try {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("nodeNum", nodeNum);
            Map<String, Object> nodeInfo = mapToolService.getNodeInfo(param);
            if (nodeInfo != null) {
                nodeInfo.put("lat", lat);
                nodeInfo.put("lng", lng);
                if (areaNum != null && areaName != null) {
                    nodeInfo.put("areaNum", areaNum);
                    nodeInfo.put("areaName", areaName);
                }

				if(nodeName!=null) {
					nodeInfo.put("nodeName", nodeName);
				}
				if(nodeID!=null) {
					nodeInfo.put("nodeID", nodeID);
				}
				if(jointName!=null) {
					nodeInfo.put("jointName", jointName);
				}
				if(cate!=null) {
					nodeInfo.put("cate", cate);
				}

                mapToolService.modifyNode(nodeInfo);

                info.put("result", "1");
                info.put("data", nodeInfo);
            } else {
                info.put("result", "3");
            }
        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value = "/maptool/node/del.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String nodeDel(@RequestParam("nodeNum") Integer nodeNum) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("nodeNum", nodeNum);
            mapToolService.deleteNode(param);

            info.put("result", "1");

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }

    @RequestMapping(value = "/maptool/nodeEdge/reg.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String nodeEdgeReg(@RequestParam(value = "floor", required = true) String floor,
                              @RequestParam(value = "startPoint", required = true) String startPoint,
                              @RequestParam(value = "endPoint", required = true) String endPoint,
                              @RequestParam(value = "type", defaultValue = "B") String type) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {
            LoginDetail loginDetail = Security.getLoginDetail();

            Map<String, Object> edgeParam = new HashMap<String, Object>();
            edgeParam.put("comNum", loginDetail.getCompanyNumber());
            edgeParam.put("floor", floor);
            edgeParam.put("type", type);
            edgeParam.put("startPoint", startPoint);
            edgeParam.put("endPoint", endPoint);
            Map<String, Object> edgeInfo = mapToolService.getNodeEdgeInfo(edgeParam);
            if (edgeInfo == null) {
                mapToolService.registerNodeEdge(edgeParam);
                info.put("result", "1");
                info.put("data", edgeParam);
            } else {
                info.put("result", "3");
            }

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }


    @RequestMapping(value = "/maptool/nodeEdge/del.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String nodeEdgeDel(@RequestParam("edgeNum") Integer edgeNum) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("edgeNum", edgeNum);
            mapToolService.deleteNodeEdge(param);

            info.put("result", "1");

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }

    @RequestMapping(value = "/maptool/contentsMapping/reg.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String contentsMappingReg(@RequestParam(value = "refType", required = true) String refType,
                                     @RequestParam(value = "refSubType", required = true) String refSubType,
                                     @RequestParam(value = "refNum", required = true) Integer refNum,
                                     @RequestParam(value = "conNum", required = true) Integer conNum) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {
            LoginDetail loginDetail = Security.getLoginDetail();

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("conNum", conNum);
            param.put("refType", refType);
            param.put("refSubType", refSubType);
            param.put("refNum", refNum);
            mapToolService.insertContentMapping(param);
            Map<String, Object> contentMappingInfo = mapToolService.getContentMappingInfo(param);

            info.put("result", "1");
            info.put("data", contentMappingInfo);

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }

    @RequestMapping(value = "/maptool/contentsMapping/del.do", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String contentsMappingDel(@RequestParam(value = "refType", required = true) String refType,
                                     @RequestParam(value = "refSubType", required = true) String refSubType,
                                     @RequestParam(value = "refNum", required = true) Integer refNum,
                                     @RequestParam(value = "conNum", required = true) Integer conNum) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();
        try {

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("conNum", conNum);
            param.put("refType", refType);
            param.put("refSubType", refSubType);
            param.put("refNum", refNum);

            mapToolService.deleteContentMapping(param);

            info.put("result", "1");

        } catch (Exception exception) {
            info.put("result", "2");
            info.put("error", exception.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }
}

