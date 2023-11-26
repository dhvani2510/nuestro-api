package com.example.nuestro.repositories;

import com.example.nuestro.entities.Comment;
import com.example.nuestro.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,String> {
    List<Comment> findByUserId(String userId);
    List<Comment> findByPostId(String post_id);
}
