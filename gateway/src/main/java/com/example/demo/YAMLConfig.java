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
    private String JWT_header;
    private String JWT_header_prefix;
    private String JWT_subject;
    private int JWT_valid_time;
    private List<String> auth_free_list = new ArrayList<>();
    private String authentication_url;


    public String getJWT_key() {
        return JWT_key;
    }

    public List<String> getAuth_free_list() {
        return auth_free_list;
    }

    public String getJWT_header() {
        return JWT_header;
    }

    public void setJWT_header(String JWT_header) {
        this.JWT_header = JWT_header;
    }

    public String getJWT_header_prefix() {
        return JWT_header_prefix;
    }

    public void setJWT_header_prefix(String JWT_header_prefix) {
        this.JWT_header_prefix = JWT_header_prefix;
    }

    public String getJWT_subject() {
        return JWT_subject;
    }

    public void setJWT_subject(String JWT_subject) {
        this.JWT_subject = JWT_subject;
    }

    public int getJWT_valid_time() {
        return JWT_valid_time;
    }

    public void setJWT_valid_time(int JWT_valid_time) {
        this.JWT_valid_time = JWT_valid_time;
    }

    public void setAuth_free_list(List<String> auth_free_list) {
        this.auth_free_list = auth_free_list;
    }

    public void setJWT_key(String JWT_key) {
        this.JWT_key = JWT_key;
    }


    public String getAuthentication_url() {
        return authentication_url;
    }

    public void setAuthentication_url(String authentication_url) {
        this.authentication_url = authentication_url;
    }
}