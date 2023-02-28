-- Eliminar tablas al ejecutar
DROP TABLE IF EXISTS EMPLEADOS;
DROP TABLE IF EXISTS DEPARTAMENTOS;
DROP TABLE IF EXISTS USUARIOS;

-- Creacion de tablas
CREATE TABLE IF NOT EXISTS DEPARTAMENTOS
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre TEXT NOT NULL UNIQUE,
    presupuesto DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS EMPLEADOS
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    departamento_id BIGINT REFERENCES DEPARTAMENTOS (id),
    avatar TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS USUARIOS
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username TEXT NOT NULL UNIQUE,
    password TEXT NOT NULL,
    role TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

-- DATOS INICIALES

INSERT INTO DEPARTAMENTOS (nombre, presupuesto)
VALUES ('Interfaces', 500.0);
INSERT INTO DEPARTAMENTOS (nombre, presupuesto)
VALUES ('Administracion', 700.0);
INSERT INTO DEPARTAMENTOS (nombre, presupuesto)
VALUES ('PSP', 400.0);

INSERT INTO EMPLEADOS (name, email, departamento_id, avatar)
VALUES ('Mario', 'mario@gmail.com', 1, 'https://cdn-icons-png.flaticon.com/512/2550/2550260.png');
INSERT INTO EMPLEADOS (name, email, departamento_id, avatar)
VALUES ('Alysys', 'alysys@gmail.com', 2, 'https://cdn-icons-png.flaticon.com/512/2550/2550260.png');
INSERT INTO EMPLEADOS (name, email, departamento_id, avatar)
VALUES ('Vincent', 'vincent@gmail.com', 2, 'https://cdn-icons-png.flaticon.com/512/2550/2550260.png');
INSERT INTO EMPLEADOS (name, email, avatar)
VALUES ('Kratos', 'kratos@gmail.com', 'https://cdn-icons-png.flaticon.com/512/2550/2550260.png');


