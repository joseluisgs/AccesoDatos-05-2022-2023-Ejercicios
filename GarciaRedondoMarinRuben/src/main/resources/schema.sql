DROP TABLE IF EXISTS DEPARTAMENTO;
DROP TABLE IF EXISTS EMPLEADO;
DROP TABLE IF EXISTS USUARIO;


CREATE TABLE IF NOT EXISTS DEPARTAMENTO
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid       UUID      NOT NULL UNIQUE,
    nombre     TEXT      NOT NULL,
    presupuesto DOUBLE      NOT NULL
);

CREATE TABLE IF NOT EXISTS EMPLEADO
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid             UUID      NOT NULL UNIQUE,
    nombre            TEXT      NOT NULL,
    email           TEXT    NOT NULL,
    avatar           TEXT    NOT NULL,
    departamento_id UUID      NOT NULL REFERENCES DEPARTAMENTO (uuid)
);

CREATE TABLE IF NOT EXISTS USUARIO
(
    id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid                    UUID      NOT NULL UNIQUE,
    correo                   TEXT      NOT NULL UNIQUE,
    usuario                TEXT      NOT NULL UNIQUE,
    password                TEXT      NOT NULL,
    rol                     TEXT      NOT NULL
);

INSERT INTO DEPARTAMENTO (uuid, nombre, presupuesto)
VALUES ('d3e5e833-f86c-4c09-b6c5-9e6e24643ccf', 'Informatica', 122.0);
INSERT INTO DEPARTAMENTO (uuid, nombre, presupuesto)
VALUES ('4c5ff0f1-9fa1-4881-96e6-4780dc76235b', 'Recursos Humanos', 210.3);
INSERT INTO DEPARTAMENTO (uuid, nombre, presupuesto)
VALUES ('fe6176e1-2982-485e-b50f-3326c7082289', 'Marketing', 230.4);

INSERT INTO EMPLEADO (uuid, nombre, email, avatar, departamento_id)
VALUES ('31fd26ed-6651-49eb-98a7-b2f48284b76a', 'Alejandro', 'alejandrosanchez@gmail.com', 'https://upload.wikimedia.org/wikipedia/commons/f/f4/User_Avatar_2.png', 'd3e5e833-f86c-4c09-b6c5-9e6e24643ccf');
INSERT INTO EMPLEADO (uuid, nombre, email, avatar, departamento_id)
VALUES ('7d645f9e-db77-4aeb-893d-0266bbad02ae', 'Mireya', 'mireyasanchez@gmail.com', 'https://upload.wikimedia.org/wikipedia/commons/f/f4/User_Avatar_2.png', '4c5ff0f1-9fa1-4881-96e6-4780dc76235b');
INSERT INTO EMPLEADO (uuid, nombre, email, avatar, departamento_id)
VALUES ('33972c31-bab0-4e55-85df-89a6f98a1469', 'Ruben', 'rubengarcia@gmail.com', 'https://upload.wikimedia.org/wikipedia/commons/f/f4/User_Avatar_2.png', 'fe6176e1-2982-485e-b50f-3326c7082289');
INSERT INTO EMPLEADO (uuid, nombre, email, avatar, departamento_id)
VALUES ('5868c5f9-09f1-462d-b24f-fbb8c89f5118', 'Andres', 'andresmarquez@gmail.com', 'https://upload.wikimedia.org/wikipedia/commons/f/f4/User_Avatar_2.png', 'fe6176e1-2982-485e-b50f-3326c7082289');

-- Contraseña: maria1234
INSERT INTO USUARIO (uuid, correo, usuario, password, rol)
VALUES ('b39a2fd2-f7d7-405d-b73c-b68a8dedbcdf', 'maria@gmail.com', 'maria1234',
        '$2a$12$Z.qGPt9plEet.ZAqeXde5uviGwp1tyqtEENnAhoBrUuhmvmfcagku', 'ADMIN, USER');
-- Contraseña: paquito1234
INSERT INTO USUARIO (uuid, correo, usuario, password, rol)
VALUES ('74559264-2595-4f0d-8f0d-3b28311fa022', 'paco@gmail.com', 'paquito1234',
        '$2a$12$2HBEGaQE322tgCCzYhTfKe6XBKvbUQoIhAyIArp.8hytzF5qG0kS2', 'ADMIN, USER');
-- Contraseña: admin1234
INSERT INTO USUARIO (uuid, correo, usuario, password, rol)
VALUES ('7b57d433-f863-4271-818f-b539d8bb8de2', 'admin@admin.com', 'admin1234',
        '$2a$12$cnVr9NsLzGFNJbpSBXdwreXV58QO8ia8eB/GLyQVJJT3m.f54cJpu', 'ADMIN, USER');
