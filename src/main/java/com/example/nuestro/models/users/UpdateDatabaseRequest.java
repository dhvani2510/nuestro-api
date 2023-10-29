package com.example.nuestro.models.users;

import com.example.nuestro.entities.datatypes.DatabaseType;

public class UpdateDatabaseRequest
{
    private  String server;
    private  String port;
    private String database;
    private String username;
    private  String password;
    private DatabaseType type;

    public UpdateDatabaseRequest() {
    }

    public UpdateDatabaseRequest(String server, String port, String database, String username, String password, DatabaseType type) {
        this.server = server;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.type = type;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public DatabaseType getType() {
        return type;
    }

    public void setType(DatabaseType type) {
        this.type = type;
    }
}
