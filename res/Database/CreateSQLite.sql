/* TODO: Create user table (username, mail, password_hash, ...) */
/* TODO: Insert default user (admin, admin) ? */
/* TODO: Insert default parkours */
/* TODO: Don't drop the database, only create it (and it's tables) if they don't exist (... IF NOT EXISTS ...) */
/* TODO: Compare script with MySQLs script files (what useful stuff are they using) */



/* Create the Child-In-Time table, and select it */
/* TODO: Remove this in production */

CREATE TABLE IF NOT EXISTS `user` (
	`id` INTEGER PRIMARY KEY AUTOINCREMENT,
	`username` TEXT NOT NULL,
	`password_hash` TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS `school` (
	`id` INTEGER PRIMARY KEY AUTOINCREMENT,
	`name` TEXT NOT NULL,
	`commune` TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS `teacher` (
	`id` INTEGER PRIMARY KEY AUTOINCREMENT,
	`first_name` TEXT NOT NULL,
	`last_name` TEXT NOT NULL,
	`gender` INTEGER NOT NULL,
	`is_gym` INTEGER NOT NULL,
	`school_id` INTEGER NOT NULL,
	FOREIGN KEY (`school_id`) REFERENCES `school`(`id`),
	CHECK(`gender` = 0 OR `gender` = 1),
	CHECK(`is_gym` = 0 OR `is_gym` = 1)
);

CREATE TABLE IF NOT EXISTS `group` (
	`id` INTEGER PRIMARY KEY AUTOINCREMENT,
	`name` TEXT NOT NULL,
	`school_id` INTEGER NOT NULL,
	FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)
);

CREATE TABLE IF NOT EXISTS `student` (
	`id` INTEGER PRIMARY KEY AUTOINCREMENT,
	`first_name` TEXT NOT NULL,
	`last_name` TEXT NOT NULL,
	`gender` INTEGER NOT NULL,
	`birthdate` DATE NOT NULL,
	`group_id` INTEGER NOT NULL,
	FOREIGN KEY (`group_id`) REFERENCES `group`(`id`),
	CHECK(`gender` = 0 OR `gender` = 1)
);

CREATE TABLE IF NOT EXISTS `bodystate` (
	`id` INTEGER PRIMARY KEY AUTOINCREMENT,
	`date` DATE NOT NULL,
	`length` INTEGER NOT NULL,
	`weight` INTEGER NOT NULL,
	`student_id` INTEGER NOT NULL,
	FOREIGN KEY (`student_id`) REFERENCES `student`(`id`)
);

CREATE TABLE IF NOT EXISTS `parkour` (
	`id` INTEGER PRIMARY KEY AUTOINCREMENT,
	`description` TEXT
);

CREATE TABLE IF NOT EXISTS `measurement` (
	`id` INTEGER AUTOINCREMENT NOT NULL,
	`student_id` INT NOT NULL,
	`date` DATE NOT NULL,
    	`time` TIME NOT NULL,
    	`parkour_id` INT NOT NULL,
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

/* Drop the create meta table statement if it already exists */
DROP PROCEDURE IF EXISTS createMetaTable;

/* Create the create meta table statement, to easily create metadata tables */
CREATE PROCEDURE createMetaTable(IN tableName VARCHAR(30))
	BEGIN

		# Build the _meta_data statement
		SET @metaDataStatement = CONCAT('CREATE TABLE `', tableName, '_meta_data` (
			`id` INT NOT NULL,
			`field` TEXT NOT NULL,
			`type` SMALLINT NOT NULL,
			`value` TEXT NULL,
			`', tableName, '_id` int(9) NOT NULL,
			PRIMARY KEY (`id`),
			FOREIGN KEY (`', tableName, '_id`) REFERENCES `', tableName, '`(`id`)
		);');

		# Build the _meta_field statement
		SET @metaFieldStatement = CONCAT('CREATE TABLE `', tableName, '_meta_field` (
			`id` INT NOT NULL,
			`name` TEXT NOT NULL,
			`type` SMALLINT NOT NULL,
			`default` TEXT NULL,
			`allow_null` TINYINT DEFAULT 1 NOT NULL,
			PRIMARY KEY (`id`)
		);');

		# Build the _meta_value statement
		SET @metaValueStatement = CONCAT('CREATE TABLE `', tableName, '_meta_value` (
			`id` INT NOT NULL,
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

/* Create meta data tables for database objects */
CALL createMetaTable('student');
CALL createMetaTable('teacher');
CALL createMetaTable('group');
CALL createMetaTable('school');
CALL createMetaTable('bodystate');
CALL createMetaTable('parkour');
CALL createMetaTable('measurement');

/* Drop the create meta table procedure, we aren't using it anymore */
DROP PROCEDURE createMetaTable;

/* Insert data */