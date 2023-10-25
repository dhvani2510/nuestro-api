package com.example.nuestro.models;

public class UserContext
{
    private  Long id;
    private  String firstName;
    private  String lastName;
    private  String email;

    public UserContext(Long id, String name, String surname, String email) {
    	this.id = id;
        this.firstName= name;
        this.lastName= surname;
        this.email=email;
    }
}
