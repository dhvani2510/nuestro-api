package com.example.nuestro.models.post;

import com.example.nuestro.entities.Post;
import com.example.nuestro.models.users.ProfileResponse;

public class PostResponse
{
    private String Id;
    private  String Content;
    private ProfileResponse User;

    public PostResponse(String id, String content) {
        Id = id;
        Content = content;
    }
    public PostResponse(Post post){
        Id= post.getId();
        Content=post.getContent();
        User= new ProfileResponse(post.getUser());
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
}
