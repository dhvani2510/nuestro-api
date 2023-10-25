package com.example.nuestro.repositories;

import com.example.nuestro.entities.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File,String>
{
    File findByNameAndContentTypeAndSizeInBytes(String name, String contentType, Long sizeInBytes);
}
