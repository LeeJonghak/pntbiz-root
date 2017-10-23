package wms.service.service;

import org.springframework.dao.DataAccessException;
import core.wms.service.domain.AttendanceSeminar;
import core.wms.service.domain.AttendanceSeminarMgr;
import core.wms.service.domain.AttendanceSeminarSearchParam;

import java.util.List;
import java.util.Map;

public interface AttendanceSeminarService {
		
	public Integer getAttendanceSeminarCount(AttendanceSeminarSearchParam param) throws DataAccessException;
	public List<?> getAttendanceSeminarList(AttendanceSeminarSearchParam param) throws DataAccessException;
	public void registerAttendanceSeminar(AttendanceSeminar attendanceSeminar) throws DataAccessException;
    public void deleteAttendanceSeminar(Map<String, Object> param) throws DataAccessException;

    public List<?> getAttendanceSeminarMgrListAll(String UUID) throws DataAccessException;
    public void registerAttendanceSeminarMgr(AttendanceSeminarMgr vi) throws DataAccessException;
    public void deleteAttendanceSeminarMgr(Map<String, Object> param) throws DataAccessException;
}