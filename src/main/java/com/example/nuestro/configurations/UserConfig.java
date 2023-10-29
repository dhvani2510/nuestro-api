package com.example.nuestro.configurations;

import com.example.nuestro.entities.User;
import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.entities.datatypes.Role;
import com.example.nuestro.models.users.UpdateDatabaseRequest;
import com.example.nuestro.repositories.UserRepository;
import com.example.nuestro.services.ClientDatabaseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

//Initializer
@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner commandLineRunner(UserRepository userRepository, ClientDatabaseService clientService, PasswordEncoder passwordEncoder){

        return args -> {

            try{
                User  ives=new User(
                        "Iverique",
                        "Nkayilu",
                        "nkayilu@uwindsor.ca",
                        LocalDate.of(1998,5,6)
                );
                String server ="iverique.com";
                //String url = "jdbc:mysql://iverique.com:3306/Nuestro";
                var database="Nuestro.Iverique";
                String username="root";
                String password="manager1User@mysql-database";
                var port="3306";

                ives.Update(new UpdateDatabaseRequest(server,port,database,username,password, DatabaseType.MYSQL));
                var hashedPassword= passwordEncoder.encode("Abc123.");
                ives.setPassword(hashedPassword);
                ives.setRole(Role.Admin);

                AddUser(userRepository, clientService, ives);
                //ives.id= UUID.randomUUID().toString();
                ives.email="test@test.com";
                ives.Update(GetMongoDbDatabase());
                AddUser(userRepository,clientService,ives);
            }
            catch (Exception exception){
                System.out.println("Exception occured "+exception.getMessage());
            };
        };
    }

    private static void AddUser(UserRepository userRepository, ClientDatabaseService clientService, User ives) throws Exception {
        var existingIves= userRepository.findByEmail(ives.email);
        if(existingIves.isEmpty()){
            userRepository.saveAll(List.of(ives));
           var clientDatabase= clientService.getDatabase(ives);
            clientDatabase.isConnectionValid(); //Set this
            clientDatabase.AddToClientDatabase(ives, null);
        }
    }

    private  UpdateDatabaseRequest GetMongoDbDatabase(){
        String server ="localhost"; //iverique.com
        var database="nuestro";
        String username="";
        String password="";
        var port="27017";
        return new UpdateDatabaseRequest(server,port,database,username,password, DatabaseType.MONGODB);
    }
}
