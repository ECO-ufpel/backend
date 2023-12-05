FROM openjdk:21-slim
COPY ./build/libs/ecoufpel-app*SNAPSHOT.jar /application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/application.jar"]