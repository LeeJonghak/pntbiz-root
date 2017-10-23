package core.wms.tracking.dao;

import core.wms.tracking.domain.PresenceSetmap;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class PresenceSetmapDao extends BaseDao {	
	
	public PresenceSetmap getPresenceSetmapInfo(PresenceSetmap presenceSetmap) throws DataAccessException {
		return (PresenceSetmap) select("getPresenceSetmapInfo", presenceSetmap);
	}

	public void insertPresenceSetmap(PresenceSetmap presenceSetmap) throws DataAccessException {
		insert("insertPresenceSetmap", presenceSetmap);
	}

	public void updatePresenceSetmap(PresenceSetmap presenceSetmap) throws DataAccessException {
		update("updatePresenceSetmap", presenceSetmap);
	}
	
}