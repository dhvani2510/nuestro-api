package com.example.nuestro.models.post;

import com.example.nuestro.entities.Like;
import com.example.nuestro.entities.Post;
import com.example.nuestro.models.users.ProfileResponse;

import java.time.LocalDateTime;
import java.util.Date;

public class PostResponse
{
    private String Id;
    private  String Content;
    private ProfileResponse User;
    private  int Likes;
    private LocalDateTime createdAt;

    public PostResponse(String id, String content) {
        Id = id;
        Content = content;
    }
    public PostResponse(Post post){
        Id= post.getId();
        Content=post.getContent();
        User= new ProfileResponse(post.getUser());
        if(post.getLikes()!=null)
         Likes= post.getLikes().size();
        createdAt= post.getCreatedAt();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public ProfileResponse getUser() {
        return User;
    }

    public void setUser(ProfileResponse user) {
        User = user;
    }

    public int getLikes() {
        return Likes;
    }

    public void setLikes(int likes) {
        Likes = likes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
