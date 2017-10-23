package core.common.config.dao;

import core.common.config.domain.InterfaceConfig;
import core.common.config.domain.InterfaceConfigSearchParam;
import framework.db.dao.BaseDao;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by ucjung on 2017-08-28.
 */
@Repository
public class InterfaceConfigDao extends BaseDao{

    public Integer getCount(InterfaceConfigSearchParam param) {
        return (Integer) select("selectInterfaceConfigCount", param);
    }

    public List<InterfaceConfig> gets(InterfaceConfigSearchParam param) {
        return (List<InterfaceConfig>) list("selectInterfaceConfigs", param);
    }

    public InterfaceConfig get(InterfaceConfig interfaceConfig) {
        return (InterfaceConfig) select("selectInterfaceConfig", interfaceConfig);
    }

    public List<?> getFloors(Integer comNum) {
        return (List<?>) list("selectFloors", comNum);
    }

    public List<?> getGeofenceGroups(Integer comNum) {
        return (List<?>) list("selectGeofenceGroups", comNum);
    }

    public Integer insert(InterfaceConfig param) {
        return (Integer)insert("insertInterfaceConfig", param);
    }

    public Integer update(InterfaceConfig param) {
        return update("updateInterfaceConfig", param);
    }

    public Integer delete(Long interfaceNum) {
        return delete("deleteInterfaceConfig", interfaceNum);
    }
}
