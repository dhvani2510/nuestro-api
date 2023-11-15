package com.example.nuestro.repositories;

import com.example.nuestro.entities.Post;
import com.example.nuestro.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,String>
{
    //Post findByEnglishAndHindiAndFrench(String english, String hindi, String french); //Optional
    List<Post> findByUserId(String userId);
    List<Post> findByContentContaining(String content);

    List<Post> findByContentContainingAndUser_Id(String content, String userId);

    //List<Post> saveAllAndFlush(List<Post> posts);
}
