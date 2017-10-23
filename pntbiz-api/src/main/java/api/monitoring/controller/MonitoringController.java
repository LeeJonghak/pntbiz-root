package api.monitoring.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import core.api.monitoring.domain.ScannerRedisParam;
import core.api.monitoring.domain.ServerRedisParam;
import api.monitoring.service.MonitoringService;
import framework.web.util.DateUtil;
import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;

@Controller
public class MonitoringController {

    private static int GLOBAL_SERVER_EXPTIME = 604800;
    private static int GLOBAL_SCANNER_EXPTIME = 604800;

    @Autowired
    private MonitoringService service;

    //pntbiz_api 로 이동
    //route_host.js > var send functoin
    @RequestMapping(value={"/host/send","/monitoring/scanner/send"} , method=RequestMethod.POST)
    @ResponseBody
    public Object sendScannerInfo(@RequestBody ScannerRedisParam vo) throws IOException{
        //return this.sendRedis("scanner", vo.getKey(), vo, GLOBAL_SCANNER_EXPTIME);

        Map<Object, Object> redisInfo = service.getRedisInfo(vo.getKey());

        long time = DateUtil.str2timestamp(DateUtil.getDate("yyyyMMddHHmmss"));
        vo.setLasttime(time);
        vo.setDowntime(0);
        vo.setMem(((Integer) ((Double) (Double.parseDouble(vo.getMem()) * 100)).intValue()).toString());
        vo.setCpu(((Integer) ((Double) (Double.parseDouble(vo.getCpu()) * 100)).intValue()).toString());

        if(redisInfo == null || redisInfo.isEmpty()){
            vo.setUptime(time);
        }else{
            long uptime = Long.parseLong(redisInfo.get("uptime").toString());
            long downtime = Long.parseLong(redisInfo.get("downtime").toString());

            vo.setUptime(uptime);
            if(uptime == 0)
                vo.setUptime(time);
            else if(uptime != 0 && downtime != 0){
                vo.setUptime(time);
            }
        }

        Map<String, Object> response = new HashMap<String, Object>();
        if(vo.getMacAddr() != null && !vo.getMacAddr().equals("")){
            service.setRedisInfo(vo.getKey(), vo);
            service.expireRedis(vo.getKey(), GLOBAL_SCANNER_EXPTIME);

            response.put("result", "success");
            response.put("code", "0");
            response.put("data", "");
        }else{
            response.put("result", "error");
        }

        return response;
    }

    //pntbiz_api 로 이동
    //route_host.js > var send functoin
    @RequestMapping(value="/monitoring/server/send", method=RequestMethod.POST)
    @ResponseBody
    public String sendServerInfo(@RequestBody ServerRedisParam vo) throws IOException{
        //return this.sendRedis("server", vo.getKey(), vo, GLOBAL_SERVER_EXPTIME);
        Map<String, Object> info = new HashMap<String, Object>();
        Map<Object, Object> rtnObj = service.getRedisInfo(vo.getKey());

        long time = DateUtil.str2timestamp(DateUtil.getDate("yyyyMMddHHmmss"));
        vo.setLasttime(time);
        vo.setDowntime(0);

        if(rtnObj == null || rtnObj.isEmpty()){
            // sendmail
            vo.setUptime(time);
        }else{
            String uptime = StringUtil.NVL((String)rtnObj.get("uptime"));
            String downtime = StringUtil.NVL((String)rtnObj.get("downtime"));

            if(uptime.equals(""))
                vo.setUptime(time);
            else if(!uptime.equals("") && !downtime.equals("")){
                vo.setUptime(time);
            }
        }

        if(!StringUtil.NVL(vo.getHostname()).equals("")){
            service.setRedisInfo(vo.getKey(), vo);
            service.expireRedis(vo.getKey(), GLOBAL_SERVER_EXPTIME);

            info.put("result", "success");
            info.put("code", "0");
            info.put("data", "");
        }else{
            info.put("result", "error");
        }
        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();

        return json;
    }
}