package wms.service.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import core.wms.service.dao.AttendanceSeminarDao;
import core.wms.service.dao.AttendanceSeminarMgrDao;
import core.wms.service.domain.AttendanceSeminar;
import core.wms.service.domain.AttendanceSeminarMgr;
import core.wms.service.domain.AttendanceSeminarSearchParam;

import java.util.HashMap;
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
    public Integer getAttendanceSeminarCount(AttendanceSeminarSearchParam searchParam) throws DataAccessException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", searchParam.getUUID());
        param.put("attdDate", searchParam.getAttdDate());
        param.put("opt", searchParam.getOpt());
        param.put("keyword", searchParam.getKeyword());
        Integer cnt = attendanceSeminarDao.getAttendanceSeminarCount(param);
        logger.info("getAttendanceSeminarCount {}", cnt);
        return cnt;
    }

    @Override
    public List<?> getAttendanceSeminarList(AttendanceSeminarSearchParam searchParam) throws DataAccessException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", searchParam.getUUID());
        param.put("attdDate", searchParam.getAttdDate());
        param.put("opt", searchParam.getOpt());
        param.put("keyword", searchParam.getKeyword());

        param.put("firstItemNo", searchParam.getFirstItemNo());
        param.put("pageSize", searchParam.getPageSize());
        param.put("page", searchParam.getPage());
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
    public void deleteAttendanceSeminar(Map<String, Object> param) throws DataAccessException {
        attendanceSeminarDao.deleteAttendanceSeminar(param);
    }

    @Override
    public List<?> getAttendanceSeminarMgrListAll(String UUID) throws DataAccessException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("UUID", UUID);
        List<?> list = attendanceSeminarMgrDao.getAttendanceSeminarMgrListAll(param);
        logger.info("getAttendanceSeminarMgrListAll {}", list);
        return list;
    }

    @Override
    @Transactional
    public void registerAttendanceSeminarMgr(AttendanceSeminarMgr vo) {
        attendanceSeminarMgrDao.insertAttendanceSeminarMgr(vo);
    }

    @Override
    @Transactional
    public void deleteAttendanceSeminarMgr(Map<String, Object> param) {
        attendanceSeminarMgrDao.deleteAttendanceSeminarMsg(param);
    }


}
