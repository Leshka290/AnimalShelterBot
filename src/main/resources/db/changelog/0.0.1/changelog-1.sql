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
create table dog_reports
(
    id              bigserial
        constraint dog_rep_pk
            primary key,
    date            date                                             not null,
    diet            varchar                                          not null,
    common_status   varchar                                          not null,
    behavior        varchar                                          not null,
    client_id    bigint
        constraint dogclient_id_fk
            references client,
    dog_id          bigint
        constraint dog_id_fk
            references pets,
    copied_owner_id bigint generated always as (client_id) stored not null
);

create table cat_reports
(
    id              bigserial
        constraint cat_rep_pk
            primary key,
    date            date                                             not null,
    diet            varchar                                          not null,
    common_status   varchar                                          not null,
    behavior        varchar                                          not null,
    client_id    bigint
        constraint cat_client_fk
            references cat_client,
    cat_id          bigint
        constraint cat_fk
            references pets,
    copied_owner_id bigint generated always as (client_id) stored not null
);
create table dog_images
(
    id            bigint not null
        constraint images_pk
            primary key,
    name          varchar,
    original_name varchar,
    size          bigint,
    content_type  varchar,
    is_preview    boolean,
    report_id     bigint
        constraint images_dog_reports_null_fk
            references dog_reports,
    bytes         oid,
    dog_id        bigint
        constraint dog_images_dogs_id_fk
            references pets
);
create table cat_images
(
    id            bigint not null
        constraint cat_images_pk
            primary key,
    name          varchar,
    original_name varchar,
    size          bigint,
    content_type  varchar,
    is_preview    boolean,
    report_id     bigint
        constraint cat_images_cat_reports_fk
            references cat_reports,
    bytes         oid,
    cat_id        bigint
        constraint cat_images_cats_id_fk
            references pets
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