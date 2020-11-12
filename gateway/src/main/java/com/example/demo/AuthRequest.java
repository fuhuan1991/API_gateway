package com.example.demo;

/*
    this class defines a request entity that will be sent to authentication service.
 */
public class AuthRequest {
    private String api_key;

    public AuthRequest(String api_key) {
        this.api_key = api_key;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }
}
