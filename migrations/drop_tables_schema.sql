--liquibase formatted sql

--changeset VinogradovAE:id1
DROP TABLE IF EXISTS users CASCADE;

--changeset VinogradovAE:id2
DROP TABLE IF EXISTS all_links CASCADE;

--changeset VinogradovAE:id3
DROP TABLE IF EXISTS github_links CASCADE;

--changeset VinogradovAE:id4
DROP TABLE IF EXISTS stackoverflow_links CASCADE;

--changeset VinogradovAE:id5
DROP TABLE IF EXISTS user_links CASCADE;
