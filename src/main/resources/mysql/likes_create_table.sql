CREATE TABLE `likes` (
	`id` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`created_at` DATETIME(6) NULL DEFAULT NULL,
	`creator_id` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`deleted_at` DATETIME(6) NULL DEFAULT NULL,
	`post_id` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`user_id` VARCHAR(255) NOT NULL COLLATE 'utf8mb4_0900_ai_ci',
	`updated_at` DATETIME(6) NULL DEFAULT NULL,
	`updater_id` VARCHAR(255) NULL DEFAULT NULL COLLATE 'utf8mb4_0900_ai_ci',
	PRIMARY KEY (`id`) USING BTREE,
	INDEX `FKry8tnr4x2vwemv2bb0h5hyl0x` (`post_id`) USING BTREE,
	INDEX `FKnvx9seeqqyy71bij291pwiwrg` (`user_id`) USING BTREE,
	CONSTRAINT `FKnvx9seeqqyy71bij291pwiwrg` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT `FKry8tnr4x2vwemv2bb0h5hyl0x` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE
)
COLLATE='utf8mb4_0900_ai_ci'
ENGINE=InnoDB
;
