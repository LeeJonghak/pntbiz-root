package wms.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import core.wms.service.dao.ServiceAttendanceDao;
import core.wms.service.domain.AttendanceSearchParam;

import java.util.List;

@Service
public class ServiceAttendanceServiceImpl implements ServiceAttendanceService {	
	
	@Autowired
	private ServiceAttendanceDao attendanceDao;
	
	private Logger logger = LoggerFactory.getLogger(getClass());	
	
	@Override
	public Integer getAttendanceCount(AttendanceSearchParam param) throws DataAccessException {
		Integer cnt = 0;
		cnt = attendanceDao.getAttendanceCount(param);
		logger.info("getAttendanceCount {}", cnt);	
		return cnt;
	}
	
	@Override
	public List<?> getAttendanceList(AttendanceSearchParam param) throws DataAccessException {
		List<?> list = null;
		list = attendanceDao.getAttendanceList(param);
		logger.info("getAttendanceList {}", list);	
		return list;
	}

	@Override
	public void deleteAttendance(AttendanceSearchParam param) throws DataAccessException {
		attendanceDao.deleteAttendance(param);
		logger.info("deleteAttendance {}");
	}


}
