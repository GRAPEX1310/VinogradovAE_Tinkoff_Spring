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
    id BIGINT UNIQUE NOT NULL
);

--changeset VinogradovAE:create_table_links
CREATE TABLE IF NOT EXISTS links
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    link_type enum_link_type NOT NULL,
    url VARCHAR(128) UNIQUE NOT NULL,
    last_update TIMESTAMP
);

--changeset VinogradovAE:create_table_user_links
CREATE TABLE IF NOT EXISTS user_links
(
    user_id BIGINT REFERENCES users(id),
    link_id BIGINT REFERENCES links(id),
    PRIMARY KEY (user_id, link_id)
);
