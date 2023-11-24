package com.example.nuestro.entities.interfaces;
//package com.example.nuestro.entities;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.entities.datatypes.Role;
import com.example.nuestro.models.users.UpdateDatabaseRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.time.LocalDate;

public interface IUser extends UserDetails {

    //String getId();

    String getFirstName();

    String getLastName();

    String getEmail();

    String getPassword();

    String getConnectionString() throws Exception;

    String getDbServer() throws Exception;

    String getDbPort() throws Exception;

    String getDbDatabase() throws Exception;

    String getDbUsername() throws Exception;

    String getDbPassword() throws Exception;

    DatabaseType getDatabaseType();

    Role getRole();

    void setRole(Role role);

    void Update(UpdateDatabaseRequest databaseRequest) throws Exception;
}
