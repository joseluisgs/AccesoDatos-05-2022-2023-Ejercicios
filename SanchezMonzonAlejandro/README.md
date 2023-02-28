# Proyecto API para consultar datos de empleados y departamentos 👨‍💼💼
> **Note** Ejercicio realizado por Alejandro Sánchez Monzón.

API para consultar datos de empleados y departamentos a través de una API. La API está construida con Spring Boot, utiliza Spring Security y SSL para la autenticación y autorización, y se utiliza Docker para desplegar la aplicación.


## Funcionalidades 📋
La API permite consultar los datos de los empleados y departamentos, los empleados pueden estar asociados a un departamento y se pueden realizar las siguientes operaciones:

- Obtener todos los empleados.
- Obtener un empleado por su identificador.
- Obtener todos los departamentos.
- Obtener un departamento por su identificador.
- Crear un empleado asociado a un departamento existente.
- Actualizar los datos de un empleado.
- Actualizar los datos de un departamento.
- Eliminar un empleado.
- Eliminar un departamento.
- Registrarse.
- Iniciar sesión.
- Listar todos los usuarios.

## Tecnologías utilizadas 🔧
- Spring Boot
- Spring Security
- SSL
- Docker
- Excepciones personalizadas
- Validaciones
- Patrones DTO
- Postman

## Despliegue de API con Docker 🐳
Mediante el uso de los archivos docker-compose.yml y Dockerfile podemos crear un despliegue de la API de forma que, ejecutando unicamente ese archivo, se levanta la misma, para poder consultar los datos.

## Cómo usar la API 🤖
Una vez con la API en funcionamiento, se pueden realizar las consultas utilizando un programa como Postman. Se deben utilizar las siguientes URLs para realizar las operaciones:

- Obtener todos los empleados: `https://localhost:6969/empleados`
- Obtener un empleado por su identificador: `https://localhost:6969/empleados/{id}`
- Obtener todos los departamentos: `https://localhost:6969/departamentos`
- Obtener un departamento por su identificador: `https://localhost:6969/departamentos/{id}`

*Todos los demás métodos de la API se hacen basándonos en esas rutas.*

Se deben enviar las solicitudes utilizando el método HTTP correspondiente y los datos en formato JSON. La autenticación se realiza utilizando un token JWT que se debe enviar en la cabecera de la solicitud.