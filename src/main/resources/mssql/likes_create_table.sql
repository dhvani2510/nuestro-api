CREATE TABLE likes (
    id NVARCHAR(255) NOT NULL,
    created_at DATETIME2(6) NULL,
    creator_id NVARCHAR(255) NULL,
    deleted_at DATETIME2(6) NULL,
    updated_at DATETIME2(6) NULL,
    updater_id NVARCHAR(255) NULL,
    post_id NVARCHAR(255) NOT NULL,
    user_id NVARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    INDEX FK_post_id (post_id),
    INDEX FK_user_id (user_id),
    CONSTRAINT FK_user_id_likes FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT FK_post_id_likes FOREIGN KEY (post_id) REFERENCES posts (id) ON UPDATE NO ACTION ON DELETE CASCADE
);
