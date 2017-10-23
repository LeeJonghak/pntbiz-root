package wms.maptool.service;

import framework.web.util.StringUtil;
import core.wms.maptool.dao.MapToolDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Service
public class MapToolServiceImpl implements MapToolService {

    @Autowired
    private MapToolDao mapToolDao;

    @Value("#{config['contentsURL']}")
    private String contentsURL;

    @Value("#{config['floor.image.src']}")
    private String floorImageSrc;

	@Override
    public List<?> getBeaconList(Map<String, Object> param) throws DataAccessException {
        List<?> list = mapToolDao.getBeaconList(param);

        return list;
    }

	@Override
	public List<?> getBeaconGroupList(Map<String, Object> param) throws DataAccessException {
		List<?> list = mapToolDao.getBeaconGroupList(param);

		return list;
	}

	@Override
	public List<?> getBeaconGroupMappingList(Map<String, Object> param) throws DataAccessException {
		List<?> list = mapToolDao.getBeaconGroupMappingList(param);

		return list;
	}

	@Transactional
	@Override
	public int insertBeaconGroupMapping(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.insertBeaconGroupMapping(param);
	}

	@Transactional
	@Override
	public int deleteBeaconGroupMapping(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.deleteBeaconGroupMapping(param);
	}

	@Override
    public List<?> getScannerList(Map<String, Object> param) throws DataAccessException {
        List<?> list = mapToolDao.getScannerList(param);

        return list;
    }

	@Override
    public List<?> getNodeList(Map<String, Object> param) throws DataAccessException {
        List<?> list = mapToolDao.getNodeList(param);

        return list;
    }

	@Override
    public List<?> getNodeEdgeList(Map<String, Object> param) throws DataAccessException {
        List<?> list = mapToolDao.getNodeEdgeList(param);

        return list;
    }

	@Override
    public List<Map<String, Object>> getFloorList(Map<String, Object> param) throws DataAccessException {
        List<?> list = mapToolDao.getFloorList(param);

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(Object obj: list) {
            Map<String, Object> map = (Map<String, Object>)obj;

            String imgUrl = ("".equals(StringUtil.NVL((String) map.get("imgSrc"), ""))) ? "" : contentsURL + "/" + map.get("comNum") + floorImageSrc + "/" + map.get("imgSrc");
            map.put("imgUrl", imgUrl);
            result.add(map);
        }
        return result;
    }

	@Override
    public List<?> getAreaList(Map<String, Object> param) throws DataAccessException {

        List<?> list = mapToolDao.getAreaList(param);
        List<?> latlngList = mapToolDao.getAreaLatlngList(param);
        Map<Long, List<Map<String, Object>>> latlngMap = new HashMap<Long, List<Map<String, Object>>>();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

        for(Object obj: latlngList) {
            Map<String, Object> areaLatlngInfo = (Map<String, Object>) obj;
            if(!latlngMap.containsKey(areaLatlngInfo.get("areaNum"))) {
                latlngMap.put((Long)areaLatlngInfo.get("areaNum"), new ArrayList<Map<String, Object>>());
            }

            latlngMap.get(areaLatlngInfo.get("areaNum")).add(areaLatlngInfo);
        }

        for(Object obj: list) {
            Map<String, Object> areaInfo = (Map<String, Object>) obj;
            List<Map<String, Object>> latlngs = latlngMap.get(areaInfo.get("areaNum"));
            areaInfo.put("latlngs", latlngs);

            Double minLat = null;
            Double maxLat = null;
            Double minLng = null;
            Double maxLng = null;
            for(Map<String, Object> latLngInfo: latlngs) {
                if(minLat==null || minLat > (Double)latLngInfo.get("lat")) {
                    minLat = (Double)latLngInfo.get("lat");
                }
                if(maxLat==null || maxLat < (Double)latLngInfo.get("lat")) {
                    maxLat = (Double)latLngInfo.get("lat");
                }
                if(minLng==null || minLng > (Double)latLngInfo.get("lng")) {
                    minLng = (Double)latLngInfo.get("lng");
                }
                if(maxLng==null || maxLng < (Double)latLngInfo.get("lng")) {
                    maxLng = (Double)latLngInfo.get("lng");
                }
            }
            Map<String, Double> latlng = new HashMap<String, Double>();
            latlng.put("lat", Math.round((minLat + ((maxLat - minLat) / 2)) * 1000000) / 1000000.0);
            latlng.put("lng", Math.round((minLng + ((maxLng - minLng) / 2)) * 1000000) / 1000000.0);
            areaInfo.put("center", latlng);

            result.add(areaInfo);
        }

        return result;
    }

	@Override
    public List<?> getGeofenceList(Map<String, Object> param) throws DataAccessException {
        List<?> fenceList = mapToolDao.getGeofenceList(param);
        List<?> latlngList = mapToolDao.getGeofenceLatlngList(param);
        Map<Long, List<Map<String, Object>>> latlngMap = new HashMap<Long, List<Map<String, Object>>>();

        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for(Object obj: latlngList) {
            Map<String, Object> latlngInfo = (Map<String, Object>)obj;
            Long key = ((BigInteger)latlngInfo.get("fcNum")).longValue();
            if(!latlngMap.containsKey(key)) {
                latlngMap.put(key, new ArrayList<Map<String, Object>>());
            }
            latlngMap.get(key).add(latlngInfo);
        }

        for(Object obj: fenceList) {
            Map<String, Object> fenceInfo = (Map<String, Object>)obj;
            Long key = ((BigInteger)fenceInfo.get("fcNum")).longValue();
            fenceInfo.put("latlngs", latlngMap.get(key));

            result.add(fenceInfo);
        }


        return result;
    }

	@Override
	public Map<String, Object> getBeaconInfo(Map<String, Object> param) throws DataAccessException {
		Map<String, Object> info = mapToolDao.getBeaconInfo(param);
		return info;
	}

	@Override
	@Transactional
	public int registerBeacon(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.insertBeacon(param);
	}

	@Override
	@Transactional
	public void modifyBeacon(Map<String, Object> param) throws DataAccessException {
		mapToolDao.modifyBeacon(param);
	}

	@Override
	@Transactional
	public void deleteBeacon(Map<String, Object> param) throws DataAccessException {
		mapToolDao.deleteBeacon(param);
	}

	@Override
	public Map<String, Object> getScannerInfo(Map<String, Object> param) throws DataAccessException {
		Map<String, Object> info = mapToolDao.getScannerInfo(param);
		return info;
	}

	@Override
	@Transactional
	public int registerScanner(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.insertScanner(param);
	}

	@Override
	@Transactional
	public void modifyScanner(Map<String, Object> param) throws DataAccessException {
		mapToolDao.modifyScanner(param);
	}

	@Override
	@Transactional
	public void deleteScanner(Map<String, Object> param) throws DataAccessException {
		mapToolDao.deleteScanner(param);
	}

	@Override
	public List<?> getGeofencingGroupList(Map<String, Object> param) throws DataAccessException {
		List<?> list = mapToolDao.getGeofencingGroupList(param);

		return list;
	}

	@Override
	public List<?> getGeofencingGroupMappingList(Map<String, Object> param) throws DataAccessException {
		List<?> list = mapToolDao.getGeofencingGroupMappingList(param);

		return list;
	}

	@Transactional
	@Override
	public int insertGeofencingGroupMapping(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.insertGeofencingGroupMapping(param);
	}

	@Transactional
	@Override
	public int deleteGeofencingGroupMapping(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.deleteGeofencingGroupMapping(param);
	}

	@Override
	@Transactional
	public Map<String, Object> getGeofenceInfo(Map<String, Object> param) throws DataAccessException {
		Map<String, Object> fenceInfo = mapToolDao.getGeofenceInfo(param);
		List<?> latlngList = mapToolDao.getGeofenceLatlngList(param);
		fenceInfo.put("latlngs", latlngList);
		return fenceInfo;
	}

	@Override
	@Transactional
	public int registerGeofence(Map<String, Object> param) throws DataAccessException {
		int result = mapToolDao.insertGeofence(param);
		if(param.containsKey("latlngs")) {
			List<Map<String, Object>> latlngs = (List<Map<String, Object>>)param.get("latlngs");
			for(Map<String, Object> latlng: latlngs) {
				latlng.put("fcNum", param.get("fcNum"));
				mapToolDao.insertGeofenceLatlng(latlng);
			}
		}
		return result;
	}

	@Override
	@Transactional
	public int registerGeofenceLatlng(Map<String, Object> param) throws DataAccessException {
		int result = mapToolDao.insertGeofenceLatlng(param);
		return result;
	}


	@Override
	@Transactional
	public void modifyGeofence(Map<String, Object> param) throws DataAccessException {
		mapToolDao.modifyGeofence(param);
		if(param.containsKey("latlngs")) {
			mapToolDao.deleteGeofenceLatlng(param);
			List<Map<String, Object>> latlngs = (List<Map<String, Object>>)param.get("latlngs");
			for(Map<String, Object> latlng: latlngs) {
				latlng.put("fcNum", param.get("fcNum"));
				mapToolDao.insertGeofenceLatlng(latlng);
			}
		}
	}

	@Override
	@Transactional
	public void deleteGeofence(Map<String, Object> param) throws DataAccessException {
		mapToolDao.deleteGeofence(param);
		mapToolDao.deleteGeofenceLatlng(param);
	}

	@Override
	@Transactional
	public Map<String, Object> getAreaInfo(Map<String, Object> param) throws DataAccessException {
		Map<String, Object> areaInfo = mapToolDao.getAreaInfo(param);
		List<?> latlngList = mapToolDao.getAreaLatlngList(param);
		areaInfo.put("latlngs", latlngList);
		return areaInfo;
	}

	@Override
	@Transactional
	public int registerArea(Map<String, Object> param) throws DataAccessException {
		int result = mapToolDao.insertArea(param);
		if(param.containsKey("latlngs")) {
			List<Map<String, Object>> latlngs = (List<Map<String, Object>>)param.get("latlngs");
			for(Map<String, Object> latlng: latlngs) {
				latlng.put("areaNum", param.get("areaNum"));
				mapToolDao.insertAreaLatlng(latlng);
			}
		}
		return result;
	}

	@Override
	@Transactional
	public void modifyArea(Map<String, Object> param) throws DataAccessException {
		mapToolDao.modifyArea(param);
		if(param.containsKey("latlngs")) {
			mapToolDao.deleteAreaLatlng(param);
			List<Map<String, Object>> latlngs = (List<Map<String, Object>>)param.get("latlngs");
			for(Map<String, Object> latlng: latlngs) {
				latlng.put("areaNum", param.get("areaNum"));
				mapToolDao.insertAreaLatlng(latlng);
			}
		}
	}

	@Override
	@Transactional
	public void deleteArea(Map<String, Object> param) throws DataAccessException {
		mapToolDao.deleteArea(param);
		mapToolDao.deleteAreaLatlng(param);
	}

	@Override
	public Map<String, Object> getNodeInfo(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.getNodeInfo(param);
	}

	@Override
	@Transactional
	public void insertNode(Map<String, Object> param) throws DataAccessException {
		mapToolDao.insertNode(param);
	}

	@Override
	@Transactional
	public void modifyNode(Map<String, Object> param) throws DataAccessException {
		mapToolDao.modifyNode(param);
	}

	@Override
	@Transactional
	public void deleteNode(Map<String, Object> param) throws DataAccessException {
		mapToolDao.deleteNode(param);
	}

	@Override
	public Map<String, Object> getNodeEdgeInfo(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.getNodeEdgeInfo(param);
	}

	@Override
	@Transactional
	public void registerNodeEdge(Map<String, Object> param) throws DataAccessException {
		mapToolDao.insertNodeEdge(param);
	}

	@Override
	@Transactional
	public void deleteNodeEdge(Map<String, Object> param) throws DataAccessException {
		mapToolDao.deleteNodeEdge(param);
	}

	@Override
	public List<?> getContentList(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.getContentList(param);
	}

	@Override
	public List<?> getContentMappingList(Map<String, Object> param) throws DataAccessException {

		return mapToolDao.getContentMappingList(param);
	}

	@Override
	public Map<String, Object> getContentMappingInfo(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.getContentMappingInfo(param);
	}

	@Override
	@Transactional
	public int insertContentMapping(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.insertContentMapping(param);
	}

	@Override
	@Transactional
	public int deleteContentMapping(Map<String, Object> param) throws DataAccessException {
		return mapToolDao.deleteContentMapping(param);
	}


	@Override
	public List<?> getPresenceLogList(Map<String, Object> param) throws DataAccessException {

		return mapToolDao.getPresenceLogList(param);
	}

	@Override
	public List<?> getPresenceBeaconLogList(Map<String, Object> param) throws DataAccessException {

		return mapToolDao.getPresenceBeaconLogList(param);
	}

	@Override
	public HashMap<String, String> getPOICateList() {
		List<?> list = mapToolDao.getPOICateList();
		HashMap<String, String> poiCategory = new HashMap<String, String>();
		for(Object obj: list) {
			HashMap<String, Object> code = (HashMap<String, Object>)obj;
			poiCategory.put(String.valueOf(code.get("sCD")), String.valueOf(code.get("sName")));
		}

		return poiCategory;
	}
}
