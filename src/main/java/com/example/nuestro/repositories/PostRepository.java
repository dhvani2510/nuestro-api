package com.example.nuestro.repositories;

import com.example.nuestro.entities.Post;
import com.example.nuestro.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,String>
{
    List<Post> findAllByOrderByCreatedAtDesc();
    List<Post> findByUserIdOrderByCreatedAtDesc(String userId);
    List<Post> findByContentContainingOrderByCreatedAtDesc(String content);
    List<Post> findByContentContainingAndUser_IdOrderByCreatedAtDesc(String content, String userId);

    //List<Post> saveAllAndFlush(List<Post> posts);
}
