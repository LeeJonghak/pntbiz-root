package core.api.service.dao;

import core.api.service.domain.Attendance;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import framework.db.dao.BaseDao;

@Repository
public class AttendanceDao extends BaseDao {
	
	public Integer getAttendanceCount(Attendance attendance) throws DataAccessException {
		return (Integer) select("getAttendanceCount", attendance);
	}

	public Attendance getAttendanceInfoBySidnum(Attendance attendance) throws DataAccessException {
		return (Attendance) select("getAttendanceInfoBySidnum", attendance);
	}

	public void insertAttendance(Attendance attendance) throws DataAccessException {
		insert("insertAttendance", attendance);
	}
	
	public void updateAttendance(Attendance attendance) throws DataAccessException {
		update("updateAttendance", attendance);
	}
}
