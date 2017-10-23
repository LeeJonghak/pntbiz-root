package framework.oauth;

import core.api.oauth.domain.OauthClient;
import framework.web.util.QueryParam;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.ClientRegistrationException;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * Created by nohsoo on 2015-09-07.
 */
public class OauthClientDetailsService implements ClientDetailsService {


    private SqlSessionTemplate sqlSessionTemplate = null;

    public void setSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {
        this.sqlSessionTemplate = sqlSessionTemplate;
    }

    public ClientDetails loadClientByClientId(String clientId) throws ClientRegistrationException {

        if(sqlSessionTemplate!=null) {
            OauthClient clientVo = sqlSessionTemplate.selectOne("OauthClientDao.getOauthClientInfo", QueryParam.create("clientID", clientId).build());

            OauthClientDetails clientDetail = new OauthClientDetails();
            clientDetail.setClientId(clientVo.getClientID());
            clientDetail.setClientSecret(clientVo.getClientSecret());
            LinkedHashSet<GrantedAuthority> authorities = new LinkedHashSet<GrantedAuthority>();
            for(String grant: Arrays.asList(StringUtils.commaDelimitedListToStringArray(clientVo.getAuthorities()))) {
                authorities.add(new SimpleGrantedAuthority(grant));
            }
            clientDetail.setAuthorities(authorities);
            clientDetail.setAuthorizedGrantTypes(Arrays.asList(StringUtils.commaDelimitedListToStringArray(clientVo.getGrantTypes())));
            clientDetail.setRegisteredRedirectUri(new HashSet<String>(Arrays.asList(StringUtils.commaDelimitedListToStringArray(clientVo.getRedirectUri()))));
            clientDetail.setResourceIds(Arrays.asList(StringUtils.commaDelimitedListToStringArray(clientVo.getResourceIDs())));
            clientDetail.setScope(Arrays.asList(StringUtils.commaDelimitedListToStringArray(clientVo.getScope())));
            clientDetail.setAccessTokenValiditySeconds(clientVo.getAccessTokenValidity());
            clientDetail.setRefreshTokenValiditySeconds(clientVo.getRefreshTokenValidity());
            return clientDetail;
        }

        return null;
    }
}
