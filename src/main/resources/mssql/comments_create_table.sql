CREATE TABLE comments (
    id NVARCHAR(255) NOT NULL,
    created_at DATETIME2(6) NULL,
    creator_id NVARCHAR(255) NULL,
    deleted_at DATETIME2(6) NULL,
    updated_at DATETIME2(6) NULL,
    updater_id NVARCHAR(255) NULL,
    comment NVARCHAR(255) NULL,
    post_id NVARCHAR(255) NULL,
    user_id NVARCHAR(255) NULL,
    PRIMARY KEY (id),
    INDEX FKh4c7lvsc298whoyd4w9ta25cr (post_id),
    INDEX FK8omq0tc18jd43bu5tjh6jvraq (user_id),
    CONSTRAINT FK8omq0tc18jd43bu5tjh6jvraq FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE CASCADE,
    CONSTRAINT FKh4c7lvsc298whoyd4w9ta25cr FOREIGN KEY (post_id) REFERENCES posts (id) ON UPDATE NO ACTION ON DELETE CASCADE
);
