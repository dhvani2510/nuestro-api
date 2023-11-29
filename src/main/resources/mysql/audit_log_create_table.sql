-- Create the audit log table
CREATE TABLE `audit_log` (
    `id` BIGINT(19) NOT NULL AUTO_INCREMENT,
    `action` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `action_at` DATETIME(6) NULL DEFAULT NULL,
    `action_by` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `entity_id` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `entity_name` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
    `old_value` TEXT NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
    PRIMARY KEY (`id`) USING BTREE
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
AUTO_INCREMENT=1;


-- Create the trigger for the users table
DELIMITER //
CREATE TRIGGER `users_after_update` AFTER UPDATE ON `users`
FOR EACH ROW
BEGIN
    DECLARE old_user_content TEXT;
    SET old_user_content = CONCAT('Name: ', OLD.first_name, ' ', OLD.last_name, ', Email: ', OLD.email);

    INSERT INTO `audit_log` (
        `action`, `action_at`, `action_by`,
        `entity_id`, `entity_name`, `old_value`
    ) VALUES (
        'UPDATE', NOW(6), USER(),
        OLD.id, 'users', old_user_content
    );
END;
//
DELIMITER ;

-- Create the trigger for the posts table
DELIMITER //
CREATE TRIGGER `posts_after_update` AFTER UPDATE ON `posts`
FOR EACH ROW
BEGIN
    DECLARE old_post_content TEXT;
    SET old_post_content = CONCAT('Content: ', OLD.content);

    INSERT INTO `audit_log` (
        `action`, `action_at`, `action_by`,
        `entity_id`, `entity_name`, `old_value`
    ) VALUES (
        'UPDATE', NOW(6), USER(),
        OLD.id, 'posts', old_post_content
    );
END;
//
DELIMITER ;

-- Create the trigger for the comments table
DELIMITER //
CREATE TRIGGER `comments_after_update` AFTER UPDATE ON `comments`
FOR EACH ROW
BEGIN
    DECLARE old_comment_content TEXT;
    SET old_comment_content = CONCAT('Content: ', OLD.comment);

    INSERT INTO `audit_log` (
        `action`, `action_at`, `action_by`,
        `entity_id`, `entity_name`, `old_value`
    ) VALUES (
        'UPDATE', NOW(6), USER(),
        OLD.id, 'comments', old_comment_content
    );
END;
//
DELIMITER ;

-- Create the trigger for the likes table
DELIMITER //
CREATE TRIGGER `likes_after_update` AFTER UPDATE ON `likes`
FOR EACH ROW
BEGIN
    -- No need to declare a variable for likes as the old value is set to NULL

    INSERT INTO `audit_log` (
        `action`, `action_at`, `action_by`,
        `entity_id`, `entity_name`, `old_value`
    ) VALUES (
        'UPDATE', NOW(6), USER(),
        OLD.id, 'likes', NULL
    );
END;
//
DELIMITER ;