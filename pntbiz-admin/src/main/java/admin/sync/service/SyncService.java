package admin.sync.service;

import java.util.List;

import core.admin.sync.domain.Sync;
import org.springframework.dao.DataAccessException;

public interface SyncService {
	
	public Sync getSyncInfo(Sync sync) throws DataAccessException;
	public List<?> getSyncList(Sync sync) throws DataAccessException; 
	public void updateSync(Sync sync) throws DataAccessException;
	
}