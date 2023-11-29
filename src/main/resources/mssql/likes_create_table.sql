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
    INDEX FKry8tnr4x2vwemv2bb0h5hyl0x (post_id),
    INDEX FKnvx9seeqqyy71bij291pwiwrg (user_id),
    CONSTRAINT FKnvx9seeqqyy71bij291pwiwrg FOREIGN KEY (user_id) REFERENCES users (id) ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT FKry8tnr4x2vwemv2bb0h5hyl0x FOREIGN KEY (post_id) REFERENCES posts (id) ON UPDATE NO ACTION ON DELETE CASCADE
);
