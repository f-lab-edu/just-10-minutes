CREATE TABLE users_table (
    id int AUTO_INCREMENT,
    login_id VARCHAR(12) UNIQUE,
    password VARCHAR(15),
    phone VARCHAR(13),
    address VARCHAR(30),
    role VARCHAR(20),
    PRIMARY KEY(id)
);