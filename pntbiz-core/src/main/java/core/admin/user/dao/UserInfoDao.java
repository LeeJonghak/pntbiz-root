package core.admin.user.dao;

import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;
import core.admin.user.domain.UserInfo;

import java.util.List;

/**
 */
@Repository
public class UserInfoDao extends BaseDao {

    public Integer getUserInfoCount(UserInfo param) throws DataAccessException {
        return (Integer) select("getUserInfoCount", param);
    }

    public List<?> getUserInfoList(UserInfo param) throws DataAccessException {
        return (List<?>) list("getUserInfoList", param);
    }

    public UserInfo getUserInfoOne(UserInfo param) throws DataAccessException {
        return (UserInfo) select("getUserInfoOne", param);
    }

    public UserInfo getUserInfoByID(UserInfo param) throws DataAccessException {
        return (UserInfo) select("getUserInfoByID", param);
    }

}
