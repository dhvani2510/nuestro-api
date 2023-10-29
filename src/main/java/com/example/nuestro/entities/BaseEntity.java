package com.example.nuestro.entities;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass

public abstract class BaseEntity {

     //public UUID Id;
     @Column(name = "created_at")
     protected LocalDateTime createdAt;
     @Column(name = "deleted_at")
     @Nullable
     protected LocalDateTime deletedAt;
     @Column(name = "creator_id")
     protected String creatorId;

     public LocalDateTime getCreatedAt() {
          return createdAt;
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

     public void setCreatorId(String creatorId) {
          this.creatorId = creatorId;
     }

//     @SequenceGenerator(
//             name = "language_sequence",
//             sequenceName = "language_sequence",
//             allocationSize = 1
//     )
//     @GeneratedValue(
//             strategy = GenerationType.SEQUENCE,
//             generator = "language_sequence"
//     )
}
