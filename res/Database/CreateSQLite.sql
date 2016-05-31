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

-- _meta_data table prototype
/*CREATE TABLE IF NOT EXISTS `MYTABLENAME_meta_data` (
  `id`      INTEGER PRIMARY KEY AUTOINCREMENT,
  `field`   TEXT    NOT NULL,
  `type`    INTEGER NOT NULL,
  `value`   TEXT,
  `MYTABLENAME_id` INTEGER NOT NULL,
  FOREIGN KEY (`MYTABLENAME_id`) REFERENCES `MYTABLENAME` (`id`)
);*/

-- _meta_field table prototype
/*CREATE TABLE IF NOT EXISTS `MYTABLENAME_meta_field` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`       TEXT    NOT NULL,
  `type`       INTEGER NOT NULL,
  `default`    TEXT,
  `allow_null` INTEGER             DEFAULT 1 NOT NULL
);*/

-- _meta_value table prototype
/*CREATE TABLE IF NOT EXISTS `MYTABLENAME_meta_value` (
  `id`       INTEGER PRIMARY KEY AUTOINCREMENT,
  `value`    TEXT    NOT NULL,
  `field_id` INTEGER NOT NULL,
  FOREIGN KEY (`field_id`) REFERENCES `MYTABLENAME_meta_field` (`id`)
);*/

CREATE TABLE IF NOT EXISTS `student_meta_data` (
  `id`      INTEGER PRIMARY KEY AUTOINCREMENT,
  `field`   TEXT    NOT NULL,
  `type`    INTEGER NOT NULL,
  `value`   TEXT,
  `student_id` INTEGER NOT NULL,
  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
);

CREATE TABLE IF NOT EXISTS `student_meta_field` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`       TEXT    NOT NULL,
  `type`       INTEGER NOT NULL,
  `default`    TEXT,
  `allow_null` INTEGER             DEFAULT 1 NOT NULL
);

CREATE TABLE IF NOT EXISTS `student_meta_value` (
  `id`       INTEGER PRIMARY KEY AUTOINCREMENT,
  `value`    TEXT    NOT NULL,
  `field_id` INTEGER NOT NULL,
  FOREIGN KEY (`field_id`) REFERENCES `student_meta_field` (`id`)
);

CREATE TABLE IF NOT EXISTS `teacher_meta_data` (
  `id`      INTEGER PRIMARY KEY AUTOINCREMENT,
  `field`   TEXT    NOT NULL,
  `type`    INTEGER NOT NULL,
  `value`   TEXT,
  `teacher_id` INTEGER NOT NULL,
  FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)
);

CREATE TABLE IF NOT EXISTS `teacher_meta_field` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`       TEXT    NOT NULL,
  `type`       INTEGER NOT NULL,
  `default`    TEXT,
  `allow_null` INTEGER             DEFAULT 1 NOT NULL
);

CREATE TABLE IF NOT EXISTS `teacher_meta_value` (
  `id`       INTEGER PRIMARY KEY AUTOINCREMENT,
  `value`    TEXT    NOT NULL,
  `field_id` INTEGER NOT NULL,
  FOREIGN KEY (`field_id`) REFERENCES `teacher_meta_field` (`id`)
);

CREATE TABLE IF NOT EXISTS `group_meta_data` (
  `id`      INTEGER PRIMARY KEY AUTOINCREMENT,
  `field`   TEXT    NOT NULL,
  `type`    INTEGER NOT NULL,
  `value`   TEXT,
  `group_id` INTEGER NOT NULL,
  FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)
);

CREATE TABLE IF NOT EXISTS `group_meta_field` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`       TEXT    NOT NULL,
  `type`       INTEGER NOT NULL,
  `default`    TEXT,
  `allow_null` INTEGER             DEFAULT 1 NOT NULL
);

CREATE TABLE IF NOT EXISTS `group_meta_value` (
  `id`       INTEGER PRIMARY KEY AUTOINCREMENT,
  `value`    TEXT    NOT NULL,
  `field_id` INTEGER NOT NULL,
  FOREIGN KEY (`field_id`) REFERENCES `group_meta_field` (`id`)
);

CREATE TABLE IF NOT EXISTS `school_meta_data` (
  `id`      INTEGER PRIMARY KEY AUTOINCREMENT,
  `field`   TEXT    NOT NULL,
  `type`    INTEGER NOT NULL,
  `value`   TEXT,
  `school_id` INTEGER NOT NULL,
  FOREIGN KEY (`school_id`) REFERENCES `school` (`id`)
);

CREATE TABLE IF NOT EXISTS `school_meta_field` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`       TEXT    NOT NULL,
  `type`       INTEGER NOT NULL,
  `default`    TEXT,
  `allow_null` INTEGER             DEFAULT 1 NOT NULL
);

CREATE TABLE IF NOT EXISTS `school_meta_value` (
  `id`       INTEGER PRIMARY KEY AUTOINCREMENT,
  `value`    TEXT    NOT NULL,
  `field_id` INTEGER NOT NULL,
  FOREIGN KEY (`field_id`) REFERENCES `school_meta_field` (`id`)
);

CREATE TABLE IF NOT EXISTS `bodystate_meta_data` (
  `id`      INTEGER PRIMARY KEY AUTOINCREMENT,
  `field`   TEXT    NOT NULL,
  `type`    INTEGER NOT NULL,
  `value`   TEXT,
  `bodystate_id` INTEGER NOT NULL,
  FOREIGN KEY (`bodystate_id`) REFERENCES `bodystate` (`id`)
);

CREATE TABLE IF NOT EXISTS `bodystate_meta_field` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`       TEXT    NOT NULL,
  `type`       INTEGER NOT NULL,
  `default`    TEXT,
  `allow_null` INTEGER             DEFAULT 1 NOT NULL
);

CREATE TABLE IF NOT EXISTS `bodystate_meta_value` (
  `id`       INTEGER PRIMARY KEY AUTOINCREMENT,
  `value`    TEXT    NOT NULL,
  `field_id` INTEGER NOT NULL,
  FOREIGN KEY (`field_id`) REFERENCES `bodystate_meta_field` (`id`)
);

CREATE TABLE IF NOT EXISTS `parkour_meta_data` (
  `id`      INTEGER PRIMARY KEY AUTOINCREMENT,
  `field`   TEXT    NOT NULL,
  `type`    INTEGER NOT NULL,
  `value`   TEXT,
  `parkour_id` INTEGER NOT NULL,
  FOREIGN KEY (`parkour_id`) REFERENCES `parkour` (`id`)
);

CREATE TABLE IF NOT EXISTS `parkour_meta_field` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`       TEXT    NOT NULL,
  `type`       INTEGER NOT NULL,
  `default`    TEXT,
  `allow_null` INTEGER             DEFAULT 1 NOT NULL
);

CREATE TABLE IF NOT EXISTS `parkour_meta_value` (
  `id`       INTEGER PRIMARY KEY AUTOINCREMENT,
  `value`    TEXT    NOT NULL,
  `field_id` INTEGER NOT NULL,
  FOREIGN KEY (`field_id`) REFERENCES `parkour_meta_field` (`id`)
);

CREATE TABLE IF NOT EXISTS `measurement_meta_data` (
  `id`      INTEGER PRIMARY KEY AUTOINCREMENT,
  `field`   TEXT    NOT NULL,
  `type`    INTEGER NOT NULL,
  `value`   TEXT,
  `measurement_id` INTEGER NOT NULL,
  FOREIGN KEY (`measurement_id`) REFERENCES `measurement` (`id`)
);

CREATE TABLE IF NOT EXISTS `measurement_meta_field` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`       TEXT    NOT NULL,
  `type`       INTEGER NOT NULL,
  `default`    TEXT,
  `allow_null` INTEGER             DEFAULT 1 NOT NULL
);

CREATE TABLE IF NOT EXISTS `measurement_meta_value` (
  `id`       INTEGER PRIMARY KEY AUTOINCREMENT,
  `value`    TEXT    NOT NULL,
  `field_id` INTEGER NOT NULL,
  FOREIGN KEY (`field_id`) REFERENCES `measurement_meta_field` (`id`)
);

INSERT INTO `user` VALUES
  (NULL, 'admin', '21232f297a57a5a743894a0e4a801fc3');

INSERT INTO `school` VALUES
  (NULL, 'De Wegwijzer', 'Alphen aan den Rijn'),
  (NULL, 'De Hobbitburcht', 'Amsterdam'),
  (NULL, 'De Stromen', 'Den Haag');