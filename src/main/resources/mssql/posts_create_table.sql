CREATE TABLE posts (
    id NVARCHAR(255) NOT NULL,
    created_at DATETIME2(6) NULL,
    creator_id NVARCHAR(255) NULL,
    deleted_at DATETIME2(6) NULL,
    updated_at DATETIME2(6) NULL,
    updater_id NVARCHAR(255) NULL,
    content NVARCHAR(255) NULL,
    user_id NVARCHAR(255) NULL,
    PRIMARY KEY (id),
    INDEX content_index (content),
    CONSTRAINT FK5lidm6cqbc7u4xhqpxm898qme FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE CASCADE
);
