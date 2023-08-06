--liquibase formatted sql

-- changeset asharipov:1

CREATE TABLE cat_client
(
    id          BIGINT PRIMARY KEY,
    firstName   VARCHAR(50),
    lastName    VARCHAR(50),
    phoneNumber BIGINT,
    chatId      BIGINT
);

CREATE TABLE dog_client
(
    id          BIGINT PRIMARY KEY,
    firstName   VARCHAR(50),
    lastName    VARCHAR(50),
    phoneNumber BIGINT,
    chatId      BIGINT
);

--changeset leshka290

CREATE TABLE adopters
(
    id           BIGINT PRIMARY KEY,
    first_name   VARCHAR,
    last_name    VARCHAR,
    passport     VARCHAR,
    age          INT,
    phoneNumber  VARCHAR,
    chat_id      VARCHAR, -- telegram id
    volunteer_id INT      -- Lookup to [volunteers] table
);

CREATE TABLE client
(
    id          BIGINT PRIMARY KEY,
    firstName   VARCHAR(50),
    lastName    VARCHAR(50),
    phoneNumber BIGINT,
    chatId      BIGINT,
    lastPetType INT
);

--changeset Kostuyra
CREATE TABLE volunteers
(
    id           BIGINT PRIMARY KEY,
    first_name   VARCHAR,
    last_name    VARCHAR,
    phone        VARCHAR,
    chat_id      VARCHAR, -- telegram id
    shelter_id   INT -- shelter id
);

CREATE TABLE pets
(
    id          BIGINT PRIMARY KEY,
    nick_name   VARCHAR,
    pet_type    INT,
    breed   VARCHAR,
    sex         INT,
    picture     BYTEA
);