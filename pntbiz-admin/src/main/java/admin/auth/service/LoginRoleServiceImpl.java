package admin.auth.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import core.admin.auth.dao.LoginRoleAuthoritiesDao;
import core.admin.auth.dao.LoginRoleDao;
import core.admin.auth.domain.LoginAuthcode;
import core.admin.auth.domain.LoginRole;
import core.admin.auth.domain.LoginRoleAuthorities;
import framework.web.util.DateUtil;
import framework.web.util.PagingParam;

@Service
public class LoginRoleServiceImpl implements LoginRoleService {

    @Autowired
    private LoginRoleDao loginRoleDao;

    @Autowired
    private LoginAuthcodeService loginAuthcodeService;

    @Autowired
    private LoginRoleAuthoritiesDao loginRoleAuthoritiesDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Integer getLoginRoleCount(PagingParam param) throws DataAccessException {
        Integer cnt = loginRoleDao.getLoginRoleCount(param);
        logger.info("getLoginRoleCount {}", cnt);
        return cnt;
    }

    @Override
    public List<?> getLoginRoleList(PagingParam param) throws DataAccessException {
        List<?> list = loginRoleDao.getLoginRoleList(param);
        logger.info("getLoginRoleList {}", list.size());
        return list;
    }

    @Override
    public List<?> bindLoginRoleList(List<?> list) throws ParseException {
        List<LoginRole> clist = new ArrayList<LoginRole>();
        for(int i=0; i<list.size(); i++) {
            LoginRole item = (LoginRole) list.get(i);
            String date = DateUtil.timestamp2str(Long.parseLong(item.getRegDate()));
            date = DateUtil.changeStrDateFormat(date, "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
            item.setRegDate(date);
            clist.add(item);
        }
        return clist;
    }

    @Override
    public LoginRole getLoginRoleInfo(LoginRole loginRole) throws DataAccessException {
        LoginRole item = loginRoleDao.getLoginRoleInfo(loginRole);
        logger.info("getLoginRoleInfo {}", item);

        return item;
    }

    /**
     * 역할 생성
     * 생성시 역할에대한 권한을 함께 생성한다
     * 2014-11-18 nohsoo
     *
     * @param loginRole 역할정보
     * @param authNums 권한번호들
     * @throws Exception
     */
    @Override
    @Transactional
    public void registerLoginRole(LoginRole loginRole, int[] authNums) throws Exception {

        int authCount = 0;
        if(authNums!=null) authCount = authNums.length;

        /**
         * 역할(LoginRole)을 등록하고 등록키를 반환받는다.
         */
        loginRole.setAuthCount(authCount);
        int roleNum = loginRoleDao.insertLoginRole(loginRole);

        for(int i=0; i<authCount; i++) {
            int authNum = authNums[i];
            LoginAuthcode loginAuthcode = new LoginAuthcode();
            loginAuthcode.setAuthNum(authNum);
            loginAuthcode = loginAuthcodeService.getLoginAuthcodeInfo(loginAuthcode);
            /*
            if(loginAuthcode==null) {
                throw new Exception("Not found Authcode authNum:"+authNum);
            }
            */

            LoginRoleAuthorities loginRoleAuthorities = new LoginRoleAuthorities();
            loginRoleAuthorities.setRoleNum(roleNum);
            loginRoleAuthorities.setAuthNum(authNum);
            loginRoleAuthorities.setAuthCode(loginAuthcode.getAuthCode());
            loginRoleAuthorities.setAuthName(loginAuthcode.getAuthName());
            loginRoleAuthoritiesDao.insertLoginRoleAuthorities(loginRoleAuthorities);
        }
    }

    @Override
    @Transactional
    public void modifyLoginRole(LoginRole loginRole, int[] authNums) throws Exception {

        loginRole.setAuthCount(authNums.length);
        loginRoleDao.updateLoginRole(loginRole);
        loginRoleAuthoritiesDao.clearLoginRoleAuthorities(loginRole);
        for(int i=0; i<authNums.length; i++) {
            int authNum = authNums[i];
            LoginAuthcode loginAuthcode = new LoginAuthcode();
            loginAuthcode.setAuthNum(authNum);
            loginAuthcode = loginAuthcodeService.getLoginAuthcodeInfo(loginAuthcode);
            if(loginAuthcode==null) {
                throw new Exception("Not found Authcode authNum:"+authNum);
            }

            LoginRoleAuthorities loginRoleAuthorities = new LoginRoleAuthorities();
            loginRoleAuthorities.setRoleNum(loginRole.getRoleNum());
            loginRoleAuthorities.setAuthNum(authNum);
            loginRoleAuthorities.setAuthCode(loginAuthcode.getAuthCode());
            loginRoleAuthorities.setAuthName(loginAuthcode.getAuthName());
            loginRoleAuthoritiesDao.insertLoginRoleAuthorities(loginRoleAuthorities);
        }
    }

    @Override
    @Transactional
    public void removeLoginRole(LoginRole loginRole) throws DataAccessException {
        loginRoleDao.deleteLoginRole(loginRole);

        /**
         * 역할이 삭제되면 맵핑된 권한정보도 제거
         * TB_LOGIN_ROLE_AUTHORITIES
         */
        loginRoleAuthoritiesDao.clearLoginRoleAuthorities(loginRole);
    }

}
