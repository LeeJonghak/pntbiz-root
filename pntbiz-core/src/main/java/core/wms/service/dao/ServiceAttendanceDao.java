package core.wms.service.dao;

import core.wms.service.domain.AttendanceSearchParam;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ServiceAttendanceDao extends BaseDao {
	public Integer getAttendanceCount(AttendanceSearchParam param) throws DataAccessException {
		return (Integer) select("getAttendanceCount", param);
	}
	public List<?> getAttendanceList(AttendanceSearchParam param) throws DataAccessException {
		return (List<?>) list("getAttendanceList", param);
	}

	public void deleteAttendance(AttendanceSearchParam param) throws DataAccessException {
		delete("deleteAttendance", param);
	}
}
