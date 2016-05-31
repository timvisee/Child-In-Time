/* TODO: Create user table (username, mail, password_hash, ...) */
/* TODO: Insert default user (admin, admin) ? */
/* TODO: Insert default parkours */
/* TODO: Don't drop the database, only create it (and it's tables) if they don't exist (... IF NOT EXISTS ...) */
/* TODO: Compare script with MySQLs script files (what useful stuff are they using) */



/* Create the Child-In-Time table, and select it */
/* TODO: Remove this in production */

CREATE TABLE IF NOT EXISTS `user` (
  `id`            INTEGER PRIMARY KEY AUTOINCREMENT,
  `username`      TEXT NOT NULL,
  `password_hash` TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS `school` (
  `id`      INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`    TEXT NOT NULL,
  `commune` TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS `teacher` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `first_name` TEXT    NOT NULL,
  `last_name`  TEXT    NOT NULL,
  `gender`     INTEGER NOT NULL,
  `is_gym`     INTEGER NOT NULL,
  `school_id`  INTEGER NOT NULL,
  FOREIGN KEY (`school_id`) REFERENCES `school` (`id`),
  CHECK (`gender` = 0 OR `gender` = 1),
  CHECK (`is_gym` = 0 OR `is_gym` = 1)
);

CREATE TABLE IF NOT EXISTS `group` (
  `id`        INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`      TEXT    NOT NULL,
  `school_id` INTEGER NOT NULL,
  FOREIGN KEY (`school_id`) REFERENCES `school` (`id`)
);

CREATE TABLE IF NOT EXISTS `student` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `first_name` TEXT    NOT NULL,
  `last_name`  TEXT    NOT NULL,
  `gender`     INTEGER NOT NULL,
  `birthdate`  DATE    NOT NULL,
  `group_id`   INTEGER NOT NULL,
  FOREIGN KEY (`group_id`) REFERENCES `group` (`id`),
  CHECK (`gender` = 0 OR `gender` = 1)
);

CREATE TABLE IF NOT EXISTS `bodystate` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `date`       DATE    NOT NULL,
  `length`     INTEGER NOT NULL,
  `weight`     INTEGER NOT NULL,
  `student_id` INTEGER NOT NULL,
  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
);

CREATE TABLE IF NOT EXISTS `parkour` (
  `id`          INTEGER PRIMARY KEY AUTOINCREMENT,
  `description` TEXT
);

CREATE TABLE IF NOT EXISTS `measurement` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `date`       DATE    NOT NULL,
  `time`       INTEGER NOT NULL,
  `parkour_id` INTEGER NOT NULL,
  `student_id` INTEGER NOT NULL,
  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`),
  FOREIGN KEY (`parkour_id`) REFERENCES `parkour` (`id`)
);

CREATE TABLE IF NOT EXISTS `group_teacher` (
  `group_id`   INTEGER NOT NULL,
  `teacher_id` INTEGER NOT NULL,
  PRIMARY KEY (`group_id`, `teacher_id`),
  FOREIGN KEY (`group_id`) REFERENCES `group` (`id`),
  FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)
);

CREATE TABLE IF NOT EXISTS `user_meta_data` (
  `id`      INTEGER PRIMARY KEY AUTOINCREMENT,
  `field`   TEXT    NOT NULL,
  `type`    INTEGER NOT NULL,
  `value`   TEXT,
  `user_id` INTEGER NOT NULL,
  FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
);

CREATE TABLE IF NOT EXISTS `user_meta_field` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`       TEXT    NOT NULL,
  `type`       INTEGER NOT NULL,
  `default`    TEXT,
  `allow_null` INTEGER             DEFAULT 1 NOT NULL
);

CREATE TABLE IF NOT EXISTS `user_meta_value` (
  `id`       INTEGER PRIMARY KEY AUTOINCREMENT,
  `value`    TEXT    NOT NULL,
  `field_id` INTEGER NOT NULL,
  FOREIGN KEY (`field_id`) REFERENCES `user_meta_field` (`id`)
);