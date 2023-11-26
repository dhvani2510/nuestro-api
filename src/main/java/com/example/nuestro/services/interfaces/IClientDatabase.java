package com.example.nuestro.services.interfaces;

import com.example.nuestro.entities.Post;
import com.example.nuestro.entities.User;
import com.example.nuestro.entities.Comment;

import java.util.List;

public interface IClientDatabase
{
    //void setDatabase(User user) throws Exception;
    boolean isConnectionValid();
    Post getPostById(String postId);
    List<Post> getPosts();
    void addRangePost(List<Post> posts);
    void addPost(Post post);
    void updatePost(Post post);
    void deletePost(String postId);
    User getUserById(String userId);
    List<User> getUsers();
    void addUser(User user) throws Exception;
    void updateUser(User user) throws Exception;
    void deleteUser(String userId);
    void createDatabaseAndTables(String databaseName);
    boolean doesTableExist(String tableName);
    boolean doesDatabaseExist(String databaseName);
    void AddToClientDatabase(User user, List<Post> posts) throws Exception;
    void addComment(Comment comment);
    void updateComment(Comment comment);
    void deleteComment (String commentId);
}
