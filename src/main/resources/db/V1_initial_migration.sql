CREATE TABLE IF NOT EXISTS persons (
	id INTEGER,
    name VARCHAR(50) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (phone)
);

CREATE TABLE IF NOT EXISTS presences (
	id INT,
    person_id INT NOT NULL,
    time_in DATETIME NOT NULL,
    time_out DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_person FOREIGN KEY (person_id) REFERENCES persons(id),
    UNIQUE KEY (person_id, time_in)
);

CREATE TABLE IF NOT EXISTS tasks (
	id INT,
    project VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    start DATETIME NOT NULL,
    end DATETIME NOT NULL,
    estimation_in_hours DOUBLE,
    completed BOOLEAN,
    PRIMARY KEY (id),
    UNIQUE (project, name)
);

CREATE TABLE IF NOT EXISTS time_trackings (
	id INT,
    presence_id INT,
    task_id INT,
    time_from DATETIME NOT NULL,
    time_to DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_presence FOREIGN KEY (presence_id) REFERENCES presences(id),
    CONSTRAINT fk_task FOREIGN KEY (task_id) REFERENCES tasks(id),
    UNIQUE (presence_id, time_from, time_to)
);

DELIMITER $$

CREATE TRIGGER prevent_time_overlap_before_insert
BEFORE INSERT ON time_trackings
FOR EACH ROW
BEGIN
    IF EXISTS (
        SELECT 1 FROM time_trackings
        WHERE presence_id = NEW.presence_id
          AND time_from < NEW.time_to
          AND time_to > NEW.time_from
    ) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Overlapping time interval for this presence_id';
    END IF;
END$$

DELIMITER ;


