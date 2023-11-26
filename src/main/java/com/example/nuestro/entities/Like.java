package com.example.nuestro.entities;

import com.example.nuestro.entities.interfaces.ILike;
import com.example.nuestro.models.post.PostRequest;
import com.example.nuestro.services.AuditListener;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "likes")
@EntityListeners(AuditListener.class)
public class Like extends BaseEntity implements ILike {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Like() {
    }

    public Like(User user, Post post) {
        this.id = UUID.randomUUID().toString();
        this.user = user;
        this.post = post;
        this.setCreatorId(user.getId());
        this.setCreatedAt(LocalDateTime.now());
    }

    // Assuming you want to set the creation time when updating the Like
    public void set(PostRequest postRequest) {
        this.setCreatedAt(LocalDateTime.now());
    }

    public String getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Post getPost() {
        return post;
    }
}
