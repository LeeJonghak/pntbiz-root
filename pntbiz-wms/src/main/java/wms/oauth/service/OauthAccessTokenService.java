package wms.oauth.service;

import wms.component.auth.LoginDetail;
import core.wms.oauth.domain.OauthAccessToken;

import java.util.List;
import java.util.Map;

/**
 * Created by nohsoo on 2015-09-25.
 */
public interface OauthAccessTokenService {

    public OauthAccessToken getOauthAccessTokenInfo(Map<String, Object> param);
    public List<?> getOauthAccessTokenList(Map<String, Object> param);
    public Integer getOauthAccessTokenCount(Map<String, Object> param);

    public void registerOauthAccessToken(LoginDetail loginDetail, OauthAccessToken oauthAccessToken);
    public void modifyOauthAccessToken(OauthAccessToken oauthAccessToken);
    public void removeOauthAccessToken(OauthAccessToken oauthAccessToken);

}
