package com.example.nuestro.repositories;

import com.example.nuestro.entities.Like;
import com.example.nuestro.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LikeRepository extends JpaRepository<Like,String> {
    Like findByPost_IdAndUser_Id(String postId, String  userId);
}

