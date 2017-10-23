package admin.oauth.service;

import framework.auth.LoginDetail;
import core.admin.oauth.domain.OauthClient;

import java.util.List;
import java.util.Map;

/**
 * Created by nohsoo on 2015-09-24.
 */
public interface OauthClientService {

    public OauthClient getOauthClientInfo(Map<String, Object> param);
    public List<?> getOauthClientList(Map<String, Object> param);
    public Integer getOauthClientCount(Map<String, Object> param);

    public void registerOauthClient(LoginDetail loginDetail, OauthClient oauthClient);
    public void modifyOauthClient(OauthClient oauthClient);
    public void removeOauthClient(OauthClient oauthClient);

}
