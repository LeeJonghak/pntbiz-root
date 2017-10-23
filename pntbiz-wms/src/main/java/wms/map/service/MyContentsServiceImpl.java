package wms.map.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wms.component.auth.LoginDetail;
import framework.web.util.PagingParam;
import core.wms.info.dao.CodeActionDao;
import core.wms.info.domain.CodeActionMapping;
import core.wms.map.dao.MyContentsDao;
import core.wms.map.domain.ContentsMapping;

/**
 */
@Service
public class MyContentsServiceImpl implements MyContentsService {

    @Autowired
    private MyContentsDao contentsDao;

    @Autowired
    private CodeActionDao codeActionDao;

    /**
     * 지오펜스에 할당된 컨텐츠 목록
     *
     * @author nohsoo 2015-03-12
     * @param loginDetail
     * @param fcNum
     * @return
     */
    @Override
    public List<?> getMyGeofencingContentsList(LoginDetail loginDetail, Long fcNum) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("fcNum", fcNum);
        List<?> list = this.contentsDao.getMyGeofencingContentsList(param);
        return list;
    }

    /**
     * 비콘에 할당된 컨텐츠 목록
     *
     * @param loginDetail
     * @param beaconNum
     * @return
     */
    @Override
    public List<?> getMyBeaconContentsList(LoginDetail loginDetail, Long beaconNum) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("beaconNum", beaconNum);
        List<?> list = this.contentsDao.getMyBeaconContentsList(param);

        return list;

    }

    public List<?> getMyNodeContentsList(LoginDetail loginDetail, Long nodeNum) {

        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("nodeNum", nodeNum);
        List<?> list = this.contentsDao.getMyNodeContentsList(param);

        return list;

    }

    /**
     * 비콘에 다수의 컨텐츠 할당(맵핑)
     *
     * @param loginDetail
     * @param beaconNum
     * @param contentNum
     */
    @Override
    @Transactional
    public void assignMyBeaconContent(LoginDetail loginDetail, Long beaconNum, Long[] contentNum) {
        for(long conNum: contentNum) {
            ContentsMapping vo = new ContentsMapping();
            vo.setConNum(conNum);
            vo.setRefType("BC");
            vo.setRefSubType("DETECT");
            vo.setRefNum(beaconNum);
            contentsDao.insertContentMapping(vo);
        }
    }

    /**
     * 노드에 다수의 컨텐츠 할당(맵핑)
     * @param loginDetail
     * @param nodeNum
     * @param conNums
     */
    @Override
    public void assignMyNodeContent(LoginDetail loginDetail, Long nodeNum, Long[] conNums) {
        for(long conNum: conNums) {
            ContentsMapping vo = new ContentsMapping();
            vo.setConNum(conNum);
            vo.setRefType("ND");
            vo.setRefSubType("DETECT");
            vo.setRefNum(nodeNum);
            contentsDao.insertContentMapping(vo);
        }
    }

    /**
     * 비콘에 컨텐츠 할당해제
     * @param loginDetail
     * @param nodeNum
     * @param conNums
     */
    @Override
    public void unassignMyNodeContent(LoginDetail loginDetail, Long nodeNum, Long[] conNums) {
        for(long conNum: conNums) {
            ContentsMapping vo = new ContentsMapping();
            vo.setConNum(conNum);
            vo.setRefType("ND");
            vo.setRefNum(nodeNum);
            contentsDao.deleteContentMapping(vo);
        }
    }

    /**
     * 지오펜스에 다수의 컨텐츠 할당(맵핑)
     *
     * @param loginDetail
     * @param fenceEventType
     * @param fcNum
     * @param conNums
     * @throws Exception
     */
    @Override
    @Transactional
    public void assignMyGeofencingContent(LoginDetail loginDetail, String fenceEventType, Long fcNum, Long[] conNums) throws Exception {
        ArrayList<String> eventTypeValidValues = new ArrayList<String>();
        eventTypeValidValues.add("ENTER");
        eventTypeValidValues.add("STAY");
        eventTypeValidValues.add("LEAVE");

        if(!eventTypeValidValues.contains(fenceEventType)) {
            throw new Exception("Parameter Error:fence event type");
        }

        for(long conNum: conNums) {
            ContentsMapping vo = new ContentsMapping();
            vo.setConNum(conNum);
            vo.setRefType("GF");
            vo.setRefSubType(fenceEventType);
            vo.setRefNum(fcNum);
            contentsDao.insertContentMapping(vo);
        }
    }

    /**
     * 비콘에 컨텐츠 할당해제
     *
     * @param loginDetail
     * @param beaconNum
     * @param contentNum
     */
    @Override
    @Transactional
    public void unassignMyBeaconContent(LoginDetail loginDetail, Long beaconNum, Long[] contentNum) {

        for(long conNum: contentNum) {
            ContentsMapping vo = new ContentsMapping();
            vo.setConNum(conNum);
            vo.setRefType("BC");
            vo.setRefNum(beaconNum);
            contentsDao.deleteContentMapping(vo);
        }
    }

    /**
     * 지오펜스 컨텐츠 할당 해제
     *
     * @param loginDetail
     * @param fcNum
     * @param contentNum 컨텐츠 번호들.
     *                   지오펜스의 경우 맵핑정보의 refSubType 을 사용하므로 펜스 번호와 ENTER,LEAVE,STAY 정보를 함께 전달 받는다.
     *                   String 형태로 전달 받으며 구분자는 :: 을 사용한다.
     * @throws Exception
     */
    @Override
    @Transactional
    public void unassignMyGeofencingContent(LoginDetail loginDetail, Long fcNum, String[] contentNum) throws Exception {

        ArrayList<String> eventTypeValidValues = new ArrayList<String>();
        eventTypeValidValues.add("ENTER");
        eventTypeValidValues.add("STAY");
        eventTypeValidValues.add("LEAVE");

        for(String raw: contentNum) {
            String[] kv = raw.split("::");
            Long num = Long.parseLong(kv[0]);
            String type = kv[1];

            if(!eventTypeValidValues.contains(type)) {
                throw new Exception("Parameter Error:fence event type");
            }

            ContentsMapping vo = new ContentsMapping();
            vo.setConNum(num);
            vo.setRefType("GF");
            vo.setRefNum(fcNum);
            vo.setRefSubType(type);
            contentsDao.deleteContentMapping(vo);
        }
    }

    @Override
    public List<?> getCodeActionAll(LoginDetail loginDetail) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        return codeActionDao.getCodeActionAll(param);
    }

    @Override
    public List<?> getMyGeofencingCodeActionList(LoginDetail loginDetail, Long fcNum) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("fcNum", fcNum);
        List<?> list = codeActionDao.getMyGeofencingCodeActionList(param);
        return list;
    }

    @Override
    public List<?> getMyBeaconCodeActionList(LoginDetail loginDetail, long beaconNum) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("beaconNum", beaconNum);
        List<?> list = codeActionDao.getMyBeaconCodeActionList(param);
        return list;
    }


    @Override
    public void assignMyBeaconCodeAction(LoginDetail loginDetail, Long beaconNum, Integer[] codeNums) {
        for(int codeNum: codeNums) {
            CodeActionMapping vo = new CodeActionMapping();
            vo.setCodeNum(codeNum);
            vo.setRefType("BC");
            vo.setRefSubType("");
            vo.setRefNum(beaconNum);
            codeActionDao.insertCodeActionMapping(vo);
        }
    }

    @Override
    public void unassignMyBeaconCodeAction(LoginDetail loginDetail, Long beaconNum, Integer[] codeNums) {
        for(int codeNum:codeNums) {
            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("codeNum", codeNum);
            param.put("refType", "BC");
            param.put("refNum", beaconNum);
            codeActionDao.deleteCodeActionMapping(param);
        }
    }

    @Override
    public void assignMyGeofencingCodeAction(LoginDetail loginDetail, Long fcNum, String fenceEventType, Integer[] codeNums) throws Exception {
        ArrayList<String> eventTypeValidValues = new ArrayList<String>();
        eventTypeValidValues.add("ENTER");
        eventTypeValidValues.add("STAY");
        eventTypeValidValues.add("LEAVE");

        if(!eventTypeValidValues.contains(fenceEventType)) {
            throw new Exception("Parameter Error:fence event type");
        }

        for(int codeNum: codeNums) {
            CodeActionMapping vo = new CodeActionMapping();
            vo.setCodeNum(codeNum);
            vo.setRefType("GF");
            vo.setRefSubType(fenceEventType);
            vo.setRefNum(fcNum);
            codeActionDao.insertCodeActionMapping(vo);
        }
    }

    @Override
    public void unassignMyGeofencingCodeAction(LoginDetail loginDetail, Long fcNum, String[] codeNums) throws Exception {

        ArrayList<String> eventTypeValidValues = new ArrayList<String>();
        eventTypeValidValues.add("ENTER");
        eventTypeValidValues.add("STAY");
        eventTypeValidValues.add("LEAVE");

        for(String raw: codeNums) {
            String[] kv = raw.split("::");
            int num = Integer.parseInt(kv[0]);
            String type = kv[1];

            if(!eventTypeValidValues.contains(type)) {
                throw new Exception("Parameter Error:fence event type");
            }

            HashMap<String, Object> param = new HashMap<String, Object>();
            param.put("codeNum", num);
            param.put("refType", "GF");
            param.put("refNum", fcNum);
            param.put("refSubType", type);
            codeActionDao.deleteCodeActionMapping(param);
        }

    }

    @Override
    public List<?> getContentsList(LoginDetail loginDetail, Long refNum, String refType, PagingParam paging, String opt, String keyword) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());

        if(StringUtils.equals("GF", refType)) {
            param.put("fcNum", refNum);
        } else if(StringUtils.equals("BC", refType)) {
            param.put("beaconNum", refNum);
        }

        param.put("firstItemNo", paging.getFirstItemNo());
        param.put("pageSize", paging.getPageSize());
        param.put("page", paging.getPage());

        param.put("opt", opt);
        param.put("keyword", keyword+"%");
        List<?> list = contentsDao.getContentsList(param);
        return list;
    }



    @Override
    public Integer countContentsList(LoginDetail loginDetail, Long refNum, String refType, String opt, String keyword) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());

        if(StringUtils.equals("GF", refType)) {
            param.put("fcNum", refNum);
        } else if(StringUtils.equals("BC", refType)) {
            param.put("beaconNum", refNum);
        }

        param.put("opt", opt);
        param.put("keyword", keyword+"%");
        int cnt = contentsDao.countContentsList(param);

        return cnt;
    }

}
