package wms.geofencing.service;

import core.wms.admin.company.domain.Company;
import core.common.geofencing.domain.Geofencing;
import core.wms.geofencing.domain.GeofencingGroup;
import core.wms.geofencing.domain.GeofencingGroupMapping;
import core.wms.geofencing.domain.GeofencingGroupSearchParam;
import core.wms.geofencing.domain.GeofencingLatlng;
import wms.component.auth.LoginDetail;

import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;

/**
 */
public interface GeofencingService {

	public List<?> getGeofencingAll(LoginDetail loginDetail, String floor);
	public List<?> getGeofencingAll(Integer comNum);
	public List<?> getGeofencingLatlngList(Long fcNum);
	List<?> getGeofencingList(GeofencingGroupSearchParam param);
	Integer getGeofencingCount(GeofencingGroupSearchParam param);
	Geofencing getGeofencing(Long fcNum);
	public void registerGeofencing(LoginDetail loginDetail, Geofencing geofencing, ArrayList<GeofencingLatlng> latlngs);
	public Long registerGeofencingReturnNum(LoginDetail loginDetail, Geofencing geofencing, ArrayList<GeofencingLatlng> latlngs);
	void modifyGeofencing(LoginDetail loginDetail, Geofencing geofencing, ArrayList<GeofencingLatlng> latlngs);
	void deleteGeofencing(Long fcNum);

	public List<?> getGeofencingLatlngAll(LoginDetail loginDetail, String floor);

	public void setGeofencingCodeAction(LoginDetail loginDetail, Long fcNum, String fenceEventType, Integer codeNum) throws Exception;

	void setGeofencingEvent(LoginDetail loginDetail, Long fcNum, String fenceEventType, Long conNum, Integer evtNum) throws Exception;

	// geofencing group
	public Integer getGeofencingGroupCount(GeofencingGroupSearchParam param) throws DataAccessException;
	public List<?> getGeofencingGroupList(GeofencingGroupSearchParam param) throws DataAccessException;
	public GeofencingGroup getGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException;
	public void registerGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException;
	public void modifyGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException;
	public void deleteGeofencingGroup(GeofencingGroup geofencingGroup) throws DataAccessException;
 // geofencing group mapping
	public void registerGeofencingGroupMapping(GeofencingGroupMapping geofencingGroupMapping) throws DataAccessException;
	public void modifyGeofencingGroupMapping(GeofencingGroupMapping geofencingGroupMapping) throws DataAccessException;
	public void deleteGeofencingGroupMapping(GeofencingGroupMapping geofencingGroupMapping) throws DataAccessException;

	public void syncGeofencingRedisData(Company company) throws DataAccessException;
}
