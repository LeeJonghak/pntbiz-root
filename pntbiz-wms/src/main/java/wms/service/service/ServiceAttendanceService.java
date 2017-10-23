package wms.service.service;

import org.springframework.dao.DataAccessException;
import core.wms.service.domain.AttendanceSearchParam;

import java.util.List;

public interface ServiceAttendanceService {
	public Integer getAttendanceCount(AttendanceSearchParam param) throws DataAccessException;
	public List<?> getAttendanceList(AttendanceSearchParam param) throws DataAccessException;
	public void deleteAttendance(AttendanceSearchParam param) throws DataAccessException;
}