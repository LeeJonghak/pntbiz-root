package api.monitoring.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.api.monitoring.dao.MonitoringDao;

@Service
public class MonitoringServiceImpl implements MonitoringService {

    @Autowired
    private MonitoringDao dao;

    @Override
    public void setRedisInfo(String key, Object obj) {
        dao.setRedisInfo(key, obj);
    }

    @Override
    public Map<Object, Object> getRedisInfo(String key) {
        return dao.getRedisInfo(key);
    }
    @Override
    public boolean expireRedis(String key, int expr) {
        return dao.expireRedis(key, expr);
    }
}
