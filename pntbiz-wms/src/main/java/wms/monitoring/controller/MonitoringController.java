package wms.monitoring.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import framework.Security;
import framework.web.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import core.wms.admin.company.domain.Company;
import wms.admin.company.service.CompanyService;
import wms.component.auth.LoginDetail;
import core.wms.monitoring.domain.Monitoring;
import wms.monitoring.service.MonitoringService;
import core.wms.scanner.domain.Scanner;
import core.wms.scanner.domain.ScannerSearchParam;
import wms.scanner.service.ScannerService;

@Controller
public class MonitoringController {

    @Autowired
    private ScannerService scannerService;

    @Autowired
    private MonitoringService service;

    @Autowired
    private CompanyService companyService;

    @RequestMapping(value="/monitoring/scanner/list.do", method=RequestMethod.GET)
    public ModelAndView list(ScannerSearchParam param) throws IOException, ParseException{
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/monitoring/scanner");

        LoginDetail loginDetail = Security.getLoginDetail();
        param.setComNum(loginDetail.getCompanyNumber());

        Company company = new Company();
        company.setComNum(loginDetail.getCompanyNumber());
        Company companyInfo = companyService.getCompanyInfo(company);

        String UUID = companyInfo.getUUID();

        param.setPageSizeZero();
        List<Scanner> list = (List<Scanner>) scannerService.getScannerList(param);

        List<Monitoring> rtnlist = new ArrayList<Monitoring>();
        Monitoring srtnVo = null;
        for(Scanner vo:list){
            srtnVo = new Monitoring();
            srtnVo.setName(vo.getScannerName());
            srtnVo.setMacAddr(vo.getMacAddr());
            srtnVo.setUUID(UUID);
            srtnVo.setId(UUID + "_" +StringUtil.removeCharacters(vo.getMacAddr(), ":"));
            //srtnVo.setId(UUID + "_" +vo.getMacAddr());

            rtnlist.add(srtnVo);
        }

        mnv.addObject("cnt", scannerService.getScannerCount(param));
        mnv.addObject("list", rtnlist);

        return mnv;
    }

    @RequestMapping(value="/monitoring/server/list.do", method=RequestMethod.GET)
    public ModelAndView listServer(ScannerSearchParam param) throws IOException, ParseException{
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/monitoring/server");

        /*LoginDetail loginDetail = CommonUtil.getLoginDetail();
        param.setComNum(loginDetail.getCompanyNumber());

        param.setPageSizeZero();
        List<Scanner> list = (List<Scanner>) scannerService.getScannerList(param);

        List<Monitoring> rtnlist = new ArrayList<Monitoring>();
        Monitoring srtnVo = null;
        for(Scanner vo:list){
            //
        }*/
        List<Monitoring> rtnlist = new ArrayList<Monitoring>();
        Monitoring vo = this.getInfo("server", "scanner_positioning_server");
        rtnlist.add(vo);

        mnv.addObject("cnt", 0);
        mnv.addObject("list", rtnlist);

        return mnv;
    }

    @RequestMapping(value="/monitoring/scanner/info_ajax.do", method=RequestMethod.POST)
    @ResponseBody
    public String getScannerInfo( @RequestParam(value="data[]") String[] param) throws IOException {
        List<Monitoring> rtnList = new ArrayList<Monitoring>();

        if(param != null && param.length > 0){
            for(String id: param){
                rtnList.add(this.getInfo("scanner", id));
            }
        }

        JsonUtil jsonUtil = new JsonUtil(rtnList);
        String json = jsonUtil.toJson();
        System.out.println(json);
        return json;
    }

    @RequestMapping(value="/monitoring/server/info_ajax.do", method=RequestMethod.POST)
    @ResponseBody
    public String getServerInfo( @RequestParam(value="data[]") String[] param) throws IOException {
        List<Monitoring> rtnList = new ArrayList<Monitoring>();

        if(param != null && param.length > 0){
            for(String id: param){
                rtnList.add(this.getInfo("server", id));
            }
        }

        JsonUtil jsonUtil = new JsonUtil(rtnList);
        String json = jsonUtil.toJson();
        return json;
    }


    private Monitoring getInfo(String div ,String id){
        boolean scannerYn = div.equals("scanner");
        String key = (scannerYn)? "SCANNER_"+id: "SERVER_"+id;

        Map<Object, Object> rtnMap = service.getRedisScanner(key);
        Monitoring rtnVo = new Monitoring();

        //if(rtnMap != null && !rtnMap.isEmpty()){
            rtnVo.setId(id);
            rtnVo.setIp(StringUtil.NVL((String)rtnMap.get("ip"), ""));
            rtnVo.setMacAddr(StringUtil.NVL((String)rtnMap.get("macAddr"), ""));
            rtnVo.setCpu(StringUtil.NVL((String)rtnMap.get("cpu"), "0"));
            rtnVo.setMem(StringUtil.NVL((String)rtnMap.get("mem"), "0"));

            //rtnVo.setLasttime(Long.parseLong(StringUtil.NVL((String)rtnMap.get("lasttime"), "0")));
            rtnVo.setLasttime(Long.parseLong(StringUtil.NVL((String)rtnMap.get("lasttime"), "0")));
            rtnVo.setUptime(Long.parseLong(StringUtil.NVL((String)rtnMap.get("uptime"), "0")));
            rtnVo.setDowntime(Long.parseLong(StringUtil.NVL((String)rtnMap.get("downtime"), "0")));

            if(scannerYn){
                rtnVo.setFwVer(StringUtil.NVL((String)rtnMap.get("fwVer"), ""));
            }else{
                rtnVo.setHostname(StringUtil.NVL((String)rtnMap.get("hostname"), ""));
                rtnVo.setName(StringUtil.NVL((String)rtnMap.get("name"), ""));
                rtnVo.setProc(StringUtil.NVL((String)rtnMap.get("proc"), "0"));
            }

            //cronjob.js var mon = cronjob function
            Map<String, Object> rtnStatus = this.setStatus(div, rtnVo);
            if(rtnStatus != null){
                rtnVo.setCpuStatus((String)rtnStatus.get("cpuStatus"));
                rtnVo.setMemStatus((String)rtnStatus.get("memStatus"));
                rtnVo.setPingStatus((String)rtnStatus.get("pingStatus"));

                if((Long)rtnStatus.get("downtime") != null){
                    rtnVo.setDowntime((Long)rtnStatus.get("downtime"));
                }

                if(!scannerYn){
                    rtnVo.setProcStatus((String)rtnStatus.get("procStatus"));
                }
            }

        //}
        return rtnVo;
    }

    private static int GLOBAL_SERVER_THRESHOLD_PROC_WARNING = 8;
    private static int GLOBAL_SERVER_THRESHOLD_PROC_CRITICAL = 1;
    private static int GLOBAL_SERVER_THRESHOLD_CPU_WARNING = 70;
    private static int GLOBAL_SERVER_THRESHOLD_CPU_CRITICAL = 80;
    private static int GLOBAL_SERVER_THRESHOLD_MEM_WARNING = 70;
    private static int GLOBAL_SERVER_THRESHOLD_MEM_CRITICAL = 80;
    private static int GLOBAL_SERVER_THRESHOLD_PING_WARNING = 120;
    private static int GLOBAL_SERVER_THRESHOLD_PING_CRITICAL = 180;

    private static int GLOBAL_SCANNER_THRESHOLD_CPU_WARNING = 70;
    private static int GLOBAL_SCANNER_THRESHOLD_CPU_CRITICAL = 80;
    private static int GLOBAL_SCANNER_THRESHOLD_MEM_WARNING = 70;
    private static int GLOBAL_SCANNER_THRESHOLD_MEM_CRITICAL = 80;
    private static int GLOBAL_SCANNER_THRESHOLD_PING_WARNING = 120;
    private static int GLOBAL_SCANNER_THRESHOLD_PING_CRITICAL = 180;


    private Map<String, Object> setStatus(String div, Monitoring vo){
        Map<String, Object> rtnMap = new HashMap<String, Object>();

        boolean scannerYn = div.equals("scanner");
        int warningLevel = 0;
        int criticlaLevel = 0;
        float chkVal = 0;

        if(scannerYn){
            warningLevel = GLOBAL_SCANNER_THRESHOLD_CPU_WARNING;
            criticlaLevel = GLOBAL_SCANNER_THRESHOLD_CPU_CRITICAL;
        }else{
            warningLevel = GLOBAL_SERVER_THRESHOLD_CPU_WARNING;
            criticlaLevel = GLOBAL_SERVER_THRESHOLD_CPU_CRITICAL;
        }

        chkVal = Float.parseFloat(vo.getCpu());
        if(warningLevel < chkVal && chkVal <= criticlaLevel) {
            rtnMap.put("cpuStatus", "W");
        } else if(criticlaLevel < chkVal) {
            rtnMap.put("cpuStatus", "C");
        } else {
            rtnMap.put("cpuStatus", "U");
        }

        if(scannerYn){
            warningLevel = GLOBAL_SCANNER_THRESHOLD_MEM_WARNING;
            criticlaLevel = GLOBAL_SCANNER_THRESHOLD_MEM_CRITICAL;
        }else{
            warningLevel = GLOBAL_SERVER_THRESHOLD_MEM_WARNING;
            criticlaLevel = GLOBAL_SERVER_THRESHOLD_MEM_CRITICAL;
        }

        chkVal = Float.parseFloat(vo.getMem());
        if(warningLevel < chkVal && chkVal <= criticlaLevel) {
            rtnMap.put("memStatus", "W");
        } else if(criticlaLevel < chkVal) {
            rtnMap.put("memStatus", "C");
        } else {
            rtnMap.put("memStatus", "U");
        }

        if(scannerYn){
            warningLevel = GLOBAL_SCANNER_THRESHOLD_PING_WARNING;
            criticlaLevel = GLOBAL_SCANNER_THRESHOLD_PING_CRITICAL;
        }else{
            warningLevel = GLOBAL_SERVER_THRESHOLD_PING_WARNING;
            criticlaLevel = GLOBAL_SERVER_THRESHOLD_PING_CRITICAL;
        }
        long now = DateUtil.str2timestamp(DateUtil.getDate("yyyyMMddHHmmss"));
        long lasttime = vo.getLasttime();
        long acttime = now - lasttime;
        if(warningLevel < acttime && acttime <= criticlaLevel) {
            rtnMap.put("pingStatus", "W");
        } else if(criticlaLevel < acttime) {
            rtnMap.put("pingStatus", "C");

            if((Long)vo.getDowntime() == 0){
                rtnMap.put("downtime", lasttime);
            }
            /*
            if(StringUtil.NVL(vo.getDowntime()).equals("")){
                vo.setDowntime(Long.toString(now));
            }*/
            rtnMap.put("cpuStatus", "C");
            rtnMap.put("memStatus", "C");
        }else{
            rtnMap.put("pingStatus", "U");
        }

        if(!scannerYn){
            warningLevel = GLOBAL_SERVER_THRESHOLD_PROC_WARNING;
            criticlaLevel = GLOBAL_SERVER_THRESHOLD_PROC_CRITICAL;

            float proc = Float.parseFloat(vo.getProc());
            if(warningLevel > proc && proc >= criticlaLevel) {
                rtnMap.put("procStatus", "W");
            } else if(criticlaLevel > proc) {
                rtnMap.put("procStatus", "C");
            } else {
                rtnMap.put("procStatus", "U");
            }
        }

        return rtnMap;
    }
}