# Ejercicio opcional PSP-AD: SPRING-API-REST REACTIVO

Servicio "Departamento-Empleado" con Usuarios, para la asignatura Programación de Procesos y Servicios
del IES Luis Vives (Leganés) curso 22/23.

## Índice

- [Diseño](#diseño)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Funcionamiento de la aplicación](#funcionamiento-de-la-aplicación)
- [Autor](#autor)

# Diseño

## Introducción

Para la generación del proyecto base se ha usado [Spring Initializr](https://start.spring.io/)

Este ejercicio se basa en el enunciado de un problema propuesto por el profesor.

Este mismo se puede hallar en la [documentation](./documentation).

## Configuración de la base de datos

En este caso, se ha aplicado una base de datos en memoria [H2](https://www.h2database.com/html/main.html).

## Versiones anteriores

- [Ejemplo-Medio](https://github.com/Mario999X/EmpleadoDepartamentoSpringMedio)
- [Ejemplo-Basico](https://github.com/Mario999X/EmpleadoDepartamentoSpringBasico)

La versión basica ofrece una API que ofrece operaciones simples entre Empleado-Departamento.

La versión media ofrece lo mismo que la básica, además de la introducción
de [Spring Security V6](https://docs.spring.io/spring-security/reference/index.html)
y la gestión de usuarios además de la introducción de roles.

## Versión Ktor

- [Ejemplo-Ktor](https://github.com/Mario999X/EmpleadoDepartamentoKtor_Opcional)

# Estructura del proyecto

El proyecto se encuentra estructurado en distintos paquetes para una mejor legibilidad.

## Documentación

Las clases se encuentran comentadas con KDoc, más la implementación
de [Dokka](https://kotlinlang.org/docs/dokka-introduction.html), que permite visualizar la documentación del programa en
un archivo *html*

Además, se ha aplicado Swagger y OpenApi para la documentación de los *end points*; se ha usado
[OpenApi de Spring](https://springdoc.org/)

# Funcionamiento de la aplicación

Contamos con una clase principal, *Application*, en ella llamamos a las funciones principales de los plugins usados
en la aplicación.

## Cache

Se ha usado la cache propia que ofrece *Spring*.

## Seguridad

Se ha aplicado *Spring Security*.

Para el cifrado de las contraseñas de los usuarios se ha usado la implementacion de Spring de Bcrypt.

## Almacenamiento

Se ha implantado un sistema de almacenamiento propio de *Spring* para la subida de ficheros multiparte.

Estos se pueden probar con sus propia ruta o bien actualizando el avatar de un empleado.

## Ejecución

Una vez se encuentre en ejecución, se recomienda el uso de un cliente que permita recibir y enviar *request-response*,
por ejemplo, [Postman](https://www.postman.com/) o el plugin [Thunder Client](https://www.thunderclient.com/)
en [VSC](https://code.visualstudio.com/)

## Despliegue

La aplicación se puede lanzar de dos formas distintas: *jar*, y *docker*

## Tests

Se han realizado test con *Postman* (E2E), se puede ver en el directorio [Postman](./postman)

Además, se han testeado los repositorios, los servicios y los controladores, usando [JUnit 5](https://junit.org/junit5/)
y
aplicando [Mockk](https://mockk.io/)

# Autor

[Mario Resa](https://github.com/Mario999X)
