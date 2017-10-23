package api.geofencing.service;

import core.api.contents.domain.Contents;
import core.api.geofencing.dao.GeofencingDao;
import core.common.geofencing.domain.Geofencing;
import core.api.geofencing.domain.GeofencingLatlng;
import core.api.geofencing.domain.GeofencingZone;

import core.api.map.domain.FloorCode;
import framework.geofence.Geofence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import framework.web.util.StringUtil;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Service
public class GeofencingServiceImpl implements GeofencingService {

    @Autowired
    private GeofencingDao geofencingDao;
    
    @Value("#{config['contentsURL']}")
	private String contentsURL;
	@Value("#{config['contents.image.path']}")
	private String contentsImagePath;
	@Value("#{config['contents.image.src']}")
	private String contentsImageSrc;	
	@Value("#{config['contents.sound.path']}")
	private String contentsSoundPath;
	@Value("#{config['contents.sound.src']}")
	private String contentsSoundSrc;	
	
	@Override
    public Geofencing getGeofencing(Geofencing geofencing) {
        Geofencing geofencingInfo = this.geofencingDao.getGeofencing(geofencing);
        return geofencingInfo;
    }
	
	@Override
    public List<?> getGeofencingZone(GeofencingZone geofencingZone) {
		List<?> list = this.geofencingDao.getGeofencingZone(geofencingZone);
        return list;
    }

    @Override
    public List<GeofencingLatlng> getGeofencingLatlngs(Geofencing geofencing) {
        List<GeofencingLatlng> list = this.geofencingDao.getGeofencingLatlngs(geofencing);
        return list;
    }
	
	@Override
    public List<?> getGeofencingList(String companyUUID) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", companyUUID);
        List<?> list = geofencingDao.getGeofencingList(param);
        return list;
    }

    @Override
    public List<?> getFloorGeofencingList(String companyUUID, String floor) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", companyUUID);
        param.put("floor", floor);
        List<?> list = geofencingDao.getFloorGeofencingList(param);
        return list;
    }

    @Override
    public List<?> getGeofencingListByAll(String companyUUID) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", companyUUID);
        List<?> list = geofencingDao.getGeofencingListByAll(param);
        return list;
    }

    @Override
    public List<?> getGeofencingListByCache(String companyUUID) {
        List<?> list = geofencingDao.getGeofencingListByCache(companyUUID);
        return list;
    }

    @Override
    public ArrayList<HashMap<String, Object>> getGeofencingCacheList(String companyUUID) throws ParseException {
        List<?> list = getGeofencingListByCache(companyUUID);

        HashMap<Long, HashMap<String, Object>> geofencings = new HashMap<Long, HashMap<String, Object>>();
        for(Object obj: list) {
            GeofencingZone geofencing = (GeofencingZone)obj;

            HashMap<String, Object> latlng = new HashMap<String, Object>();
            latlng.put("lat", geofencing.getLat());
            latlng.put("lng", geofencing.getLng());
            latlng.put("orderSeq", geofencing.getOrderSeq());
            latlng.put("radius", geofencing.getRadius());
            latlng.put("fcNum", geofencing.getFcNum());

            if(!geofencings.containsKey(geofencing.getFcNum())) {

                geofencing.setField1(StringUtil.NVL(geofencing.getField1(), ""));
                geofencing.setField2(StringUtil.NVL(geofencing.getField2(), ""));
                geofencing.setField3(StringUtil.NVL(geofencing.getField3(), ""));
                geofencing.setField4(StringUtil.NVL(geofencing.getField4(), ""));
                geofencing.setField5(StringUtil.NVL(geofencing.getField5(), ""));

                HashMap<String, Object> fencing = new HashMap<String, Object>();
                fencing.put("fcNum", geofencing.getFcNum());
                fencing.put("comNum", geofencing.getComNum());
                fencing.put("fcType", geofencing.getFcType());
                fencing.put("fcShape", geofencing.getFcShape());
                fencing.put("fcName", geofencing.getFcName());
                fencing.put("floor", geofencing.getFloor());
                fencing.put("evtEnter", geofencing.getEvtEnter());
                fencing.put("evtLeave", geofencing.getEvtLeave());
                fencing.put("evtStay", geofencing.getEvtStay());
                fencing.put("userID", geofencing.getUserID());
                fencing.put("numEnter", geofencing.getNumEnter());
                fencing.put("numLeave", geofencing.getNumLeave());
                fencing.put("numStay", geofencing.getNumStay());
                fencing.put("modDate", geofencing.getModDate());
                fencing.put("regDate", geofencing.getRegDate());
                fencing.put("zoneType", geofencing.getZoneType());
                fencing.put("field1", geofencing.getField1());
                fencing.put("field2", geofencing.getField2());
                fencing.put("field3", geofencing.getField3());
                fencing.put("field4", geofencing.getField4());
                fencing.put("field5", geofencing.getField5());
                fencing.put("latlngs", new ArrayList<HashMap<String, Object>>());
                geofencings.put(geofencing.getFcNum(), fencing);
            }

            @SuppressWarnings("unchecked")
            ArrayList<HashMap<String, Object>> latlngs = (ArrayList<HashMap<String, Object>>) geofencings.get(geofencing.getFcNum()).get("latlngs");
            latlngs.add(latlng);
        }
        ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for(HashMap<String, Object> item: geofencings.values()) {
            data.add(item);
        }
        return data;
    }
	
    @Override
    public List<?> getGeofencingContentsList(String companyUUID, String floor) throws ParseException {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", companyUUID);
        param.put("floor", floor);
        List<?> list = geofencingDao.getGeofencingContentsList(param);
        list = bindGeofencingContentsList(list);
        return list;
    }
    
    @Override
    public List<?> getGeofencingContentsList(String companyUUID, Long fcNum) throws ParseException {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", companyUUID);
        param.put("fcNum", fcNum);
        List<?> list = geofencingDao.getGeofencingContentsList(param);
        list = bindGeofencingContentsList(list);
        return list;
    }
    
    @Override
	public List<?> bindGeofencingContentsList(List<?> list) throws ParseException {
		List<Contents> clist = new ArrayList<Contents>();
		for(int i=0; i<list.size(); i++) {
			Contents contents = (Contents) list.get(i);
			contents = bindGeofencingContentsInfo(contents);
			clist.add(contents);
		}
		return clist;
	}
	
	@Override
	public Contents bindGeofencingContentsInfo(Contents contents) throws ParseException {
		String contentsImageURL = contentsURL + "/" + contents.getComNum() + contentsImageSrc + "/";
		String contentsSoundURL = contentsURL + "/" + contents.getComNum() + contentsSoundSrc + "/";

		String imgURL1 = ("".equals(StringUtil.NVL(contents.getImgSrc1(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc1(), 0, 6) + "/" + contents.getImgSrc1();
		String imgURL2 = ("".equals(StringUtil.NVL(contents.getImgSrc2(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc2(), 0, 6) + "/" + contents.getImgSrc2();
		String imgURL3 = ("".equals(StringUtil.NVL(contents.getImgSrc3(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc3(), 0, 6) + "/" + contents.getImgSrc3();
		String imgURL4 = ("".equals(StringUtil.NVL(contents.getImgSrc4(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc4(), 0, 6) + "/" + contents.getImgSrc4();
		String imgURL5 = ("".equals(StringUtil.NVL(contents.getImgSrc5(), ""))) ? "" : contentsImageURL + StringUtil.getSubString(contents.getImgSrc5(), 0, 6) + "/" + contents.getImgSrc5();
		String soundURL1 = ("".equals(StringUtil.NVL(contents.getSoundSrc1(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc1(), 0, 6) + "/" + contents.getSoundSrc1();
		String soundURL2 = ("".equals(StringUtil.NVL(contents.getSoundSrc2(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc2(), 0, 6) + "/" + contents.getSoundSrc2();
		String soundURL3 = ("".equals(StringUtil.NVL(contents.getSoundSrc3(), ""))) ? "" : contentsSoundURL + StringUtil.getSubString(contents.getSoundSrc3(), 0, 6) + "/" + contents.getSoundSrc3();
		contents.setImgURL1(imgURL1);
		contents.setImgURL2(imgURL2);
		contents.setImgURL3(imgURL3);
		contents.setImgURL4(imgURL4);
		contents.setImgURL5(imgURL5);
		contents.setSoundURL1(soundURL1);
		contents.setSoundURL2(soundURL2);
		contents.setSoundURL3(soundURL3);
		return contents;
	}

    @Override
    public List<?> getGeofencingActionList(String companyUUID, String floor) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", companyUUID);
        param.put("floor", floor);
        List<?> list = geofencingDao.getGeofencingActionList(param);
        return list;
    }
    
    @Override
    public List<?> getGeofencingActionList(String companyUUID, Long fcNum) {
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", companyUUID);
        param.put("fcNum", fcNum);
        List<?> list = geofencingDao.getGeofencingActionList(param);
        return list;
    }

    @Override
    @Transactional
	public void modifyGeofencing(Geofencing geofencing) {
		geofencingDao.updateGeofencing(geofencing);
	}

    @Override
    public List<Geofencing> checkGeofence(String SUUID, String floor, Double lat, Double lng) {
        List<Geofencing> res = new ArrayList<Geofencing>();
        HashMap<String, Object> listParam = new HashMap<String, Object>();
        listParam.put("UUID", SUUID);
        List<Geofencing> fenceList = (List<Geofencing>) geofencingDao.getGeofencingList(listParam);

        return checkInboundGeofence(floor, lat, lng, fenceList);
    }

    @Override
    public List<Geofencing> checkGeofence(String SUUID, String floor, List<Geofencing> fenceList, Double lat, Double lng) {
        List<Geofencing> res = new ArrayList<Geofencing>();
        return checkInboundGeofence(floor, lat, lng, fenceList);
    }

    private List<Geofencing> checkInboundGeofence(String floor, Double lat, Double lng, List<Geofencing> fenceList) {

        List<Geofencing> inboundFences = new ArrayList<Geofencing>();

        for (Object geofencing : fenceList) {
            Geofencing fence = (Geofencing) geofencing;
            if (!fence.getFloor().equals(floor)) continue;

            List<GeofencingLatlng> latlngs = geofencingDao.getGeofencingLatlngs(fence);

//            List<Map<String, Double>> bounds = new ArrayList<Map<String, Double>>();
            List<Geofence.LatLng> bounds = new ArrayList<Geofence.LatLng>();
            for(GeofencingLatlng geofencingLatlng : latlngs) {
                Geofence.LatLng latlng = new Geofence.LatLng();
                latlng.setLat(geofencingLatlng.getLat());
                latlng.setLng(geofencingLatlng.getLng());
//                Map<String, Double> latlng = new HashMap<String, Double>();
//                latlng.put("lat", geofencingLatlng.getLat());
//                latlng.put("lng", geofencingLatlng.getLng());
                bounds.add(latlng);
            }
            if (Geofence.containLatlng(bounds, lat, lng)) {
                inboundFences.add(fence);
            }
        }
        return inboundFences;
    }

    @Override
    public List<GeofencingZone> getGeofencingFloorCodeList(String UUID, List<FloorCode> floorCodeList) {
        List<GeofencingZone> list = new ArrayList<GeofencingZone>();
        HashMap<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        param.put("floorCodeList", floorCodeList);
        list = geofencingDao.getGeofencingFloorCodeList(param);
        return list;
    }
}
