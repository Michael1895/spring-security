package com.example.springsecurity.payload.DTO;

import javax.validation.constraints.NotBlank;

public class ConfirmationDTO {

        @NotBlank
        private String email;
        @NotBlank
        private String token;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

    }

