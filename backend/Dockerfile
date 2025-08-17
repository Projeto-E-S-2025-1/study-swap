FROM ubuntu:latest AS build

RUN apt-get update
RUN apt-get install -y openjdk-21-jdk maven
COPY . .

RUN mvn clean install -DskipTests

FROM openjdk:21-jdk

EXPOSE 8080

COPY --from=build /target/housing-0.0.1-SNAPSHOT.jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
