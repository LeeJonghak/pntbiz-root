package wms.map.controller;

import wms.component.auth.LoginDetail;
import framework.web.util.JsonUtil;
import framework.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import core.wms.scanner.domain.Scanner;
import core.wms.scanner.domain.ScannerSearchParam;
import wms.scanner.service.ScannerService;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 스캐너 배치도
 * @author nohsoo 2015-04-22
 */
@Controller
public class SPlanMapController {

    @Autowired
    private ScannerService scannerService;

    /**
     * 지도 표시
     * create: nohsoo 2015-04-23
     * @return
     */
    @RequestMapping(value="/map/splan.do", method = RequestMethod.GET)
    public String splan() {
        return "/map/splan";
    }

    /**
     * 스캐너 목록
     * create: nohsoo 2015-04-23
     * @param floor
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/map/splan/scanner/list.do", method = RequestMethod.GET)
    @ResponseBody
    public String splanScannerList(@RequestParam(value = "floor", required = true)String floor) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();
            ScannerSearchParam param = new ScannerSearchParam();
            param.setComNum(loginDetail.getCompanyNumber());
            param.setFloor(floor);
            param.setPageSizeZero(); // 페이징 않함
            List<?> list = scannerService.getScannerList(param);

            info.put("result", "1");
            info.put("list", list);
        }
        catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;
    }

    @RequestMapping(value = "/map/splan/scanner/modify.do", method = RequestMethod.GET)
    @ResponseBody
    public String splanScannerModify(@RequestParam(value = "scannerNum", required = true)Long scannerNum,
                                     @RequestParam(value = "lat", required = true)Double lat,
                                     @RequestParam(value = "lng", required = true)Double lng) throws IOException {

        Map<String, Object> info = new HashMap<String, Object>();

        try {
            LoginDetail loginDetail = Security.getLoginDetail();

            Scanner param = new Scanner();
            param.setComNum(loginDetail.getCompanyNumber());
            param.setScannerNum(scannerNum.intValue());
            Scanner scannerInfo = scannerService.getScannerInfoByNum(param);
            if(scannerInfo!=null) {
                scannerInfo.setLat(lat);
                scannerInfo.setLng(lng);
                scannerService.modifyScanner(scannerInfo);
            }
            info.put("result", "1");
            info.put("scanner", scannerInfo);
        }
        catch(Exception e) {
            info.put("result", "2");
            info.put("message", e.getMessage());
        }

        JsonUtil jsonUtil = new JsonUtil(info);
        String json = jsonUtil.toJson();
        return json;

    }

    @RequestMapping(value = "/map/splan/scanner/info.ajax.do", method = RequestMethod.GET)
    public String splanScannerInfo(@RequestParam(value = "scannerNum", required = true)Long scannerNum,
                                   ModelMap map) {

        LoginDetail loginDetail = Security.getLoginDetail();
        Scanner param = new Scanner();
        param.setComNum(loginDetail.getCompanyNumber());
        param.setScannerNum(scannerNum.intValue());
        Scanner scannerInfo = scannerService.getScannerInfoByNum(param);
        map.addAttribute("scannerInfo", scannerInfo);

        return "map/ajax.scannerinfo";
    }

}
