package com.example.nuestro.models.auth;

public class AuthenticationResponse
{
    private  String Token;

    public AuthenticationResponse(String token) {
        Token = token;
    }

    public String getToken() {
        return Token;
    }

    public static class  Builder{
        private String token;
        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public AuthenticationResponse build() {
            return new AuthenticationResponse(token);
        }
    }

}
