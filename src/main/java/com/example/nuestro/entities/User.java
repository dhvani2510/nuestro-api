package com.example.nuestro.entities;

import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.entities.datatypes.Role;
import com.example.nuestro.entities.interfaces.IUser;
import com.example.nuestro.models.users.UpdateDatabaseRequest;
import com.example.nuestro.services.AuditListener;
import com.example.nuestro.shared.helpers.DatabaseHelper;
import com.example.nuestro.shared.helpers.EncryptionHelper;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import org.hibernate.envers.Audited;

@Entity
@Table(name = "users")
@Audited
@EntityListeners(AuditListener.class)
public class User extends  BaseEntity implements UserDetails, IUser
{
    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.UUID)
    private String id;
    @Column(name = "first_name")
    private  String firstName;
    @Column(name = "last_name")
    private   String lastName;
    @Column(unique = true)
    private   String email;

    private   String password; // Hashed

   // @OneToOne(cascade = CascadeType.PERSIST)
    //@Transient
    //private  File image;

    @Transient
    //@Column(name = "connection_string")
    private  String connectionString;

    private  String dbServer;
    private  String dbPort;
    private String dbDatabase;
    private String dbUsername;
    private  String dbPassword;

    @Column(name = "database_type")
    @Enumerated(EnumType.STRING)
    private DatabaseType databaseType;

    @Enumerated(EnumType.STRING)
     private  Role role;

    public  User(String name, String surname, String email){

        this.firstName= name;
        this.lastName= surname;
        this.email=email;
        this.createdAt = LocalDateTime.now();
        this.role = com.example.nuestro.entities.datatypes.Role.User;
        this.setCreatedAt(LocalDateTime.now());
    }

    public User() {
    }

    public  void Update (UpdateDatabaseRequest databaseRequest) throws Exception {
        this.dbDatabase = EncryptionHelper.encrypt( databaseRequest.getDatabase());
        this.dbPassword = EncryptionHelper.encrypt(databaseRequest.getPassword());
        this.dbPort = EncryptionHelper.encrypt(databaseRequest.getPort());
        this.dbServer = EncryptionHelper.encrypt(databaseRequest.getServer());
        this.dbUsername = EncryptionHelper.encrypt(databaseRequest.getUsername());
        this.databaseType=(databaseRequest.getType());
    }

    public void setRole(Role role) {
        this.role =role;
    }
    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email; //email
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Builder design pattern
    private User(UserBuilder builder) {
        this.firstName=builder.firstName;
        this.lastName=builder.lastName;
        this.id =builder.id;
        this.email=builder.email;
        this.password = builder.password;
        this.role =builder.role;
        this.setCreatedAt(LocalDateTime.now());
        this.setCreatorId(this.id);
    }

    public void setPassword(String password) {
        this.password = password; // encrypt
    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public String getDbServer()  {
        return dbServer;
    }

    public void setDbServer(String db_server) {
        this.dbServer = db_server;
    }

    public String getDbPort() throws Exception {
        return dbPort;
    }

    public void setDbPort(String db_port) {
        this.dbPort = db_port;
    }

    public String getDbDatabase() throws Exception {
        return dbDatabase;
    }

    public void setDbDatabase(String db_database) {
        this.dbDatabase = db_database;
    }

    public String getDbUsername() throws Exception {
        return dbUsername;
    }

    public void setDbUsername(String db_username) {
        this.dbUsername = db_username;
    }

    public String getDbPassword() throws Exception {
        return dbPassword;
    }

    public void setDbPassword(String db_password) {
        this.dbPassword = db_password;
    }

    public String getConnectionString() throws Exception {
        return  GenerateConnectionString(databaseType);
    }

    private String GenerateConnectionString(DatabaseType databaseType) throws Exception {
        var server = EncryptionHelper.decrypt(getDbServer());
        var port = Integer.parseInt(EncryptionHelper.decrypt(getDbPort()));
        var database = EncryptionHelper.decrypt(getDbDatabase());
        var username = EncryptionHelper.decrypt(getDbUsername());
        var password = EncryptionHelper.decrypt(getDbPassword());

        return switch (databaseType) {
            case MYSQL -> DatabaseHelper.GenerateMySQLConnectionString(server, port, database, username, password);
            case MONGODB -> DatabaseHelper.GenerateMongoDBURI(server, port, database, username, password);
                   // DatabaseHelper.GenerateMongoDBURI(server, port, database, username, password);
            case MSSQL -> DatabaseHelper.GenerateMSSQLConnectionString(server, port, database, username, password);
            default -> "";
            //throw new IllegalArgumentException("Unsupported database type: " + databaseType);
        };
    }


    //Builder Class
    public static class UserBuilder{

        // required parameters
        public String firstName;
        public String lastName;

        // optional parameters
        private String id;
        private String email;
        private String password;
        private  Role role;

        public UserBuilder(String name, String surname){
            this.firstName=name;
            this.lastName=surname;
        }

        public UserBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }
        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }
        public UserBuilder setRole(Role role) {
            this.role = role;
            return this;
        }

        public User build(){
            return new User(this);
        }
    }
}
