package wms.map.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import core.wms.map.domain.FloorCode;

public interface FloorCodeService {
    public Integer getFloorCodeCount(FloorCode param) throws DataAccessException;
    public List<?> getFloorCodeList(FloorCode param) throws DataAccessException;
    public FloorCode getFloorCodeInfo(FloorCode codeAction) throws DataAccessException;

    public void registerFloorCode(FloorCode codeAction) throws DataAccessException;
    public void modifyFloorCode(FloorCode codeAction) throws DataAccessException;
    public void removeFloorCode(FloorCode codeAction) throws DataAccessException;
}