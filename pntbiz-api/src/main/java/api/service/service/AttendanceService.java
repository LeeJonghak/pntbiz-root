package api.service.service;

import org.springframework.dao.DataAccessException;

import core.api.service.domain.Attendance;

public interface AttendanceService {
		
	public Integer getAttendanceCount(Attendance attendance) throws DataAccessException;
	public Attendance getAttendanceInfoBySidnum(Attendance attendance) throws DataAccessException;
	public void registerAttendance(Attendance attendance) throws DataAccessException;
	public void modifyAttendance(Attendance attendance) throws DataAccessException;
}