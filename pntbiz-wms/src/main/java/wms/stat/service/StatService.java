package wms.stat.service;

import java.util.List;

import core.wms.stat.domain.StatBeaconSearchParam;

public interface StatService {
	
	// 비콘 관제
	public Integer statBeaconMonitorCount(StatBeaconSearchParam param) throws Exception;
	public List<?> statBeaconMonitorList(StatBeaconSearchParam param) throws Exception;
	
	public Integer statBeaconPresenceCount(StatBeaconSearchParam param) throws Exception;
}
