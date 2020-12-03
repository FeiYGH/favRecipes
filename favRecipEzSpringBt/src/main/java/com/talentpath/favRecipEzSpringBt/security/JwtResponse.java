package com.talentpath.favRecipEzSpringBt.security;

import java.util.List;

public class JwtResponse {
    private String token;
    private String type="Bearer";

    private Integer id;

    private String userName;
    private String email;
    private List<String> roles;

    public JwtResponse(String token, Integer id, String username, String email, List<String> roles){
        this.token = token;
        this.id= id;
        this.userName = username;
        this.email = email;
        this.roles = roles;
    }

    //TODO:
    public JwtResponse(){};

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
