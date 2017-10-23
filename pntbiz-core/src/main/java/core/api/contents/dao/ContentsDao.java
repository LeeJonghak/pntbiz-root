package core.api.contents.dao;

import java.util.List;

import core.api.contents.domain.Contents;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class ContentsDao extends BaseDao {
	
	public List<?> getContentsList(Contents contents) throws DataAccessException {
		return (List<?>) list("getContentsList", contents);
	}
	
	public Contents getContentsInfo(Contents contents) throws DataAccessException {
		return (Contents) select("getContentsInfo", contents);
	}
	
	public Contents getContentsMessage(Contents contents) throws DataAccessException {
		return (Contents) select("getContentsMessage", contents);
	}
	
}
