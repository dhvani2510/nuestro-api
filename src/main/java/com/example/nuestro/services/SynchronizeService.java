package com.example.nuestro.services;

import com.example.nuestro.entities.User;
import com.example.nuestro.repositories.PostRepository;
import com.example.nuestro.repositories.UserRepository;
import com.example.nuestro.shared.exceptions.NuestroException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SynchronizeService
{
   private final   UserRepository userRepository;
   private  final PostRepository postRepository;
   private  final  ClientDatabaseService clientService;

    @Autowired
    public SynchronizeService(UserRepository userRepository, PostRepository postRepository, ClientDatabaseService clientService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.clientService = clientService;
    }

    public void synchronizeData() throws Exception { // Get users, for each, get data verify and

        var users= userRepository.findAll();

        for (var user : users) {
            var posts= postRepository.findByUserId(user.getId());
            var clientDatabase= clientService.getDatabase(user);
            clientDatabase.AddToClientDatabase(user, posts);
        }
    }

    public void synchronizeData(String userId) throws Exception { // Get users, for each, get data verify and

        var user= userRepository.findById(userId).orElseThrow(()-> new NuestroException("User not found"));
        var posts= postRepository.findByUserId(user.getId());
        var clientDatabase= clientService.getDatabase(user);
        clientDatabase.AddToClientDatabase(user, posts);
    }
}
