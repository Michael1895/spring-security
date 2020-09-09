package com.example.springsecurity.payload.DTO;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;


public class ResetPasswordDTO {

	@NotBlank
	private String password;
		
	@NotBlank
	private String email;
		
	private String token;

	public String getEmail() {
            return email;
        }

    public void setEmail(String email) {
            this.email = email;
        }

    public String getPassword() {
			return password;
		}

    public void setPassword(String password) {
        this.password = password;
    }
	
    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
