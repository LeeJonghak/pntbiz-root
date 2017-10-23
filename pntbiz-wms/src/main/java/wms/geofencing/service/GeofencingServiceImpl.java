package wms.geofencing.service;

import core.wms.admin.company.dao.CompanyDao;
import core.wms.admin.company.domain.Company;
import framework.Security;
import core.wms.geofencing.dao.GeofencingDao;
import core.wms.geofencing.dao.GeofencingLatlngDao;
import core.wms.geofencing.dao.GeofencingRedisDao;
import core.common.geofencing.domain.Geofencing;
import core.wms.geofencing.domain.GeofencingGroup;
import core.wms.geofencing.domain.GeofencingGroupMapping;
import core.wms.geofencing.domain.GeofencingGroupSearchParam;
import core.wms.geofencing.domain.GeofencingLatlng;
import wms.component.auth.LoginDetail;
import core.wms.info.dao.CodeActionDao;
import core.wms.info.domain.CodeActionMapping;
import core.wms.map.dao.MyContentsDao;
import core.wms.map.domain.ContentsMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 펜스 관리 서비스
 *
 * create 2015-02-27
 */
@Service
public class GeofencingServiceImpl implements GeofencingService {

    @Autowired
    private CompanyDao companyDao;

    @Autowired
    private GeofencingDao geofencingDao;

    @Autowired
    private GeofencingRedisDao geofencingRedisDao;

    @Autowired
    private GeofencingLatlngDao geofencingLatlngDao;

    @Autowired
    private CodeActionDao codeActionDao;

    @Autowired
    private MyContentsDao myContentsDao;

//    @PostConstruct
    public void setGeofenceRedisData() {
        // 지오펜스 레디스 데이터 초기화
        Set<?> listSet = geofencingRedisDao.keys(geofencingRedisDao.GEOFENCE_LIST_PREFIX+"*");
        Set<?> infoSet = geofencingRedisDao.keys(geofencingRedisDao.GEOFENCE_INFO_PREFIX+"*");
        Set<?> latlngSet = geofencingRedisDao.keys(geofencingRedisDao.GEOFENCE_LATLNG_PREFIX+"*");
        for(Object data : listSet) {
            String key = (String) data;
            geofencingRedisDao.del(key);
        }
        for(Object data : infoSet) {
            String key = (String) data;
            geofencingRedisDao.del(key);
        }
        for(Object data : latlngSet) {
            String key = (String) data;
            geofencingRedisDao.del(key);
        }
        // 지오펜스 레디스 데이터 셋팅
        List<Company> companyList = companyDao.getCompanyListAll();
        for(Company company : companyList) {
            syncGeofencingRedisData(company);
        }
    }

    @Override
    public void syncGeofencingRedisData(Company company) throws DataAccessException {
        Integer comNum = company.getComNum();
        String uuid = company.getUUID();
        Long fcNum = null;
        String strFcNum = "";
        // 데이터 삭제
        Set<?> listSet = geofencingRedisDao.getGeofenceList(uuid);
        for(Object data : listSet) {
            String key = (String) data;
            geofencingRedisDao.del(geofencingRedisDao.GEOFENCE_LATLNG_PREFIX+key);
            geofencingRedisDao.del(geofencingRedisDao.GEOFENCE_INFO_PREFIX+key);
        }
        geofencingRedisDao.del(geofencingRedisDao.GEOFENCE_LIST_PREFIX+uuid);
        // 데이터 셋팅
        List<?> geofencingList = getGeofencingAll(comNum);
        for(Object gf : geofencingList) {
            Geofencing geofencing = (Geofencing) gf;
            fcNum = geofencing.getFcNum();
            strFcNum = fcNum.toString();
            geofencingRedisDao.setGeofenceList(uuid, strFcNum);
            geofencingRedisDao.setGeofenceInfo(strFcNum, geofencing);
            List<?> latlngs = getGeofencingLatlngList(fcNum);
            for(Object latlng : latlngs) {
                latlng = (GeofencingLatlng) latlng;
                Double lat = ((GeofencingLatlng) latlng).getLat();
                Double lng = ((GeofencingLatlng) latlng).getLng();
                geofencingRedisDao.setGeofenceLatlng(strFcNum, lat, lng);
            }
        }
    }

    @Override
    public List<?> getGeofencingAll(LoginDetail loginDetail, String floor) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("floor", floor);
        List<?> list = this.geofencingDao.getGeofencingAll(param);
        return list;
    }
    @Override
    public List<?> getGeofencingAll(Integer comNum) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", comNum);
        List<?> list = this.geofencingDao.getGeofencingAll(param);
        return list;
    }

    @Override
    public List<?> getGeofencingLatlngList(Long fcNum) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("fcNum", fcNum);
        List<?> list = this.geofencingLatlngDao.getGeofencingLatlngList(param);
        return list;
    }

    @Override
    public List<?> getGeofencingList(GeofencingGroupSearchParam param) {
        return this.geofencingDao.getGeofencingList(param);
    }

    @Override
    public Integer getGeofencingCount(GeofencingGroupSearchParam param) {
        return this.geofencingDao.getGeofencingCount(param);
    }

    @Override
    public Geofencing getGeofencing(Long fcNum) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("fcNum", fcNum);
        Geofencing geofencing = this.geofencingDao.getGeofencing(param);
        return geofencing;
    }

    @Override
    @Transactional
    public void registerGeofencing(LoginDetail loginDetail, Geofencing geofencing, ArrayList<GeofencingLatlng> latlngs) {
        geofencing.setUserID(loginDetail.getUsername());
        geofencing.setComNum(loginDetail.getCompanyNumber());
        long fcNum = this.geofencingDao.insertGeofencing(geofencing);
        geofencing.setFcNum(fcNum);
        for(GeofencingLatlng latlng: latlngs) {
            latlng.setFcNum(fcNum);
            this.geofencingLatlngDao.insertGeofencingLatlng(latlng);
        }
        setGeofence(loginDetail, geofencing, latlngs, fcNum);

    }

    private void setGeofence(LoginDetail loginDetail, Geofencing geofencing, ArrayList<GeofencingLatlng> latlngs, long fcNum) {
        try {
            String uuid = loginDetail.getUuid();
            String strFcNum = Long.toString(fcNum);
            geofencingRedisDao.setGeofenceList(uuid, strFcNum);
            geofencingRedisDao.setGeofenceInfo(strFcNum, geofencing);
            geofencingRedisDao.setGeofenceLatlng(strFcNum, latlngs);
        } catch(Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public Long registerGeofencingReturnNum(LoginDetail loginDetail, Geofencing geofencing, ArrayList<GeofencingLatlng> latlngs) {
        geofencing.setUserID(loginDetail.getUsername());
        geofencing.setComNum(loginDetail.getCompanyNumber());
        Long fcNum = this.geofencingDao.insertGeofencing(geofencing);
        for(GeofencingLatlng latlng: latlngs) {
            latlng.setFcNum(fcNum);
            this.geofencingLatlngDao.insertGeofencingLatlng(latlng);
        }
        setGeofence(loginDetail, geofencing, latlngs, fcNum);
        return fcNum;

    }

    @Override
    @Transactional
    public void modifyGeofencing(LoginDetail loginDetail, Geofencing geofencing, ArrayList<GeofencingLatlng> latlngs) {
        this.geofencingDao.modifyGeofencing(geofencing);

        long fcNum = geofencing.getFcNum();
        if(latlngs!=null) {
            HashMap<String, Object> delParam = new HashMap<String, Object>();
            delParam.put("fcNum", fcNum);
            this.geofencingLatlngDao.deleteGeofencingLatlng(delParam);
            for(GeofencingLatlng latlng: latlngs) {
                latlng.setFcNum(fcNum);
                this.geofencingLatlngDao.insertGeofencingLatlng(latlng);
            }
            setGeofence(loginDetail, geofencing, latlngs, fcNum);
        }
    }

    @Override
    @Transactional
    public void deleteGeofencing(Long fcNum) {
        LoginDetail loginDetail = Security.getLoginDetail();

        /**
         * 지오펜스에 할당된 CodeAction 삭제
         */
        HashMap<String, Object> caparam = new HashMap<String, Object>();
        caparam.put("refNum", fcNum);
        caparam.put("refType", "GF");
        codeActionDao.deleteCodeActionMapping(caparam);
        /**
         * 지오펜스에 할당된 컨텐츠 삭제
         */
        ContentsMapping cparam = new ContentsMapping();
        cparam.setRefNum(fcNum);
        cparam.setRefType("GF");
        myContentsDao.deleteContentMapping(cparam);
        /**
         * 지오펜스 삭제
         */
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("fcNum", fcNum);
        this.geofencingDao.deleteGeofencing(param);

        try {
            String uuid = loginDetail.getUuid();
            String strFcNum = Long.toString(fcNum);
            geofencingRedisDao.deleteGeofenceList(uuid, strFcNum);
            geofencingRedisDao.deleteGeofenceInfo(strFcNum);
            geofencingRedisDao.deleteGeofenceLatlng(strFcNum);
        } catch(Exception e) {
            //e.printStackTrace();
        }

    }

    /**
     * 모든 펜스 좌표정보
     *
     * @author nohsoo 2015-03-11
     * @param loginDetail 로그인객체
     * @param floor 층번호
     * @return
     */
    @Override
    public List<?> getGeofencingLatlngAll(LoginDetail loginDetail, String floor) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("comNum", loginDetail.getCompanyNumber());
        param.put("floor", floor);
        List<?> list = this.geofencingLatlngDao.getGeofencingLatlngAll(param);
        return list;
    }

    /**
     *
     * 펜스에 코드액션(태스크) 할당(단일 처리)
     *     기존에 추가되어 있는 코드액션은 제거됨.
     *
     * @author nohsoo 2015-03-12
     * @param loginDetail
     * @param fcNum
     * @param fenceEventType
     * @param codeNum
     * @throws Exception
     */
    @Override
    @Transactional
    public void setGeofencingCodeAction(LoginDetail loginDetail, Long fcNum, String fenceEventType, Integer codeNum) throws Exception {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("refNum", fcNum);
        param.put("refSubType", fenceEventType);
        param.put("refType", "GF");
        codeActionDao.deleteCodeActionMapping(param);

        if(codeNum!=null) {
            CodeActionMapping vo = new CodeActionMapping();
            vo.setCodeNum(codeNum);
            vo.setRefNum(fcNum);
            vo.setRefType("GF");
            vo.setRefSubType(fenceEventType);
            codeActionDao.insertCodeActionMapping(vo);
        }
    }

    /**
     * 펜스에 이벤트 할당(단일 처리)
     *     기존에 추가되어 있는 이벤트는 제거됨.
     * create: nohsoo 2015-04-22
     *
     * @param loginDetail
     * @param fcNum
     * @param refSubType
     * @param evtNum
     * @throws Exception
     */
    @Override
    @Transactional
    public void setGeofencingEvent(LoginDetail loginDetail, Long fcNum, String refSubType, Long conNum, Integer evtNum) throws Exception {

        ContentsMapping vo = new ContentsMapping();
        vo.setConNum(conNum);
        vo.setRefSubType(refSubType);
        vo.setRefType("GF");
        vo.setRefNum(fcNum);
        if(evtNum==null) {
            vo.setEvtNum(0);
        } else {
            vo.setEvtNum(evtNum);
        }
        this.myContentsDao.modifyContentMapping(vo);
    }

    @Override
	public Integer getGeofencingGroupCount(GeofencingGroupSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = geofencingDao.getGeofencingGroupCount(param);
		return cnt;
	}

	@Override
	public List<?> getGeofencingGroupList(GeofencingGroupSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = geofencingDao.getGeofencingGroupList(param);
		return list;
	}

	@Override
	public GeofencingGroup getGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException {
		return geofencingDao.getGeofencingGroup(geofencingGroup);
	}

	@Override
	@Transactional
	public void registerGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException {
		geofencingDao.insertGeofencingGroup(geofencingGroup);
	}

	@Override
	@Transactional
	public void modifyGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException {
		geofencingDao.updateGeofencingGroup(geofencingGroup);
	}

	@Override
	@Transactional
	public void deleteGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException {
		geofencingDao.deleteGeofencingGroup(geofencingGroup);
	}

	@Override
	@Transactional
	public void registerGeofencingGroupMapping(GeofencingGroupMapping geofencingGroupMapping) throws DataAccessException {
		geofencingDao.insertGeofencingGroupMapping(geofencingGroupMapping);
	}

	@Override
	@Transactional
	public void modifyGeofencingGroupMapping(GeofencingGroupMapping geofencingGroupMapping) throws DataAccessException {
		geofencingDao.updateGeofencingGroupMapping(geofencingGroupMapping);
	}

	@Override
	@Transactional
	public void deleteGeofencingGroupMapping(GeofencingGroupMapping geofencingGroupMapping) throws DataAccessException {
		geofencingDao.deleteGeofencingGroupMapping(geofencingGroupMapping);
	}

}
