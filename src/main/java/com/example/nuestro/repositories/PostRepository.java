package com.example.nuestro.repositories;

import com.example.nuestro.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post,String>
{
    //Post findByEnglishAndHindiAndFrench(String english, String hindi, String french); //Optional
    //Post findByEnglish(String english);
}
