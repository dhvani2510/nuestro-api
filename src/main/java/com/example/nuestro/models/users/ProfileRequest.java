package com.example.nuestro.models.users;

import com.example.nuestro.entities.datatypes.DatabaseType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class ProfileRequest
{
    private  String firstName;
    private  String lastName;

   public ProfileRequest(String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
    }

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
}
