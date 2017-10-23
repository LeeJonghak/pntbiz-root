package admin.oauth.service;

import core.admin.oauth.dao.OauthClientDao;
import core.admin.oauth.domain.OauthClient;
import framework.auth.LoginDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by nohsoo on 2015-09-24.
 */
@Service
public class OauthClientServiceImpl implements OauthClientService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OauthClientDao oauthClientDao;

    private String generateRandomKey() {
        String key = KeyGenerators.string().generateKey();
        return key;
    }

    @Override
    public OauthClient getOauthClientInfo(Map<String, Object> param) {
        OauthClient oauthClient = oauthClientDao.getOauthClientInfo(param);
        return oauthClient;
    }

    @Override
    public List<?> getOauthClientList(Map<String, Object> param) {
        List<?> oauthClient = oauthClientDao.getOauthClientList(param);
        logger.info("getOauthClientList {}", oauthClient.size());
        return oauthClient;
    }

    @Override
    public Integer getOauthClientCount(Map<String, Object> param) {
        Integer cnt = oauthClientDao.getOauthClientCount(param);
        logger.info("getOauthClientCount {}", cnt);
        return cnt;
    }

    @Override
    @Transactional
    public void registerOauthClient(LoginDetail loginDetail, OauthClient oauthClient) {
        oauthClient.setComNum(loginDetail.getCompanyNumber());
        oauthClient.setClientID(loginDetail.getUsername() + "." + generateRandomKey());
        oauthClient.setClientSecret(generateRandomKey());
        oauthClient.setScope("beacon");
        oauthClient.setAccessTokenValidity(43200);
        oauthClient.setRefreshTokenValidity(2592000);
        oauthClient.setAuthorities("ROLE_OAUTH_CLIENT");
        oauthClient.setResourceIDs("beacon_api");

        oauthClientDao.insertOauthClient(oauthClient);
    }

    @Override
    @Transactional
    public void modifyOauthClient(OauthClient oauthClient) {

        oauthClientDao.updateOauthClient(oauthClient);
    }

    @Override
    @Transactional
    public void removeOauthClient(OauthClient oauthClient) {

        oauthClientDao.deleteOauthClient(oauthClient);
    }
}
