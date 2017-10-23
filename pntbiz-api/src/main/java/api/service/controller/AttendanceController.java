package api.service.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import api.common.service.CommonService;
import core.api.service.domain.Attendance;
import api.service.service.AttendanceService;

import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import framework.web.util.DateUtil;
import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;
 
@Controller
public class AttendanceController {	
	
	@Autowired
	private AttendanceService attendanceService;

	@Autowired
	private CommonService commonService;

	private Logger logger = LoggerFactory.getLogger(getClass());	
	
	// 2015-06-08 jhlee 계속입력받도록 변경
	// 2015-06-10 jhlee  휴대폰번호 하이픈 제거
	@RequestMapping(value="/attendance/info", method=RequestMethod.POST)
	@ResponseBody
	public Object attendanceReg(HttpServletRequest request, Attendance attendance
			) throws Exception {
		Map<String, Object> res = new HashMap<String, Object>();	
		
		String str = JsonUtil.getJson(request);
		JsonNode node = JsonUtil.toNode(str);
		
		String UUID = StringUtil.NVL(node.path("UUID").getTextValue(), "");
		String sidNum = StringUtil.NVL(node.path("sidNum").getTextValue(), "");
		String sName = StringUtil.NVL(node.path("sName").getTextValue(), "");
		String subject = StringUtil.NVL(node.path("subject").getTextValue(), "");
		String sPhoneNumber = StringUtil.NVL(node.path("sPhoneNumber").getTextValue(), "");
		String attdDate = DateUtil.getDate("yyyyMMdd");

		commonService.checkAuthorized(UUID);

		// 필수 값 체크
		if("".equals(UUID) || "".equals(sidNum) || "".equals(sName) || "".equals(subject) || "".equals(sPhoneNumber)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {			
			try {
				attendance.setUUID(UUID);
				attendance.setSidNum(sidNum);
				attendance.setsName(sName);
				attendance.setSubject(subject);
				attendance.setsPhoneNumber(StringUtil.replace(sPhoneNumber, "-", ""));
				attendance.setAttdDate(attdDate);
				attendanceService.registerAttendance(attendance);
				logger.debug("attendance {} ", attendance);
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
	
	// 출석체크 - old
//	@RequestMapping(value="/attendance/info", method=RequestMethod.POST)
//	@ResponseBody
//	public String attendanceReg(HttpServletRequest request, Attendance attendance
//			) throws Exception {
//		Map<String, PresenceRequestParam> res = new HashMap<String, PresenceRequestParam>();
//		
//		String str = JsonUtil.getJson(request);
//		JsonNode node = JsonUtil.toNode(str);
//		
//		String UUID = StringUtil.NVL(node.path("UUID").getTextValue(), "");
//		String sidNum = StringUtil.NVL(node.path("sidNum").getTextValue(), "");
//		String sName = StringUtil.NVL(node.path("sName").getTextValue(), "");
//		String subject = StringUtil.NVL(node.path("subject").getTextValue(), "");
//		String sPhoneNumber = StringUtil.NVL(node.path("sPhoneNumber").getTextValue(), "");
//		String attdDate = DateUtil.getDate("yyyyMMdd");
//		
//		// 필수 값 체크
//		if("".equals(UUID) || "".equals(sidNum) || "".equals(sName) || "".equals(subject) || "".equals(sPhoneNumber)) {
//			res.put("result", "error");
//			res.put("code", "200");
//		} else {			
//			try {
//				attendance.setUUID(UUID);
//				attendance.setSidNum(sidNum);
//				attendance.setsName(sName);
//				attendance.setSubject(subject);
//				attendance.setsPhoneNumber(sPhoneNumber);
//				attendance.setAttdDate(attdDate);
//				int cnt = attendanceService.getAttendanceCount(attendance);			
//				logger.debug("attendaceCount : {}", cnt);
//				if(cnt > 0) {
//					attendanceService.modifyAttendance(attendance);
//				} else {
//					attendanceService.registerAttendance(attendance);
//				}
//				res.put("result", "success");
//				res.put("code", "0");
//			} catch(DataAccessException dae) {
//				res.put("result", "error");
//				res.put("code", "303");
//			}
//		}
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
//	}
	
}