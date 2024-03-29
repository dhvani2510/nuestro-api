package com.example.nuestro.models.auth;

public class RegisterRequest {
    private  String firstName;
    private  String lastName;

    private  String Email;
    private  String Password;

    public RegisterRequest(String name, String surname, String email, String password) {
        firstName = name;
        lastName = surname;
        Email = email;
        Password = password;
    }


    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPassword(String password) {
        Password = password;
    }
    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return Email;
    }

    public String getPassword() {
        return Password;
    }
}
