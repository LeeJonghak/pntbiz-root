package wms.maptool.service;

import org.springframework.dao.DataAccessException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 */
public interface MapToolService {

    public List<?> getScannerList(Map<String, Object> param) throws DataAccessException;
    public List<?> getBeaconList(Map<String, Object> param) throws DataAccessException;
	public List<?> getBeaconGroupList(Map<String, Object> param) throws DataAccessException;
	public List<?> getBeaconGroupMappingList(Map<String, Object> param) throws DataAccessException;
	public int insertBeaconGroupMapping(Map<String, Object> param) throws DataAccessException;
	public int deleteBeaconGroupMapping(Map<String, Object> param) throws DataAccessException;
    public List<Map<String, Object>> getFloorList(Map<String, Object> param) throws DataAccessException;
    public List<?> getAreaList(Map<String, Object> param) throws DataAccessException;
    public List<?> getGeofenceList(Map<String, Object> param) throws DataAccessException;
    public List<?> getNodeList(Map<String, Object> param) throws DataAccessException;
    public List<?> getNodeEdgeList(Map<String, Object> param) throws DataAccessException;

	public Map<String, Object> getBeaconInfo(Map<String, Object> param) throws DataAccessException;
	public int registerBeacon(Map<String, Object> param) throws DataAccessException;
	public void modifyBeacon(Map<String, Object> param) throws DataAccessException;
	public void deleteBeacon(Map<String, Object> param) throws DataAccessException;

	public Map<String, Object> getScannerInfo(Map<String, Object> param) throws DataAccessException;
	public int registerScanner(Map<String, Object> param) throws DataAccessException;
	public void modifyScanner(Map<String, Object> param) throws DataAccessException;
	public void deleteScanner(Map<String, Object> param) throws DataAccessException;

	public List<?> getGeofencingGroupList(Map<String, Object> param) throws DataAccessException;
	public List<?> getGeofencingGroupMappingList(Map<String, Object> param) throws DataAccessException;
	public int insertGeofencingGroupMapping(Map<String, Object> param) throws DataAccessException;
	public int deleteGeofencingGroupMapping(Map<String, Object> param) throws DataAccessException;
	public Map<String, Object> getGeofenceInfo(Map<String, Object> param) throws DataAccessException;
	public int registerGeofence(Map<String, Object> param) throws DataAccessException;
	public int registerGeofenceLatlng(Map<String, Object> param) throws DataAccessException;
	public void modifyGeofence(Map<String, Object> param) throws DataAccessException;
	public void deleteGeofence(Map<String, Object> param) throws DataAccessException;

	public Map<String, Object> getAreaInfo(Map<String, Object> param) throws DataAccessException;
	public int registerArea(Map<String, Object> param) throws DataAccessException;
	public void modifyArea(Map<String, Object> param) throws DataAccessException;
	public void deleteArea(Map<String, Object> param) throws DataAccessException;

	public Map<String, Object> getNodeInfo(Map<String, Object> param) throws DataAccessException;
	public void insertNode(Map<String, Object> param) throws DataAccessException;
	public void modifyNode(Map<String, Object> param) throws DataAccessException;
	public void deleteNode(Map<String, Object> param) throws DataAccessException;
	public Map<String, Object> getNodeEdgeInfo(Map<String, Object> param) throws DataAccessException;
	public void registerNodeEdge(Map<String, Object> param) throws DataAccessException;
	public void deleteNodeEdge(Map<String, Object> param) throws DataAccessException;

	public List<?> getContentList(Map<String, Object> param) throws DataAccessException;
	public List<?> getContentMappingList(Map<String, Object> param) throws DataAccessException;
	public Map<String, Object> getContentMappingInfo(Map<String, Object> param) throws DataAccessException;
	public int insertContentMapping(Map<String, Object> param) throws DataAccessException;
	public int deleteContentMapping(Map<String, Object> param) throws DataAccessException;

	public List<?> getPresenceLogList(Map<String, Object> param) throws DataAccessException;

	public List<?> getPresenceBeaconLogList(Map<String, Object> param) throws DataAccessException;

	public HashMap<String, String> getPOICateList();
}

