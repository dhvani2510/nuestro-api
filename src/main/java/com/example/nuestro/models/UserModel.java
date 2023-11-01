package com.example.nuestro.models;

import com.example.nuestro.entities.User;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.shared.helpers.StringHelper;

import java.time.LocalDate;
import java.time.Period;

public class UserModel {
    public String id;
    public  String firstName;
    public  String lastName;
    public LocalDate birthDate;
    public  String email;

    private String username;

    public Integer age;
    private DatabaseType databaseType;
    public  UserModel(){}

//    public UserModel(String id, String name, String surname, LocalDate birthDay, String email, Integer age, String imageId) {
//        this.id = id;
//        this.firstName= name;
//        this.lastName= surname;
//        this.email=email;
//        this.birthDate = birthDay;
//        this.age = age==null? getAge(birthDay): age;
//        this.username= use;
//    }
    public  UserModel(User user)
    {   this.id = user.getId();
        this.firstName= user.getFirstName();
        this.lastName= user.getLastName();
        this.email=user.getEmail();
        this.birthDate = user.getBirthDate();
        this.age = age==null? getAge(birthDate): age;
        this.username= user.getUsername();
        this.databaseType=user.getDatabaseType();
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

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }
}
