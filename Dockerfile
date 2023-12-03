FROM ubuntu:latest AS build
RUN apt-get update -y
RUN apt-get install -y openjdk-21-jdk
COPY . .
RUN gradlew build

FROM openjdk:21-slim AS runtime
COPY --from=build /build/libs/ecoufpel-app*SNAPSHOT.jar /application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/application.jar"]
