package api.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.api.service.dao.AttendanceDao;
import core.api.service.domain.Attendance;

@Service
public class AttendanceServiceImpl implements AttendanceService {	
	
	@Autowired
	private AttendanceDao attendanceDao;
	
	private Logger logger = LoggerFactory.getLogger(getClass());	
	
	@Override
	public Integer getAttendanceCount(Attendance attendance) throws DataAccessException {
		Integer cnt = 0;
		cnt = attendanceDao.getAttendanceCount(attendance);
		logger.info("getAttendanceCount {}", cnt);	
		return cnt;
	}

	@Override
	public Attendance getAttendanceInfoBySidnum(Attendance attendance) throws DataAccessException {
		Attendance attendanceInfo = null;
		attendanceInfo = attendanceDao.getAttendanceInfoBySidnum(attendance);
		logger.info("attendanceInfo {}", attendanceInfo);	
		return attendanceInfo;
	}

	@Override
	@Transactional
	public void registerAttendance(Attendance attendance) throws DataAccessException {
		attendanceDao.insertAttendance(attendance);
	}
	
	@Override
	@Transactional
	public void modifyAttendance(Attendance attendance) throws DataAccessException {
		attendanceDao.updateAttendance(attendance);
	}

}
