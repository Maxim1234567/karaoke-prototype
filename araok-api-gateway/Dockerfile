FROM openjdk:20
COPY /target/araok-api-gateway-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8765
ENTRYPOINT ["java", "-jar", "/app/app.jar"]