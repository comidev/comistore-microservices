package comidev.apigateway.dto;

public class Tokens {
    private String token_access;
    private String refresh_token;

    public String getToken_access() {
        return token_access;
    }
    public void setToken_access(String token_access) {
        this.token_access = token_access;
    }
    public String getRefresh_token() {
        return refresh_token;
    }
    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
