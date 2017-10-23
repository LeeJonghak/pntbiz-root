package core.wms.maptool.dao;

import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
@Repository
public class MapToolDao extends BaseDao {

    public List<?> getBeaconList(Map<String, Object> param) throws DataAccessException {

        return list("maptool-getBeaconList", param);
    }

	public List<?> getBeaconGroupList(Map<String, Object> param) throws DataAccessException {

		return list("maptool-getBeaconGroupList", param);
	}

	public List<?> getBeaconGroupMappingList(Map<String, Object> param) throws DataAccessException {

		return list("maptool-getBeaconGroupMappingList", param);
	}

	public int insertBeaconGroupMapping(Map<String, Object> param) throws DataAccessException {
		return (int)insert("maptool-insertBeaconGroupMapping", param);
	}

	public int deleteBeaconGroupMapping(Map<String, Object> param) throws DataAccessException {
		return (int)delete("maptool-deleteBeaconGroupMapping", param);
	}

    public List<?> getScannerList(Map<String, Object> param) throws DataAccessException {

        return list("maptool-getScannerList", param);
    }

    public List<?> getNodeList(Map<String, Object> param) throws DataAccessException {

        return list("maptool-getNodeList", param);
    }

    public List<?> getNodeEdgeList(Map<String, Object> param) throws DataAccessException {

        return list("maptool-getNodeEdgeList", param);
    }

    public List<?> getFloorList(Map<String, Object> param) throws DataAccessException {

        return list("maptool-getFloorList", param);
    }

    public List<?> getAreaList(Map<String, Object> param) throws DataAccessException {

        return list("maptool-getAreaList", param);
    }

    public List<?> getAreaLatlngList(Map<String, Object> param) throws DataAccessException {
        return (List<?>) list("maptool-getAreaLatlngList", param);
    }

    public List<?> getGeofenceList(Map<String, Object> param) throws DataAccessException {
        return (List<?>) list("maptool-getGeofenceList", param);
    }

    public List<?> getGeofenceLatlngList(Map<String, Object> param) throws DataAccessException {
        return (List<?>) list("maptool-getGeofenceLatlngList", param);
    }

	public Map<String, Object> getBeaconInfo(Map<String, Object> param) throws DataAccessException {
		return (Map<String, Object>) select("maptool-getBeaconInfo", param);
	}

	public int insertBeacon(Map<String, Object> param) throws DataAccessException {
		return (int)insert("maptool-insertBeacon", param);
	}

	public int modifyBeacon(Map<String, Object> param) throws DataAccessException {
		return update("maptool-modifyBeacon", param);
	}

	public int deleteBeacon(Map<String, Object> param) throws DataAccessException {
		return delete("maptool-deleteBeacon", param);
	}

	public Map<String, Object> getScannerInfo(Map<String, Object> param) throws DataAccessException {
		return (Map<String, Object>) select("maptool-getScannerInfo", param);
	}

	public int insertScanner(Map<String, Object> param) throws DataAccessException {
		return (int)insert("maptool-insertScanner", param);
	}

	public int modifyScanner(Map<String, Object> param) throws DataAccessException {
		return update("maptool-modifyScanner", param);
	}

	public int deleteScanner(Map<String, Object> param) throws DataAccessException {
		return delete("maptool-deleteScanner", param);
	}

	public List<?> getGeofencingGroupList(Map<String, Object> param) throws DataAccessException {

		return list("maptool-getGeofencingGroupList", param);
	}

	public List<?> getGeofencingGroupMappingList(Map<String, Object> param) throws DataAccessException {

		return list("maptool-getGeofencingGroupMappingList", param);
	}

	public int insertGeofencingGroupMapping(Map<String, Object> param) throws DataAccessException {
		return (int)insert("maptool-insertGeofencingGroupMapping", param);
	}

	public int deleteGeofencingGroupMapping(Map<String, Object> param) throws DataAccessException {
		return (int)delete("maptool-deleteGeofencingGroupMapping", param);
	}

	public Map<String, Object> getGeofenceInfo(Map<String, Object> param) throws DataAccessException {
		return (Map<String, Object>) select("maptool-getGeofenceInfo", param);
	}

	public int insertGeofence(Map<String, Object> param) throws DataAccessException {
		return (int)insert("maptool-insertGeofence", param);
	}

	public int insertGeofenceLatlng(Map<String, Object> param) throws DataAccessException {
		Map<String, Object> p = new HashMap<String, Object>();
		p.put("fcNum", param.get("fcNum"));
		p.put("lat", param.get("lat"));
		p.put("lng", param.get("lng"));
		p.put("radius", param.get("radius"));
		p.put("orderSeq", param.get("orderSeq"));
		return (int)insert("maptool-insertGeofenceLatlng", p);
	}

	public int modifyGeofence(Map<String, Object> param) throws DataAccessException {
		return update("maptool-modifyGeofence", param);
	}

	public int deleteGeofence(Map<String, Object> param) throws DataAccessException {
		return delete("maptool-deleteGeofence", param);
	}

	public int deleteGeofenceLatlng(Map<String, Object> param) throws DataAccessException {
		return delete("maptool-deleteGeofenceLatlng", param);
	}

	public Map<String, Object> getAreaInfo(Map<String, Object> param) throws DataAccessException {
		return (Map<String, Object>) select("maptool-getAreaInfo", param);
	}

	public int insertArea(Map<String, Object> param) throws DataAccessException {
		return (int)insert("maptool-insertArea", param);
	}

	public int insertAreaLatlng(Map<String, Object> param) throws DataAccessException {
		return (int)insert("maptool-insertAreaLatlng", param);
	}

	public int modifyArea(Map<String, Object> param) throws DataAccessException {
		return update("maptool-modifyArea", param);
	}

	public int deleteArea(Map<String, Object> param) throws DataAccessException {
		return delete("maptool-deleteArea", param);
	}

	public int deleteAreaLatlng(Map<String, Object> param) throws DataAccessException {
		return delete("maptool-deleteAreaLatlng", param);
	}

	public Map<String, Object> getNodeInfo(Map<String, Object> param) throws DataAccessException {
		return (Map<String, Object>)select("maptool-getNodeInfo", param);
	}

	public int insertNode(Map<String, Object> param) throws DataAccessException {
		int result = (int)insert("maptool-insertNode", param);
		sqlSessionTemplate.flushStatements();
		return result;
	}

	public int modifyNode(Map<String, Object> param) throws DataAccessException {
		return update("maptool-modifyNode", param);
	}

	public int deleteNode(Map<String, Object> param) throws DataAccessException {
		return delete("maptool-deleteNode", param);
	}

	public Map<String, Object> getNodeEdgeInfo(Map<String, Object> param) throws DataAccessException {
		return (Map<String, Object>) select("maptool-getNodeEdgeInfo", param);
	}

	public int insertNodeEdge(Map<String, Object> param) throws DataAccessException {
		return (int)insert("maptool-insertNodeEdge", param);
	}

	public int deleteNodeEdge(Map<String, Object> param) throws DataAccessException {
		return delete("maptool-deleteNodeEdge", param);
	}

	public List<?> getContentList(Map<String, Object> param) throws DataAccessException {
		return list("maptool-getContentList", param);
	}

	public List<?> getContentMappingList(Map<String, Object> param) throws DataAccessException {
		return list("maptool-getContentMappingList", param);
	}

	public Map<String, Object> getContentMappingInfo(Map<String, Object> param) throws DataAccessException {
		return (Map<String, Object>)select("maptool-getContentMappingInfo", param);
	}

	public int insertContentMapping(Map<String, Object> param) throws DataAccessException {
		return (int)insert("maptool-insertContentMapping", param);
	}

	public int deleteContentMapping(Map<String, Object> param) throws DataAccessException {
		return delete("maptool-deleteContentMapping", param);
	}

	public List<?> getPresenceLogList(Map<String, Object> param) throws DataAccessException {
		return list("maptool-getPresenceLogList", param);
	}

	public List<?> getPresenceBeaconLogList(Map<String, Object> param) throws DataAccessException {
		return list("maptool-getPresenceBeaconLogList", param);
	}

	public List<?> getPOICateList() throws DataAccessException {
		return list("maptool-getPOICateList");
	}
}
