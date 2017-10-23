package wms.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import core.wms.sync.domain.Sync;

public interface SyncService {
	
	public Sync getSyncInfo(Sync sync) throws DataAccessException; 
	public List<?> getSyncList(Sync sync) throws DataAccessException; 
	public void updateSync(Sync sync) throws DataAccessException;
	
}