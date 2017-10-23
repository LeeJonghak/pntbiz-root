package wms.map.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import framework.Security;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.jcraft.jsch.SftpException;

import core.wms.admin.company.domain.Company;
import wms.component.auth.LoginDetail;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;
import core.wms.map.domain.FloorCode;
import wms.map.service.FloorCodeService;
import wms.map.service.FloorService;

@Controller
public class FloorCodeController {

    @Autowired
    private FloorCodeService service;

    @Autowired
    private FloorService floorService;
    private Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(value="/map/floorCode/list.do", method=RequestMethod.GET)
    public ModelAndView floorCodeList(FloorCode param) throws DataAccessException, ParseException {
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/map/floorCode/list");
        Gson gson = new Gson();

        // 로그인정보
        LoginDetail loginDetail = Security.getLoginDetail();
        // 업체정보
        Company company = new Company();
        company.setComNum(loginDetail.getCompanyNumber());

        param.setComNum(loginDetail.getCompanyNumber());
        param.setPageSize(30);
        Integer cnt = service.getFloorCodeCount(param);
        List<?> list = service.getFloorCodeList(param);

        Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
        pagination.queryString = param.getQueryString();

        mnv.addObject("result", "1");
        mnv.addObject("cnt", cnt);
        mnv.addObject("list", list);
        mnv.addObject("pagination", pagination.print());
        mnv.addObject("page", param.getPage());
        mnv.addObject("param", param);
        mnv.addObject("floorCodeList", gson.toJson(floorService.getFloorCodeList(company)));

        return mnv;
    }

    @RequestMapping(value="/map/floorCode/form.do", method=RequestMethod.GET)
    public ModelAndView floorCodeForm(FloorCode param) {
        ModelAndView mnv = new ModelAndView();
        Gson gson = new Gson();

        // 로그인정보
        LoginDetail loginDetail = Security.getLoginDetail();

        // 업체정보
        Company company = new Company();
        company.setComNum(loginDetail.getCompanyNumber());

        mnv.addObject("floorCodeList", gson.toJson(floorService.getFloorCodeList(company)));
        mnv.setViewName("/map/floorCode/form");

        return mnv;
    }

    @RequestMapping(value="/map/floorCode/mform.do", method=RequestMethod.GET)
    public ModelAndView floorCodeMForm(FloorCode param) {
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/map/floorCode/mform");
        Gson gson = new Gson();

        // 로그인정보
        LoginDetail loginDetail = Security.getLoginDetail();

        // 업체정보
        Company company = new Company();
        company.setComNum(loginDetail.getCompanyNumber());

        param.setComNum(loginDetail.getCompanyNumber());

        mnv.addObject("floorCodeList", gson.toJson(floorService.getFloorCodeList(company)));
        mnv.addObject("floorCodeInfo", service.getFloorCodeInfo(param));

        return mnv;
    }

    @RequestMapping(value="/map/floorCode/reg.do", method=RequestMethod.POST)
    @ResponseBody
    public String floorCodeReg(HttpServletResponse response, FloorCode param) throws ServletException, SftpException, IOException {
        return this.floorCodeAction(param, "I");
    }

    @RequestMapping(value="/map/floorCode/mod.do", method=RequestMethod.POST)
    @ResponseBody
    public String floorCodeMod(HttpServletResponse response, FloorCode param) throws ServletException, SftpException, IOException {
        return this.floorCodeAction(param, "M");
    }

    @RequestMapping(value="/map/floorCode/del.do", method=RequestMethod.POST)
    @ResponseBody
    public String floorCodeDel(HttpServletResponse response, FloorCode param) throws ServletException, SftpException, IOException {

        return this.floorCodeAction(param, "D");
    }

    private String floorCodeAction(FloorCode param, String type) throws ServletException, SftpException, IOException {
        Map<String, Object> info = new HashMap<String, Object>();
        // 로그인정보
        LoginDetail loginDetail = Security.getLoginDetail();
        param.setComNum(loginDetail.getCompanyNumber());
        String rtn = "1";
        try {
            //1. 중복체크
            if(type.equals("I"))
                if(service.getFloorCodeInfo(param) != null) {
                    rtn ="3"; // 이미등록
                }else{
                    service.registerFloorCode(param);
                }
            else if (type.equals("M"))
                service.modifyFloorCode(param);
            else if (type.equals("D"))
                service.removeFloorCode(param);

        } catch(DataAccessException dae) {
            dae.printStackTrace();
            rtn = "2";
        }

        info.put("result", rtn);
        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();

        return json;
    }
}