package com.example.nuestro.models.users;

import com.example.nuestro.entities.datatypes.DatabaseType;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public class ProfileRequest
{
    private  String firstName;
    private  String lastName;
    private MultipartFile image;
    private LocalDate birthDate;
    private DatabaseType databaseType;

   public ProfileRequest(String firstName, String lastName, MultipartFile image, LocalDate birthDay)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.image = image;
        this.birthDate = birthDay;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }
}
