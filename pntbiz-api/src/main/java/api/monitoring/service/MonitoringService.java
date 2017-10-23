package api.monitoring.service;

import java.util.Map;

public interface MonitoringService {

    public void setRedisInfo(String key, Object obj);
    public boolean expireRedis(String key, int expr);

    public Map<Object, Object> getRedisInfo(String key);
}