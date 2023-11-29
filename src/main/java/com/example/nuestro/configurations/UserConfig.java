package com.example.nuestro.configurations;

import com.example.nuestro.entities.User;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.entities.datatypes.Role;
import com.example.nuestro.models.auth.RegisterRequest;
import com.example.nuestro.models.users.UpdateDatabaseRequest;
import com.example.nuestro.repositories.UserRepository;
import com.example.nuestro.services.ClientDatabaseService;
import com.example.nuestro.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

//Initializer
@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserService userService, UserRepository userRepository, ClientDatabaseService clientService, PasswordEncoder passwordEncoder){

        return args -> {

            try{
                var  ives=new RegisterRequest(
                        "Iverique",
                        "Nkayilu",
                        "nkayilu@uwindsor.ca",
                        "Abc123."
                );

                //ives.Update(new UpdateDatabaseRequest(server,port,database,username,password, DatabaseType.MYSQL));
               // var hashedPassword= passwordEncoder.encode("Abc123.");
                //ives.setPassword(hashedPassword);
                //ives.setRole(Role.Admin);

                AddUser(userService, clientService, ives, GetMySQLDbDatabase());
                //ives.setEmail("test@test.com"); ives.setFirstName("test"); ives.setLastName("test");
                //AddUser(userService,clientService,ives, GetMongoDbDatabase());
                //ives.setEmail("test1@test.com"); ives.setFirstName("test"); ives.setLastName("test");
                //AddUser(userService,clientService,ives, GetMSSQLDbDatabase());
            }
            catch (Exception exception){
                System.out.println("Exception occured "+exception.getMessage());
            };
        };
    }

    private static void AddUser(UserService userService, ClientDatabaseService clientService, RegisterRequest admin, UpdateDatabaseRequest updateDatabaseRequest) throws Exception {

        var existingAdmin= userService.findByEmail(admin.getEmail());
        if(existingAdmin.isEmpty()){
            var register= userService.register(admin);
            var user= userService.GetUser(register.getId());

            UserDetails userDetails =(UserDetails)  user;

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken( userDetails, null, userDetails.getAuthorities());

            // Create a SecurityContext and set the Authentication object
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authenticationToken);

            // Set the SecurityContext in the SecurityContextHolder
            SecurityContextHolder.setContext(securityContext);

            userService.UpdateDatabase(updateDatabaseRequest);

            user= userService.GetUser(register.getId());
           var clientDatabase= clientService.getDatabase(user);
            var isValid=  clientDatabase.isConnectionValid(); //Set this
            clientDatabase.AddToClientDatabase(user, null);
        }
    }

    private  UpdateDatabaseRequest GetMongoDbDatabase(){
        String server ="localhost"; //iverique.com
        var database="nuestro";
        String username="root";
        String password="Hjdj@0389";
        var port="27017";
        return new UpdateDatabaseRequest(server,port,database,username,password, DatabaseType.MONGODB);
    }

    private  UpdateDatabaseRequest GetMySQLDbDatabase(){
        String server ="iverique.com";
        var database="Nuestro2" ;//"Nuestro.Iverique";
        String username="root";
        String password="manager1User@mysql-database";
        var port="3306";
        return new UpdateDatabaseRequest(server,port,database,username,password, DatabaseType.MYSQL);
    }

    private  UpdateDatabaseRequest GetMSSQLDbDatabase(){
        String server ="iverique.com";
        var database="Nuestro" ;
        String username="sa";
        String password="ThisIsAStrongPassword!";
        var port="1433";
        return new UpdateDatabaseRequest(server,port,database,username,password, DatabaseType.MSSQL);
    }
}