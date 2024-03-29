package com.example.nuestro.repositories;

import com.example.nuestro.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,String> {

    Optional<User> findByEmail(String email);
    @Query("SELECT e FROM User e WHERE e.databaseType != 'NONE'")
    List<User> findByDatabaseTypeIsNotNone();

    //List<User> saveAllAndFlush(List<User> users);
}
