-- Create a database
CREATE DATABASE IF NOT EXISTS inpogram;

-- Use the database
USE inpogram;

-- Create tables
CREATE TABLE IF NOT EXISTS post (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    featured_image_name VARCHAR(255) NOT NULL,
    content MEDIUMTEXT NOT NULL,
    created_at DATETIME NOT NULL
);

CREATE TABLE IF NOT EXISTS tag (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    usage_count INT NOT NULL
);

CREATE TABLE IF NOT EXISTS `user` (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    profile_image_url VARCHAR(255),
    provider_id VARCHAR(255) UNIQUE,
    email_verified TINYINT(1) NOT NULL DEFAULT 0,
    password_hash VARCHAR(255) DEFAULT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    provider ENUM('facebook', 'google', 'local') NOT NULL default 'local'
);

CREATE TABLE IF NOT EXISTS `like` (
    user_id INT,
    post_id INT,
    liked_at DATETIME NOT NULL,
    PRIMARY KEY (user_id, post_id),
    CONSTRAINT fk_like_user_id
        FOREIGN KEY (user_id) REFERENCES user (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_like_post_id
        FOREIGN KEY (post_id) REFERENCES post (id)
        ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Create junction tables
CREATE TABLE IF NOT EXISTS post_tag (
    post_id INT,
    tag_id INT,
    PRIMARY KEY (post_id, tag_id),
    CONSTRAINT fk_post_tag_post_id
        FOREIGN KEY (post_id) REFERENCES post (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_post_tag_tag_id
        FOREIGN KEY (tag_id) REFERENCES tag (id)
        ON DELETE CASCADE
);


-- Insert default data
INSERT INTO `user` (username, email) VALUES
    ('test', 'test@example.com'),
    ('user', 'user@example.com');

INSERT INTO Role (name) VALUES
    ('ADMIN'),
    ('USER'),
    ('MEMBER');

INSERT INTO tag (name, usage_count) VALUES
    ('Technology', 0), 
    ('Java', 0), 
    ('Spring Boot', 0), 
    ('Software Design', 0),
    ('System Design', 0), 
    ('Programming', 0), 
    ('Microservices', 0), 
    ('Software Architecture', 0);

