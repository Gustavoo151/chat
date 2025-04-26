# Build comum - Use uma imagem que suporta Java 21
# Servidor
FROM eclipse-temurin:21-jre AS server
WORKDIR /app
COPY target/*.jar ./app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar", "--server"]

# Cliente
FROM eclipse-temurin:21-jre AS client
WORKDIR /app
COPY target/*.jar ./app.jar
CMD ["java", "-jar", "app.jar", "--client"]