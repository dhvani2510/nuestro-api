package com.example.nuestro.models.users;

import com.example.nuestro.entities.User;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.shared.helpers.StringHelper;

import java.time.LocalDate;

public class ProfileResponse
{
    private  String firstName;
    private  String lastName;
    private String image;
    private LocalDate birthDate;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public ProfileResponse(User user)
    {
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        //this.image = StringHelper.GetFileUrl(user.getImageId());
        this.birthDate = user.getBirthDate();
        //this.getDatabaseType=user.getDatabaseType();
    }
}
