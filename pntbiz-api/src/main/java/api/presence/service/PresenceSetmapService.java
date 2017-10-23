package api.presence.service;

import core.api.presence.domain.PresenceSetmap;
import org.springframework.dao.DataAccessException;

public interface PresenceSetmapService {
	
	// presence map
	public PresenceSetmap getPresenceSetmapInfo(PresenceSetmap presenceSetmap) throws DataAccessException;
				
}