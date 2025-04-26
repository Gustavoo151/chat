FROM ubuntu:latest
FROM openjdk:23-slim
LABEL authors="joseg"

WORKDIR /app
COPY target/sd-server.jar .
EXPOSE 8080
CMD ["java", "-jar", "sd-server.jar"]

FROM openjdk:23-slim
LABEL authors="joseg"

WORKDIR /app
COPY target/sd-client.jar .
CMD ["java", "-jar", "sd-client.jar"]

ENTRYPOINT ["top", "-b"]