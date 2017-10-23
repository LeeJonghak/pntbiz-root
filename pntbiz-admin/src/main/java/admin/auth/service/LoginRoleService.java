package admin.auth.service;

import java.text.ParseException;
import java.util.List;

import core.admin.auth.domain.LoginRole;
import org.springframework.dao.DataAccessException;

import framework.web.util.PagingParam;

/**
 * Created by Administrator on 2014-11-18.
 */
public interface LoginRoleService {

    public Integer getLoginRoleCount(PagingParam param) throws DataAccessException;
    public List<?> getLoginRoleList(PagingParam param) throws DataAccessException;
    public List<?> bindLoginRoleList(List<?> list) throws ParseException;
    public LoginRole getLoginRoleInfo(LoginRole loginRole) throws DataAccessException;

    public void registerLoginRole(LoginRole loginRole, int[] authNums) throws Exception;
    public void modifyLoginRole(LoginRole loginRole, int[] authNums) throws Exception;
    public void removeLoginRole(LoginRole loginRole) throws DataAccessException;

}
