package com.example.nuestro.services;

import com.example.nuestro.entities.User;
import com.example.nuestro.repositories.PostRepository;
import com.example.nuestro.repositories.UserRepository;
import com.example.nuestro.shared.exceptions.NuestroException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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
    @Async
    public void synchronizeData() throws Exception { // Get users, for each, get data verify and
        System.out.println("Async task is running in thread: " + Thread.currentThread().getName());
        var users= userRepository.findAll();

        for (var user : users) {
            var posts= postRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
            var clientDatabase= clientService.getDatabase(user);
            clientDatabase.AddToClientDatabase(user, posts);
        }
    }
    @Async
    public void synchronizeData(String userId) throws Exception { // Get users, for each, get data verify and

        System.out.println("Async task is running in thread: " + Thread.currentThread().getName());

        var user= userRepository.findById(userId).orElseThrow(()-> new NuestroException("User not found"));
        var posts= postRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        var clientDatabase= clientService.getDatabase(user);
        clientDatabase.AddToClientDatabase(user, posts);
    }
}
