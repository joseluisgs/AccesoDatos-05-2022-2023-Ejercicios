# Ejercicio Api Departamento - Empleado con Spring Data
## Creado por: Daniel Carmona Rodriguez

---
### Api
Esta creada con Spring Boot.
### Inyección de dependencias
Se utiliza el inyector de dependencias de Spring, utilizando @Autowired
### Repositorios
Los repositorios son los proporcionados por spring.
### Servicios
Servicios que implementan el repositorio de Spring y la cache.
### Cache
Se ha utilizado la cache que te proporciona Spring.
### Almacenamiento
Se almacenan los datos en memoria utilizando H2 y R2DBC para que sea reactivo.
### Seguridad
Se utiliza Spring Security la cual implementamos JWT para los tokens y BCrypt para cifrar las contraseñas.
También se utiliza SSL para la seguridad y asi poder utilizar https de forma más segura y con el certificado correspondiente.
### Test
Adjuntado ficheros de postman donde se comprueba el funcionamiento de la api.
### WebSockets
Implementación de tiempo real para cada actualización de departamento y/o empleado.
### Storage
Almacenamiento de imagenes y obtención de las mismas.

---
## EndPoints


### Empleado
#### Sin autenticar:
- GetAll: /empleados
- GetById: /empleados/{id}
- GetDepartamentoById: /empleados/{id}/departamento
#### Con autenticación:
- Post: /empleados
- Put: /empleados/{id}
- Delete: /empleados/{id}
#### WebSocket
- WS: /updates/empleados


### Departamento
#### Sin autenticar:
- GetAll: /departamentos
- GetById: /departamentos/{id}
- GetEmpleadosById: /departamentos/{id}/empleados
#### Con autenticación:
- Post: /departamentos
- Put: /departamentos/{id}
#### WebSocket
- WS: /updates/departamentos


### User
#### Sin autenticar:
- PostLogin: /users/login
- PostRegister: /users/register
#### Con autenticación:
- GetAll: /users/list


### Storage
#### Sin autenticar:
- Post: /storage
- Get: /storage/{fileName}
#### Con autenticación:
- Delete: /storage/{fileName}
