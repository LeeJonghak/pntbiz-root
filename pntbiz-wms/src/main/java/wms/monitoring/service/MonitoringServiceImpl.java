package wms.monitoring.service;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.wms.monitoring.dao.MonitoringDao;

@Service
public class MonitoringServiceImpl implements MonitoringService {

    @Autowired
    private MonitoringDao dao;

    @Override
    public Map<Object, Object> getRedisScanner(String key) {
        return dao.getRedisInfo(key);
    }

    @Override
    public Set<Object> getRedisScanners(String keys) {
        return dao.getRedisScanners(keys);
    }
}
