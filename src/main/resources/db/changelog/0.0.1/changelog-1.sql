--liquibase formatted sql

-- changeset asharipov:1

CREATE TABLE cat_client
(
    id          BIGINT PRIMARY KEY constraint cat_client_id,
    firstName   VARCHAR(50),
    lastName    VARCHAR(50),
    phoneNumber BIGINT,
    chatId      BIGINT
);

CREATE TABLE dog_client
(
    id          BIGINT PRIMARY KEY constraint dog_client_id,
    firstName   VARCHAR(50),
    lastName    VARCHAR(50),
    phoneNumber BIGINT,
    chatId      BIGINT
);

--changeset leshka290

CREATE TABLE adopters
(
    id           BIGINT PRIMARY KEY constraint adopter_id,
    first_name   VARCHAR,
    last_name    VARCHAR,
    passport     VARCHAR,
    age          INT,
    phoneNumber  VARCHAR,
    chat_id      VARCHAR, -- telegram id
    volunteer_id INT      -- Lookup to [volunteers] table
);

--changeset Kostuyra
CREATE TABLE volunteers
(
    id         BIGINT PRIMARY KEY constraint volunteers_id,
    first_name VARCHAR,
    last_name  VARCHAR,
    phone      VARCHAR,
    chat_id    VARCHAR, -- telegram id
    shelter_id INT      -- shelter id
);
create table dog_reports
(
    id  BIGINT PRIMARY KEY,
    date            DATE                                          not null,
    diet            VARCHAR                                       not null,
    common_status   VARCHAR                                        not null,
    behavior        VARCHAR                                       not null,
    client_id       BIGINT
        constraint dogclient_id_fk
            references dog_client,
    dog_id          BIGINT
        constraint dog_id_fk
            references pets,
    copied_client_id BIGINT generated always as (client_id) stored not null
);

create table cat_reports
(
    id              bigserial
        constraint cat_rep_pk
            primary key,
    date            date                                          not null,
    diet            VARCHAR                                       not null,
    common_status   VARCHAR                                       not null,
    behavior        VARCHAR                                       not null,
    client_id       BIGINT
        constraint cat_client_fk
            references cat_client,
    cat_id          BIGINT
        constraint cat_fk
            references pets,
    copied_owner_id BIGINT generated always as (client_id) stored not null
);
create table dog_images
(
    id            BIGINT not null
        constraint dog_images_id
            primary key,
    name          VARCHAR,
    original_name VARCHAR,
    size          BIGINT,
    content_type  VARCHAR,
    is_preview    boolean,
    report_id     BIGINT
        constraint images_dog_reports_null_fk
            references dog_reports,
    bytes         oid,
    dog_id        BIGINT
        constraint dog_images_dogs_id_fk
            references pets
);
create table cat_images
(
    id            BIGINT not null
        constraint cat_images_id
            primary key,
    name          VARCHAR,
    original_name VARCHAR,
    size          BIGINT,
    content_type  VARCHAR,
    is_preview    boolean,
    report_id     BIGINT
        constraint cat_images_cat_reports_id
            references cat_reports,
    bytes         oid,
    cat_id        BIGINT
        constraint cat_images_cats_id_fk
            references pets
);

CREATE TABLE pets
(
    id        BIGINT PRIMARY KEY constraint pet_id,
    nick_name VARCHAR,
    pet_type  INT,
    breed     VARCHAR,
    sex       INT,
    picture   BYTEA
);