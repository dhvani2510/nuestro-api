package com.example.nuestro.models.comment;

import com.example.nuestro.entities.Comment;
import com.example.nuestro.entities.Post;
import com.example.nuestro.models.users.ProfileResponse;

public class CommentResponse {
    private String Id;
    private  String Comment;
    private ProfileResponse User;

    public CommentResponse(String id, String comment) {
        Id = id;
        Comment = comment;
    }
    public CommentResponse(Comment comment){
        Id= comment.getId();
        Comment=comment.getComment();
        User= new ProfileResponse(comment.getUser());
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = Comment;
    }

    public ProfileResponse getUser() {
        return User;
    }

    public void setUser(ProfileResponse user) {
        User = user;
    }
}
