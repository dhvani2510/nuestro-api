package com.example.nuestro.entities;

import com.example.nuestro.shared.helpers.StringHelper;
import jakarta.persistence.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Entity
@Table
public class File extends  BaseEntity
{
    @Id
    @GeneratedValue(strategy=GenerationType.UUID)
    private String Id;
    private String name ;
    private String contentType ;// Image, Audio, Video
    @Column(length = 10485760 ) //10MB
    private byte[] bytes ;
    private Long sizeInBytes ;

    public  File(){}

    public File(String name, String contentType, byte[] bytes, Long sizeInBytes) {
        this.name = name;
        this.contentType = contentType;
        this.bytes = bytes;
        this.sizeInBytes = sizeInBytes;
    }

    public void Update(MultipartFile file) throws IOException {
        this.name = file.getOriginalFilename();
        this.contentType = file.getContentType();
        this.bytes = file.getBytes();
        this.sizeInBytes = file.getSize();
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public Long getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(Long sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public String getUrl(){
        return StringHelper.GetFileUrl(this.getId());
    }
}
