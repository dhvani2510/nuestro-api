package com.example.nuestro.services;
import com.example.nuestro.entities.*;
import com.example.nuestro.services.interfaces.IClientDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

@Service
public class ClientMongoDbService implements IClientDatabase
{
    private static final Logger logger = LoggerFactory.getLogger(ClientDatabaseService.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    public  ClientMongoDbService(MongoTemplate mongoTemplate)
    {
        this.mongoTemplate= mongoTemplate;
    }


    public void AddToClientDatabase(User user, List<Post> posts) throws Exception {
        logger.info("Connecting to client database, creating user and posts");

        Query query = new Query(Criteria.where("id").is(user.getId()));
        var userInTheDb= mongoTemplate.findOne(query, User.class);
        // Create a user in MongoDB
        if(userInTheDb !=null)
        mongoTemplate.save(user);

        // Create posts for the user in MongoDB
        if(posts!=null)
        for (Post post : posts) {
            //post.setUser(user);
            mongoTemplate.save(post);
        }
    }


//    public void setDatabase(User user) {
//        // No need for database creation in MongoDB, as databases and collections are created dynamically.
//        // The database will be set to the user's ID by default.
//    }

    public boolean isConnectionValid() {
        try {
            // Execute a simple MongoDB command to check the connection validity
            mongoTemplate.executeCommand("{ ping: 1 }");
            logger.info("Connection is valid");
            //System.out.println("Connection is valid.");
            return true;
        } catch (Exception e) {
            logger.error("Connection is not valid: " + e.getMessage());
            return false;
        }
    }

    public Post getPostById(String postId) {
        Query query = new Query(Criteria.where("id").is(postId));
        return mongoTemplate.findOne(query, Post.class);
    }

    public List<Post> getPosts() {
        return mongoTemplate.findAll(Post.class);
    }

    public void addRangePost(List<Post> posts) {
        mongoTemplate.insertAll(posts);
    }

    public void addPost(Post post) {
        mongoTemplate.insert(post);
    }

    public void updatePost(Post post) {
        mongoTemplate.save(post);
    }

    public void deletePost(String postId) {
        Query query = new Query(Criteria.where("id").is(postId));
        mongoTemplate.remove(query, Post.class);
    }

    @Override
    public void likePost(Like like) {
       var result= mongoTemplate.insert(like);
        logger.info("Result: "+ result.toString());
    }

    @Override
    public void deleteLike(String likeId) {
        Query query = new Query(Criteria.where("id").is(likeId));
       var result=  mongoTemplate.remove(query, Like.class);
       logger.info("Result: "+ result.toString());
    }

    @Override
    public List<Like> getLikes() {
        return mongoTemplate.findAll(Like.class);
    }

    @Override
    public List<Comment> getComments() {
        return mongoTemplate.findAll(Comment.class);
    }

    public User getUserById(String userId) {
        Query query = new Query(Criteria.where("id").is(userId));
        return mongoTemplate.findOne(query, User.class);
    }

    public List<User> getUsers() {
        return mongoTemplate.findAll(User.class);
    }

    public void addUser(User user) {
        mongoTemplate.insert(user);
    }

    public void updateUser(User user) {
        mongoTemplate.save(user);
    }

    public void deleteUser(String userId) {
        Query query = new Query(Criteria.where("id").is(userId));
        mongoTemplate.remove(query, User.class);
    }

    public void createDatabaseAndTables(String databaseName) {
        // In MongoDB, there is no need to create a database or collections explicitly.
        // Collections (equivalent to tables in relational databases) are created dynamically.

        // However, you can switch to a specific database (use it) using MongoTemplate.
        // The database name is usually the one set in the MongoTemplate configuration.
        mongoTemplate.getDb().getName();
    }

    @Override
    public boolean doesTableExist(String tableName) {
        return false;
    }

    @Override
    public boolean doesDatabaseExist(String databaseName) {
        return false;
    }

    public boolean isConnectionValid2() {
        try {
            // Execute a simple MongoDB command to check the connection validity
            mongoTemplate.executeCommand("{ ping: 1 }");
            System.out.println("Connection is valid.");
            return true;
        } catch (Exception e) {
            // Handle any exceptions here
            System.out.println("Connection is not valid: " + e.getMessage());
            return false;
        }
    }

    //Use Mongo comment
    public void addComment(Comment comment) {
        try{
            var mongoComment= new MongoComment(comment);
            var result= mongoTemplate.insert(mongoComment);
            logger.info("Result: "+ result.toString());
        }
        catch (Exception exception){
            logger.error(exception.getMessage());
        }
    }

    public void updateComment(Comment comment) {
        var mongoComment= new MongoComment(comment);
      var result=  mongoTemplate.save(mongoComment);
        logger.info("Result: "+ result.toString());
    }

    public void deleteComment(String commentId) {
        Query query = new Query(Criteria.where("id").is(commentId));
       var result= mongoTemplate.remove(query, MongoComment.class);
        logger.info("Result: "+ result.toString());
    }
}
