package com.example.nuestro.entities;

import com.example.nuestro.entities.interfaces.IPost;
import com.example.nuestro.models.post.PostRequest;
import jakarta.persistence.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "posts")
//@Document(collection = "posts")
public class Post extends  BaseEntity  implements IPost
{
    @Id
    //@Indexed(unique=true)
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;
    private  String content;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(unique = false)
    private User user;

    public Post() {}
    public Post(String content, User user) {
        this.id= UUID.randomUUID().toString();
        this.setCreatorId(user.getId());
        this.content = content;
        this.user= user;
        this.setCreatedAt(LocalDateTime.now());
    }
    public void Set(PostRequest translationRequest) {
      this.content=translationRequest.getContent();
      this.setCreatedAt(LocalDateTime.now());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
