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
)

--changeset leshka290

CREATE TABLE adopters_cat
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

CREATE TABLE adopters_cat
(
    id           BIGINT PRIMARY KEY,
    first_name   VARCHAR,
    last_name    VARCHAR,
    passport     VARCHAR,
    age          INT,
    phone        VARCHAR,
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