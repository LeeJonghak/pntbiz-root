package wms.service.controller;

import core.wms.admin.company.domain.Company;
import wms.admin.company.service.CompanyService;
import au.com.bytecode.opencsv.CSVWriter;
import wms.component.auth.LoginDetail;
import framework.Security;
import framework.web.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import core.wms.service.domain.AttendanceSeminar;
import core.wms.service.domain.AttendanceSeminarMgr;
import core.wms.service.domain.AttendanceSeminarSearchParam;
import wms.service.service.AttendanceSeminarService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ServiceAttendanceSeminarController {

	@Autowired
	private AttendanceSeminarService attendanceSeminarService;

	@Autowired
	private CompanyService companyService;

	private Logger logger = LoggerFactory.getLogger(getClass());

	// 출석체크 리스트
	@RequestMapping(value="/service/attendanceSeminar/list.do", method=RequestMethod.GET)
	public ModelAndView list(AttendanceSeminarSearchParam param, Company company) throws Exception {
		ModelAndView mnv = new ModelAndView();
		mnv.setViewName("/service/attendanceSeminar/list");

		param.setPageSize(30);
		LoginDetail loginDetail = Security.getLoginDetail();
		company.setComNum(loginDetail.getCompanyNumber());
		Company companyInfo = companyService.getCompanyInfo(company);
		param.setUUID(companyInfo.getUUID());
		if("".equals(param.getAttdDate()) || param.getAttdDate() == null) {
			param.setAttdDate(DateUtil.getDate("yyyyMMdd"));
		}

		Integer cnt = attendanceSeminarService.getAttendanceSeminarCount(param);
		List<?> list = attendanceSeminarService.getAttendanceSeminarList(param);

		Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
		pagination.queryString = param.getQueryString();
		String page = pagination.print();

		mnv.addObject("cnt", cnt);
		mnv.addObject("list", list);
		mnv.addObject("page", page);
		mnv.addObject("param", param);
		mnv.addObject("attdDate", param.getAttdDate());
		logger.info("list {}", list);

        mnv.addObject("seminarList", attendanceSeminarService.getAttendanceSeminarMgrListAll(companyInfo.getUUID()));

		return mnv;
	}

    @RequestMapping(value="/service/attendanceSeminar/export.do", method=RequestMethod.GET)
    public ModelAndView export(HttpServletRequest request, HttpServletResponse response, AttendanceSeminarSearchParam param, Company company
    ) throws Exception {
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/service/attendanceSeminar/list");

        LoginDetail loginDetail = Security.getLoginDetail();
        company.setComNum(loginDetail.getCompanyNumber());
        Company companyInfo = companyService.getCompanyInfo(company);
        param.setUUID(companyInfo.getUUID());
        if("".equals(param.getAttdDate()) || param.getAttdDate() == null) {
            param.setAttdDate(DateUtil.getDate("yyyyMMdd"));
        }
        param.setPageSize(10000);
        List<?> list = attendanceSeminarService.getAttendanceSeminarList(param);

        response.setContentType("data:text/csv;charset=euc-kr");
        response.setHeader("Content-Disposition","attachment; filename=\"export.csv\"");
        OutputStream resOs= response.getOutputStream();
        OutputStream buffOs= new BufferedOutputStream(resOs);
        OutputStreamWriter outputwriter = new OutputStreamWriter(buffOs, "euc-kr");

        @SuppressWarnings("resource")
		CSVWriter writer = new CSVWriter(outputwriter, ',');

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        writer.writeNext(new String[]{"No.", "Seminar Name", "Major Version", "Minor Version", "Phone Number", "DeviceInfo", "Enter/Leave", "Date", "Date Time"});
        for(Object item: list) {
            AttendanceSeminar attendance = (AttendanceSeminar) item;
            writer.writeNext(new String[]{
                    String.valueOf(attendance.getLogNum()),
                    attendance.getSubject(),
                    String.valueOf(attendance.getMajorVer()),
                    String.valueOf(attendance.getMinorVer()),
                    attendance.getPhoneNumber(),
                    String.valueOf(attendance.getDeviceInfo()),
                    "E".equals(attendance.getState())?"Enter":"Leave",
                    attendance.getAttdDate(),
                    dateFormat.format(new Date(attendance.getRegDate()*1000))
            });
        }
        outputwriter.flush();
        outputwriter.close();

        return mnv;
    }

    @RequestMapping(value="/service/attendanceSeminar/reg.do", method=RequestMethod.POST)
    @ResponseBody String reg(@RequestParam(value = "seminarName")String seminarName,
                             @RequestParam(value = "majorVer")String majorVer,
                             @RequestParam(value = "minorVer")String minorVer,
                             @RequestParam(value = "macAddr")String macAddr) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            Company companyParam = new Company();
            companyParam.setComNum(loginDetail.getCompanyNumber());
            Company companyInfo = companyService.getCompanyInfo(companyParam);

            AttendanceSeminarMgr vo = new AttendanceSeminarMgr();
            vo.setSubject(seminarName);
            vo.setUUID(companyInfo.getUUID());
            vo.setMajorVer(Integer.parseInt(majorVer));
            vo.setMinorVer(Integer.parseInt(minorVer));
            vo.setMacAddr(macAddr);
            attendanceSeminarService.registerAttendanceSeminarMgr(vo);
            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();

    }

    @RequestMapping(value="/service/attendanceSeminar/del.do", method=RequestMethod.POST)
    @ResponseBody String del(@RequestParam(value = "majorVer")String majorVer,
                             @RequestParam(value = "minorVer")String minorVer) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            Company companyParam = new Company();
            companyParam.setComNum(loginDetail.getCompanyNumber());
            Company companyInfo = companyService.getCompanyInfo(companyParam);

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("UUID", companyInfo.getUUID());
            param.put("majorVer", Integer.parseInt(majorVer));
            param.put("minorVer", Integer.parseInt(minorVer));
            attendanceSeminarService.deleteAttendanceSeminarMgr(param);
            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();

    }

    @RequestMapping(value="/service/attendanceSeminar/delLog.do", method=RequestMethod.POST)
    @ResponseBody String delLog(@RequestParam(value = "logNum")String pLogNum,
                                @RequestParam(value = "attdDate")String attdDate) throws IOException {

        Map<String, String> info = new HashMap<String, String>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            Company companyParam = new Company();
            companyParam.setComNum(loginDetail.getCompanyNumber());
            Company companyInfo = companyService.getCompanyInfo(companyParam);

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("UUID", companyInfo.getUUID());
            param.put("logNum", Long.parseLong(pLogNum));
            param.put("attdDate", Long.parseLong(attdDate));
            attendanceSeminarService.deleteAttendanceSeminar(param);
            info.put("result", "1");
        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();

    }

    //attdDate
    @RequestMapping(value="/service/attendanceSeminar/reset.do", method=RequestMethod.POST)
    @ResponseBody String del(@RequestParam(value = "attdDate", required = true)String attdDate) throws IOException {
        Map<String, String> info = new HashMap<String, String>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            Company companyParam = new Company();
            companyParam.setComNum(loginDetail.getCompanyNumber());
            Company companyInfo = companyService.getCompanyInfo(companyParam);

            Map<String, Object> param = new HashMap<String, Object>();
            param.put("UUID", companyInfo.getUUID());
            param.put("attdDate", attdDate);
            attendanceSeminarService.deleteAttendanceSeminar(param);
            info.put("result", "1");

        } catch(Exception e) {
            info.put("result", "2");
            logger.error("exception", e);
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

}