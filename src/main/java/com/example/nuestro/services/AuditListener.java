package com.example.nuestro.services;

import com.example.nuestro.entities.*;
import com.example.nuestro.shared.exceptions.NuestroException;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.context.SecurityContextHolder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuditListener extends AuditingEntityListener {
    @Transient
    private static final Map<Object, Object> initialStates = new HashMap<>();
    @Autowired
    @Lazy
    private EntityManager entityManager;
    @PostLoad
    public void postLoad(Object entity) {
        if (entity instanceof User || entity instanceof Post
                || entity instanceof Like || entity instanceof Comment) {
            initialStates.put(entity, deepCopy(entity));
        }
    }

    private Object deepCopy(Object entity) {
        try {
            Class<?> entityClass = entity.getClass();
            Object copiedEntity = entityClass.getDeclaredConstructor().newInstance();

            Field[] fields = entityClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                field.set(copiedEntity, field.get(entity));
            }

            return copiedEntity;
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            // Log the exception instead of printing the stack trace

            return null;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }


    private String getOldValues(Object entity) {
        if (entity instanceof User || entity instanceof Post || entity instanceof Comment) {
            Object initialState = initialStates.get(entity);
            if (initialState != null) {
                StringBuilder differences = new StringBuilder();
                try {
                    Class<?> entityClass = entity.getClass();
                    Field[] fields = entityClass.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Object initialValue = field.get(initialState);
                        Object currentValue = field.get(entity);
                        if (!Objects.equals(initialValue, currentValue)) {
                            differences.append("Field '").append(field.getName()).append("': ")
                                    .append("Old value: ").append(initialValue).append(", ")
                                    .append("New value: ").append(currentValue).append("    ");
                        }
                    }
                } catch (IllegalAccessException e) {
                    // Log the exception instead of printing the stack trace
                    e.printStackTrace();
                }

                return differences.toString();
            }
            else
                initialStates.put(entity,deepCopy(entity));
        }
        return null;
    }
    private String getDetail() {
        var current_user = SecurityContextHolder.getContext().getAuthentication();
        if (current_user != null) {
            if (!current_user.isAuthenticated()) {
                return "Admin";
            }
            var p_user = current_user.getPrincipal();
            if ("anonymousUser".equals(p_user)) {
                return "Admin";
            }
            var user = (User) current_user.getPrincipal();
            return user.getId();
        } else {
            return "Admin";
        }
    }
    @PrePersist
    public void prePersist(Object entity) throws NuestroException {
        String oldValues = getOldValues(entity);
        logAudit(entity, oldValues, "CREATE");
    }
    @PreUpdate
    public void preUpdate(Object entity) throws NuestroException {
        // Handle pre-update event (entity modification)
        String oldValues = getOldValues(entity);
        logAudit(entity, oldValues, "UPDATE");
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    public void postCommit(Object entity) {
        // Clear initial state after the transaction is committed
        initialStates.remove(entity);
    }
    @PreRemove
    public void preRemove(Object entity) throws NuestroException {
        // Handle pre-remove event (entity deletion)
        String oldValues = getOldValues(entity);
        logAudit(entity, oldValues, "DELETE");
    }
    private void logAudit(Object entity,String oldValue, String action) throws NuestroException {
        AuditLog auditLog = new AuditLog();
        auditLog.setEntityName(entity.getClass().getSimpleName());
        auditLog.setOld_value(oldValue);
        auditLog.setAction(action);

        if (entity instanceof User) {
            User auditedEntity = (User) entity;
            auditLog.setEntityId(auditedEntity.getId());
            auditLog.setActionBy(getDetail());
            auditLog.setActionAt(LocalDateTime.now());
            entityManager.persist(auditLog);
        }
        else  if(entity instanceof Post) {
            Post auditedEntity = (Post) entity;
            auditLog.setEntityId(auditedEntity.getId());
            auditLog.setActionBy(getDetail());
            auditLog.setActionAt(LocalDateTime.now());
            entityManager.persist(auditLog);
        }
        else  if(entity instanceof Like) {
            Like auditedEntity = (Like) entity;
            auditLog.setEntityId(auditedEntity.getId());
            auditLog.setActionBy(getDetail());
            auditLog.setActionAt(LocalDateTime.now());
            entityManager.persist(auditLog);
        }
        else  if(entity instanceof Comment) {
            Comment auditedEntity = (Comment) entity;
            auditLog.setEntityId(auditedEntity.getId());
            auditLog.setActionBy(getDetail());
            auditLog.setActionAt(LocalDateTime.now());
            entityManager.persist(auditLog);
        }
        else
            throw new NuestroException("Invalid return type to store");
    }
}
