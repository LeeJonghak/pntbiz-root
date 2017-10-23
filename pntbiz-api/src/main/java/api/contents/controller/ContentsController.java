package api.contents.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import api.common.service.CommonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import framework.web.util.StringUtil;
import core.api.contents.domain.Contents;
import api.contents.service.ContentsService;

@Controller
public class ContentsController {

	@Autowired
	private ContentsService contentsService;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CommonService commonService;

	// 콘텐츠목록
	@RequestMapping(value="/contents/info/{UUID}", method=RequestMethod.GET)
	@ResponseBody
	public Object contentsInfo(HttpServletRequest request, Contents contents,
			@PathVariable("UUID") String UUID
			) throws IOException, ServletException, ParseException {

		commonService.checkAuthorized(UUID);

		Map<String, Object> res = new HashMap<String, Object>();

		UUID = StringUtil.NVL(UUID, "");

		if("".equals(UUID)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			contents.setUUID(UUID);
			List<?> list = contentsService.getContentsList(contents);
			list = contentsService.bindContentsList(list);
			logger.info("contents list {}", list);
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
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}

	// 콘텐츠정보
	@RequestMapping(value="/contents/info/{UUID}/{conNum}", method=RequestMethod.GET)
	@ResponseBody
	public Object contentsInfoConNum(HttpServletRequest request, Contents contents,
			@PathVariable("UUID") String UUID,
			@PathVariable("conNum") Integer conNum
			) throws IOException, ServletException, ParseException {

		commonService.checkAuthorized(UUID);

		Map<String, Object> res = new HashMap<String, Object>();

		UUID = StringUtil.NVL(UUID, "");
		conNum = Integer.parseInt(StringUtil.NVL(conNum.toString(), "0"));

		if("".equals(UUID) || conNum == 0) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			contents.setUUID(UUID);
			contents.setConNum(conNum);
			Contents contentsInfo = contentsService.getContentsInfo(contents);
			contentsInfo = contentsService.bindContentsInfo(contentsInfo);
			logger.info("contents info", contentsInfo);
			if(contentsInfo == null) {
				res.put("result", "error");
				res.put("code", "303");
			} else {
				res.put("result", "success");
				res.put("code", "0");
				res.put("data", contentsInfo);
			}
		}
		return res;
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}

	// 콘텐츠정보-웰컴
	@RequestMapping(value="/contents/welcome/{UUID}", method=RequestMethod.GET)
	@ResponseBody
	public Object contentsWelcome(HttpServletRequest request, Contents contents,
			@PathVariable("UUID") String UUID
			) throws IOException, ServletException, ParseException {

		commonService.checkAuthorized(UUID);

		Map<String, Object> res = new HashMap<String, Object>();

		UUID = StringUtil.NVL(UUID, "");

		if("".equals(UUID)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			contents.setUUID(UUID);
			contents.setConType("WEL");
			Contents contentsInfo = contentsService.getContentsMessage(contents);
			if(contentsInfo == null) {
				res.put("result", "error");
				res.put("code", "303");
			} else {
	            contentsInfo = contentsService.bindContentsInfo(contentsInfo);
	            logger.info("contents info", contentsInfo);

				res.put("result", "success");
				res.put("code", "0");
				res.put("data", contentsInfo);
			}
		}
		return res;
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}

	// 콘텐츠정보-굿바이
	@RequestMapping(value="/contents/goodbye/{UUID}", method=RequestMethod.GET)
	@ResponseBody
	public Object contentsGoodbye(HttpServletRequest request, Contents contents,
			@PathVariable("UUID") String UUID
			) throws IOException, ServletException, ParseException {

		commonService.checkAuthorized(UUID);

		Map<String, Object> res = new HashMap<String, Object>();

		UUID = StringUtil.NVL(UUID, "");

		if("".equals(UUID)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			contents.setUUID(UUID);
			contents.setConType("BYE");
			Contents contentsInfo = contentsService.getContentsMessage(contents);
			if(contentsInfo == null) {
				res.put("result", "error");
				res.put("code", "303");
			} else {
	            contentsInfo = contentsService.bindContentsInfo(contentsInfo);
	            logger.info("contents info", contentsInfo);

				res.put("result", "success");
				res.put("code", "0");
				res.put("data", contentsInfo);
			}
		}
		return res;
//		JsonUtil jsonUtil = new JsonUtil(res);
//		String json = jsonUtil.toJson();
//		return json;
	}
}