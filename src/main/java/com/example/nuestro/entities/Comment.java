package com.example.nuestro.entities;

import com.example.nuestro.entities.interfaces.IComment;
import com.example.nuestro.models.comment.CommentRequest;
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
    @JoinColumn(unique = false, name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(unique = false, name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;

    public Comment() {}

    public Comment(String content, User user, Post post) {
        this.id= UUID.randomUUID().toString();
        this.setCreatorId(user.getId());
        this.comment = content;
        this.user= user;
        this.post = post;
        this.setCreatedAt(LocalDateTime.now());
    }
    public void Set(CommentRequest commentRequest) {
        this.comment=commentRequest.getComment();
        this.updatedAt=LocalDateTime.now();
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
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

}
