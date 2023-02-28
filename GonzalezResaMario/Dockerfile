# Generacion de una imagen docker
FROM gradle:7-jdk17 as build

# Se copia el codigo fuente de la aplicacion; el directorio actual y se ejecuta la operacion gradle que genera el jar
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon

# Generamos la imagen que ejecuta la aplicacion
FROM openjdk:17-jdk-slim-buster
EXPOSE 6969:6969
# Directorio donde se almacena la aplicacion
RUN mkdir /app
# Copiamos el jar
COPY --from=build /home/gradle/src/build/libs/empleadodepartamentospring3-0.0.1-SNAPSHOT.jar /app/empleadoDepartamentoSpring.jar
# Ejecutamos la aplicacion
ENTRYPOINT ["java", "-jar", "/app/empleadoDepartamentoSpring.jar"]