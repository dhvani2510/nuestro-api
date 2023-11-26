package com.example.nuestro.models;

import com.example.nuestro.entities.User;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.models.users.UpdateDatabaseResponse;
import com.example.nuestro.shared.helpers.StringHelper;

import java.time.LocalDate;
import java.time.Period;

public class UserModel {
    public String id;
    public  String firstName;
    public  String lastName;

    public  String email;

    private String username;

    private UpdateDatabaseResponse database;
    public  UserModel(){}

    public  UserModel(User user) {   this.id = user.getId();
        this.firstName= user.getFirstName();
        this.lastName= user.getLastName();
        this.email=user.getEmail();
        this.username= user.getUsername();

        try{
            this.database= new UpdateDatabaseResponse(user, true);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        };

    }

    private  Integer getAge(LocalDate birthDay){

        if(birthDay==null)
            return null;
        Period age= Period.between(birthDay, LocalDate.now());
        return age.getYears();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UpdateDatabaseResponse getDatabase() {
        return database;
    }

    public void setDatabase(UpdateDatabaseResponse database) {
        this.database = database;
    }
}
