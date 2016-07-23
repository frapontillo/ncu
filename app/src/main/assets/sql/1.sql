CREATE TABLE `location` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`setting`	TEXT UNIQUE NOT NULL,
	`city_name` TEXT,
	`latitude`  REAL,
	`longitude` REAL
);

CREATE TABLE `weather` (
	`_id`	INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	`location_id`	INTEGER NOT NULL,
	`date` INTEGER NOT NULL,
	`weather_id`	INTEGER,
	`temp_max`	REAL,
	`temp_min`	REAL,
	`humidity`	REAL,
	`pressure`	REAL,
	`wind_speed`	REAL,
	`wind_direction`	TEXT,
	FOREIGN KEY(`location_id`) REFERENCES `location`(`_id`)
);

