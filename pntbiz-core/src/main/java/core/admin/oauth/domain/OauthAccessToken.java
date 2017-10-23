package core.admin.oauth.domain;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;

/**
 * Created by nohsoo on 2015-09-25.
 */
public class OauthAccessToken {

    private String tokenID;

    private byte[] token; // OAuth2AccessToken

    private String userID;

    private String clientID;

    private byte[] authentication; // OAuth2Authentication

    private String authenticationID;

    private byte[] refreshToken; // OAuth2RefreshToken

    private Integer regDate;

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public byte[] getToken() {
        return token;
    }

    public void setToken(byte[] token) {
        this.token = token;
    }

    public OAuth2AccessToken getTokenObject() {
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(this.token);
            ObjectInput in = new ObjectInputStream(bis);
            OAuth2AccessToken oAuth2AccessToken = (OAuth2AccessToken)in.readObject();
            return oAuth2AccessToken;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public byte[] getAuthentication() {
        return authentication;
    }

    public void setAuthentication(byte[] authentication) {
        this.authentication = authentication;
    }

    public void setRefreshToken(byte[] refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getAuthenticationID() {
        return authenticationID;
    }

    public void setAuthenticationID(String authenticationID) {
        this.authenticationID = authenticationID;
    }

    public byte[] getRefreshToken() {
        return refreshToken;
    }

    public Integer getRegDate() {
        return regDate;
    }

    public void setRegDate(Integer regDate) {
        this.regDate = regDate;
    }
}
