package com.example.nuestro.entities;

import com.example.nuestro.entities.interfaces.IUser;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

public abstract class BaseEntity{

@MappedSuperclass

public abstract class BaseEntity{

     @CreatedDate
     //public UUID Id;
     @Column(name = "created_at")
     protected LocalDateTime createdAt;
     @Column(name = "deleted_at")
     @Nullable
     protected LocalDateTime deletedAt;
     @CreatedBy
     @Column(name = "creator_id")
     protected String creatorId;

     @LastModifiedDate
     @Column(name="updated_at")
     protected LocalDateTime updatedAt;

     @LastModifiedBy
     @Column(name = "updater_id")
     protected String updaterId;

     public LocalDateTime getUpdatedAt() {
          return updatedAt;
     }

     public LocalDateTime getCreatedAt() {
          return createdAt;
     }
     public void setUpdatedAt(LocalDateTime updatedAt) {
          this.updatedAt = updatedAt;
     }

     public void setCreatedAt(LocalDateTime createdAt) {
          this.createdAt = createdAt;
     }

     @Nullable
     public LocalDateTime getDeletedAt() {
          return deletedAt;
     }

     public void setDeletedAt(@Nullable LocalDateTime deletedAt) {
          this.deletedAt = deletedAt;
     }

     public String getCreatorId() {
          return creatorId;
     }
     public String getUpdaterId() {
          return updaterId;
     }

     public void setCreatorId(String creatorId) {
          this.creatorId = creatorId;
     }
     public void setUpdaterId(String creatorId) {
          this.updaterId = creatorId;
     }
}
