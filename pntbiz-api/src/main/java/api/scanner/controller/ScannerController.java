package api.scanner.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import core.api.beacon.domain.Node;
import api.beacon.service.NodeService;
import core.api.common.domain.Company;
import api.common.service.CommonService;
import core.api.contents.domain.Contents;
import core.api.scanner.domain.Scanner;
import api.scanner.service.ScannerService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import framework.web.util.StringUtil;
 
@Controller
public class ScannerController {	
	
	@Autowired
	private ScannerService scannerService;
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
    private NodeService nodeService;

	private Logger logger = LoggerFactory.getLogger(getClass());	
	
	// 스캐너정보
	@RequestMapping(value="/scanner/info/{UUID}", method=RequestMethod.GET)
	@ResponseBody
	public Object scannerInfo(HttpServletRequest request, Company company, Scanner scanner,
			@PathVariable("UUID") String UUID) throws IOException, ServletException {


		commonService.checkAuthorized(UUID);

		Map<String, Object> res = new HashMap<String, Object>();		
		UUID = StringUtil.NVL(UUID, "");
		
		// 필수 값 체크
		if("".equals(UUID)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			company.setUUID(UUID);
			Company companyInfo = commonService.getCompanyInfoByUUID(company);			
			if(companyInfo == null) {
				res.put("result", "error");
				res.put("code", "303");
			} else {
				scanner.setComNum(companyInfo.getComNum());
				List<?> list = scannerService.getScannerList(scanner);
				res.put("result", "success");
				res.put("code", "0");
				res.put("data", list);				
				logger.info("scannerInfo list : {}", list);
			}
		}
		return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
	}
	
	@RequestMapping(value = "/scanner/node/{UUID}", method = RequestMethod.GET)
    @ResponseBody
    public Object scannerNode(@PathVariable(value = "UUID")String UUID) throws IOException, ParseException {

		commonService.checkAuthorized(UUID);

		Map<String, Object> res = new HashMap<String, Object>();
		UUID = StringUtil.NVL(UUID, "");
		if("".equals(UUID)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			List<?> nodeList = nodeService.getNodeList(UUID, null, "S");
			List<?> edgeList = nodeService.getNodeEdgeList(UUID, null, "S");
			List<?> contentsList = nodeService.getNodeContentsList(UUID, null, "S");
			
			HashMap<Long, ArrayList<Contents>> contentsMap = new HashMap<Long, ArrayList<Contents>>();
			for(Object obj: contentsList) {
				Contents con = (Contents)obj;
				
				if(!contentsMap.containsKey(con.getRefNum())) {
					contentsMap.put(con.getRefNum().longValue(), new ArrayList<Contents>());
				}
				contentsMap.get(con.getRefNum().longValue()).add(con);
			}
			
			for(Object obj: nodeList) {
				Node nod = (Node)obj;
				Long nodeNum = nod.getNodeNum();
				if(contentsMap.containsKey(nodeNum)) {
					nod.setContents(contentsMap.get(nodeNum));
				}
			}
			if(nodeList == null || nodeList.size() < 1) {
				res.put("result", "error");
				res.put("code", "303");
			} else {
				Map<String, List<?>> node = new HashMap<String, List<?>>();
				node.put("node", nodeList);
				node.put("edge", edgeList);
				res.put("result", "success");
				res.put("code", "0");
				res.put("data", node);
			}
		}
		return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
    }
	
	@RequestMapping(value = "/scanner/node/{UUID}/{floor}", method = RequestMethod.GET)
    @ResponseBody
    public Object scannerNode(@PathVariable(value = "UUID")String UUID, @PathVariable(value = "floor")String floor) throws IOException, ParseException {

		commonService.checkAuthorized(UUID);

		Map<String, Object> res = new HashMap<String, Object>();
		UUID = StringUtil.NVL(UUID, "");
		floor = StringUtil.NVL(floor, "");
        if("".equals(UUID) || "".equals(floor)) {
            res.put("result", "error");
            res.put("code", "200");
        } else {
			List<?> nodeList = nodeService.getNodeList(UUID, floor, "S");
			List<?> edgeList = nodeService.getNodeEdgeList(UUID, floor, "S");
			List<?> contentsList = nodeService.getNodeContentsList(UUID, floor, "S");
			
			HashMap<Long, ArrayList<Contents>> contentsMap = new HashMap<Long, ArrayList<Contents>>();
			for(Object obj: contentsList) {
				Contents con = (Contents)obj;
				
				if(!contentsMap.containsKey(con.getRefNum())) {
					contentsMap.put(con.getRefNum().longValue(), new ArrayList<Contents>());
				}
				contentsMap.get(con.getRefNum().longValue()).add(con);
			}
			
			for(Object obj: nodeList) {
				Node nod = (Node)obj;
				Long nodeNum = nod.getNodeNum();
				if(contentsMap.containsKey(nodeNum)) {
					nod.setContents(contentsMap.get(nodeNum));
				}
			}
			if(nodeList == null || nodeList.size() < 1) {
				res.put("result", "error");
				res.put("code", "303");
			} else {
				Map<String, List<?>> node = new HashMap<String, List<?>>();
				node.put("node", nodeList);
				node.put("edge", edgeList);
				res.put("result", "success");
				res.put("code", "0");
				res.put("data", node);
			}
		}
        return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
    }
	
	// 스캐너 테스트
	@RequestMapping(value="/scanner/test", method=RequestMethod.POST)
	@ResponseBody
	public void scannerTest(HttpServletRequest request) throws Exception {		
		System.out.println(request.getParameter("beacon_type"));
		System.out.println(request.getParameter("uuid"));
		System.out.println(request.getParameter("major"));
		System.out.println(request.getParameter("minor"));
		System.out.println(request.getParameter("power"));
		System.out.println(request.getParameter("rssi"));		
	}
	
}