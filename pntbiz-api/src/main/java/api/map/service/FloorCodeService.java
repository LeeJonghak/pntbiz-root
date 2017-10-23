package api.map.service;

import core.api.map.domain.FloorCode;
import org.springframework.dao.DataAccessException;

import java.util.List;

public interface FloorCodeService {
    FloorCode getFloorCodeInfo(FloorCode floorCode) throws DataAccessException;
    List<FloorCode> getFloorCodeList(FloorCode floorCode) throws DataAccessException;
    List<String> getFloorCodeNodeIDList(FloorCode floorCode) throws DataAccessException;
    List<FloorCode> getFloorCodeLevelNoDescList(FloorCode floorCode);
    List<FloorCode> getFloorCodeLevelNoAscList(FloorCode floorCode);
    List<FloorCode> getFloorCodeHierarchyList(FloorCode floorCode);

    List<FloorCode> getFloorCodeChildNodeList(FloorCode floorCode, List<FloorCode> floorCodeDepthList);
    List<FloorCode> getFloorCodeChildNode(List<FloorCode> floorCodeDepthList, List<FloorCode> floorCodeHierarchyList);
    List<FloorCode> getFloorCodeLastChildNodeList(List<FloorCode> floorCodeList);
}