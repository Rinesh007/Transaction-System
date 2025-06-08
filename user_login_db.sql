CREATE DATABASE user_login_db;

USE user_login_db;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin', 'user') NOT NULL,
    balance DECIMAL(10,2) DEFAULT 0.00
);

INSERT INTO users (username, password, role)
VALUES
    ('admin1', 'adminpass', 'admin'),
    ('user1', 'userpass', 'user')
ON DUPLICATE KEY UPDATE username=username; 

CREATE TABLE IF NOT EXISTS recharge_codes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) UNIQUE NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    used BOOLEAN DEFAULT FALSE
);

INSERT INTO users (username, password, role)
VALUES ('newuser', 'newpass', 'user');