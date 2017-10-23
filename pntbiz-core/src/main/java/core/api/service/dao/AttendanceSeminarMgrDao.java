package core.api.service.dao;

import core.api.service.domain.AttendanceSeminarMgr;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 */
@Repository
public class AttendanceSeminarMgrDao extends BaseDao {

    public void insertAttendanceSeminarMgr(AttendanceSeminarMgr vo) {
        insert("insertAttendanceSeminarMgr", vo);
    }

    public void deleteAttendanceSeminarMsg(Map<String, Object> param) {
        delete("deleteAttendanceSeminarMgr", param);
    }

    public List<?> getAttendanceSeminarMgrListAll(Map param) throws DataAccessException {
        return list("getAttendanceSeminarMgrListAll", param);
    }

    public AttendanceSeminarMgr getAttendanceSeminarMgrInfo(Map param) throws DataAccessException {
        return (AttendanceSeminarMgr)select("getAttendanceSeminarMgrInfo", param);
    }

}
