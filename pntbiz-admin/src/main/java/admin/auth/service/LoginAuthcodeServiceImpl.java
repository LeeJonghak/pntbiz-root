package admin.auth.service;

import core.admin.auth.dao.LoginAuthcodeDao;
import core.admin.auth.dao.LoginRoleAuthoritiesDao;
import core.admin.auth.domain.LoginAuthcode;
import framework.web.util.DateUtil;
import framework.web.util.PagingParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * 권한코드(LoginAuthcode)
 * 2014-11-18 nohsoo
 */
@Service
public class LoginAuthcodeServiceImpl implements LoginAuthcodeService {

    @Autowired
    private LoginAuthcodeDao loginAuthcodeDao;

    @Autowired
    private LoginRoleAuthoritiesDao loginRoleAuthoritiesDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Integer getLoginAuthcodeCheck(String authCode) throws DataAccessException{
        LoginAuthcode loginAuthcode = new LoginAuthcode();
        loginAuthcode.setAuthCode(authCode);

        Integer cnt = loginAuthcodeDao.getLoginAuthcodeCheck(loginAuthcode);
        logger.info("getLoginAuthcodeCheck {}", cnt);
        return cnt;
    }

    @Override
    public Integer getLoginAuthcodeCount(PagingParam param) throws DataAccessException {
        Integer cnt = loginAuthcodeDao.getLoginAuthcodeCount(param);
        logger.info("getLoginAuthcodeCount {}", cnt);
        return cnt;
    }

    @Override
    public List<?> getLoginAuthcodeList(PagingParam param) throws DataAccessException {
        List<?> list = loginAuthcodeDao.getLoginAuthcodeList(param);
        logger.info("getLoginAuthcodeList {}", list.size());
        return list;
    }

    @Override
    public List<?> getLoginAuthcodeListAll() throws DataAccessException {
        List<?> list = loginAuthcodeDao.getLoginAuthcodeListAll();
        logger.info("getLoginAuthcodeListAll {}", list.size());
        return list;
    }

    @Override
    public List<?> bindLoginAuthcodeList(List<?> list) throws ParseException {
        List<LoginAuthcode> clist = new ArrayList<LoginAuthcode>();
        for(int i=0; i<list.size(); i++) {
            LoginAuthcode item = (LoginAuthcode) list.get(i);
            String date = DateUtil.timestamp2str(Long.parseLong(item.getRegDate()));
            date = DateUtil.changeStrDateFormat(date, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
            item.setRegDate(date);
            clist.add(item);
        }
        return clist;
    }

    @Override
    public LoginAuthcode getLoginAuthcodeInfo(LoginAuthcode loginAuthcode) throws DataAccessException {
        LoginAuthcode item = loginAuthcodeDao.getLoginAuthcodeInfo(loginAuthcode);
        logger.info("getLoginAuthcodeInfo {}", item);

        return item;
    }

    @Override
    @Transactional
    public void registerLoginAuthcode(LoginAuthcode loginAuthcode) throws DataAccessException {
        loginAuthcodeDao.insertLoginAuthcode(loginAuthcode);
    }

    @Override
    @Transactional
    public void modifyLoginAuthcode(LoginAuthcode loginAuthcode) throws DataAccessException {
        loginAuthcodeDao.updateLoginAuthcode(loginAuthcode);

        /**
         * TB_LOGIN_ROLE_AUTHORITIES 테이블의 권한코드와 권한명을 함께 수정
         */
        loginRoleAuthoritiesDao.updateLoginRoleAuthoritiesAll(loginAuthcode);
    }



    @Override
    @Transactional
    public void removeLoginAuthcode(LoginAuthcode loginAuthcode) throws DataAccessException {
        /**
         * 권한정보 제거
         */
        loginAuthcodeDao.deleteLoginAuthcode(loginAuthcode);

        /**
         * 권한이 삭제되면 TB_LOGIN_ROLE_AUTHORITIES 테이블의 권한정보도 함께 제거
         */
        loginRoleAuthoritiesDao.deleteLoginRoleAuthoritiesAll(loginAuthcode);
    }
}
