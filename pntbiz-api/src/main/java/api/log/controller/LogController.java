package api.log.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import api.common.service.CommonService;
import core.api.log.domain.BeaconSensorLog;
import core.api.log.domain.ContentsInteractionLog;
import core.api.log.domain.PresenceBeaconLog;
import core.api.log.domain.PresenceExhibitionLog;
import core.api.log.domain.PresenceInteractionLog;
import api.log.service.LogService;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;

@Controller
public class LogController {

	@Autowired
	private LogService logService;

	@Autowired
	private CommonService commonService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 비콘 프리젠스 로그
	 * create nohsoo 2015-06-23
	 * edit   nohsoo 2015-06-30 majorVer, minorVer 파라미터 제거
	 * edit   nohssoo 2015-07-08 파라미터 JSON 방식 으로 변경
	 * edit   jhlee 2015-07-16 /beacon/presenc에서 이동
	 * edit   nohsoo 2015-08-10 m2comm, 지오펜스, 노드관련 정보 저장할 수 있도록 파라미터 수정
	 */
	@RequestMapping(value = "/log/presence/beacon", method = RequestMethod.POST)
	@ResponseBody
	public Object beaconPresenceLog(HttpServletRequest request
	) throws IOException {

		Map<String, Object> res = new HashMap<String, Object>();

		String str = JsonUtil.getJson(request);
		JsonNode node = JsonUtil.toNode(str);
		String UUID = node.get("UUID").getTextValue();
		Double lat = node.get("lat").getDoubleValue();
		Double lng = node.get("lng").getDoubleValue();
		String floor = node.get("floor").getTextValue();

		commonService.checkAuthorized(UUID);

		// 2015-08-10
		String phoneNumber = null;
		String deviceInfo = null;
		Long fcNum = null;
		String fcName = null;
		Long nodeID = null;
		if(node.has("phoneNumber")==true) {
			phoneNumber = node.get("phoneNumber").getTextValue();
		}
		if(node.has("deviceInfo")==true) {
			deviceInfo = node.get("deviceInfo").getTextValue();
		}
		if(node.has("fcNum")==true) {
			fcNum = node.get("fcNum").getLongValue();
		}
		if(node.has("fcName")==true) {
			fcName = node.get("fcName").getTextValue();
		}
		if(node.has("nodeID")==true) {
			nodeID = node.get("nodeID").getLongValue();
		}
		// end

		logger.debug("UUID:"+UUID+",lat:"+lat+",lng:"+lng+",floor:"+floor+",phoneNumber:"+phoneNumber+",deviceInfo:"+deviceInfo);

		if ("".equals(UUID) || "".equals(lat) || "".equals(lng) || "".equals(floor)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {

			PresenceBeaconLog vo = new PresenceBeaconLog();
			vo.setUUID(UUID);
			vo.setLat(lat);
			vo.setLng(lng);
			vo.setFloor(floor);
			vo.setPhoneNumber(phoneNumber);
			vo.setDeviceInfo(deviceInfo);
			// 2015-08-10
			vo.setFcNum(fcNum);
			vo.setFcName(fcName);
			vo.setNodeID(nodeID);
			// end
			logService.registerPresenceBeaconLog(vo);

			res.put("result", "success");
			res.put("code", "0");
		}
		return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
	}

	// 전시회 로그
	@RequestMapping(value="/log/presence/exhibition", method=RequestMethod.POST)
	@ResponseBody
	public Object presenceExhibition(HttpServletRequest request, PresenceExhibitionLog presenceExhibitionLog
	) throws IOException, ServletException {
		Map<String, Object> res = new HashMap<String, Object>();

		String str = JsonUtil.getJson(request);
		JsonNode node = JsonUtil.toNode(str);

		String UUID = StringUtil.NVL(node.path("UUID").getTextValue(), "");
		Double lat = node.path("lat").getDoubleValue();
		Double lng = node.path("lng").getDoubleValue();
		Integer conNum = node.path("conNum").getIntValue();
		Integer fcNum = node.path("fcNum").getIntValue();
		String floor = StringUtil.NVL(node.path("floor").getTextValue(), "");
		String evtType = StringUtil.NVL(node.path("evtType").getTextValue(), "");
		String deviceInfo = StringUtil.NVL(node.path("deviceInfo").getTextValue(), "");
		String phoneNumber = StringUtil.NVL(node.path("phoneNumber").getTextValue(), "");

		commonService.checkAuthorized(UUID);

		// 필수 값 체크
		if("".equals(UUID) || conNum == null || fcNum == null) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			try {
				presenceExhibitionLog.setUUID(UUID);
				presenceExhibitionLog.setLat(lat);
				presenceExhibitionLog.setLng(lng);
				presenceExhibitionLog.setFloor(floor);
				presenceExhibitionLog.setConNum(conNum);
				presenceExhibitionLog.setFcNum(fcNum);
				presenceExhibitionLog.setEvtType(evtType);
				presenceExhibitionLog.setDeviceInfo(deviceInfo);
				presenceExhibitionLog.setPhoneNumber(phoneNumber);
				logService.registerPresenceExhibitionLog(presenceExhibitionLog);
				res.put("result", "success");
				res.put("code", "0");
			} catch(DataAccessException dae) {
				res.put("result", "error");
				res.put("code", "303");
			}
		}
		return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
	}

	// 프레즌스 인터랙션 로그
	@RequestMapping(value="/log/presence/interaction", method=RequestMethod.POST)
	@ResponseBody
	public Object presenceInteraction(HttpServletRequest request, PresenceInteractionLog presenceInteractionLog
	) throws IOException, ServletException {
		Map<String, Object> res = new HashMap<String, Object>();

		String str = JsonUtil.getJson(request);
		JsonNode node = JsonUtil.toNode(str);

		String UUID = StringUtil.NVL(node.path("UUID").getTextValue(), "");
		String intType = StringUtil.NVL(node.path("intType").getTextValue(), "");
		String deviceInfo = StringUtil.NVL(node.path("deviceInfo").getTextValue(), "");
		String phoneNumber = StringUtil.NVL(node.path("phoneNumber").getTextValue(), "");

		commonService.checkAuthorized(UUID);

		// 필수 값 체크
		if("".equals(UUID) ||"".equals(intType)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			try {
				presenceInteractionLog.setUUID(UUID);
				presenceInteractionLog.setIntType(intType);
				presenceInteractionLog.setDeviceInfo(deviceInfo);
				presenceInteractionLog.setPhoneNumber(phoneNumber);
				logService.registerPresenceInteractionLog(presenceInteractionLog);
				res.put("result", "success");
				res.put("code", "0");
			} catch(DataAccessException dae) {
				res.put("result", "error");
				res.put("code", "303");
			}
		}
		return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
	}

	// 콘텐츠 인터랙션 로그
	@RequestMapping(value="/log/contents/interaction", method=RequestMethod.POST)
	@ResponseBody
	public Object contentsInteraction(HttpServletRequest request, ContentsInteractionLog contentsInteractionLog
	) throws IOException, ServletException {
		Map<String, Object> res = new HashMap<String, Object>();

		String str = JsonUtil.getJson(request);
		JsonNode node = JsonUtil.toNode(str);

		String UUID = StringUtil.NVL(node.path("UUID").getTextValue(), "");
		Integer conNum = node.path("conNum").getIntValue();
		String deviceInfo = StringUtil.NVL(node.path("deviceInfo").getTextValue(), "");
		String phoneNumber = StringUtil.NVL(node.path("phoneNumber").getTextValue(), "");

		commonService.checkAuthorized(UUID);

		// 필수 값 체크
		if("".equals(UUID) ||conNum == null) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			try {
				contentsInteractionLog.setUUID(UUID);
				contentsInteractionLog.setConNum(conNum);
				contentsInteractionLog.setDeviceInfo(deviceInfo);
				contentsInteractionLog.setPhoneNumber(phoneNumber);
				logService.registerContentsInteractionLog(contentsInteractionLog);
				res.put("result", "success");
				res.put("code", "0");
			} catch(DataAccessException dae) {
				res.put("result", "error");
				res.put("code", "303");
			}
		}
		return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
	}

	@RequestMapping(value = "/log/beacon/sensor", method = RequestMethod.POST)
	@ResponseBody
	public Object beaconSensorLog(HttpServletRequest request) throws IOException {

	    Map<String, Object> res = new HashMap<String, Object>();

        String str = JsonUtil.getJson(request);
        JsonNode node = JsonUtil.toNode(str);

        String SUUID = node.get("SUUID").getTextValue();
        String floor = StringUtil.NVL(node.path("floor").getTextValue());

        commonService.checkAuthorized(SUUID);

        if ("".equals(SUUID) || "".equals(floor)) {
            res.put("result", "error");
            res.put("code", "200");
        } else {
            BeaconSensorLog vo = new BeaconSensorLog();
            vo.setSUUID(SUUID);
            vo.setUUID(StringUtil.NVL(node.get("UUID").getTextValue()));
            vo.setCollector(StringUtil.NVL(node.path("collector").getTextValue()));
            vo.setLat(StringUtil.NVL(node.path("lat").getDoubleValue()));
            vo.setLng(StringUtil.NVL(node.path("lng").getDoubleValue()));
            vo.setFloor(floor);
            vo.setCo2(StringUtil.NVL(node.path("co2").getDoubleValue()));
            vo.setO2(StringUtil.NVL(node.path("o2").getDoubleValue()));
            vo.setDust(StringUtil.NVL(node.path("dust").getDoubleValue()));
            vo.setTemperature(StringUtil.NVL(node.path("temperature").getDoubleValue()));
            vo.setHumidity(StringUtil.NVL(node.path("humidity").getDoubleValue()));
            vo.setBattery(StringUtil.NVL(node.path("battery").getDoubleValue()));

            logService.registerBeaconSensorLog(vo);
            res.put("result", "success");
            res.put("code", "0");
        }
		return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
	}

}