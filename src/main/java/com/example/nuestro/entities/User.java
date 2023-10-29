package com.example.nuestro.entities;

import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.entities.datatypes.Role;
import com.example.nuestro.models.users.UpdateDatabaseRequest;
import com.example.nuestro.shared.helpers.DatabaseHelper;
import com.example.nuestro.shared.helpers.EncryptionHelper;
import jakarta.persistence.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
//@Document(collection = "users")
public class User extends  BaseEntity implements UserDetails
{
    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    //@Indexed(unique=true)
    @GeneratedValue(strategy=GenerationType.UUID)
    public String id;
    @Column(name = "first_name")
    public  String firstName;
    @Column(name = "last_name")
    public  String lastName;
    @Column(name = "birth_date")
    public LocalDate birthDate;
    @Column(unique = true)
    public  String email;

    public  String password; // Hashed

   // @OneToOne(cascade = CascadeType.PERSIST)
    //@Transient
    //private  File image;

    @Transient
    //@Column(name = "connection_string")
    private  String connectionString;

    private  String db_server;
    private  String db_port;
    private String db_database;
    private String db_username;
    private  String db_password;

    @Column(name = "database_type")
    @Enumerated(EnumType.STRING)
    private DatabaseType databaseType;
    @Transient
    public Integer age;

    @Enumerated(EnumType.STRING)
     private  Role role;

    public  User(String name, String surname, String email, LocalDate birthDate){

        this.firstName= name;
        this.lastName= surname;
        this.email=email;
        this.birthDate = birthDate;
        this.age =getAge();
        this.createdAt = LocalDateTime.now();
        this.role = com.example.nuestro.entities.datatypes.Role.User;
        this.setCreatedAt(LocalDateTime.now());
    }

    public User() {
    }

    public  void Update (UpdateDatabaseRequest databaseRequest) throws Exception {
        this.db_database= EncryptionHelper.encrypt( databaseRequest.getDatabase());
        this.db_password= EncryptionHelper.encrypt(databaseRequest.getPassword());
        this.db_port= EncryptionHelper.encrypt(databaseRequest.getPort());
        this.db_server= EncryptionHelper.encrypt(databaseRequest.getServer());
        this.db_username= EncryptionHelper.encrypt(databaseRequest.getUsername());
        this.setDatabaseType(databaseRequest.getType());
    }

    public Integer getAge(){
        return Period.between(this.birthDate,LocalDate.now()).getYears();
    }
    public void setRole(Role role) {
        this.role =role;
    }
    public String getEmail() {
        return email;
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
    }

    public void setPassword(String password) {
        this.password = password; // encrypt
    }

//    public String getImageId() {
//        return image==null? null: image.getId();
//    }
//    public File getImage() {
//        return image;
//    }
//
//    public void setImage(File image) {
//        this.image = image;
//    }

    public DatabaseType getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }

    public String getDbServer() throws Exception {
        return EncryptionHelper.decrypt(db_server);
    }

    public void setDbServer(String db_server) {
        this.db_server = db_server;
    }

    public String getDbPort() throws Exception {
        return EncryptionHelper.decrypt(db_port);
    }

    public void setDbPort(String db_port) {
        this.db_port = db_port;
    }

    public String getDbDatabase() throws Exception {
        return EncryptionHelper.decrypt(db_database);
    }

    public void setDbDatabase(String db_database) {
        this.db_database = db_database;
    }

    public String getDbUsername() throws Exception {
        return EncryptionHelper.decrypt(db_username);
    }

    public void setDbUsername(String db_username) {
        this.db_username = db_username;
    }

    public String getDbPassword() throws Exception {
        return EncryptionHelper.decrypt(db_password);
    }

    public void setDbPassword(String db_password) {
        this.db_password = db_password;
    }

    public String getConnectionString() throws Exception {

        return  databaseType== DatabaseType.MYSQL?
                GetMySqlConnectionString():
                databaseType== DatabaseType.MONGODB?
                        GetMongoDbConnectionString():
                        "";
    }

    private String GetMySqlConnectionString() throws Exception { var server= getDbServer();
        var port= Integer.parseInt(getDbPort());
        var database= getDbDatabase();
        var username= getDbUsername();
        var password= getDbPassword();
        return DatabaseHelper.GenerateMySQLConnectionString(server,
                port, database, username,password);

    }

    private String GetMongoDbConnectionString() throws Exception {
        var server= getDbServer();
        var port= Integer.parseInt(getDbPort());
        var database= getDbDatabase();
        var username= getDbUsername();
        var password= getDbPassword();
        return DatabaseHelper.GenerateMongoDBURI(server,
                port, database, username,password);

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
