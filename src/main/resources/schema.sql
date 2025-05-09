-- Table: doctor
CREATE TABLE doctor (
    doctor_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    middle_name VARCHAR(100),
    specialty VARCHAR(100) NOT NULL
);

-- Table: consulting_room
CREATE TABLE consulting_room (
    consulting_room_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    room_number INT NOT NULL,
    floor INT NOT NULL
);

-- Insert doctors
INSERT INTO doctor (first_name, last_name, middle_name, specialty) VALUES
    ('John', 'Smith', 'Anderson', 'Cardiology'),
    ('Laura', 'Martinez', 'Garcia', 'Pediatrics'),
    ('Michael', 'Brown', 'Lee', 'Dermatology'),
    ('Anna', 'Taylor', 'Lopez', 'Neurology'),
    ('David', 'Wilson', 'Perez', 'Orthopedics');

-- Insert consulting rooms
INSERT INTO consulting_room (room_number, floor) VALUES
    (101, 1),
    (102, 1),
    (201, 2),
    (202, 2),
    (301, 3);
