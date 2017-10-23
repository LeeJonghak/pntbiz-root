package core.api.common.dao;

import java.util.List;

import core.api.common.domain.Sync;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class SyncDao extends BaseDao {	
	
	public Sync getSyncInfo(Sync sync) throws DataAccessException {
		return (Sync) select("getSyncInfo", sync);
	}	
	
	public List<?> getSyncList(Sync sync) throws DataAccessException {
		return (List<?>) list("getSyncList", sync);
	}
	
	public void insertSync(Sync sync) throws DataAccessException {
		update("insertSync", sync);		
	}

	public void updateSync(Sync sync) throws DataAccessException {
		update("updateSync", sync);		
	}
}