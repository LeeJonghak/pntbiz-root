package wms.beacon.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import core.common.beacon.domain.BeaconRestrictedZone;
import org.springframework.dao.DataAccessException;

import com.jcraft.jsch.SftpException;
import com.oreilly.servlet.MultipartRequest;

import core.wms.beacon.domain.Beacon;
import core.wms.beacon.domain.BeaconGroup;
import core.wms.beacon.domain.BeaconGroupMapping;
import core.wms.beacon.domain.BeaconGroupSearchParam;
import core.wms.beacon.domain.BeaconSearchParam;
import core.wms.beacon.domain.BeaconState;
import core.wms.beacon.domain.BeaconStateSearchParam;
import wms.component.auth.LoginDetail;

/**
 */
public interface BeaconService {

    public void registerBeacon(LoginDetail loginDetail, Beacon beacon);

    List<?> getBeaconList(BeaconSearchParam paramVo);
    public List<?> bindBeaconList(List<?> list, List<?> beaconTypeCD) throws ParseException;

    Integer getBeaconCount(BeaconSearchParam paramVo);

    Beacon getBeacon(Long beaconNum);

    void modifyBeacon(Beacon vo);

    void deleteBeacon(Long beaconNum);

    public void setBeaconCodeAction(LoginDetail loginDetail, Long beaconNum, Integer codeNum) throws Exception;

    public Integer getBeaconStateCount(BeaconStateSearchParam param)  throws Exception;
    public List<?> getBeaconStateList(BeaconStateSearchParam param) throws Exception;

    public List<BeaconState> getLogBeaconStateList(HashMap<String, Object> param) throws Exception;
    public List<BeaconState> getChartLogLossBeaconStateList(HashMap<String, Object> param) throws DataAccessException;
    public List<Beacon> getLogLossBeaconStateList(HashMap<String, Object> param) throws DataAccessException;

    public void modifyBeaconState(BeaconState beaconState);

    public void modifyBeaconImage(MultipartRequest multi, Beacon beacon) throws DataAccessException, IOException, SftpException;

    public Map<String, String> uploadBeaconImage(MultipartRequest multi, Beacon beacon) throws IOException;

    public String deleteBeaconImage(Beacon beacon) throws IOException, SftpException;

    public void updateBeaconBlankImage(Beacon beacon) throws DataAccessException;

    void setBeaconEvent(LoginDetail loginDetail, Long fcNum, String fenceEventType, Long conNum, Integer evtNum) throws Exception;

    // beacon group
    public Integer getBeaconGroupCount(BeaconGroupSearchParam param) throws DataAccessException;
    public List<?> getBeaconGroupList(BeaconGroupSearchParam param) throws DataAccessException;
    public BeaconGroup getBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException;
    public void registerBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException;
    public void modifyBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException;
    public void deleteBeaconGroup(BeaconGroup beaconGroup) throws DataAccessException;

    public void registerBeaconGroupMapping(BeaconGroupMapping beaconGroupMapping) throws DataAccessException;
    public void modifyGeofencingGroupMapping(BeaconGroupMapping beaconGroupMapping) throws DataAccessException;
    public void deleteBeaconGroupMapping(BeaconGroupMapping beaconGroupMapping) throws DataAccessException;

    public Integer getBeaconRestrictedZoneCount(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException;
    public List<BeaconRestrictedZone> getBeaconRestrictedZoneList(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException;

    public void registerBeaconRestrictedZone(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException;

    public BeaconRestrictedZone getBeaconRestrictedZoneInfo(BeaconRestrictedZone beaconRestrictedZone) throws  DataAccessException;
    public void modifyBeaconRestrictedZone(BeaconRestrictedZone beaconRestrictedZone) throws  DataAccessException;
    public void modifyAllBeaonRestrictedZoneForPermitted(BeaconRestrictedZone beaconRestrictedZone) throws  DataAccessException;
    public void deleteBeaconRestrictedZone(BeaconRestrictedZone beaconRestrictedZone) throws DataAccessException;

/*
 * 사용안함
 *
    List<?> getBeaconAll(LoginDetail loginDetail, String floor);
    public List<?> getBeaconAll(HashMap<String, PresenceRequestParam> param);
*/
}
