package wms.stat.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import core.wms.stat.dao.StatDao;
import core.wms.stat.domain.StatBeaconSearchParam;

@Service
public class StatServiceImpl implements StatService{
	
	@Autowired
	private StatDao statDao;	

	@Override
	public Integer statBeaconMonitorCount(StatBeaconSearchParam param) throws Exception {		
		return statDao.statBeaconMonitorCount(param);
	}

	@Override
	public List<?> statBeaconMonitorList(StatBeaconSearchParam param) throws Exception {
		return (List<?>) statDao.statBeaconMonitorList(param);
	}

	@Override
	public Integer statBeaconPresenceCount(StatBeaconSearchParam param) throws Exception {
		return statDao.statBeaconPresenceCount(param);
	}	
}
