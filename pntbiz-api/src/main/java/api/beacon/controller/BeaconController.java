package api.beacon.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import api.map.service.FloorCodeService;
import core.api.beacon.domain.Beacon;
import core.api.beacon.domain.Node;
import api.beacon.service.ChaosAreaService;
import api.beacon.service.NodeService;
import core.api.common.domain.Company;
import core.api.common.domain.Sync;
import api.common.service.CommonService;
import core.api.contents.domain.Contents;
import core.api.geofencing.domain.CodeAction;

import core.api.map.domain.FloorCode;
import framework.exception.BaseException;
import framework.util.ClassUtil;
import framework.util.JsonUtils;
import org.codehaus.jackson.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import framework.web.util.JsonUtil;
import framework.web.util.StringUtil;
import core.api.beacon.domain.BeaconContents;
import core.api.beacon.domain.BeaconState;
import api.beacon.service.BeaconService;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class BeaconController {	
	
	@Autowired
	private BeaconService beaconService;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private ChaosAreaService chaosAreaService;

    @Autowired
    private CommonService commonService;

    @Autowired
    private FloorCodeService floorCodeService;

	private Logger logger = LoggerFactory.getLogger(getClass());	
	
	/**
     * SSL 테스트 비콘 조회
     *
     * @param UUID
     * @return
     * @throws IOException
     * @throws ParseException 
     */
    @RequestMapping(value = "/beacon/ssl/{UUID}", method = RequestMethod.GET)
    @ResponseBody
    public Object beaconSSL(@PathVariable(value = "UUID")String UUID) throws IOException, ParseException {

        Map<String, Object> res = getBeaconList(UUID, null, null, null);
        return res;
//        JsonUtil jsonUtil = new JsonUtil(res);
//        String json = jsonUtil.toJson();
//        return json;
    }
	
	// 비콘배터리 정보입력
	@RequestMapping(value="/beacon/battery/{SUUID}", method=RequestMethod.POST)
	@ResponseBody
	public Object testPresence(HttpServletRequest request, BeaconState beaconState,
			@PathVariable(value = "SUUID")String SUUID
			) throws IOException, ServletException {		
		Map<String, Object> res = new HashMap<String, Object>();
		
		String str = JsonUtil.getJson(request);
		JsonNode node = JsonUtil.toNode(str);
		JsonNode itemNode = null;
		
		String UUID = "";
		Integer majorVer = 10;
		Integer minorVer = 10;
		Integer battery = 10;
		
		int totalCnt = node.size(); // 총 갯수
		int errorCnt = 0; // 에러수
		int successCnt = 0; // 성공수
		
		if("".equals(SUUID) || totalCnt < 1) {
			res.put("result", "error");
			res.put("code", "200");
		} else {			
			for(int i=0; i<totalCnt; i++) {
				
				itemNode = node.get(i);	
				UUID = itemNode.path("UUID").getTextValue();
				majorVer = itemNode.path("majorVer").getIntValue();
				minorVer = itemNode.path("minorVer").getIntValue();
				battery = itemNode.path("battery").getIntValue();
				// 값 검증 후 에러 데이터 입력
				if("".equals(UUID)) {	
					errorCnt++;
				} else {
					try {
						if(battery > 0) { 
							beaconState.setSUUID(SUUID);
							beaconState.setUUID(UUID);
							beaconState.setMajorVer(majorVer);
							beaconState.setMinorVer(minorVer);
							beaconState.setBattery(battery);
							beaconService.registerBeaconState(beaconState);
						}
						logger.info("beaconState : ", beaconState);
						successCnt++;
					} catch(DataAccessException dae) {
						errorCnt++;
					} finally {
					}	
				}
			}
		}
		
		Map<String, Integer> cnt = new HashMap<String, Integer>();
		cnt.put("totalCnt", totalCnt);
		cnt.put("errorCnt", errorCnt);
		cnt.put("successCnt", successCnt);
		res.put("data", cnt);
		
		if(errorCnt < 1) {
			res.put("result", "success");
			res.put("code", "0");
		} else {
			res.put("result", "error");
			res.put("code", "300");
		}
		return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
	}
	
	/**
     * 비콘 조회
     *
     * @param UUID
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/beacon/list/{UUID}", method = RequestMethod.GET)
    @ResponseBody
    public Object beaconList(@PathVariable(value = "UUID")String UUID) throws IOException, ParseException {
        Map<String, Object> res = new HashMap<String, Object>();

        commonService.checkAuthorized(UUID);
        
        if("".equals(UUID)) throw new BaseException("200","");

        List<?> list = beaconService.getBeaconList(UUID);
        res.put("result", "success");
        res.put("code", "0");
        res.put("data", list);

        return res;
    }

    /**
     * 비콘 목록 조회
     */
    @RequestMapping(value = "/beacon/list/comNum/{comNum}", method = RequestMethod.GET)
    @ResponseBody
    public Object beaconList(@PathVariable(value = "comNum")Integer comNum) throws IOException, ParseException {
        Map<String, Object> res = new HashMap<String, Object>();

        Company company = new Company();
        company.setComNum(comNum);
        company = commonService.getCompanyInfo(company);

        if(company == null) throw  new BaseException("300",null);

        commonService.checkAuthorized(company.getUUID());
        List<?> list = beaconService.getBeaconList(company.getUUID());

        res.put("result", "success");
        res.put("code", "0");
        res.put("data", list);
        return res;
    }
	
	/**
     * 비콘 조회
     *
     * @param UUID
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/beacon/info/{UUID}", method = RequestMethod.GET)
    @ResponseBody
    public Object beaconInfo(@PathVariable(value = "UUID")String UUID) throws IOException, ParseException {
        Map<String, Object> res = new HashMap<String, Object>();

        commonService.checkAuthorized(UUID);

        List<?> list = beaconService.getBeaconList(UUID, null, null, null);
        this.bindExtendsData(UUID, null, null, null, list);

        res.put("result", "success");
        res.put("code", "0");
        res.put("data", list);
        return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
    }

    /**
     * 비콘 조회
     *
     * @param UUID
     * @param majorVer
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/beacon/info/{UUID}/{major}", method = RequestMethod.GET)
    @ResponseBody
    public Object beaconInfo(@PathVariable(value = "UUID")String UUID,
                             @PathVariable(value = "major")Integer majorVer) throws IOException, ParseException {

        Map<String, Object> res = getBeaconList(UUID, majorVer, null, null);
        return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
    }

    /**
     * 비콘 조회
     *
     * @param UUID
     * @param majorVer
     * @param minorVer
     * @return
     * @throws IOException
     * @throws ParseException 
     */
    @RequestMapping(value = "/beacon/info/{UUID}/{major}/{minor}", method = RequestMethod.GET)
    @ResponseBody
    public Object beaconInfo(@PathVariable(value = "UUID")String UUID,
                             @PathVariable(value = "major")Integer majorVer,
                             @PathVariable(value = "minor")Integer minorVer) throws IOException, ParseException {

        Map<String, Object> res = getBeaconList(UUID, majorVer, minorVer, null);
        return res;
    }

    /**
     * 비콘 조회 by Mac Address
     *
     * @param macAddr
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/beacon/info/macAddr/{macAddr}", method = RequestMethod.GET)
    @ResponseBody
    public Object beaconInfoByMacAddr(@PathVariable(value = "macAddr")String macAddr) throws IOException, ParseException {
        Beacon beacon = beaconService.getBeaconInfoByMacAddr(macAddr);
        if (beacon == null) throw new BaseException("303",null);

        Map<String, Object> res = new HashMap<String, Object>();

        res.put("result", "success");
        res.put("code", "0");
        res.put("data", JsonUtils.convertValue(beacon, Map.class));

        return res;
    }

    /**
     * 비콘 콘텐츠
     * @param UUID
     * @param conType
     * @return
     * @throws IOException
     * @throws ParseException
     */
    /*@RequestMapping(value = "/beacon/contents/{UUID}/{conType}", method = RequestMethod.GET)
    @ResponseBody
    public Object beaconFloorListAll(@PathVariable(value = "UUID")String UUID,
                                  @PathVariable(value = "conType")String conType
    ) throws IOException, ParseException {

        commonService.checkAuthorized(UUID);

        Map<String, Object> res = new HashMap<String, Object>();
        List<BeaconContents> list = new ArrayList<BeaconContents>();

        if(UUID.equals("")) {
            res.put("result", "error");
            res.put("code", "200");
        } else {
           list = beaconService.getBeaconFloorCodeList(UUID, conType, null);
            this.bindExtendsData(UUID, null, null, conType, list);
            res.put("result", "success");
            res.put("code", "0");
            res.put("data", list);
        }
        return res;
    }*/

    /**
     * 비콘 콘텐츠
     * @param request
     * @param UUID
     * @param conType
     * @return
     * @throws IOException
     * @throws ParseException
     */
//    @RequestMapping(value = "/beacon/contents/{UUID}/{conType}/**", method = RequestMethod.GET)
//    @ResponseBody
//    public Object beaconFloorList(HttpServletRequest request,
//                                  @PathVariable(value = "UUID")String UUID,
//                                  @PathVariable(value = "conType")String conType
//    ) throws IOException, ParseException {
//
//        commonService.checkAuthorized(UUID);
//
//        Map<String, Object> res = new HashMap<String, Object>();
//        List<BeaconContents> list = new ArrayList<BeaconContents>();
//        List<FloorCode> floorCodeDepthList = new ArrayList<FloorCode>();
//        List<FloorCode> floorCodeChildNodeList =  new ArrayList<FloorCode>();
//        List<FloorCode> floorCodeLastChildNodeList =  new ArrayList<FloorCode>();
//
//        String path = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
//        int pathSize = 5; // /beacon/contents/uuid/contype
//        String[] param = path.split("/");
//        int paramSize = param.length;
//        int level = 0;
//        boolean isLastNode = true;
//
//        if(UUID.equals("") || paramSize <= pathSize) {
//            res.put("result", "error");
//            res.put("code", "200");
//        } else {
//            for(int i=pathSize; i<paramSize; i++) {
//                FloorCode floorCode = new FloorCode();
//                floorCode.setNodeId(param[i]);
//                floorCode.setLevelNo(level);
//                floorCodeDepthList.add(floorCode);
//                level++;
//            }
//            if(paramSize > pathSize) {
//                FloorCode floorCode = new FloorCode();
//                floorCode.setUUID(UUID);
//                floorCodeChildNodeList = floorCodeService.getFloorCodeChildNodeList(floorCode, floorCodeDepthList);
//                floorCodeLastChildNodeList = floorCodeService.getFloorCodeLastChildNodeList(floorCodeChildNodeList);
//                if(floorCodeLastChildNodeList.size() == 0) isLastNode = false;
//            }
//            if(isLastNode == true) {
//                list = beaconService.getBeaconFloorCodeList(UUID, conType, floorCodeLastChildNodeList);
//                // LGD에서 임의로 floorcode > floor 로 변경하여 내려주도록 처리
////                list = beaconService.getBeaconFloorCodeListByField(UUID, conType, floorCodeLastChildNodeList);
//            }
//            this.bindExtendsData(UUID, null, null, conType, list);
//
//            res.put("result", "success");
//            res.put("code", "0");
//            res.put("data", list);
//        }
//        return res;
//    }

    /**
     * 컨텐츠(conType)이 일치하는 목록만 표시
     *
     * @author nohsoo 2015-03-10
     * @param conType
     * @param UUID
     * @return
     * @throws IOException
     * @throws ParseException 
     */
    @RequestMapping(value = "/beacon/contents/{conType}/{UUID}", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    public Object beaconInfo(@PathVariable(value = "conType")String conType,
                             @PathVariable(value = "UUID")String UUID) throws IOException, ParseException {

        Map<String, Object> res = getBeaconList(UUID, null, null, conType);
        return res;
    }

    /**
     * 컨텐츠(conType)이 일치하는 목록만 표시
     *
     * @author nohsoo 2015-03-10
     * @param conType
     * @param UUID 비콘 UUID
     * @param majorVer 비콘 Major Version
     * @return
     * @throws IOException
     * @throws ParseException 
     */
    @RequestMapping(value = "/beacon/contents/{conType}/{UUID}/{major}", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    public Object beaconInfo(@PathVariable(value = "conType")String conType,
                             @PathVariable(value = "UUID")String UUID,
                             @PathVariable(value = "major")Integer majorVer) throws IOException, ParseException {

        Map<String, Object> res = getBeaconList(UUID, majorVer, null, conType);
        return res;
    }

    /**
     * 컨텐츠(conType)이 일치하는 목록만 표시
     *
     * @author nohsoo 2015-03-10
     * @param conType
     * @param UUID 비콘 UUID
     * @param majorVer 비콘 Major Version
     * @param minorVer 비콘 Minor Version
     * @return
     * @throws IOException
     * @throws ParseException 
     */
    @RequestMapping(value = "/beacon/contents/{conType}/{UUID}/{major}/{minor}", method = RequestMethod.GET)
    @ResponseBody
    @Deprecated
    public Object beaconInfo(@PathVariable(value = "conType")String conType,
                             @PathVariable(value = "UUID")String UUID,
                             @PathVariable(value = "major")Integer majorVer,
                             @PathVariable(value = "minor")Integer minorVer) throws IOException, ParseException {

        Map<String, Object> res = getBeaconList(UUID, majorVer, minorVer, conType);
        return res;
    }

    private Map<String, Object> getBeaconList(String UUID, Integer majorVer, Integer minorVer, String conType) throws ParseException {
        commonService.checkAuthorized(UUID);

        Map<String, Object> res = new HashMap<String, Object>();

        List<?> list = beaconService.getBeaconList(UUID, majorVer, minorVer, conType);
        this.bindExtendsData(UUID, majorVer, minorVer, conType, list);

        res.put("result", "success");
        res.put("code", "0");
        res.put("data", list);
        return res;
    }

    /**
     * 비콘 목록에 컨텐츠, 액션코드 데이터 연결
     *
     * @param UUID
     * @param major
     * @param minor
     * @param list
     * @throws ParseException 
     */
    @SuppressWarnings("unchecked")
	private void bindExtendsData(String UUID, Integer major, Integer minor, String conType, List<?> list) throws ParseException {
        /**
         * 지오펜스 컨텐츠 목록 조회
         */
        List<?> contentsList = beaconService.getBeaconContentsList(UUID, major, minor, conType);
        HashMap<String, ArrayList<Contents>> contentMap = new HashMap<String, ArrayList<Contents>>();
        for(Object item: contentsList) {
            Contents contents = (Contents)item;
            Integer beaconNum = contents.getRefNum();
            if(!contentMap.containsKey(String.valueOf(beaconNum))) {
                contentMap.put(String.valueOf(beaconNum), new ArrayList<Contents>());
            }
            contentMap.get(String.valueOf(beaconNum)).add(contents);
        }


        /**
         * 액션코드, 목록 조회
         */
        List<?> actionList = beaconService.getBeaconActionList(UUID, major, minor, conType);
        HashMap<String, ArrayList<CodeAction>> actionMap = new HashMap<String, ArrayList<CodeAction>>();
        for(Object item: actionList) {
            CodeAction codeAction = (CodeAction)item;
            Integer beaconNum = codeAction.getRefNum();
            if(!actionMap.containsKey(String.valueOf(beaconNum))) {
                actionMap.put(String.valueOf(beaconNum), new ArrayList<CodeAction>());
            }
            actionMap.get(String.valueOf(beaconNum)).add(codeAction);
        }



        for(Object obj: list) {
            BeaconContents beacon = (BeaconContents)obj;

            if(contentMap.containsKey(String.valueOf(beacon.getBeaconNum()))) {
                @SuppressWarnings("rawtypes")
				ArrayList contents = contentMap.get(String.valueOf(beacon.getBeaconNum()));
                beacon.setContents(contents);
            }

            if(actionMap.containsKey(String.valueOf(beacon.getBeaconNum()))) {
                @SuppressWarnings("rawtypes")
				ArrayList tasks = actionMap.get(String.valueOf(beacon.getBeaconNum()));
                beacon.setCodeActions(tasks);
            }
        }

    }


    /**
     * 노드 목록
     *
     * @author nohsoo 2015-03-10
     * @param UUID 업체 UUID
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/beacon/node/{UUID}", method = RequestMethod.GET)
    @ResponseBody
    public Object nodeList(@PathVariable(value = "UUID")String UUID) throws IOException, ParseException {

        commonService.checkAuthorized(UUID);

        Map<String, Object> res = new HashMap<String, Object>();
        UUID = StringUtil.NVL(UUID, "");        
        if("".equals(UUID)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			List<?> nodeList = nodeService.getNodeList(UUID, null, "B");
	        List<?> edgeList = nodeService.getNodeEdgeList(UUID, null, "B");
            List<?> contentsList = nodeService.getNodeContentsList(UUID, null, "B");

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

    /**
     * 노드 목록
     *
     * @author nohsoo 2015-03-10
     * @param UUID  업체 UUID
     * @param floor 층번호
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/beacon/node/{UUID}/{floor}", method = RequestMethod.GET)
    @ResponseBody
    public Object nodeList(@PathVariable(value = "UUID")String UUID,
                           @PathVariable(value = "floor")String floor) throws IOException, ParseException {

        commonService.checkAuthorized(UUID);

    	Map<String, Object> res = new HashMap<String, Object>();
    	UUID = StringUtil.NVL(UUID, "");        
    	floor = StringUtil.NVL(floor, "");        
        if("".equals(UUID) || "".equals(floor)) {
			res.put("result", "error");
			res.put("code", "200");
		} else {
			List<?> nodeList = nodeService.getNodeList(UUID, floor, "B");
	        List<?> edgeList = nodeService.getNodeEdgeList(UUID, floor, "B");
            List<?> contentsList = nodeService.getNodeContentsList(UUID, floor, "B");

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

    /**
     * 노드 목록 - 타입(스캐너,비콘) 지정
     *
     * @author nohsoo 2015-06-01
     * @param UUID  업체 UUID
     * @param floor 층번호
     * @param type 타입(스캐너,비콘)
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/beacon/node/{UUID}/{floor}/{type}", method = RequestMethod.GET)
    @ResponseBody
    public Object nodeList(@PathVariable(value = "UUID")String UUID,
                           @PathVariable(value = "floor")String floor,
                           @PathVariable(value = "type")String type) throws IOException, ParseException {

        commonService.checkAuthorized(UUID);

        Map<String, Object> res = new HashMap<String, Object>();
        UUID = StringUtil.NVL(UUID, "");
        floor = StringUtil.NVL(floor, "");
        if("".equals(UUID) || "".equals(floor)) {
            res.put("result", "error");
            res.put("code", "200");
        } else {
            List<?> nodeList = nodeService.getNodeList(UUID, floor, type);
            List<?> edgeList = nodeService.getNodeEdgeList(UUID, floor, type);
            List<?> contentsList = nodeService.getNodeContentsList(UUID, floor);

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
    
    @RequestMapping(value = "/beacon/node/{UUID}", method = RequestMethod.DELETE)
    @ResponseBody
    public Object nodeDelete(HttpServletRequest request,
    		@PathVariable(value = "UUID")String UUID,
    		Company company, Sync sync) throws IOException, ParseException {

        commonService.checkAuthorized(UUID);

        Map<String, Object> res = new HashMap<String, Object>();
        UUID = StringUtil.NVL(UUID, "");
        
        String str = JsonUtil.getJson(request);
		JsonNode node = JsonUtil.toNode(str);
		
		company.setUUID(UUID);
    	Company companyInfo = commonService.getCompanyInfoByUUID(company);
		
		Iterator<JsonNode> list = node.path("nodeNum").getElements();
		int cnt = 0;
		
		if("".equals(UUID)) {
            res.put("result", "error");
            res.put("code", "200");
        } else {        	
        	while (list.hasNext()) {
    			JsonNode nodeNumJson = list.next();
    			Long nodeNum = nodeNumJson.getLongValue();    			
    			Node param = new Node();
    			param.setNodeNum(nodeNum);
            	Node nodeInfo = nodeService.getNode(param);            	
            	try {
    	        	nodeService.deleteNode(UUID, nodeNum);
    	        	nodeService.deleteNodePair(UUID, nodeInfo.getNodeID());	    
    	        	cnt++;
            	} catch(Exception e) {            		
            	}
    		}        	
        	if(cnt > 0) {
        		sync.setComNum(companyInfo.getComNum());
            	sync.setSyncType("NODE");
            	commonService.updateSync(sync);
            	res.put("result", "success");
                res.put("code", "0");
        	} else {
        		res.put("result", "error");
                res.put("code", "302");
        	}
        }
        return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
    }

    /**
     * 혼돈영역 전체 목록
     * create: nohsoo 2015-04-23
     *
     * @param UUID 업체UUID
     * @return호
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/beacon/chaosarea/{UUID}", method = RequestMethod.GET)
    @ResponseBody
    public Object chaosAreaList(@PathVariable(value = "UUID")String UUID) throws IOException, ParseException {

        commonService.checkAuthorized(UUID);

        Map<String, Object> res = new HashMap<String, Object>();
        UUID = StringUtil.NVL(UUID, "");
        if ("".equals(UUID)) {
            res.put("result", "error");
            res.put("code", "200");
        } else {
            List<?> list = chaosAreaService.getChaosAreaListAll(UUID);
            res.put("result", "success");
            res.put("code", "0");
            res.put("data", list);
        }
        return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
    }

    /**
     * 혼돈영역 전체 목록
     * create: nohsoo 2015-04-23
     *
     * @param UUID 업체UUID
     * @param floor 층번
     * @return
     * @throws IOException
     * @throws ParseException
     */
    @RequestMapping(value = "/beacon/chaosarea/{UUID}/{floor}", method = RequestMethod.GET)
    @ResponseBody
    public Object chaosAreaList(@PathVariable(value = "UUID")String UUID,
                                @PathVariable(value = "floor")String floor) throws IOException, ParseException {

        commonService.checkAuthorized(UUID);

        Map<String, Object> res = new HashMap<String, Object>();
        UUID = StringUtil.NVL(UUID, "");
        floor = StringUtil.NVL(floor, "");
        if ("".equals(UUID) || "".equals(floor)) {
            res.put("result", "error");
            res.put("code", "200");
        } else {
            List<?> list = chaosAreaService.getChaosAreaListAll(UUID, floor);
            res.put("result", "success");
            res.put("code", "0");
            res.put("data", list);
        }
        return res;
//      JsonUtil jsonUtil = new JsonUtil(res);
//      String json = jsonUtil.toJson();
//      return json;
    }
    
}