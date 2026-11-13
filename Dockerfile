
# Etapa 1: Compilacion
FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar pom.xml y descargar dependencias
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar codigo fuente
COPY src ./src

# Compilar el proyecto
RUN mvn clean package -DskipTests

# Etapa 2: Ejecucion
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copiar el JAR generado
COPY --from=build /app/target/*.jar app.jar

# Puerto de Spring Boot
EXPOSE 8080

# Ejecutar como usuario sin privilegios
USER 10001

ENTRYPOINT ["java", "-jar", "app.jar"]
