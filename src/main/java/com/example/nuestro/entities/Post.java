package com.example.nuestro.entities;

import com.example.nuestro.entities.interfaces.IPost;
import com.example.nuestro.models.post.PostRequest;
import com.example.nuestro.services.AuditListener;
import jakarta.persistence.*;
import org.hibernate.envers.Audited;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "posts")
@Audited
@EntityListeners(AuditListener.class)
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

    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(unique = false, name = "post_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Like> Likes;

    public Post() {}
    public Post(String content, User user) {
        this.id= UUID.randomUUID().toString();
        this.setCreatorId(user.getId());
        this.content = content;
        this.user= user;
        this.setCreatedAt(LocalDateTime.now());
        this.creatorId=this.user.getId();
    }
    public void Set(PostRequest translationRequest) {
      this.content=translationRequest.getContent();
      this.setUpdatedAt(LocalDateTime.now());
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
    public List<Like> getLikes() {
        return Likes;
    }

    public void setLikes(List<Like> likes) {
        Likes = likes;
    }
}
