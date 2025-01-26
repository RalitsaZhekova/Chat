CREATE TABLE IF NOT EXISTS td_users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    is_active INT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS td_channels (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    owner_id INT NOT NULL,
    is_active INT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS tc_channel_members (
    id INT PRIMARY KEY AUTO_INCREMENT,
    channel_id INT NOT NULL,
    user_id INT NOT NULL,
    role VARCHAR(50) DEFAULT 'MEMBER',
    is_active INT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS td_messages (
    id INT PRIMARY KEY AUTO_INCREMENT,
    sender_id INT NOT NULL,
    channel_id INT DEFAULT NULL,
    receiver_id INT DEFAULT NULL,
    content VARCHAR(255) NOT NULL,
    is_active INT DEFAULT 1
);

CREATE TABLE IF NOT EXISTS td_friends (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id_1 INT NOT NULL,
    user_id_2 INT NOT NULL,
    is_active INT DEFAULT 1
);