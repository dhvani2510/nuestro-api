package com.example.nuestro.entities;

import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.entities.datatypes.Role;
import com.example.nuestro.shared.helpers.EncryptionHelper;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Collection;
import java.util.List;

@Entity
@Table
public class User extends  BaseEntity implements UserDetails
{
    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.UUID)
    public String id;
    public  String firstName;
    public  String lastName;
    public LocalDate birthDate;
    @Column(unique = true)
    public  String email;

    public  String Password; // Hashed

   // @OneToOne(cascade = CascadeType.PERSIST)
    //@Transient
    //private  File image;

    private  String connectionString;
    @Enumerated(EnumType.STRING)
    private DatabaseType databaseType;
    @Transient
    public Integer Age;

    @Enumerated(EnumType.STRING)
     private  Role Role;

    public  User(String name, String surname, String email, LocalDate birthDay){

        this.firstName= name;
        this.lastName= surname;
        this.email=email;
        this.birthDate = birthDay;
        this.Age=getAge();
        this.CreatedAt= LocalDateTime.now();
        this.Role= com.example.nuestro.entities.datatypes.Role.User;
        this.setCreatedAt(LocalDateTime.now());
    }

    public User() {
    }

    public Integer getAge(){
        return Period.between(this.birthDate,LocalDate.now()).getYears();
    }
    public void setRole(Role role) {
        Role=role;
    }
    public String getEmail() {
        return email;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(Role.name()));
    }

    @Override
    public String getUsername() {
        return email; //email
    }

    @Override
    public String getPassword() {
        return Password;
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
        this.id =builder.Id;
        this.email=builder.Email;
        this.Password= builder.Password;
        this.Role=builder.Role;
        this.setCreatedAt(LocalDateTime.now());
    }

    public void setPassword(String password) {
        Password = password; // encrypt
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

    public String getConnectionString() throws Exception {
        return EncryptionHelper.decrypt(connectionString)  ;
    }

    public void setConnectionString(String connectionString) throws Exception {
        this.connectionString = EncryptionHelper.encrypt(connectionString);
    }

    //Builder Class
    public static class UserBuilder{

        // required parameters
        private String firstName;
        private String lastName;

        // optional parameters
        private String Id;

        private String Email;
        private String Password;
        private  Role Role;

        public UserBuilder(String name, String surname){
            this.firstName=name;
            this.lastName=surname;
        }

        public UserBuilder setId(String id) {
            this.Id = id;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.Email = email;
            return this;
        }
        public UserBuilder setPassword(String password) {
            this.Password = password;
            return this;
        }
        public UserBuilder setRole(Role role) {
            this.Role = role;
            return this;
        }

        public User build(){
            return new User(this);
        }

    }
}
