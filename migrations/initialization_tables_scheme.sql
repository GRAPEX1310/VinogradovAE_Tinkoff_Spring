--liquibase formatted sql

--changeset VinogradovAE:id1
CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    chat_id BIGINT UNIQUE NOT NULL
);

--changeset VinogradovAE:id2
CREATE TABLE IF NOT EXISTS all_links
(
    link_id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    link_type VARCHAR(15) NOT NULL CHECK (link_type IN ('github', 'stackoverflow')),
    url VARCHAR(128) UNIQUE NOT NULL
);

--changeset VinogradovAE:id3
CREATE TABLE IF NOT EXISTS github_links
(
    link_id BIGINT PRIMARY KEY REFERENCES all_links(link_id),
    last_update TIMESTAMP,
    url VARCHAR(128) UNIQUE NOT NULL
    --todo
);

--changeset VinogradovAE:id4
CREATE TABLE IF NOT EXISTS stackoverflow_links
(
    link_id BIGINT PRIMARY KEY REFERENCES all_links(link_id),
    last_update TIMESTAMP,
    url VARCHAR(128) UNIQUE NOT NULL
    --todo
);

--changeset VinogradovAE:id5
CREATE TABLE IF NOT EXISTS user_links
(
    user_id BIGINT REFERENCES users(user_id),
    link_id BIGINT REFERENCES all_links(link_id),
    PRIMARY KEY (user_id, link_id)
);

--changeset VinogradovAE:for_tests
INSERT INTO users(chat_id)
VALUES (1), (2), (13);
