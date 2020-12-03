package com.talentpath.favRecipEzSpringBt.security;

import javax.validation.constraints.NotBlank;

//view model to hold incoming data, so don't have to do @Entity and @Table
public class LoginRequest {
    @NotBlank
    private  String username;

    @NotBlank
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
