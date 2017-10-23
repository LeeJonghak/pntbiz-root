package api.geofencing.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import core.api.contents.domain.Contents;
import core.common.geofencing.domain.Geofencing;
import core.api.geofencing.domain.GeofencingLatlng;
import core.api.geofencing.domain.GeofencingZone;
import core.api.map.domain.FloorCode;

/**
 */
public interface GeofencingService {
	
	public Geofencing getGeofencing(Geofencing geofencing);
    public List<?> getGeofencingZone(GeofencingZone geofencingZone);
    public List<GeofencingLatlng> getGeofencingLatlngs(Geofencing geofencing);
	
	public List<?> getGeofencingList(String companyUUID);

    List<?> getFloorGeofencingList(String companyUUID, String floor);

    public List<?> getGeofencingListByAll(String companyUUID);
    public List<?> getGeofencingListByCache(String companyUUID);
    public ArrayList<HashMap<String, Object>> getGeofencingCacheList(String companyUUID) throws ParseException;

    public List<?> getGeofencingContentsList(String companyUUID, String floor) throws ParseException;
    public List<?> getGeofencingContentsList(String companyUUID, Long fcNum) throws ParseException;
     
    public List<?> bindGeofencingContentsList(List<?> list) throws ParseException;
	public Contents bindGeofencingContentsInfo(Contents contents) throws ParseException;

    public List<?> getGeofencingActionList(String companyUUID, String floor);
    public List<?> getGeofencingActionList(String companyUUID, Long fcNum);
    
    void modifyGeofencing(Geofencing geofencing);

    public List<Geofencing> checkGeofence(String SUUID, String floor, Double lat, Double lng) throws ParseException;

    List<Geofencing> checkGeofence(String SUUID, String floor, List<Geofencing> fenceList, Double lat, Double lng) throws ParseException;

    List<GeofencingZone> getGeofencingFloorCodeList(String UUID, List<FloorCode> floorCodeList);
}
