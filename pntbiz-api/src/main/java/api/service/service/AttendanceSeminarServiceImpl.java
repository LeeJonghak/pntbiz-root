package api.service.service;

import core.api.service.dao.AttendanceSeminarDao;
import core.api.service.dao.AttendanceSeminarMgrDao;
import core.api.service.domain.AttendanceSeminar;
import core.api.service.domain.AttendanceSeminarMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by nohsoo on 15. 7. 28.
 */
@Service
public class AttendanceSeminarServiceImpl implements AttendanceSeminarService {

    @Autowired
    private AttendanceSeminarDao attendanceSeminarDao;

    @Autowired
    private AttendanceSeminarMgrDao attendanceSeminarMgrDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Integer getAttendanceSeminarCount(Map param) throws DataAccessException {
        Integer cnt = attendanceSeminarDao.getAttendanceSeminarCount(param);
        logger.info("getAttendanceSeminarCount {}", cnt);
        return cnt;
    }

    @Override
    public List<?> getAttendanceSeminarList(Map param) throws DataAccessException {
        List<?> list = attendanceSeminarDao.getAttendanceSeminarList(param);
        logger.info("getAttendanceSeminarList {}", list);
        return list;
    }

    @Override
    @Transactional
    public void registerAttendanceSeminar(AttendanceSeminar attendanceSeminar) throws DataAccessException {
        attendanceSeminarDao.insertAttendanceSeminar(attendanceSeminar);
        logger.info("registerAttendanceSeminar");
    }

    @Override
    @Transactional
    public void registerAttendanceSeminarMgr(AttendanceSeminarMgr vo) {
        attendanceSeminarMgrDao.insertAttendanceSeminarMgr(vo);
    }

    @Override
    @Transactional
    public void deleteAttendanceSeminarMsg(Map<String, Object> param) {
        attendanceSeminarMgrDao.deleteAttendanceSeminarMsg(param);
    }

    public AttendanceSeminarMgr getAttendanceSeminarMsgInfo(Map<String, Object> param) {
        return attendanceSeminarMgrDao.getAttendanceSeminarMgrInfo(param);
    }
}
