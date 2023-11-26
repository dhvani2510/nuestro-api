package com.example.nuestro.models.users;

import com.example.nuestro.entities.User;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.shared.helpers.EncryptionHelper;

public class UpdateDatabaseResponse
{
    private  String server;
    private  String port;
    private String database;
    private String username;
    private DatabaseType type;

    public UpdateDatabaseResponse() {
    }

    public UpdateDatabaseResponse(User user, boolean decrypt) throws Exception {
        this.server = decrypt? EncryptionHelper.decrypt(user.getDbServer()): user.getDbServer();
        this.username = decrypt? EncryptionHelper.decrypt(user.getDbUsername()):  user.getDbUsername();
        this.port = decrypt? EncryptionHelper.decrypt(user.getDbPort()):  user.getDbPort();
        this.database= decrypt? EncryptionHelper.decrypt(user.getDbDatabase()): user.getDbDatabase();
        this.type =  user.getDatabaseType();
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
