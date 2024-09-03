DROP TABLE IF EXISTS payment_results;

DROP TABLE IF EXISTS users;

DROP TABLE IF EXISTS products;

DROP TABLE IF EXISTS orders;

DROP TABLE IF EXISTS point_histories;

DROP TABLE IF EXISTS billing_keys;

CREATE TABLE IF NOT EXISTS point_histories (
    id INT NOT NULL AUTO_INCREMENT,
    login_id VARCHAR(12) NOT NULL,
    requested_quantity INT NOT NULL,
    reason VARCHAR(50),
    total_quantity INT NOT NULL,
    PRIMARY KEY(id)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS users (
    id int NOT NULL AUTO_INCREMENT,
    login_id VARCHAR(12) NOT NULL UNIQUE,
    password VARCHAR(15) NOT NULL,
    phone VARCHAR(13),
    address VARCHAR(30),
    role VARCHAR(20) NOT NULL,
    point BIGINT NOT NULL CHECK(point >= 0),
    PRIMARY KEY(id)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS products (
    id INT NOT NULL AUTO_INCREMENT,
    title VARCHAR(50) NOT NULL,
    description VARCHAR(100),
    seller_id VARCHAR(12) NOT NULL,
    original_price BIGINT NOT NULL,
    total_stock BIGINT NOT NULL,
    purchased_stock BIGINT NOT NULL,
    PRIMARY KEY(id)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS orders (
    id VARCHAR(50) NOT NULL,
    seller_login_id VARCHAR(12) NOT NULL,
    buyer_login_id VARCHAR(12) NOT NULL,
    product_id INT NOT NULL,
	buy_quantity INT NOT NULL,
    used_point BIGINT NOT NULL,
    total_price BIGINT NOT NULL,
    refunded_price BIGINT NOT NULL,
    payment_tx_id VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL,
    PRIMARY KEY(id)
)ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS billing_keys (
    login_id VARCHAR(12) NOT NULL,
    customer_uid VARCHAR(50) NOT NULL,
    PRIMARY KEY(login_id, customer_uid)
)ENGINE=InnoDB;


CREATE TABLE IF NOT EXISTS payment_results (
	id INT NOT NULL AUTO_INCREMENT,
    imp_uid VARCHAR(50) UNIQUE NOT NULL,
    merchant_uid VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL,
    buyer_name VARCHAR(12) NOT NULL,
    amount BIGINT NOT NULL,
    currency VARCHAR(30),
    paid_at DATE,
    pay_method VARCHAR(30),
    status VARCHAR(50),
    cancel_amount BIGINT,
    cancel_reason VARCHAR(50),
    cancelled_at DATE,
    fail_reason VARCHAR(50),
    failed_at DATE,
    PRIMARY KEY(id)
)ENGINE=InnoDB;