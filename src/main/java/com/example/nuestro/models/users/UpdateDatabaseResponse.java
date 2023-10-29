package com.example.nuestro.models.users;

import com.example.nuestro.entities.User;
import com.example.nuestro.entities.datatypes.DatabaseType;

public class UpdateDatabaseResponse
{
    private  String server;
    private  String port;
    private String database;
    private String username;
    private DatabaseType type;

    public UpdateDatabaseResponse() {
    }

    public UpdateDatabaseResponse(User user) throws Exception {
        this.server = user.getDbServer();
        this.username = user.getDbUsername();
        this.port = user.getDbPort();
        this.type = user.getDatabaseType();
        this.database=user.getDbDatabase();
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


    public DatabaseType getType() {
        return type;
    }

    public void setType(DatabaseType type) {
        this.type = type;
    }

}
