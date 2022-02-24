FROM openjdk:11
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} ordine-service.jar
ENTRYPOINT ["java", "-jar", "/ordine-service.jar"]
EXPOSE 9001
