FROM openjdk:20
COPY /target/araok-eureka-server-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "/app/app.jar"]