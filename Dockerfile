FROM ubuntu:latest AS build
RUN apt-get update -y
RUN apt-get install -y openjdk-21-jdk
COPY . /app
RUN /app/gradlew build

FROM openjdk:21-slim AS runtime
COPY --from=build /app/build/libs/ecoufpel-app*SNAPSHOT.jar /app/application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/application.jar"]
