package com.example.nuestro.models.post;

import com.example.nuestro.entities.Post;

public class PostResponse
{
    private String Id;
    private  String Content;
    private  String User;

    public PostResponse(String id, String content) {
        Id = id;
        Content = content;
    }
    public PostResponse(Post translation){
        Id= translation.getId();
        Content=translation.getContent();
        User= translation.getUser().getUsername();
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

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
