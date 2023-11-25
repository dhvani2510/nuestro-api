package com.example.nuestro.entities;

import com.example.nuestro.entities.interfaces.ILike;
import com.example.nuestro.models.post.PostRequest;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "like")
//@Document(collection = "posts")
public class Like extends  BaseEntity  implements ILike {
    @Id
    //@Indexed(unique=true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(unique = false)
    private User user;

    public Like() {}
    public Like(User user) {
        this.id= UUID.randomUUID().toString();
        this.setCreatorId(user.getId());
        this.user= user;
        this.setCreatedAt(LocalDateTime.now());
    }
    public void Set(PostRequest translationRequest) {
        this.setCreatedAt(LocalDateTime.now());
    }
    public String getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
}