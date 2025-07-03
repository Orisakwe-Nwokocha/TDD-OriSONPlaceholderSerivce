DROP TABLE IF EXISTS post;


CREATE TABLE IF NOT EXISTS Post (
    id INT NOT NULL,
    user_id INT NOT NULL,
    title varchar(250) NOT NULL,
    body text NOT NULL,
    version int,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS users;

CREATE TABLE IF NOT EXISTS users (
--     id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100),
    username VARCHAR(50),
    email VARCHAR(100) NOT NULL,
    phone VARCHAR(50),
    website VARCHAR(100),

-- Embedded Address fields
    address_street VARCHAR(100),
    address_suite VARCHAR(50),
    address_city VARCHAR(50),
    address_zipcode VARCHAR(20),

-- Embedded Geo inside Address
    address_geo_lat VARCHAR(20),
    address_geo_lng VARCHAR(20),

-- Embedded Company fields
    company_name VARCHAR(100),
    company_catch_phrase VARCHAR(255),
    company_bs VARCHAR(100),

    version BIGINT
);
