package com.example.nuestro.services;

import com.example.nuestro.entities.AuditLog;
import com.example.nuestro.entities.User;
import com.example.nuestro.shared.exceptions.NuestroException;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

public class AuditListener extends AuditingEntityListener {
    @Autowired
    @Lazy
    private EntityManager entityManager;

    private String getDetail() {
        var current_user = SecurityContextHolder.getContext().getAuthentication();
        if(current_user!=null) {
            if(!current_user.isAuthenticated()){ // toggle this
               return  "Admin";
            }
            var p_user = current_user.getPrincipal();
            if(p_user == "anonymousUser" )
                return "Admin";
            var user = (User)current_user.getPrincipal();
            return user.getId();
        }
        else
            return "Admin";
    }
    @PrePersist
    public void prePersist(Object entity) {
        // Handle pre-persist event (entity creation)
        logAudit(entity, "CREATE");
    }
    @PreUpdate
    public void preUpdate(Object entity) {
        // Handle pre-update event (entity modification)
        logAudit(entity, "UPDATE");

    }

    @PreRemove
    public void preRemove(Object entity) {
        // Handle pre-remove event (entity deletion)
        logAudit(entity, "DELETE");
    }
    private void logAudit(Object entity, String action) {
        if (entity instanceof User) {
            User auditedEntity = (User) entity;
            AuditLog auditLog = new AuditLog();
            auditLog.setEntityName(entity.getClass().getSimpleName());
            auditLog.setEntityId(auditedEntity.getId());
            auditLog.setCreatedBy(getDetail());
            auditLog.setCreatedAt(auditedEntity.getCreatedAt());
            auditLog.setUpdatedBy(getDetail());
            auditLog.setUpdatedAt(auditedEntity.getUpdatedAt());
            entityManager.persist(auditLog);
        }
    }
}
