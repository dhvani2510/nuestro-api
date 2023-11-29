package com.example.nuestro.entities;

import com.example.nuestro.models.comment.CommentRequest;
import com.example.nuestro.services.AuditListener;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "comments")
public class MongoComment extends BaseEntity {

    @Id
    private String id;

    private String comment;

    @DBRef
    private User user;

    @DBRef
    private Post post;

    public MongoComment(Comment comment)
    {
        this.id=comment.getId();
        this.comment= comment.getComment();
        this.user= comment.getUser();
        this.post= comment.getPost();
        this.createdAt= comment.createdAt;
        this.creatorId=comment.creatorId;
    }

    public MongoComment(String content, User user, Post post) {
        this.id = UUID.randomUUID().toString();
        this.setCreatorId(user.getId());
        this.comment = content;
        this.user = user;
        this.post = post;
        this.setCreatedAt(LocalDateTime.now());
    }

    public void set(CommentRequest commentRequest) {
        this.comment = commentRequest.getComment();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and setters

    public String getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
