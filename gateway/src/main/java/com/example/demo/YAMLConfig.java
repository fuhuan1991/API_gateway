package com.example.demo;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 *  Through this class, we can get properties from configuration file
 */
@EnableConfigurationProperties
@ConfigurationProperties
public class YAMLConfig {

    private String JWT_key;
    private List<String> authFree = new ArrayList<>();

    public String getJWT_key() {
        return JWT_key;
    }

    public List<String> getAuthFree() {
        return authFree;
    }

    public void setJWT_key(String JWT_key) {
        this.JWT_key = JWT_key;
    }

    public void setAuthFree(List<String> authFree) {
        this.authFree = authFree;
    }
}