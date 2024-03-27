--liquibase formatted sql


--changeset VinogradovAE:create_link_types
CREATE TYPE enum_link_type AS ENUM
(
    'stackoverflow',
    'github'
);

--changeset VinogradovAE:create_table_users
CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    chat_id BIGINT UNIQUE NOT NULL
);

--changeset VinogradovAE:create_table_links
CREATE TABLE IF NOT EXISTS links
(
    link_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    link_type enum_link_type NOT NULL,
    url VARCHAR(128) UNIQUE NOT NULL
);

--changeset VinogradovAE:create_table_user_links
CREATE TABLE IF NOT EXISTS user_links
(
    user_id BIGINT REFERENCES users(user_id),
    link_id BIGINT REFERENCES links(link_id),
    PRIMARY KEY (user_id, link_id)
);
