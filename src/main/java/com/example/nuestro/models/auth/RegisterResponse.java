package com.example.nuestro.models.auth;

public class RegisterResponse {
    private  String Id;
    private  String firstName;
    private  String lastName;
    private  String Email;

    public RegisterResponse(String id, String name, String surname, String email) {
        Id = id;
        firstName = name;
        lastName = surname;
        Email = email;
    }

    public String getId() {
        return Id;
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
}
