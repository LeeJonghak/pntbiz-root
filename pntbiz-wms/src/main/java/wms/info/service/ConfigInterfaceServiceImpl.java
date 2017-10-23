package wms.info.service;

import core.common.config.dao.InterfaceConfigDao;
import core.common.config.domain.InterfaceConfig;
import core.common.config.domain.InterfaceConfigSearchParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by sl.kim on 2017-09-01.
 */

@Service
public class ConfigInterfaceServiceImpl implements ConfigInterfaceService {

    @Autowired
    private InterfaceConfigDao interfaceConfigDao;

    @Override
    public Integer getInterfaceConfigCount(InterfaceConfigSearchParam param) throws DataAccessException {
        return (Integer)  interfaceConfigDao.getCount(param);
    }

    @Override
    public List<?> getInterfaceConfigList(InterfaceConfigSearchParam param) throws DataAccessException {
        return (List<?>) interfaceConfigDao.gets(param);
    }

    @Override
    public InterfaceConfig getInterfaceConfigInfo(InterfaceConfig interfaceConfig) throws DataAccessException {
        return (InterfaceConfig) interfaceConfigDao.get(interfaceConfig);
    }

    @Override
    public List<?> getFloorList(Integer comNum) throws DataAccessException {
        return (List<?>) interfaceConfigDao.getFloors(comNum);
    }

    @Override
    public List<?> getGeofenceGroupList(Integer comNum) throws DataAccessException {
        return (List<?>) interfaceConfigDao.getGeofenceGroups(comNum);
    }

    @Override
    public void registerInterfaceConfig(InterfaceConfig interfaceConfig) throws DataAccessException {
        interfaceConfigDao.insert(interfaceConfig);
    }

    @Override
    public void modifyInterfaceConfig(InterfaceConfig interfaceConfig) throws DataAccessException {
        interfaceConfigDao.update(interfaceConfig);
    }

    @Override
    public void removeInterfaceConfig(Long interfaceNum) throws DataAccessException {
        interfaceConfigDao.delete(interfaceNum);
    }
}
