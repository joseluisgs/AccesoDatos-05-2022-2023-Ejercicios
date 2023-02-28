DROP TABLE IF EXISTS USUARIOS;
DROP TABLE IF EXISTS EMPLEADOS;

CREATE TABLE IF NOT EXISTS EMPLEADOS
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid       UUID      NOT NULL UNIQUE,
    nombre     TEXT      NOT NULL,
    email      TEXT      NOT NULL,
    salario    DOUBLE    NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted    BOOLEAN   NOT NULL DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS USUARIOS
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid       UUID      NOT NULL UNIQUE,
    nombre     TEXT      NOT NULL,
    username   TEXT      NOT NULL UNIQUE,
    password   TEXT      NOT NULL,
    email      TEXT      NOT NULL UNIQUE,
    avatar     TEXT,
    rol        TEXT      NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted    BOOLEAN   NOT NULL DEFAULT FALSE
);
--Contraseña: sebs1234
INSERT INTO USUARIOS (uuid, nombre, username, password, email, avatar, rol)
VALUES ('b39a2fd2-f7d7-405d-b73c-b68a8dedbcdf', 'Sebastian Mendoza', 'SebsMendoza',
        '$2a$12$2y89oYV4HrPslVSs36Auw.hQjPTX58QwTX5D9G1894LjrI.nnm5YS', 'sebs@mendoza.com',
        'https://upload.wikimedia.org/wikipedia/commons/f/f4/User_Avatar_2.png', 'ADMIN');
-- Contraseña: sand1234
INSERT INTO USUARIOS (uuid, nombre, username, password, email, avatar, rol)
VALUES ('c53062e4-31ea-4f5e-a99d-36c228ed01a3', 'Sandra Ortega', 'SandOr',
        '$2a$12$FkXGcKC2gEuGhkNigSBjkeyn4DYIlY4Rl9Ccr3GSNon2aa0y.MS.y', 'san@ortega.com',
        'https://upload.wikimedia.org/wikipedia/commons/f/f4/User_Avatar_2.png', 'USER');

INSERT INTO EMPLEADOS(uuid, nombre, email, salario)
VALUES ('0457ae2d-37da-4ef2-9101-16961aba565c', 'Alberto Marin', 'al@ma.com', 1500.0);
INSERT INTO EMPLEADOS(uuid, nombre, email, salario)
VALUES ('70759a24-c252-4e0c-b613-9414a8d13439', 'Andrea Pereira', 'an@pere.com', 1400.0);
INSERT INTO EMPLEADOS(uuid, nombre, email, salario)
VALUES ('a7721895-642f-431b-aff9-2c59cc03fb9a', 'Juan Garzon', 'ju@ga.com', 1600.0);
INSERT INTO EMPLEADOS(uuid, nombre, email, salario)
VALUES ('d63e30c4-d713-46f0-b48e-08bfc4cdf12f', 'Maria Serna', 'mari@ser.com', 1300.0);
INSERT INTO EMPLEADOS(uuid, nombre, email, salario)
VALUES ('4512bc43-5d98-459e-b9b2-2a8243d56a00', 'Julian Perez', 'juli@pe.com', 1200.0);
INSERT INTO EMPLEADOS(uuid, nombre, email, salario)
VALUES ('2b365ef6-d64e-4cbb-9c31-c6dd3641e24c', 'Celine Atan', 'cel@atan.com', 1800.0);
INSERT INTO EMPLEADOS(uuid, nombre, email, salario)
VALUES ('e23572b0-3faa-4937-90de-820eb488d893', 'Alejandro Gonzalo', 'ale@gon.com', 1700.0);
