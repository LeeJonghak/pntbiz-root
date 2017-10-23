package admin.user.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import core.admin.user.dao.UserInfoDao;
import core.admin.user.domain.UserInfo;

import java.util.List;

/**
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoDao userInfoDao;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Integer getUserInfoCount(UserInfo param) throws DataAccessException {
        Integer cnt = userInfoDao.getUserInfoCount(param);
        logger.info("getUserInfoCount {}", cnt);
        return cnt;
    }

    @Override
    public List<?> getUserInfoList(UserInfo param) throws DataAccessException {
        List<?> userList = null;
        userList = userInfoDao.getUserInfoList(param);
        logger.info("getUserInfoList {}", userList.size());
        return userList;
    }

    @Override
    public UserInfo getUserInfo(UserInfo param) throws DataAccessException {
        UserInfo userInfo = userInfoDao.getUserInfoOne(param);
        return userInfo;
    }
}
