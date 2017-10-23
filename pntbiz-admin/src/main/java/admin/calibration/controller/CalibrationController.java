package admin.calibration.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import core.admin.calibration.domain.Calibration;
import core.admin.calibration.domain.CalibrationSearchParam;
import admin.calibration.service.CalibrationService;
import core.common.code.domain.Code;
import admin.code.service.CodeService;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;

@Controller
public class CalibrationController {

	@Autowired
	private CalibrationService calibrationService;
	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private CodeService codeService;

	@RequestMapping(value="/admin/calibration/list.do", method=RequestMethod.GET)
	public ModelAndView list(CalibrationSearchParam param, Code code) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/calibration/list");

		code.setgCD("MAKER");
		List<?> makerCD = codeService.getCodeListByCD(code);
		code.setgCD("TELECOM");
		List<?> telecomCD = codeService.getCodeListByCD(code);

		param.setPageSize(30);
		Integer cnt = calibrationService.getCalibrationCount(param);
		List<?> list = calibrationService.getCalibrationList(param);
		list = calibrationService.bindCalibrationList(list, makerCD, telecomCD);

		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("makerCD", makerCD);
		mnv.addObject("telecomCD", telecomCD);
		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("page", page);
		mnv.addObject("param", param);
		logger.info("list {}", list);

		return mnv;
	}

	@RequestMapping(value="/admin/calibration/form.do", method=RequestMethod.GET)
	public ModelAndView calibrationForm(Code code) {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/calibration/form");

		code.setgCD("MAKER");
        mnv.addObject("makerCD", codeService.getCodeListByCD(code));
		code.setgCD("TELECOM");
        mnv.addObject("telecomCD", codeService.getCodeListByCD(code));

		return mnv;
	}

	@RequestMapping(value="/admin/calibration/mform.do", method=RequestMethod.GET)
	public ModelAndView calibrationMForm(Code code, Calibration calibration) throws ParseException {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/admin/calibration/mform");

        code.setgCD("MAKER");
        mnv.addObject("makerCD", codeService.getCodeListByCD(code));
        code.setgCD("TELECOM");
        mnv.addObject("telecomCD", codeService.getCodeListByCD(code));

        //캘리브레이션 정보
		mnv.addObject("calibrationInfo", calibrationService.getCalibrationInfo(calibration));
		return mnv;
	}

	// 캘리브레이션 등록
	@RequestMapping(value="/admin/calibration/reg.do", method=RequestMethod.POST)
	@ResponseBody
	public String calibrationReg(HttpServletRequest request, Calibration calibration) throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		try {
			calibrationService.registerCalibration(request, calibration);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {
		}
		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	// 캘리브레이션 수정
	@RequestMapping(value="/admin/calibration/mod.do", method=RequestMethod.POST)
	@ResponseBody
	public String calibrationMod(HttpServletRequest request, Calibration calibration) throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		try {
			calibrationService.modifyCalibration(request, calibration);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		} finally {
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}

	// 캘리브레이션 삭제
	@RequestMapping(value="/admin/calibration/del.do", method=RequestMethod.POST)
	@ResponseBody
	public String calibrationDel(Calibration calibration) throws ServletException, IOException {
		Map<String, Object> info = new HashMap<String, Object>();

		try {
			calibrationService.removeCalibration(calibration);
			info.put("result", "1");
		} catch(DataAccessException dae) {
			info.put("result", "2");
		}

		JsonUtil jsonUtil = new JsonUtil(info);
		String json = jsonUtil.toJson();
		return json;
	}
}