    CREATE TABLE posts (
        id VARCHAR(255) NOT NULL,
        created_at DATETIME(6) NULL DEFAULT NULL,
        creator_id VARCHAR(255) NULL DEFAULT NULL,
        deleted_at DATETIME(6) NULL DEFAULT NULL,
        content VARCHAR(255) NULL DEFAULT NULL,
        user_id VARCHAR(255) NULL DEFAULT NULL,
        PRIMARY KEY (id),
        CONSTRAINT FK5lidm6cqbc7u4xhqpxm898qme FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION
    );