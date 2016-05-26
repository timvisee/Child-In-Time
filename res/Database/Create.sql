DROP DATABASE `childintime`;
CREATE DATABASE `childintime`;
USE childintime;

create table `school` (
`id` int(9) NOT NULL AUTO_INCREMENT,
    	`name` varchar(30) NOT NULL,
    	`commune` varchar(30) NOT NULL,
    	PRIMARY KEY (`id`)
 );


create table `teacher` (
	`id` int(9) NOT NULL AUTO_INCREMENT,
    	`first_name` varchar(30) NOT NULL,
    	`last_name` varchar(30) NOT NULL,
    	`is_gym` char(1) NOT NULL,
    	`school_id` int(9) NOT NULL,
    	PRIMARY KEY (`id`),
    	FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)

);

create table `group` (
	`id` int(9) NOT NULL AUTO_INCREMENT,
	`name` varchar(30) NOT NULL,
    	`school_id` int(9) NOT NULL,
    	PRIMARY KEY (`id`),
    	FOREIGN KEY (`school_id`) REFERENCES `school`(`id`)
);

create table `student` (
	`id` int(9) NOT NULL AUTO_INCREMENT,
    	`first_name` varchar(30) NOT NULL,
    	`last_name` varchar(30) NOT NULL,
    	`gender` int(1) NOT NULL,
    	`birthdate` date NOT NULL,
    	`group_id` int(9) NOT NULL,
    	PRIMARY KEY (`id`),
    	FOREIGN KEY (`group_id`) REFERENCES `group`(`id`)

);

create table `bodystate` (
	`student_id` int(9) NOT NULL,
    	`date` date NOT NULL,
    	`length` decimal(2,1) NOT NULL,
    	`weight` int(3) NOT NULL,
    	PRIMARY KEY (`student_id`, `date`),
    	FOREIGN KEY (`student_id`) REFERENCES `student`(`id`)
);

create table `parkour` (
	`id` int(9) NOT NULL AUTO_INCREMENT,
    	`description` varchar(200) NULL,
    	PRIMARY KEY (`id`)
);

create table `measurement` (
	`student_id` int(9) NOT NULL,
	`date` date NOT NULL,
    	`time` time NOT NULL,
    	`parkour_id` int(9) NOT NULL,
   	PRIMARY KEY (`student_id`, `date`),
    	FOREIGN KEY	(`student_id`) REFERENCES `student`(`id`),
    	FOREIGN KEY (`parkour_id`) REFERENCES `parkour`(`id`)
);

create table `group_teacher` (
	`group_id` int(9) NOT NULL,
    	`teacher_id` int(9) NOT NULL,
    	PRIMARY KEY (`group_id`, `teacher_id`),
    	FOREIGN KEY (`group_id`) REFERENCES `group`(`id`),
    	FOREIGN KEY (`teacher_id`) REFERENCES `teacher`(`id`)
);




create procedure createMetaTable(tableName VARCHAR(30))
	BEGIN

		PREPARE table1 FROM 'CREATE TABLE `?` (
			`id` int(9) NOT NULL,
			`field` varchar(30) NOT NULL,
			`type` varchar(30) NOT NULL,
			`value` varchar(30) NULL,
			`?` int(9) NOT NULL,
			PRIMARY KEY (`id`),
			FOREIGN KEY (`?`) REFERENCES `?`(`id`)
		);';

		PREPARE table2 FROM 'CREATE TABLE `?` (
			`id` int(9) NOT NULL,
			`name` varchar(30) NOT NULL,
			`type` varchar(30) NOT NULL,
			`default` varchar(30) NOT NULL,
			`allowNull` enum(''Y'', ''N''),
			PRIMARY KEY (`id`),
		);';

		PREPARE table3 FROM 'CREATE TABLE `?` (
			`id` int(9) NOT NULL,
			`value` varchar(30) NOT NULL,
			`field_id` int(9) NOT NULL,
			PRIMARY KEY (`id`),
			FOREIGN KEY (`field_id`) REFERENCES `?`(`id`)
		);';


		SET @a = CONCAT(tableName, '_meta_data');
		SET @b = CONCAT(tableName, '_id');
		EXECUTE table1 USING @a, @b, @b, tableName;

		SET @c = CONCAT(tableName, '_meta_field');
		EXECUTE table2 USING @c;

		SET @d = CONCAT(tableName, '_meta_value');
		SET @e = CONCAT(tableName, '_meta_field');
		EXECUTE table3 using @d, @e;

	END;
;

CALL createMetaTable('student');
CALL createMetaTable('teacher');
CALL createMetaTable('group');
CALL createMetaTable('school');
CALL createMetaTable('parkour');
CALL createMetaTable('measurement');






