DROP TABLE IF EXISTS EMPLEADOS;
DROP TABLE IF EXISTS USUARIOS;
DROP TABLE IF EXISTS DEPARTAMENTOS;

CREATE TABLE IF NOT EXISTS DEPARTAMENTOS
(
    id   BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid TEXT NOT NULL UNIQUE,
    name TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS EMPLEADOS
(
    id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid         TEXT    NOT NULL UNIQUE,
    name         TEXT    NOT NULL,
    departamento BIGINT  NOT NULL REFERENCES DEPARTAMENTOS (id),
    available    BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS USUARIOS
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid                    TEXT      NOT NULL UNIQUE,
    nombre                  TEXT      NOT NULL,
    username                TEXT      NOT NULL UNIQUE,
    email                   TEXT      NOT NULL UNIQUE,
    password                TEXT      NOT NULL,
    avatar                  TEXT,
    rol                     TEXT      NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    deleted                 BOOLEAN   NOT NULL DEFAULT FALSE,
    last_password_change_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO DEPARTAMENTOS (uuid, name)
VALUES ('b39a2fd2-f7d7-405d-b73c-b68a8dedbcdf', 'Administracion');
INSERT INTO DEPARTAMENTOS (uuid, name)
VALUES ('c53062e4-31ea-4f5e-a99d-36c228ed01a3', 'Gestion');

INSERT INTO EMPLEADOS (uuid, name, departamento, available)
VALUES ('86084458-4733-4d71-a3db-34b50cd8d68f', 'Daniel', 1, true);
INSERT INTO EMPLEADOS (uuid, name, departamento, available)
VALUES ('af04e495-bacc-4bde-8d61-d52f78b52a86', 'Nuria', 1, true);
INSERT INTO EMPLEADOS (uuid, name, departamento, available)
VALUES ('a711040a-fb0d-4fe4-b726-75883ca8d907', 'Aura', 2, true);