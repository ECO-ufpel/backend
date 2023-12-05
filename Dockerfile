FROM openjdk:21-slim
COPY --from=build /app/build/libs/ecoufpel-app*SNAPSHOT.jar /application.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/application.jar"]