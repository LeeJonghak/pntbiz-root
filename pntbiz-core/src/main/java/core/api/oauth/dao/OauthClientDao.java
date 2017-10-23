package core.api.oauth.dao;

import core.api.oauth.domain.OauthClient;
import framework.db.dao.BaseDao;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * Created by nohsoo on 2015-09-24.
 */
@Repository
public class OauthClientDao extends BaseDao {

    public OauthClient getOauthClientInfo(Map<String, Object> param) throws DataAccessException {
        return (OauthClient)select("getOauthClientInfo", param);
    }

    public Integer getOauthClientCount(Map<String, Object> param) throws DataAccessException {
        return (Integer) select("getOauthClientCount", param);
    }

    public List<?> getOauthClientList(Map<String, Object> param) throws DataAccessException {
        return (List<?>) list("getOauthClientList", param);
    }

    public void insertOauthClient(OauthClient oauthClient) {
        this.insert("insertOauthClient", oauthClient);
    }

    public void updateOauthClient(OauthClient oauthClient) {
        this.update("modifyOauthClient", oauthClient);
    }

    public void deleteOauthClient(OauthClient oauthClient) {
        this.delete("deleteOauthClient", oauthClient);
    }

}
