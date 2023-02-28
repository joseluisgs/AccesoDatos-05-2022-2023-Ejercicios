FROM gradle:7-jdk17 as build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle bootJar --no-daemon
FROM openjdk:17-jdk-slim-buster
EXPOSE 6969:6969
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/empleadodepartamentospring3-0.0.1-SNAPSHOT.jar /app/empleadoDepartamentoSpring.jar
ENTRYPOINT ["java", "-jar", "/app/empleadoDepartamentoSpring.jar"]