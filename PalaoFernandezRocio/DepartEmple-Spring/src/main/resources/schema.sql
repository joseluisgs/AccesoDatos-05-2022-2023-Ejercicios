DROP TABLE IF EXISTS departamentos;
DROP TABLE IF EXISTS empleados;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE IF NOT EXISTS departamentos
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    uuid       UUID      NOT NULL UNIQUE,
    nombre     TEXT      NOT NULL,
    presupuesto FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS empleados
(
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre            TEXT      NOT NULL,
    email            TEXT      NOT NULL,
    uuidDepartamento UUID NOT NULL,
    rol TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS usuarios
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email TEXT NOT NULL,
  username TEXT NOT NULL,
  password TEXT NOT NULL,
  rol TEXT NOT NULL
);


INSERT INTO departamentos (uuid, nombre, presupuesto)
VALUES ('b39a2fd2-f7d7-405d-b73c-b68a8dedbcdf', 'Departamento Marketing', 12500.50);
INSERT INTO departamentos (uuid, nombre, presupuesto)
VALUES ('e9d8ce8d-262e-4c70-85d4-6df4ab4a86e0', 'Departamento Compras', 50000.0);
INSERT INTO departamentos (uuid, nombre, presupuesto)
VALUES ('5cb31633-0580-4cf9-a361-c1d7dd0ed78b', 'Departamento Ventas', 4000.20);
INSERT INTO departamentos (uuid, nombre, presupuesto)
VALUES ('12ab4f3f-d505-443e-bafa-99dfe5ac3d56', 'Departamento Diseño', 500.50);



