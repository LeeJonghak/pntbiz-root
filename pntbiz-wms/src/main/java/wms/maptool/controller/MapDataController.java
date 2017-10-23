package wms.maptool.controller;

import framework.web.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import wms.component.auth.LoginDetail;
import framework.web.util.JsonUtil;
import framework.Security;
import wms.maptool.service.MapToolService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.*;

/**
 */
@Controller
public class MapDataController {

	@Autowired
	private MapToolService mapToolService;

	@Value("#{config['contentsURL']}")
	private String contentsURL;

	@Value("#{config['beacon.image.src']}")
	private String beaconImageSrc;


	@RequestMapping(value = "/maptool/data/floor.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String floorListJson(@RequestParam(value = "comNum", required = false)Integer comNum) throws IOException {

		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> floorParam = new HashMap<String, Object>();
			if(comNum!=null) {
				floorParam.put("comNum", comNum);
			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				floorParam.put("comNum", loginDetail.getCompanyNumber());
			}
			List<Map<String, Object>> list = mapToolService.getFloorList(floorParam);

			info.put("result", "1");
			info.put("data", list);
		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value = "/maptool/data/area.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String areaListJson(@RequestParam(value = "comNum", required = false)Integer comNum) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> floorParam = new HashMap<String, Object>();
			if(comNum!=null) {
				floorParam.put("comNum", comNum);
			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				floorParam.put("comNum", loginDetail.getCompanyNumber());
			}
			List<?> list = mapToolService.getAreaList(floorParam);

			info.put("result", "1");
			info.put("data", list);
		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value = "/maptool/data/scanner.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String scannerListJson(@RequestParam("floor")String floor,
								  @RequestParam(value = "comNum", required = false)Integer comNum) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if(comNum!=null) {
				param.put("comNum", comNum);
			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				param.put("comNum", loginDetail.getCompanyNumber());
			}


			param.put("floor", floor);
			List<?> list = mapToolService.getScannerList(param);

			info.put("result", "1");
			info.put("data", list);
		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value = "/maptool/data/beacon.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String beaconListJson(@RequestParam(value = "floor", required = false)String floor,
								 @RequestParam(value = "comNum", required = false)Integer comNum,
								 @RequestParam(value = "field", required = false)String field) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		List<String> allowField = new ArrayList<String>();
		if(field!=null) {
			String[] arr = field.split(",");
			allowField = Arrays.asList(arr);
		}

		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if(comNum!=null) {
				param.put("comNum", comNum);
			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				param.put("comNum", loginDetail.getCompanyNumber());
			}
			if(floor!=null) {
				param.put("floor", floor);
			}
			List<?> list = mapToolService.getBeaconList(param);

			List<?> groupMappingList = mapToolService.getBeaconGroupMappingList(param);
			HashMap<String, List<Map<String, Object>>> groupMappingMap = new HashMap<String, List<Map<String, Object>>>();
			for(Object obj: groupMappingList) {
				Map<String, Object> mappingInfo = (Map<String, Object>)obj;
				String key = String.valueOf(mappingInfo.get("beaconNum"));
				if(!groupMappingMap.containsKey(key)) {
					groupMappingMap.put(key, new ArrayList<Map<String, Object>>());
				}
				groupMappingMap.get(key).add(mappingInfo);
			}

			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for(Object obj: list) {
				Map<String, Object> beaconInfo = (Map<String, Object>)obj;

				String key = String.valueOf(beaconInfo.get("beaconNum"));
				if(groupMappingMap.containsKey(key)) {
					beaconInfo.put("groupMapping", groupMappingMap.get(key));
					List<String> groupNames = new ArrayList<String>();
					for(Map<String, Object> groupInfo: groupMappingMap.get(key)) {
						groupNames.add(String.valueOf(groupInfo.get("beaconGroupName")));
					}
					beaconInfo.put("groupNames", StringUtils.join(groupNames, ","));
				} else {
					beaconInfo.put("groupMapping", new ArrayList<Map<String, Object>>());
					beaconInfo.put("groupNames", "");
				}

				// 비콘 설치 사진
				if(!"".equals(StringUtil.NVL((String)beaconInfo.get("imgSrc"), ""))){
					String fileDate = StringUtil.getSubString((String)beaconInfo.get("imgSrc"), 0, 6);

					String beaconImageURL = contentsURL + "/" + beaconInfo.get("comNum") + beaconImageSrc + "/" + fileDate + "/";
					String imgUrl = beaconImageURL + beaconInfo.get("imgSrc");
					beaconInfo.put("imgUrl", imgUrl);
				}
				// --

				if(allowField.size()>0) {
					Map<String, Object> item = new HashMap<String, Object>();
					for(int i=0; i<allowField.size(); i++) {
						String fieldName = StringUtils.trim(allowField.get(i));
						if(beaconInfo.containsKey(fieldName)) {
							item.put(fieldName, beaconInfo.get(fieldName));
						}
					}
					result.add(item);
				} else {
					result.add(beaconInfo);
				}
			}

			info.put("result", "1");
			info.put("data", result);
		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value = "/maptool/data/beaconGroup.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String beaconGroupListJson(@RequestParam(value = "comNum", required = false)Integer comNum) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if(comNum!=null) {
				param.put("comNum", comNum);
			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				param.put("comNum", loginDetail.getCompanyNumber());
			}
			List<?> list = mapToolService.getBeaconGroupList(param);

			info.put("result", "1");
			info.put("data", list);
		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value = "/maptool/data/node.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String nodeListJson(@RequestParam("floor")String floor,
							   @RequestParam(value = "type", required = false)String type,
							   @RequestParam(value = "comNum", required = false)Integer comNum) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if(comNum!=null) {
				param.put("comNum", comNum);
			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				param.put("comNum", loginDetail.getCompanyNumber());
			}
			param.put("floor", floor);
			if(type!=null) {
				param.put("type", type);
			}
			List<?> list = mapToolService.getNodeList(param);

			info.put("result", "1");
			info.put("data", list);
		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value = "/maptool/data/nodeEdge.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String nodeEdgeListJson(@RequestParam("floor")String floor,
								   @RequestParam(value = "type", required = false)String type,
								   @RequestParam(value = "comNum", required = false)Integer comNum) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if(comNum!=null) {
				param.put("comNum", comNum);
			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				param.put("comNum", loginDetail.getCompanyNumber());
			}
			param.put("floor", floor);
			if(type!=null) {
				param.put("type", type);
			}
			List<?> list = mapToolService.getNodeEdgeList(param);

			info.put("result", "1");
			info.put("data", list);
		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}



	@RequestMapping(value = "/maptool/data/geofence.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String geofenceListJson(@RequestParam("floor")String floor,
								   @RequestParam(value = "comNum", required = false)Integer comNum) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if(comNum!=null) {
				param.put("comNum", comNum);
			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				param.put("comNum", loginDetail.getCompanyNumber());
			}
			param.put("floor", floor);
			List<?> list = mapToolService.getGeofenceList(param);

			List<?> groupMappingList = mapToolService.getGeofencingGroupMappingList(param);
			HashMap<String, List<Map<String, Object>>> groupMappingMap = new HashMap<String, List<Map<String, Object>>>();
			for(Object obj: groupMappingList) {
				Map<String, Object> mappingInfo = (Map<String, Object>)obj;
				String key = String.valueOf(mappingInfo.get("fcNum"));
				if(!groupMappingMap.containsKey(key)) {
					groupMappingMap.put(key, new ArrayList<Map<String, Object>>());
				}
				groupMappingMap.get(key).add(mappingInfo);
			}

			List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
			for(Object obj: list) {
				Map<String, Object> geofenceInfo = (Map<String, Object>)obj;

				String key = String.valueOf(geofenceInfo.get("fcNum"));
				if(groupMappingMap.containsKey(key)) {
					geofenceInfo.put("groupMapping", groupMappingMap.get(key));
					List<String> groupNames = new ArrayList<String>();
					for(Map<String, Object> groupInfo: groupMappingMap.get(key)) {
						groupNames.add(String.valueOf(groupInfo.get("fcGroupName")));
					}
					geofenceInfo.put("groupNames", StringUtils.join(groupNames, ","));
				} else {
					geofenceInfo.put("groupMapping", new ArrayList<Map<String, Object>>());
					geofenceInfo.put("groupNames", "");
				}
				result.add(geofenceInfo);
			}

			info.put("result", "1");
			info.put("data", result);
		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value = "/maptool/data/geofenceGroup.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String geofenceGroupListJson(@RequestParam(value = "comNum", required = false)Integer comNum) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if(comNum!=null) {
				param.put("comNum", comNum);
			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				param.put("comNum", loginDetail.getCompanyNumber());
			}
			List<?> list = mapToolService.getGeofencingGroupList(param);

			info.put("result", "1");
			info.put("data", list);
		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}



	@RequestMapping(value = "/maptool/data/contents.do", method = RequestMethod.GET)
	@ResponseBody
	public String contentsListJson(@RequestParam(value = "comNum", required = false)Integer comNum) throws IOException {

		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if(comNum!=null) {
				param.put("comNum", comNum);
			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				param.put("comNum", loginDetail.getCompanyNumber());
			}
			List<?> list = mapToolService.getContentList(param);

			info.put("result", "1");
			info.put("data", list);

		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value = "/maptool/data/contentsMapping.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String contentsMappingListJson(@RequestParam(value = "floor", required = false)String floor,
										  @RequestParam(value = "comNum", required = false)Integer comNum) throws IOException {

		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if(comNum!=null) {
				param.put("comNum", comNum);
			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				param.put("comNum", loginDetail.getCompanyNumber());
			}
			if(floor!=null) {
				param.put("floor", floor);
			}
			List<?> list = mapToolService.getContentMappingList(param);

			info.put("result", "1");
			info.put("data", list);

		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}


	@RequestMapping(value = "/maptool/data/presenceLog.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String presenceLogListJson(@RequestParam(value = "floor", required = false)String floor,
								      @RequestParam(value = "comNum", required = false)Integer comNum,
									  @RequestParam(value = "majorVer", required = false)Integer majorVer,
									  @RequestParam(value = "minorVer", required = false)Integer minorVer,
									  @RequestParam(value = "beaconNum", required = false)Long beaconNum,
									  @RequestParam(value = "startRegDate", required = false)Integer startRegDate,
									  @RequestParam(value = "endRegDate", required = false)Integer endRegDate) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if (comNum != null) {
				param.put("comNum", comNum);

			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				param.put("comNum", loginDetail.getCompanyNumber());
			}
			if(floor!=null) {
				param.put("floor", floor);
			}
			if(startRegDate!=null && endRegDate!=null) {
				param.put("startRegDate", startRegDate);
				param.put("endRegDate", endRegDate);
			}
			if(majorVer!=null) {
				param.put("majorVer", majorVer);
			}
			if(minorVer!=null) {
				param.put("minorVer", minorVer);
			}

			if(beaconNum!=null) {
				Map<String, Object> beaconParam = new HashMap<String, Object>();
				beaconParam.put("beaconNum", beaconNum);
				Map<String, Object> beaconInfo = mapToolService.getBeaconInfo(beaconParam);

				if(beaconInfo!=null) {
					param.put("majorVer", beaconInfo.get("majorVer"));
					param.put("minorVer", beaconInfo.get("minorVer"));

				} else {
					param.put("majorVer", 0);
					param.put("minorVer", 0);
				}
			}

			List<?> list = mapToolService.getPresenceLogList(param);
			info.put("data", list);

		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	@RequestMapping(value = "/maptool/data/presenceBeaconLog.do", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
	@ResponseBody
	public String presenceBeaconLogListJson(@RequestParam(value = "floor", required = false)String floor,
									  @RequestParam(value = "comNum", required = false)Integer comNum,
									  @RequestParam(value = "phoneNumber", required = false)String phoneNumber,
									  @RequestParam(value = "startRegDate", required = false)Integer startRegDate,
									  @RequestParam(value = "endRegDate", required = false)Integer endRegDate) throws IOException {
		Map<String, Object> info = new HashMap<String, Object>();
		try {

			Map<String, Object> param = new HashMap<String, Object>();
			if (comNum != null) {
				param.put("comNum", comNum);

			} else {
				LoginDetail loginDetail = Security.getLoginDetail();
				param.put("comNum", loginDetail.getCompanyNumber());
			}
			if(floor!=null) {
				param.put("floor", floor);
			}
			if(phoneNumber!=null) {
				param.put("phoneNumber", phoneNumber);
			}
			if(startRegDate!=null && endRegDate!=null) {
				param.put("startRegDate", startRegDate);
				param.put("endRegDate", endRegDate);
			}


			List<?> list = mapToolService.getPresenceBeaconLogList(param);
			info.put("data", list);

		} catch(Exception exception) {
			info.put("result", "2");
			info.put("error", exception.getMessage());
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

}
