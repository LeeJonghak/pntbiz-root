package api.map.controller;

import api.common.service.CommonService;
import api.map.service.FloorCodeService;
import core.api.map.domain.Floor;
import api.map.service.FloorService;

import core.api.map.domain.FloorCode;
import framework.web.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MapController {

    private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private FloorService floorService;

	@Autowired
	private FloorCodeService floorCodeService;

	@Autowired
	private CommonService commonService;

	// 층리스트
	@RequestMapping(value="/floor/info/{UUID}", method=RequestMethod.GET)
	@ResponseBody
	public Object floorList(HttpServletRequest request, HttpServletResponse response, Floor floor, 
			@PathVariable("UUID") String UUID
			) throws IOException, ParseException {

		commonService.checkAuthorized(UUID);

		Map<String, Object> res = new HashMap<String, Object>();
		
		UUID = StringUtil.NVL(UUID, "");
		
		if("".equals(UUID)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			floor.setUUID(UUID);
			List<?> list = floorService.getFloorList(floor);
			list = floorService.bindFloorList(list);
			logger.info("floor list {}", list);
			if(list == null || list.size() < 1) {
				res.put("result", "error");
				res.put("code", "303");
			} else {
				res.put("result", "success");
				res.put("code", "0");
				res.put("data", list);
			}
		}
		return res;
	}
	
	// 층정보
	@RequestMapping(value="/floor/info/{UUID}/{floorParam}", method=RequestMethod.GET)
	@ResponseBody
	public Object floorInfo(HttpServletRequest request, HttpServletResponse response, Floor floor, 
			@PathVariable("UUID") String UUID,
			@PathVariable("floorParam") String floorParam
			) throws IOException, ParseException {

		commonService.checkAuthorized(UUID);

		Map<String, Object> res = new HashMap<String, Object>();
		
		UUID = StringUtil.NVL(UUID, "");
		floorParam = StringUtil.NVL(floorParam, "");
		
		if("".equals(UUID) || "".equals(floorParam)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			floor.setUUID(UUID);
			floor.setFloor(floorParam);
			List<?> list = floorService.getFloorList(floor);
			list = floorService.bindFloorList(list);
			logger.info("floor list {}", list);
			if(list == null || list.size() < 1) {
				res.put("result", "error");
				res.put("code", "303");
			} else {
				res.put("result", "success");
				res.put("code", "0");
				res.put("data", list);
			}
		}
		return res;
	}

	// 층코드 리스트
	@RequestMapping(value="/floor/code/{UUID}", method=RequestMethod.GET)
	@ResponseBody
	public Object floorCodeList(FloorCode floorCode,
								@PathVariable("UUID") String UUID
	) throws IOException, ParseException {

		commonService.checkAuthorized(UUID);

		Map<String, Object> res = new HashMap<String, Object>();

		UUID = StringUtil.NVL(UUID, "");

		if("".equals(UUID)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			floorCode.setUUID(UUID);
			floorCode.setUseFlag("Y");
			List<?> list = floorCodeService.getFloorCodeHierarchyList(floorCode);
			logger.info("floorcode list {}", list);
			if(list == null || list.size() < 1) {
				res.put("result", "error");
				res.put("code", "303");
			} else {
				res.put("result", "success");
				res.put("code", "0");
				res.put("data", list);
			}
		}
		return res;
	}

}