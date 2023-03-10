DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS empleados;
DROP TABLE IF EXISTS departamentos;

CREATE TABLE IF NOT EXISTS users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(15) NOT NULL
    );

CREATE TABLE IF NOT EXISTS departamentos(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    presupuesto DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS empleados(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid UUID NOT NULL UNIQUE,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    avatar VARCHAR(255),
    departamento_id uuid NOT NULL REFERENCES departamentos (uuid)
    );