package com.example.nuestro.entities.interfaces;

import com.example.nuestro.entities.User;
import com.example.nuestro.entities.Post;

public interface IComment {

    String getComment();
    void setComment(String comment);
    String getId();
    void setId(String id);
    User getUser();
    void setUser(User user);
    Post getPost();

    void setPost(Post post);

}
