package com.example.nuestro.models.users;

import com.example.nuestro.entities.User;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.shared.helpers.StringHelper;

import java.time.LocalDate;

public class ProfileResponse
{
    private  String id;
    private  String firstName;
    private  String lastName;
    private String email;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProfileResponse(User user)
    {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.id = user.getId();
        this.email=user.getEmail();
    }
}
