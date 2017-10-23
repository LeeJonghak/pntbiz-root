package wms.tracking.service;

import org.springframework.dao.DataAccessException;

import core.wms.tracking.domain.PresenceSetmap;

public interface PresenceSetmapService {
	
	// presence map
	public PresenceSetmap getPresenceSetmapInfo(PresenceSetmap presenceSetmap) throws DataAccessException;
	
	// presence maptransaction
	public void registerPresenceSetmap(PresenceSetmap presenceSetmap) throws DataAccessException;
	public void modifyPresenceSetmap(PresenceSetmap presenceSetmap) throws DataAccessException;
				
}