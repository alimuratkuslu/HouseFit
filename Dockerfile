FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-Dserver.port=9080", "-jar", "app.jar"]