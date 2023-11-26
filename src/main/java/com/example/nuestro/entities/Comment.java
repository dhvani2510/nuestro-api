package com.example.nuestro.entities;

import com.example.nuestro.entities.interfaces.IComment;
import com.example.nuestro.models.post.PostRequest;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "comments")
//@
public class Comment extends BaseEntity implements IComment {

    @Id
    @GeneratedValue(strategy= GenerationType.UUID)
    private String id;

    private  String comment;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(unique = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
    private Post post_id;

    public Comment() {}

    public Comment(String content, User user, Post post) {
        this.id= UUID.randomUUID().toString();
        this.setCreatorId(user.getId());
        this.comment = comment;
        this.user= user;
        this.post_id = post;
        this.setCreatedAt(LocalDateTime.now());
    }
    public void Set(PostRequest translationRequest) {
        this.comment=translationRequest.getContent();
        this.setCreatedAt(LocalDateTime.now());
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
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

    public Post getPost() {
        return post_id;
    }

    public void setPost(Post post) {
        this.post_id = post;
    }

}
