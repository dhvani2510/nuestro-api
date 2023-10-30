package com.example.nuestro.entities.interfaces;

import com.example.nuestro.entities.User;

public interface IPost {
    String getContent();
    void setContent(String content);
    String getId();
    void setId(String id);
    User getUser();
    void setUser(User user);
}
