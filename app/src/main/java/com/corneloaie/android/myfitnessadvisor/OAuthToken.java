package com.corneloaie.android.myfitnessadvisor;

/**
 * Created by Cornel-PC on 04/11/2017.
 */


public class OAuthToken {


    private String accessToken;


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
}
