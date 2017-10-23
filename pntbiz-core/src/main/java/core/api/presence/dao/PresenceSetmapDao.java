package core.api.presence.dao;

import core.api.presence.domain.PresenceSetmap;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

@Repository
public class PresenceSetmapDao extends BaseDao {	
	
	public PresenceSetmap getPresenceSetmapInfo(PresenceSetmap presenceSetmap) throws DataAccessException {
		return (PresenceSetmap) select("getPresenceSetmapInfo", presenceSetmap);
	}
	
}