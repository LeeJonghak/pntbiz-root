package api.beacon.controller;

import api.beacon.domain.BeaconExternalRequestParam;
import api.beacon.service.BeaconExternalService;
import api.beacon.service.BeaconService;
import core.api.beacon.domain.Beacon;
import core.common.beacon.domain.BeaconExternal;
import core.common.beacon.domain.BeaconExternalLog;
import core.common.beacon.domain.BeaconExternalWidthRestrictedZone;
import core.common.beacon.domain.BeaconRestrictedZoneLog;
import framework.exception.BaseException;
import framework.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by nsyun on 17. 7. 20..
 */
@Controller
public class BeaconExternalController {


	@Autowired
	private BeaconService beaconService;

	@Autowired
	private BeaconExternalService beaconExternalService;

	@RequestMapping(value = "/beacon/external/{UUID}/{majorVer}/{minorVer}/{command}", method = RequestMethod.POST)
	@ResponseBody
	public Object beaconExternalPost(@PathVariable(value = "UUID", required = true)String UUID,
									 @PathVariable(value = "majorVer", required = true)Integer majorVer,
									 @PathVariable(value = "minorVer", required = true)Integer minorVer,
									 @PathVariable(value = "command", required = true)String command,
									 @RequestBody(required = false) String requestJson) {

		Map<String, Object> res = new HashMap<String, Object>();

		Beacon beaconParam = new Beacon();
		beaconParam.setUUID(UUID);
		beaconParam.setMajorVer(majorVer);
		beaconParam.setMinorVer(minorVer);
		Beacon beaconInfo = beaconService.getBeaconInfo(beaconParam);

		if (beaconInfo == null) throw new BaseException("303", "");	// 비콘정보 찾을 수 없음

		if(StringUtils.equals("assign", command)) {

			Map<Object, Object> requestData = JsonUtils.readValue(requestJson, Map.class);
			BeaconExternalRequestParam requestParam = JsonUtils.convertValue(requestData, BeaconExternalRequestParam.class);

			beaconExternalService.assign(beaconInfo.getBeaconNum(), requestParam);
			res.put("result", "success");
			res.put("code", "0");
		}
		else if(StringUtils.equals("unassign", command)) {

			beaconExternalService.unassign(beaconInfo.getBeaconNum());

			res.put("result", "success");
			res.put("code", "0");
		} else {
			res.put("result", "error");
			res.put("code", "200");
		}

		return res;
	}



	@RequestMapping(value = "/beacon/external/{UUID}/{majorVer}/{minorVer}/info", method = RequestMethod.GET)
	@ResponseBody
	public Object beaconExternalInfo(@PathVariable(value = "UUID", required = true)String UUID,
									 @PathVariable(value = "majorVer", required = true)Integer majorVer,
									 @PathVariable(value = "minorVer", required = true)Integer minorVer) {

		Beacon beaconParam = new Beacon();
		beaconParam.setUUID(UUID);
		beaconParam.setMajorVer(majorVer);
		beaconParam.setMinorVer(minorVer);
		Beacon beaconInfo = beaconService.getBeaconInfo(beaconParam);

		if (beaconInfo == null) throw new BaseException("303", "");	// 비콘정보 찾을 수 없음

		Map<String, Object> res = new HashMap<>();
		BeaconExternal externalInfo = beaconExternalService.info(beaconInfo.getBeaconNum());
		res.put("result", "success");
		res.put("code", "0");
		res.put("data", externalInfo);

		return res;

	}

	@RequestMapping(value = "/beacon/external/{UUID}/{majorVer}/{minorVer}/log", method = RequestMethod.GET)
	@ResponseBody
	public Object beaconExternalLogInfo(@PathVariable(value = "UUID", required = true)String UUID,
									 @PathVariable(value = "majorVer", required = true)Integer majorVer,
									 @PathVariable(value = "minorVer", required = true)Integer minorVer) {
		Map<String, Object> res = new HashMap<String, Object>();

		Beacon beaconParam = new Beacon();
		beaconParam.setUUID(UUID);
		beaconParam.setMajorVer(majorVer);
		beaconParam.setMinorVer(minorVer);
		Beacon beaconInfo = beaconService.getBeaconInfo(beaconParam);

		if(beaconInfo!=null) {

			Map<String, Object> data = new HashMap<String, Object>();

			List<BeaconExternalLog> externalLogList = beaconExternalService.listExternalLog(beaconInfo.getBeaconNum());
			List<BeaconRestrictedZoneLog> restrictedZoneLogList = beaconExternalService.listRestrictedZoneLog(beaconInfo.getBeaconNum());
			data.put("external", externalLogList);
			data.put("restrictedZone", restrictedZoneLogList);

			res.put("result", "success");
			res.put("code", "0");
			res.put("data", data);

		} else {

			res.put("result", "not found beacon");
			res.put("code", "300");
		}

		return res;
	}

	@RequestMapping(value = "/beacon/external/{UUID}/{floor}", method = RequestMethod.GET)
	@ResponseBody
	public Object beaconExternalList(@PathVariable(value = "UUID", required = true)String UUID,
									 @PathVariable(value = "floor", required = true)String floor) {

		Map<String, Object> res = new HashMap<String, Object>();
		List<BeaconExternalWidthRestrictedZone> externalList = beaconExternalService.list(UUID, floor);
		res.put("result", "success");
		res.put("code", "0");
		res.put("data", externalList);

		return res;
	}

}
