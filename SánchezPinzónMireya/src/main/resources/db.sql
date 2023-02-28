DROP TABLE IF EXISTS Empleados;
DROP TABLE IF EXISTS Departamentos;
DROP TABLE IF EXISTS Usuarios;

CREATE TABLE IF NOT EXISTS Departamentos
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre TEXT NOT NULL,
    presupuesto DOUBLE NOT NULL
);

CREATE TABLE IF NOT EXISTS Empleados
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre TEXT NOT NULL,
    email TEXT NOT NULL,
    departamento_id BIGINT REFERENCES Departamentos (id)
    );

CREATE TABLE IF NOT EXISTS Usuarios
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    rol TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL
);


INSERT INTO Departamentos (nombre, presupuesto)
VALUES ('Marketing', 1700.0);
INSERT INTO Departamentos (nombre, presupuesto)
VALUES ('Finanzas', 5300.0);
INSERT INTO Departamentos (nombre, presupuesto)
VALUES ('Desarrollo', 10000.0);

INSERT INTO Empleados (nombre, email, departamento_id)
VALUES ('Mireya', 'mireya@gmail.com', 1);
INSERT INTO Empleados (nombre, email, departamento_id)
VALUES ('Juan', 'juan@gmail.com', 2);
INSERT INTO Empleados (nombre, email, departamento_id)
VALUES ('Manolo', 'manolo@gmail.com', 3);