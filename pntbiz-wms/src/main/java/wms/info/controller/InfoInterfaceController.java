package wms.info.controller;

import core.admin.auth.domain.Login;
import core.common.config.domain.InterfaceConfig;
import core.common.config.domain.InterfaceConfigSearchParam;
import core.common.enums.InterfaceBindingType;
import core.common.enums.InterfaceCommandType;
import framework.Security;
import framework.web.util.JsonUtil;
import framework.web.util.Pagination;
import org.omg.CosNaming.BindingType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import wms.component.auth.LoginDetail;
import wms.info.service.ConfigInterfaceService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sl.kim on 2017-09-01.
 */
@Controller
public class InfoInterfaceController {

    @Autowired
    private ConfigInterfaceService configInterfaceService;

    @RequestMapping(value = "/info/interface/list.do", method = RequestMethod.GET)
    public ModelAndView interfaceConfigList(InterfaceConfigSearchParam param) throws IOException {
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/info/interface/list");

        LoginDetail loginDetail = Security.getLoginDetail();
        param.setComNum(loginDetail.getCompanyNumber());
        param.setPageSize(15);

        Integer cnt = configInterfaceService.getInterfaceConfigCount(param);
        List<?> list = configInterfaceService.getInterfaceConfigList(param);

        Pagination pagination = new Pagination(param.getPage(), cnt, param.getPageSize(), param.getBlockSize());
        pagination.queryString = param.getQueryString();
        String page = pagination.print();

        mnv.addObject("cnt", cnt);
        mnv.addObject("param", param);
        mnv.addObject("list", list);
        mnv.addObject("pagination", page);
        mnv.addObject("page", param.getPage());

        return mnv;
    }

    @RequestMapping(value = "/info/interface/form.do", method = RequestMethod.GET)
    public ModelAndView interfaceConfigForm() throws IOException {
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/info/interface/form");
        return mnv;
    }

    @RequestMapping(value = "/info/interface/mform.do", method = RequestMethod.GET)
    public ModelAndView interfaceConfigMform(InterfaceConfig interfaceConfig) throws IOException {
        ModelAndView mnv = new ModelAndView();
        mnv.setViewName("/info/interface/mform");
        LoginDetail loginDetail = Security.getLoginDetail();
        interfaceConfig.setComNum(loginDetail.getCompanyNumber());
        interfaceConfig = configInterfaceService.getInterfaceConfigInfo(interfaceConfig);

        mnv.addObject("interfaceConfig", interfaceConfig);
        return mnv;
    }

    @RequestMapping(value = "/info/interface/zoneidlist.do", method = RequestMethod.POST)
    public List<?> zoneIdList(@RequestParam(value = "interfaceBindingType", required = true)InterfaceBindingType interfaceBindingType) throws IOException {

        LoginDetail loginDetail = Security.getLoginDetail();
        List<?> list;
        if(interfaceBindingType.equals(InterfaceBindingType.FLOOR))
            list = configInterfaceService.getFloorList(loginDetail.getCompanyNumber());
        else if(interfaceBindingType.equals(InterfaceBindingType.GEOFENCE_GROUP))
            list = configInterfaceService.getGeofenceGroupList(loginDetail.getCompanyNumber());
        else
            list = null;

        return list;
    }

    @RequestMapping(value = "/info/interface/reg.do", method = RequestMethod.POST)
    @ResponseBody
    public String interfaceConfigReg(InterfaceConfig interfaceConfig,
                                     @RequestParam(value = "protocol", required = false)String protocol,
                                     @RequestParam(value = "host", required = false)String host,
                                     @RequestParam(value = "port", required = false)Integer port,
                                     @RequestParam(value = "uri", required = false)String uri,
                                     @RequestParam(value = "method", required = false)String method,
                                     @RequestParam(value = "key", required = false)String[] key,
                                     @RequestParam(value = "value", required = false)String[] value) throws IOException {
        Map<String, String> info = new HashMap<String, String>();
        LoginDetail loginDetail = Security.getLoginDetail();


        interfaceConfig.setComNum(loginDetail.getCompanyNumber());

        Map<String, Object> targetInfoMap = new HashMap<>();
        targetInfoMap.put("protocol", protocol);
        targetInfoMap.put("host", host);
        targetInfoMap.put("port", port);
        targetInfoMap.put("uri", uri);
        if(method.equals(""))
            targetInfoMap.put("method", "POST");
        else
            targetInfoMap.put("method", method);
        JsonUtil targetInfoJson = new JsonUtil(targetInfoMap);
        interfaceConfig.setTargetInfo(targetInfoJson.toJson());

        Map<String, Object> headersMap = new HashMap<>();
        for(int i=0; i<key.length; i++) {
            if(!key[i].equals("") && !value[i].equals(""))
                headersMap.put(key[i], value[i]);
        }
        JsonUtil headersJson = new JsonUtil(headersMap);
        interfaceConfig.setHeaders(headersJson.toJson());

        Map<String, Object> bodyMetaDataMap = new HashMap<>();
        JsonUtil bodyMetaDataJson = new JsonUtil(bodyMetaDataMap);
        interfaceConfig.setBodyMetaData(bodyMetaDataJson.toJson());


        try {
            configInterfaceService.registerInterfaceConfig(interfaceConfig);
            info.put("result", "1");
        } catch (Exception e) {
            info.put("result", "2");
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value = "/info/interface/mod.do", method = RequestMethod.POST)
    @ResponseBody
    public String interfaceConfigMod(InterfaceConfig interfaceConfig,
                                     @RequestParam(value = "protocol", required = false)String protocol,
                                     @RequestParam(value = "host", required = false)String host,
                                     @RequestParam(value = "port", required = false)Integer port,
                                     @RequestParam(value = "uri", required = false)String uri,
                                     @RequestParam(value = "method", required = false)String method,
                                     @RequestParam(value = "key", required = false)String[] key,
                                     @RequestParam(value = "value", required = false)String[] value) throws IOException {

        Map<String, String> info = new HashMap<String, String>();
        LoginDetail loginDetail = Security.getLoginDetail();

        interfaceConfig.setComNum(loginDetail.getCompanyNumber());

        Map<String, Object> targetInfoMap = new HashMap<>();
        targetInfoMap.put("protocol", protocol);
        targetInfoMap.put("host", host);
        targetInfoMap.put("port", port);
        targetInfoMap.put("uri", uri);
        if(method.equals(""))
            targetInfoMap.put("method", "POST");
        else
            targetInfoMap.put("method", method);
        JsonUtil targetInfoJson = new JsonUtil(targetInfoMap);
        interfaceConfig.setTargetInfo(targetInfoJson.toJson());

        Map<String, Object> headersMap = new HashMap<>();
        for(int i=0; i<key.length; i++) {
            if(!key[i].equals("") && !value[i].equals(""))
                headersMap.put(key[i], value[i]);
        }
        JsonUtil headersJson = new JsonUtil(headersMap);
        interfaceConfig.setHeaders(headersJson.toJson());

        try {
            configInterfaceService.modifyInterfaceConfig(interfaceConfig);
            info.put("result", "1");
        } catch (Exception e) {
            info.put("result", "2");
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

    @RequestMapping(value = "/info/interface/selectzones.do", method = RequestMethod.POST)
    @ResponseBody
    public List<?> selectZones(@RequestParam(value = "interfaceBindingType", required = true)InterfaceBindingType interfaceBindingType) throws IOException {

        LoginDetail loginDetail = Security.getLoginDetail();

        List<?> list = null;
        if(interfaceBindingType.equals(InterfaceBindingType.FLOOR))
            list = configInterfaceService.getFloorList(loginDetail.getCompanyNumber());
        /*else if(interfaceBindingType.equals(InterfaceBindingType.GEOFENCE_COMMON))
            list = configInterfaceService.getGeofenceList(loginDetail.getCompanyNumber());*/
        else if(interfaceBindingType.equals(InterfaceBindingType.GEOFENCE_GROUP))
            list = configInterfaceService.getGeofenceGroupList(loginDetail.getCompanyNumber());

        return list;
    }

    @RequestMapping(value = "/info/interface/del.do", method = RequestMethod.POST)
    @ResponseBody
    public String interfaceConfigDel(@RequestParam(value = "interfaceNum", required = true)Long interfaceNum) throws IOException {
        Map<String, String> info = new HashMap<String, String>();

        try {
            configInterfaceService.removeInterfaceConfig(interfaceNum);
            info.put("result", "1");
        } catch (Exception e) {
            info.put("result", "2");
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        return jsonUtil.toJson();
    }

}
