CREATE TABLE USERS
(
    id           bigserial PRIMARY KEY,
    name         varchar(256) not null,
    password     varchar(256) not null,
    birth_day    date    not null,
    email        varchar(256) not null,
    phone_number varchar(256) not null,
    photo        bytea,
    role         varchar(256) not null
);
