--liquibase formatted sql

--asharipov: create tables for clients

CREATE TABLE cat_client (
    id BIGINT PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    phoneNumber BIGINT,
    chatId BIGINT
);

CREATE TABLE dog_client (
    id BIGINT PRIMARY KEY,
    firstName VARCHAR(50),
    lastName VARCHAR(50),
    phoneNumber BIGINT,
    chatId BIGINT
);