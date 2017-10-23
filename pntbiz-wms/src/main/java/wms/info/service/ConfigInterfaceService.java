package wms.info.service;

import core.common.config.domain.InterfaceConfig;
import core.common.config.domain.InterfaceConfigSearchParam;
import org.springframework.dao.DataAccessException;

import java.util.List;

/**
 * Created by sl.kim on 2017-09-01.
 */
public interface ConfigInterfaceService {

    public Integer getInterfaceConfigCount(InterfaceConfigSearchParam param) throws DataAccessException;
    public List<?> getInterfaceConfigList(InterfaceConfigSearchParam param) throws DataAccessException;

    public InterfaceConfig getInterfaceConfigInfo(InterfaceConfig interfaceConfig) throws DataAccessException;

    public List<?> getFloorList(Integer comNum) throws DataAccessException;
    public List<?> getGeofenceGroupList(Integer comNum) throws DataAccessException;

    public void registerInterfaceConfig(InterfaceConfig interfaceConfig) throws DataAccessException;
    public void modifyInterfaceConfig(InterfaceConfig interfaceConfig) throws DataAccessException;
    public void removeInterfaceConfig(Long interfaceNum) throws DataAccessException;

}
