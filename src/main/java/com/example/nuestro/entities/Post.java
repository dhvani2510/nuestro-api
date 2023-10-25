package com.example.nuestro.entities;

import com.example.nuestro.models.post.PostRequest;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table
public class Post extends  BaseEntity
{
    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String Id;
    private  String content;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(unique = false)
    private User user;

    public Post(String content, User user) {
        this.content = content;
        this.user= user;
    }
    public void Set(PostRequest translationRequest) {
      this.content=translationRequest.getContent();
      this.setCreatedAt(LocalDateTime.now());
    }


    public Post() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
