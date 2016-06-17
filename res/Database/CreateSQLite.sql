/* Create the Child-In-Time table, and select it */
CREATE TABLE IF NOT EXISTS `user` (
  `id`               INTEGER PRIMARY KEY AUTOINCREMENT,
  `username`         TEXT    NOT NULL,
  `password_hash`    TEXT    NOT NULL,
  `permission_level` INTEGER NOT NULL
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
  FOREIGN KEY (`school_id`) REFERENCES `school` (`id`)
    ON DELETE CASCADE,
  CHECK (`gender` = 0 OR `gender` = 1),
  CHECK (`is_gym` = 0 OR `is_gym` = 1)
);

CREATE TABLE IF NOT EXISTS `group` (
  `id`        INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`      TEXT    NOT NULL,
  `school_id` INTEGER NOT NULL,
  FOREIGN KEY (`school_id`) REFERENCES `school` (`id`)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `student` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `first_name` TEXT    NOT NULL,
  `last_name`  TEXT    NOT NULL,
  `gender`     INTEGER NOT NULL,
  `birthdate`  DATE    NOT NULL,
  `group_id`   INTEGER NOT NULL,
  FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)
    ON DELETE CASCADE,
  CHECK (`gender` = 0 OR `gender` = 1)
);

CREATE TABLE IF NOT EXISTS `bodystate` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `date`       DATE    NOT NULL,
  `length`     INTEGER NOT NULL,
  `weight`     INTEGER NOT NULL,
  `student_id` INTEGER NOT NULL,
  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
    ON DELETE CASCADE
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
  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
    ON DELETE CASCADE,
  FOREIGN KEY (`parkour_id`) REFERENCES `parkour` (`id`)
    ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `group_teacher` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `group_id`   INTEGER NOT NULL,
  `teacher_id` INTEGER NOT NULL,
  FOREIGN KEY (`group_id`) REFERENCES `group` (`id`)
  ON DELETE CASCADE,
  FOREIGN KEY (`teacher_id`) REFERENCES `teacher` (`id`)
  ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS `sport` (
  `id`         INTEGER PRIMARY KEY AUTOINCREMENT,
  `name`       TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS `student_sport` (
  `id` INTEGER PRIMARY KEY AUTOINCREMENT,
  `student_id`   INTEGER NOT NULL,
  `sport_id` INTEGER NOT NULL,
  FOREIGN KEY (`student_id`) REFERENCES `student` (`id`)
  ON DELETE CASCADE,
  FOREIGN KEY (`sport_id`) REFERENCES `sport` (`id`)
  ON DELETE CASCADE
);

INSERT INTO `user` VALUES
  (NULL, 'admin', '21232f297a57a5a743894a0e4a801fc3', 0);

INSERT INTO `school` VALUES
  (NULL, 'De Wegwijzer', 'Alphen aan den Rijn'),
  (NULL, 'De Hobbitburcht', 'Amsterdam'),
  (NULL, 'De Stromen', 'Den Haag');

INSERT INTO `teacher` VALUES
  (NULL, 'Henk', 'Hoornald', 1, 1, 1),
  (NULL, 'Henry', 'Hunter', 1, 1, 1),
  (NULL, 'Phillip', 'Ward', 1, 0, 1),
  (NULL, 'Carlos', 'Ryan', 1, 0, 1),
  (NULL, 'Karen', 'Diaz', 0, 1, 2),
  (NULL, 'Paula', 'Black', 0, 0, 2),
  (NULL, 'Julie', 'Sims', 0, 0, 2),
  (NULL, 'Laura', 'Garcia', 0, 1, 3),
  (NULL, 'Mark', 'Romero', 1, 0, 3),
  (NULL, 'Ashley', 'Jacobs', 0, 0, 3),
  (NULL, 'Linda', 'Jackson', 0, 0, 3);

INSERT INTO `group` VALUES
  (NULL, 'Groep 1', 1),
  (NULL, 'Groep 2', 1),
  (NULL, 'Groep 3', 1),
  (NULL, 'Groep 1', 2),
  (NULL, 'Groep 2', 2),
  (NULL, 'Groep 3', 2),
  (NULL, 'Groep 1', 3),
  (NULL, 'Groep 2', 3),
  (NULL, 'Groep 3', 3);

INSERT INTO `student` VALUES
  (NULL, 'George', 'Barnes', 1, '2014-04-17', 1),
  (NULL, 'Robin', 'Hughes', 0, '2011-01-10', 1),
  (NULL, 'Anne', 'Diaz', 0, '2015-02-26', 2),
  (NULL, 'Sandra', 'Knight', 0, '2013-08-27', 2),
  (NULL, 'Terry', 'Morales', 1, '2015-09-06', 3),
  (NULL, 'Jimmy', 'Smith', 1, '2013-04-07', 3),
  (NULL, 'Nicholas', 'Dunn', 1, '2013-09-01', 4),
  (NULL, 'Raymond', 'Franklin', 1, '2015-12-13', 4),
  (NULL, 'Maria', 'Barnes', 0, '2005-08-28', 5),
  (NULL, 'Wayne', 'Graham', 1, '2010-10-20', 5),
  (NULL, 'Kathy', 'James', 0, '2006-06-05', 6),
  (NULL, 'Evelyn', 'Elliott', 0, '2008-11-26', 6),
  (NULL, 'Barbara', 'Parker', 0, '2008-05-03', 7),
  (NULL, 'Mildred', 'Sanders', 0, '2009-08-22', 7),
  (NULL, 'Jason', 'Ross', 1, '2007-07-31', 8),
  (NULL, 'Anna', 'Porter', 0, '2008-12-21', 8),
  (NULL, 'Karen', 'Wells', 0, '2011-10-05', 8),
  (NULL, 'Jonathan', 'Ferguson', 1, '2014-02-06', 9),
  (NULL, 'Kathryn', 'Carr', 0, '2006-03-06', 9),
  (NULL, 'Sean', 'Reid', 1, '2009-04-20', 9);

INSERT INTO `bodystate` VALUES
  (NULL, '2016-01-11', 141, 38, 5),
  (NULL, '2015-06-07', 150, 46, 20),
  (NULL, '2016-02-03', 116, 41, 13),
  (NULL, '2015-08-11', 140, 50, 2),
  (NULL, '2016-04-08', 109, 46, 11),
  (NULL, '2016-05-03', 150, 32, 8),
  (NULL, '2016-02-17', 125, 39, 12),
  (NULL, '2015-09-06', 92, 31, 16),
  (NULL, '2015-08-06', 102, 47, 4),
  (NULL, '2016-02-02', 95, 47, 5),
  (NULL, '2015-08-01', 107, 33, 18),
  (NULL, '2015-06-22', 119, 44, 5),
  (NULL, '2015-08-08', 147, 40, 14),
  (NULL, '2015-12-16', 134, 46, 3),
  (NULL, '2016-02-08', 97, 31, 17),
  (NULL, '2015-11-01', 123, 38, 1),
  (NULL, '2015-07-13', 124, 33, 2),
  (NULL, '2015-12-26', 117, 40, 11),
  (NULL, '2016-04-08', 116, 35, 6),
  (NULL, '2016-04-03', 107, 49, 8);

INSERT INTO `parkour` VALUES
  (NULL, 'Parkour 1'),
  (NULL, 'Parkour 2'),
  (NULL, 'Parkour 3');

INSERT INTO `measurement` VALUES
  (1, '2016-02-09', 64, 1, 18),
  (2, '2016-02-15', 85, 1, 2),
  (3, '2015-08-11', 86, 3, 4),
  (4, '2015-06-14', 79, 2, 8),
  (5, '2015-07-21', 23, 3, 5),
  (6, '2015-06-22', 78, 3, 16),
  (7, '2016-04-15', 71, 3, 8),
  (8, '2015-08-22', 59, 2, 13),
  (9, '2016-05-03', 16, 3, 10),
  (10, '2015-09-26', 52, 3, 18),
  (11, '2016-02-04', 17, 2, 13),
  (12, '2016-03-24', 59, 2, 12),
  (13, '2016-04-01', 35, 2, 13),
  (14, '2015-10-24', 71, 1, 7),
  (15, '2016-03-29', 89, 1, 7),
  (16, '2016-04-15', 58, 2, 5),
  (17, '2016-03-11', 53, 2, 20),
  (18, '2015-06-06', 32, 2, 20),
  (19, '2016-05-19', 80, 1, 8),
  (20, '2015-06-07', 47, 3, 19);

INSERT INTO `group_teacher` VALUES
  (NULL, 1, 1),
  (NULL, 2, 2),
  (NULL, 3, 3),
  (NULL, 4, 1),
  (NULL, 5, 2),
  (NULL, 6, 3),
  (NULL, 7, 1),
  (NULL, 8, 2),
  (NULL, 9, 3);

INSERT INTO `sport` VALUES
  (NULL, 'Football'),
  (NULL, 'Hockey'),
  (NULL, 'Baseball');

INSERT INTO `student_sport` VALUES
  (NULL, 1, 1),
  (NULL, 2, 2),
  (NULL, 3, 3),
  (NULL, 4, 1),
  (NULL, 5, 2),
  (NULL, 6, 3),
  (NULL, 7, 1),
  (NULL, 8, 2),
  (NULL, 9, 3);