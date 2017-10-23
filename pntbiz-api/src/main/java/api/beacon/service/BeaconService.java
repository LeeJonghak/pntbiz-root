package api.beacon.service;

import core.api.beacon.domain.BeaconContents;
import core.api.map.domain.FloorCode;
import org.springframework.dao.DataAccessException;

import core.api.beacon.domain.Beacon;
import core.api.beacon.domain.BeaconState;
import core.api.contents.domain.Contents;

import java.text.ParseException;
import java.util.List;

public interface BeaconService {
	public Beacon getBeaconInfo(Beacon beacon) throws DataAccessException;

    public Beacon getBeaconInfoByMacAddr(String macAddr) throws DataAccessException;

    public List<?> getBeaconList(String UUID, Integer majorVer, Integer minorVer, String conType) throws DataAccessException;
    
    public List<?> getBeaconList(String UUID) throws DataAccessException;

    public List<?> getBeaconContentsList(String UUID, Integer majorVer, Integer minorVer, String conType) throws ParseException;
    public List<?> bindBeaconContentsList(List<?> list) throws ParseException;
	public Contents bindBeaconContentsInfo(Contents contents) throws ParseException;
    public List<?> getBeaconActionList(String UUID, Integer majorVer, Integer minorVer, String conType);
    
    public void registerBeaconState(BeaconState beaconState) throws DataAccessException;

    List<BeaconContents> getBeaconFloorCodeList(String UUID, String conType, List<FloorCode> floorCodeList) throws DataAccessException;
    List<BeaconContents> getBeaconFloorCodeListByField(String UUID, String conType, List<FloorCode> floorCodeList) throws DataAccessException;

    void updateScannerBeaconState(Beacon beacon);
}