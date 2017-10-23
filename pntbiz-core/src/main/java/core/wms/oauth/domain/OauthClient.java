package core.wms.oauth.domain;

/**
 * Table: TB_OAUTH_CLIENT
 */
public class OauthClient {

    private Integer comNum;
    private String clientID;
    private String clientSecret;
    private String grantTypes;
    private String authorities;
    private String scope;
    private String resourceIDs;
    private String redirectUri;
    private String additionalInformation;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    private String  memo;
    private Integer modDate;
    private Integer regDate;

    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getGrantTypes() {
        return grantTypes;
    }

    public void setGrantTypes(String grantTypes) {
        this.grantTypes = grantTypes;
    }

    public String getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String authorities) {
        this.authorities = authorities;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getResourceIDs() {
        return resourceIDs;
    }

    public void setResourceIDs(String resourceIDs) {
        this.resourceIDs = resourceIDs;
    }

    public String getRedirectUri() {
        return redirectUri;
    }

    public void setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public Integer getAccessTokenValidity() {
        return accessTokenValidity;
    }

    public void setAccessTokenValidity(Integer accessTokenValidity) {
        this.accessTokenValidity = accessTokenValidity;
    }

    public Integer getRefreshTokenValidity() {
        return refreshTokenValidity;
    }

    public void setRefreshTokenValidity(Integer refreshTokenValidity) {
        this.refreshTokenValidity = refreshTokenValidity;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getModDate() {
        return modDate;
    }

    public void setModDate(Integer modDate) {
        this.modDate = modDate;
    }

    public Integer getRegDate() {
        return regDate;
    }

    public void setRegDate(Integer regDate) {
        this.regDate = regDate;
    }
}
