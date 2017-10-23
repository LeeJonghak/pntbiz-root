package core.admin.oauth.dao;

import core.admin.oauth.domain.OauthAccessToken;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by nohsoo on 2015-09-25.
 */
@Repository
public class OauthAccessTokenDao extends BaseDao {

    public OauthAccessToken getOauthAccessTokenInfo(Map<String, Object> param) throws DataAccessException {
        return (OauthAccessToken)select("getOauthAccessTokenInfo", param);
    }

    public Integer getOauthAccessTokenCount(Map<String, Object> param) throws DataAccessException {
        return (Integer) select("getOauthAccessTokenCount", param);
    }

    public List<?> getOauthAccessTokenList(Map<String, Object> param) throws DataAccessException {
        return (List<?>) list("getOauthAccessTokenList", param);
    }

    public void insertOauthAccessToken(OauthAccessToken oauthAccessToken) {
        this.insert("insertOauthAccessToken", oauthAccessToken);
    }

    public void updateOauthAccessToken(OauthAccessToken oauthAccessToken) {
        this.update("modifyOauthAccessToken", oauthAccessToken);
    }

    public void deleteOauthAccessToken(OauthAccessToken oauthAccessToken) {
        this.delete("deleteOauthAccessToken", oauthAccessToken);
    }

}
