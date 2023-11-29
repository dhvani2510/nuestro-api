CREATE TABLE audit_log (
    id BIGINT NOT NULL IDENTITY,
    action VARCHAR(255) NULL DEFAULT NULL,
    action_at DATETIME2(6) NULL DEFAULT NULL,
    action_by VARCHAR(255) NULL DEFAULT NULL,
    entity_id VARCHAR(255) NULL DEFAULT NULL,
    entity_name VARCHAR(255) NULL DEFAULT NULL,
    old_value VARCHAR(max) NULL DEFAULT NULL,
    PRIMARY KEY (id)
);

DROP TRIGGER IF EXISTS users_after_update;
CREATE TRIGGER IF NOT EXISTS users_after_update AFTER UPDATE ON
FOR EACH ROW
BEGIN
    DECLARE @old_user_content VARCHAR(max);
    SET @old_user_content = CONCAT('Name: ', OLD.first_name, ' ', OLD.last_name, ', Email: ', OLD.email);

    INSERT INTO audit_log (
        action, action_at, action_by,
        entity_id, entity_name, old_value
    ) VALUES (
        'UPDATE', GETDATE(6), dbo.USER(),
        OLD.id, 'users', @old_user_content
    );
END;

DROP TRIGGER IF EXISTS posts_after_update;
CREATE TRIGGER IF NOT EXISTS posts_after_update AFTER UPDATE ON
FOR EACH ROW
BEGIN
    DECLARE @old_post_content VARCHAR(max);
    SET @old_post_content = CONCAT('Content: ', OLD.content);

    INSERT INTO audit_log (
        action, action_at, action_by,
        entity_id, entity_name, old_value
    ) VALUES (
        'UPDATE', GETDATE(6), dbo.USER(),
        OLD.id, 'posts', @old_post_content
    );
END;

DROP TRIGGER IF EXISTS comments_after_update;
CREATE TRIGGER IF NOT EXISTS comments_after_update AFTER UPDATE ON
FOR EACH ROW
BEGIN
    DECLARE @old_comment_content VARCHAR(max);
    SET @old_comment_content = CONCAT('Content: ', OLD.comment);

    INSERT INTO audit_log (
        action, action_at, action_by,
        entity_id, entity_name, old_value
    ) VALUES (
        'UPDATE', GETDATE(6), dbo.USER(),
        OLD.id, 'comments', @old_comment_content
    );
END;

DROP TRIGGER IF EXISTS likes_after_updateNuestro1;
CREATE TRIGGER IF NOT EXISTS likes_after_update AFTER UPDATE ON
FOR EACH ROW
BEGIN
    INSERT INTO audit_log (
        action, action_at, action_by,
        entity_id, entity_name, old_value
    ) VALUES (
        'UPDATE', GETDATE(6), dbo.USER(),
        OLD.id, 'likes', NULL
    );
END;
