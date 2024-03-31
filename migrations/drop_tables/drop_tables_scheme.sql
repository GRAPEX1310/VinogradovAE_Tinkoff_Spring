--liquibase formatted sql

--changeset VinogradovAE:id1
DROP TABLE IF EXISTS users CASCADE;

--changeset VinogradovAE:id2
DROP TABLE IF EXISTS links CASCADE;

--changeset VinogradovAE:id3
DROP TABLE IF EXISTS user_links CASCADE;
