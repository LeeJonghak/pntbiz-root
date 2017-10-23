package wms.monitoring.service;

import java.util.Map;
import java.util.Set;

public interface MonitoringService {

    public Map<Object, Object> getRedisScanner(String key);

    Set<Object> getRedisScanners(String keys);
}