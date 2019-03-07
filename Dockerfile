FROM java:8
ADD target/docker-member-service.jar docker-member-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "docker-member-service.jar"]