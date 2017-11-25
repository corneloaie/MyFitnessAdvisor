package com.corneloaie.android.myfitnessadvisor;

/**
 * Created by Cornel-PC on 04/11/2017.
 */


public class OAuthTokenAndId {


    private String accessToken;
    private String userID;
    private String tokenType;

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
