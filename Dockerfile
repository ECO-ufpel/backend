FROM ubuntu:latest AS build
RUN apt-get update -y
RUN apt-get install -y openjdk-21-jdk
COPY . ./app
WORKDIR /app
RUN ["./gradlew", "build", "--exclude-task=test"]
#
FROM openjdk:21-slim
COPY --from=build /app/build/libs/ecoufpel-app*SNAPSHOT.jar /application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/application.jar"]