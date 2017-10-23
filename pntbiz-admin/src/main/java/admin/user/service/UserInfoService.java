package admin.user.service;

import org.springframework.dao.DataAccessException;
import core.admin.user.domain.UserInfo;

import java.util.List;

/**
 */
public interface UserInfoService {

    public Integer getUserInfoCount(UserInfo param) throws DataAccessException;
    public List<?> getUserInfoList(UserInfo param) throws DataAccessException;
    public UserInfo getUserInfo(UserInfo param) throws DataAccessException;

}
