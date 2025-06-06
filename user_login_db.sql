CREATE DATABASE user_login_db;

USE user_login_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'user') NOT NULL
);

INSERT INTO users (username, password, role) VALUES
('admin1', 'adminpass', 'admin'),
('user1', 'userpass', 'user');








