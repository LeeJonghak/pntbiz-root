package api.service.service;

import core.api.service.domain.AttendanceSeminar;
import core.api.service.domain.AttendanceSeminarMgr;
import org.springframework.dao.DataAccessException;

import java.util.List;
import java.util.Map;

public interface AttendanceSeminarService {
		
	public Integer getAttendanceSeminarCount(Map param) throws DataAccessException;
	public List<?> getAttendanceSeminarList(Map param) throws DataAccessException;
	public void registerAttendanceSeminar(AttendanceSeminar attendanceSeminar) throws DataAccessException;
    public void registerAttendanceSeminarMgr(AttendanceSeminarMgr vo) throws DataAccessException;
    public void deleteAttendanceSeminarMsg(Map<String, Object> param) throws DataAccessException;
    public AttendanceSeminarMgr getAttendanceSeminarMsgInfo(Map<String, Object> param) throws DataAccessException;
}