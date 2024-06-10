package com.woka.mysqlproject.request;


public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return this.username;
    }

    public Object getPassword() {
        return this.password;
    }

    public void setUsername( String username){
        this.username=username;
    }
    public void setPassword(String password){
        this.password=password;
    }
}
