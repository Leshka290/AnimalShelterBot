--liquibase formatted sql

-- changeset asharipov:1

CREATE TABLE client
(
    id          BIGINT PRIMARY KEY constraint cat_client_id,
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
create table reports
(
    id  BIGINT PRIMARY KEY,
    date            DATE                                          not null,
    diet            VARCHAR                                       not null,
    common_status   VARCHAR                                        not null,
    behavior        VARCHAR                                       not null,
    client_id       BIGINT
        constraint client_id_fk
            references client,
    dog_id          BIGINT
        constraint id_fk
            references pets,
    copied_client_id BIGINT generated always as (client_id) stored not null
);


create table images
(
    id            BIGINT not null
        constraint images_id
            primary key,
    name          VARCHAR,
    original_name VARCHAR,
    size          BIGINT,
    content_type  VARCHAR,
    is_preview    boolean,
    report_id     BIGINT
        constraint images_reports_null_fk
            references reports,
    bytes         oid,
    dog_id        BIGINT
        constraint images_id_fk
            references pets
);


CREATE TABLE pets
(
    id        BIGINT PRIMARY KEY constraint pet_id,
    nick_name VARCHAR not null ,
    pet_type  INT not null ,
    breed     VARCHAR,
    sex       INT not null ,
    picture   BYTEA
);