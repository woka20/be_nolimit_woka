package com.woka.mysqlproject.response;

public class LoginResponse {

    private String jwt;
    private long expiresIn;


    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getJwt() {
        return this.jwt;
    }
    public void setExpiresIn(long expiresIn){
        this.expiresIn=expiresIn;
    }

    public Long getExpiresIn() {
        return this.expiresIn;
    }

}