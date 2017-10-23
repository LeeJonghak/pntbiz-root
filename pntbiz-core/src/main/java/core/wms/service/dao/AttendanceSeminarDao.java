package core.wms.service.dao;

import core.wms.service.domain.AttendanceSeminar;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by nohsoo on 15. 7. 28.
 */
@Repository
public class AttendanceSeminarDao extends BaseDao {

    public Integer getAttendanceSeminarCount(Map param) throws DataAccessException {
        return (Integer) select("getAttendanceSeminarCount", param);
    }

    public List<?> getAttendanceSeminarList(Map param) throws DataAccessException {
        return list("getAttendanceSeminarList", param);
    }

    public void insertAttendanceSeminar(AttendanceSeminar attendanceSeminar) {
        this.insert("insertAttendanceSeminar", attendanceSeminar);
    }

    public void deleteAttendanceSeminar(Map<String, Object> param) {
        this.delete("deleteAttendanceSeminar", param);
    }
}
