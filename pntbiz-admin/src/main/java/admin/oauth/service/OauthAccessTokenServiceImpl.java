package admin.oauth.service;

import core.admin.oauth.dao.OauthAccessTokenDao;
import core.admin.oauth.domain.OauthAccessToken;
import framework.auth.LoginDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by nohsoo on 2015-09-25.
 */
@Service
public class OauthAccessTokenServiceImpl implements OauthAccessTokenService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private OauthAccessTokenDao oauthAccessTokenDao;

    @Override
    public OauthAccessToken getOauthAccessTokenInfo(Map<String, Object> param) {
        OauthAccessToken oauthAccessToken = oauthAccessTokenDao.getOauthAccessTokenInfo(param);
        logger.info("getOauthAccessTokenInfo {}", oauthAccessToken.getTokenID());
        return oauthAccessToken;
    }

    @Override
    public List<?> getOauthAccessTokenList(Map<String, Object> param) {
        List<?> list = oauthAccessTokenDao.getOauthAccessTokenList(param);
        logger.info("getOauthAccessTokenList {}", list.size());
        return list;
    }

    @Override
    public Integer getOauthAccessTokenCount(Map<String, Object> param) {
        Integer cnt = oauthAccessTokenDao.getOauthAccessTokenCount(param);
        logger.info("getOauthAccessTokenCount {}", cnt);
        return cnt;
    }

    @Override
    @Transactional
    public void registerOauthAccessToken(LoginDetail loginDetail, OauthAccessToken oauthAccessToken) {
    }

    @Override
    @Transactional
    public void modifyOauthAccessToken(OauthAccessToken oauthAccessToken) {
    }

    @Override
    @Transactional
    public void removeOauthAccessToken(OauthAccessToken oauthAccessToken) {
    }
}
