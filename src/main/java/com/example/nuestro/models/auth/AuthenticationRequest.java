package com.example.nuestro.models.auth;

public class AuthenticationRequest {
    private  String Email;
    private  String Password;


    public AuthenticationRequest(String email, String password) {
        Email = email;
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }

    public class AuthenticationRequestBuilder {
        private String email;
        private String password;

        public AuthenticationRequestBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public AuthenticationRequestBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public AuthenticationRequest createAuthenticationRequest() //build
        {
            return new AuthenticationRequest(email, password);
        }
    }
}
