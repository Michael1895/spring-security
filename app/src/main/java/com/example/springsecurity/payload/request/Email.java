package com.example.springsecurity.payload.request;

import javax.validation.constraints.NotBlank;

public class Email {

    @NotBlank
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
