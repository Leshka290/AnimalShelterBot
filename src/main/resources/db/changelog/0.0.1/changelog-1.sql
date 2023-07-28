--liquibase formatted sql

--asharipov: create tables for clients
CREATE TABLE cat_clients (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    phoneNumber BIGINT,
    chatId BIGINT
);

CREATE TABLE dog_clients (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    phoneNumber BIGINT,
    chatId BIGINT
);