-- ---
-- Globals
-- ---

-- SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
-- SET FOREIGN_KEY_CHECKS=0;

-- ---
-- Table 'user'
-- 
-- ---

DROP TABLE IF EXISTS `user`;
    
CREATE TABLE `user` (
  `id` INTEGER NOT NULL AUTO_INCREMENT,
  `login` VARCHAR(50) NOT NULL,
  `password` VARCHAR(50) NOT NULL,
  `last_latitude` DECIMAL NULL DEFAULT NULL,
  `last_longitude` DECIMAL NULL DEFAULT NULL,
  `visible` BOOLEAN DEFAULT TRUE,
  `token` VARCHAR(64) NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
);

-- ---
-- Table 'group'
-- 
-- ---

DROP TABLE IF EXISTS `groups`;
    
CREATE TABLE `groups` (
  `id` INTEGER NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `id_user` INTEGER NOT NULL,
  PRIMARY KEY (`id`)
);

-- ---
-- Table 'group_has_users'
-- 
-- ---

DROP TABLE IF EXISTS `group_has_users`;
    
CREATE TABLE `group_has_users` (
  `id_group` INTEGER NOT NULL,
  `id_user` INTEGER NOT NULL
);

-- ---
-- Table 'pin'
-- 
-- ---

DROP TABLE IF EXISTS `pin`;
    
CREATE TABLE `pin` (
  `id` INTEGER NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `description` MEDIUMTEXT NULL DEFAULT NULL,
  `latitude` INTEGER NOT NULL,
  `longitude` INTEGER NOT NULL,
  `creation_time` DATE NULL DEFAULT NULL,
  `deletion_time` DATE NULL DEFAULT NULL,
  `id_user` INTEGER NOT NULL,
  `id_group` INTEGER NOT NULL,
  PRIMARY KEY (`id`)
);

-- ---
-- Foreign Keys 
-- ---

ALTER TABLE `group` ADD FOREIGN KEY (id_user) REFERENCES `user` (`id`);
ALTER TABLE `group_has_users` ADD FOREIGN KEY (id_group) REFERENCES `group` (`id`);
ALTER TABLE `group_has_users` ADD FOREIGN KEY (id_user) REFERENCES `user` (`id`);
ALTER TABLE `group_has_users` ADD UNIQUE `unique_index`(`id_group`, `id_user`);
ALTER TABLE `pin` ADD FOREIGN KEY (id_user) REFERENCES `user` (`id`);
ALTER TABLE `pin` ADD FOREIGN KEY (id_group) REFERENCES `group` (`id`);

-- ---
-- Table Properties
-- ---

-- ALTER TABLE `user` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `group` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `group_has_users` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
-- ALTER TABLE `pin` ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ---
-- Test Data
-- ---

-- INSERT INTO `user` (`id`,`login`,`password`,`last_longitude`,`last_longitude`) VALUES
-- ('','','','','');
-- INSERT INTO `group` (`id`,`name`,`id_user`) VALUES
-- ('','','');
-- INSERT INTO `group_has_users` (`id_group`,`id_user`) VALUES
-- ('','');
-- INSERT INTO `pin` (`id`,`name`,`description`,`latitude`,`longitude`,`creation_time`,`deletion_time`,`id_user`,`id_group`) VALUES
-- ('','','','','','','','','');