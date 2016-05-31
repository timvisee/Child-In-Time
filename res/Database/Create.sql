# TODO: Create user table (username, mail, password_hash, ...)
# TODO: Insert default user (admin, admin) ?
# TODO: Insert default parkours
# TODO: Don't drop the database, only create it (and it's tables) if they don't exist (... IF NOT EXISTS ...)
# TODO: Compare script with MySQLs script files (what useful stuff are they using)



# Create the Child-In-Time table, and select it
# TODO: Remove this in production
CREATE DATABASE IF NOT EXISTS `childintime` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE childintime;

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

CREATE TABLE IF NOT EXISTS `user` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`username` TEXT NOT NULL,
	`password_hash` TEXT NOT NULL,
	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `school` (
`id` INT NOT NULL AUTO_INCREMENT,
    	`name` TEXT NOT NULL,
    	`commune` TEXT NOT NULL,
    	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `teacher` (
	`id` INT NOT NULL AUTO_INCREMENT,
    	`first_name` TEXT NOT NULL,
    	`last_name` TEXT NOT NULL,
			`gender` TINYINT NOT NULL,
    	`is_gym` TINYINT NOT NULL,
    	`school_id` INT NOT NULL,
    	PRIMARY KEY (`id`),
    	FOREIGN KEY (`school_id`) REFERENCES `school`(`id`),
			CHECK (`gender` = 0 or `gender` = 1),
			CHECK (`is_gym` = 0 or `is_gym` = 1)
);

CREATE TABLE IF NOT EXISTS `group` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`name` TEXT NOT NULL,
    	`school_id` INT NOT NULL,
    	PRIMARY KEY (`id`),
    	FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)
);

CREATE TABLE IF NOT EXISTS `student` (
	`id` INT NOT NULL AUTO_INCREMENT,
    	`first_name` TEXT NOT NULL,
    	`last_name` TEXT NOT NULL,
    	`gender` TINYINT NOT NULL,
    	`birthdate` DATE NOT NULL,
    	`group_id` INT NOT NULL,
    	PRIMARY KEY (`id`),
    	FOREIGN KEY (`group_id`) REFERENCES `group`(`id`),
		  CHECK (`gender` = 0 or `gender` = 1)

);

CREATE TABLE IF NOT EXISTS `bodystate` (
			`id` INT NOT NULL AUTO_INCREMENT,
    	`date` DATE NOT NULL,
    	`length` SMALLINT NOT NULL,
    	`weight` SMALLINT NOT NULL,
			`student_id` INT NOT NULL,
    	PRIMARY KEY (`id`),
    	FOREIGN KEY (`student_id`) REFERENCES `student`(`id`)
);

CREATE TABLE IF NOT EXISTS `parkour` (
			`id` INT NOT NULL AUTO_INCREMENT,
    	`description` TEXT NULL,
    	PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `measurement` (
		`id` INT NOT NULL AUTO_INCREMENT,
		`date` DATE NOT NULL,
    `time` INT NOT NULL,
    `parkour_id` INT NOT NULL,
		`student_id` INT NOT NULL,
   	PRIMARY KEY (`id`),
		FOREIGN KEY	(`student_id`) REFERENCES `student`(`id`),
		FOREIGN KEY (`parkour_id`) REFERENCES `parkour`(`id`)
);

CREATE TABLE IF NOT EXISTS `group_teacher` (
			`group_id` INT NOT NULL,
    	`teacher_id` INT NOT NULL,
    	PRIMARY KEY (`group_id`, `teacher_id`),
    	FOREIGN KEY (`group_id`) REFERENCES `group`(`id`),
    	FOREIGN KEY (`teacher_id`) REFERENCES `teacher`(`id`)
);

# Drop the create meta table statement if it already exists
DROP PROCEDURE IF EXISTS createMetaTable;

# Create the create meta table statement, to easily create metadata tables
CREATE PROCEDURE createMetaTable(IN tableName VARCHAR(30))
	BEGIN

		# Build the _meta_data statement
		SET @metaDataStatement = CONCAT('CREATE TABLE IF NOT EXISTS `', tableName, '_meta_data` (
			`id` INT NOT NULL AUTO_INCREMENT,
			`field` TEXT NOT NULL,
			`type` SMALLINT NOT NULL,
			`value` TEXT NULL,
			`', tableName, '_id` INT NOT NULL,
			PRIMARY KEY (`id`),
			FOREIGN KEY (`', tableName, '_id`) REFERENCES `', tableName, '`(`id`)
		);');

		# Build the _meta_field statement
		SET @metaFieldStatement = CONCAT('CREATE TABLE IF NOT EXISTS `', tableName, '_meta_field` (
			`id` INT NOT NULL AUTO_INCREMENT,
			`name` TEXT NOT NULL,
			`type` SMALLINT NOT NULL,
			`default` TEXT NULL,
			`allow_null` TINYINT DEFAULT 1 NOT NULL,
			PRIMARY KEY (`id`)
		);');

		# Build the _meta_value statement
		SET @metaValueStatement = CONCAT('CREATE TABLE IF NOT EXISTS `', tableName, '_meta_value` (
			`id` INT NOT NULL AUTO_INCREMENT,
			`value` TEXT NOT NULL,
			`field_id` INT NOT NULL,
			PRIMARY KEY (`id`),
			FOREIGN KEY (`field_id`) REFERENCES `', tableName,'_meta_field`(`id`)
		);');

		# Prepare the statements
		PREPARE metaDataPrepared FROM @metaDataStatement;
		PREPARE metaFieldPrepared FROM @metaFieldStatement;
		PREPARE metaValuePrepared FROM @metaValueStatement;

		# Execute the statements
		EXECUTE metaDataPrepared;
		EXECUTE metaFieldPrepared;
		EXECUTE metaValuePrepared;

		# Deallocate the statements
		DEALLOCATE PREPARE metaDataPrepared;
		DEALLOCATE PREPARE metaFieldPrepared;
		DEALLOCATE PREPARE metaValuePrepared;

	END;
;

# Create meta data tables for database objects
CALL createMetaTable('student');
CALL createMetaTable('teacher');
CALL createMetaTable('group');
CALL createMetaTable('school');
CALL createMetaTable('bodystate');
CALL createMetaTable('parkour');
CALL createMetaTable('measurement');

# Drop the create meta table procedure, we aren't using it anymore
DROP PROCEDURE createMetaTable;

# Insert data

LOCK TABLES `school` WRITE;
INSERT INTO `school` VALUES
	(NULL, 'De Wegwijzer', 'Alphen aan den Rijn'),
	(NULL, 'De Hobbitburcht', 'Amsterdam'),
	(NULL, 'De Stromen', 'Den Haag');
UNLOCK TABLES;

LOCK TABLES `teacher` WRITE;
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
UNLOCK TABLES;

LOCK TABLES `group` WRITE;
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
UNLOCK TABLES;

LOCK TABLES `student` WRITE;
INSERT INTO `student` VALUES
	(NULL, 'George', 'Barnes', 1, '2014-4-17', 1),
	(NULL, 'Robin', 'Hughes', 0, '2011-1-10', 1),
	(NULL, 'Anne', 'Diaz', 0, '2015-2-26', 2),
	(NULL, 'Sandra', 'Knight', 0, '2013-8-27', 2),
	(NULL, 'Terry', 'Morales', 1, '2015-9-6', 3),
	(NULL, 'Jimmy', 'Smith', 1, '2013-4-7', 3),
	(NULL, 'Nicholas', 'Dunn', 1, '2013-9-1', 4),
	(NULL, 'Raymond', 'Franklin', 1, '2015-12-13', 4),
	(NULL, 'Maria', 'Barnes', 0, '2005-8-28', 5),
	(NULL, 'Wayne', 'Graham', 1, '2010-10-20', 5),
	(NULL, 'Kathy', 'James', 0, '2006-6-5', 6),
	(NULL, 'Evelyn', 'Elliott', 0, '2008-11-26', 6),
	(NULL, 'Barbara', 'Parker', 0, '2008-5-3', 7),
	(NULL, 'Mildred', 'Sanders', 0, '2009-8-22', 7),
	(NULL, 'Jason', 'Ross', 1, '2007-7-31', 8),
	(NULL, 'Anna', 'Porter', 0, '2008-12-21', 8),
	(NULL, 'Karen', 'Wells', 0, '2011-10-5', 8),
	(NULL, 'Jonathan', 'Ferguson', 1, '2014-2-6', 9),
	(NULL, 'Kathryn', 'Carr', 0, '2006-3-6', 9),
	(NULL, 'Sean', 'Reid', 1, '2009-4-20', 9);
UNLOCK TABLES;



LOCK TABLES `bodystate` WRITE;
INSERT INTO `bodystate` VALUES
	(NULL, '2016-1-11', 141, 38, 5),
	(NULL, '2015-6-7', 150, 46, 20),
	(NULL, '2016-2-3', 116, 41, 13),
	(NULL, '2015-8-11', 140, 50, 2),
	(NULL, '2016-4-8', 109, 46, 11),
	(NULL, '2016-5-3', 150, 32, 8),
	(NULL, '2016-2-17', 125, 39, 12),
	(NULL, '2015-9-6', 92, 31, 16),
	(NULL, '2015-8-6', 102, 47, 4),
	(NULL, '2016-2-2', 95, 47, 5),
	(NULL, '2015-8-1', 107, 33, 18),
	(NULL, '2015-6-22', 119, 44, 5),
	(NULL, '2015-8-8', 147, 40, 14),
	(NULL, '2015-12-16', 134, 46, 3),
	(NULL, '2016-2-8', 97, 31, 17),
	(NULL, '2015-11-1', 123, 38, 1),
	(NULL, '2015-7-13', 124, 33, 2),
	(NULL, '2015-12-26', 117, 40, 11),
	(NULL, '2016-4-8', 116, 35, 6),
	(NULL, '2016-4-3', 107, 49, 8);
UNLOCK TABLES;

LOCK TABLES `parkour` WRITE;
INSERT INTO `parkour` VALUES
	(NULL, NULL),
	(NULL, NULL),
	(NULL, NULL);
UNLOCK TABLES;

LOCK TABLES `measurement` WRITE;
INSERT INTO `measurement` VALUES
	(1, '2016-2-9', 64, 1, 18),
	(2, '2016-2-15', 85, 1, 2),
	(3, '2015-8-11', 86, 3, 4),
	(4, '2015-6-14', 79, 2, 8),
	(5, '2015-7-21', 23, 3, 5),
	(6, '2015-6-22', 78, 3, 16),
	(7, '2016-4-15', 71, 3, 8),
	(8, '2015-8-22', 59, 2, 13),
	(9, '2016-5-3', 16, 3, 10),
	(10, '2015-9-26', 52, 3, 18),
	(11, '2016-2-4', 17, 2, 13),
	(12, '2016-3-24', 59, 2, 12),
	(13, '2016-4-1', 35, 2, 13),
	(14, '2015-10-24', 71, 1, 7),
	(15, '2016-3-29', 89, 1, 7),
	(16, '2016-4-15', 58, 2, 5),
	(17, '2016-3-11', 53, 2, 20),
	(18, '2015-6-6', 32, 2, 20),
	(19, '2016-5-19', 80, 1, 8),
	(20, '2015-6-7', 47, 3, 19);
	UNLOCK TABLES;

LOCK TABLES `group_teacher` WRITE;
INSERT INTO `group_teacher` VALUES
	(1, 1),
	(2, 2),
	(3, 3),
	(4, 1),
	(5, 2),
	(6, 3),
	(7, 1),
	(8, 2),
	(9, 3);
UNLOCK TABLES;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;