package com.corneloaie.android.myfitnessadvisor;

import java.io.Serializable;

/**
 * Created by Cornel-PC on 04/11/2017.
 */


public class OAuthTokenAndId implements Serializable {


    private String accessToken;
    private String userID;
    private String tokenType;
    private long expireTimeInSeconds;

    public OAuthTokenAndId() {

    }

    public OAuthTokenAndId(String accessToken, String userID, String tokenType, long expireTimeInSeconds) {
        this.accessToken = accessToken;
        this.userID = userID;
        this.tokenType = tokenType;
        this.expireTimeInSeconds = expireTimeInSeconds;
    }

    public long getExpireTimeInSeconds() {
        return expireTimeInSeconds;
    }

    public void setExpireTimeInSeconds(long expireTimeInSeconds) {
        this.expireTimeInSeconds = expireTimeInSeconds;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getAuthorization() {
        return getTokenType() + " " + getAccessToken();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
