package com.example.nuestro.services;

import com.example.nuestro.entities.datatypes.DatabaseType;
import com.example.nuestro.repositories.CommentRepository;
import com.example.nuestro.repositories.LikeRepository;
import com.example.nuestro.repositories.PostRepository;
import com.example.nuestro.repositories.UserRepository;
import com.example.nuestro.shared.exceptions.NuestroException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class SynchronizeService
{
   private final   UserRepository userRepository;
   private  final PostRepository postRepository;
   private  final CommentRepository commentRepository;
   private  final LikeRepository likeRepository;
   private  final  ClientDatabaseService clientService;

    private static final Logger logger= LoggerFactory.getLogger(SynchronizeService.class);
    @Autowired
    public SynchronizeService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, LikeRepository likeRepository, ClientDatabaseService clientService) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.clientService = clientService;
    }
    @Async
    public void synchronizeData() throws Exception { // Get users, for each, get data verify and
        System.out.println("Async task is running in thread: " + Thread.currentThread().getName());
        //var users= userRepository.findAll();
        var users= userRepository.findByDatabaseTypeIsNotNone();

        for (var user : users) {
            if(user.getDatabaseType()!= DatabaseType.None){

                var clientDatabase= clientService.getDatabase(user);
                var posts= postRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
                //var comments= commentRepository.

                var clientUser= clientDatabase.getUserById(user.getId());
                var clientPosts= clientDatabase.getPosts();
                var clientComments=clientDatabase.getComments();
                var clientLikes= clientDatabase.getLikes();

                var count=0;
                for(var clientPost: clientPosts){
                  var foundPost= posts.stream().filter(p-> Objects.equals(p.getId(), clientPost.getId())).findFirst();
                  if(foundPost.isEmpty()){
                      postRepository.save(clientPost);
                      count++;
                  }
                  else{
                      if(foundPost.get().getUpdatedAt().isBefore(clientPost.getUpdatedAt())){
                          postRepository.save(clientPost);
                          count++;
                      }
                  }
                }
                logger.info("Posts synchronize, {} changes", count);
                //if(clientDatabase!=null)
                    //clientDatabase.AddToClientDatabase(user, posts);
            }
        }
    }


    @Async
    public void replicateData(String userId) throws Exception { // Get users, for each, get data verify and

        System.out.println("Async task is running in thread: " + Thread.currentThread().getName());

        var user= userRepository.findById(userId).orElseThrow(()-> new NuestroException("User not found"));
        var posts= postRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        var clientDatabase= clientService.getDatabase(user);
        if(clientDatabase!=null)
          clientDatabase.AddToClientDatabase(user, posts);
        else {
            //log and queue again?
        }
    }
}
