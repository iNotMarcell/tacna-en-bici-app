
# Usamos una imagen oficial de Maven con Java 21
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copiamos el pom.xml y descargamos las dependencias
COPY pom.xml .
# Copiamos el código fuente
COPY src ./src

# Compilamos el proyecto creando el archivo .jar (omitiendo los tests para mayor velocidad)
RUN mvn clean package -DskipTests


# Usamos una imagen más ligera de Java 21 para correr la app
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app

# Copiamos el .jar generado en la etapa 1 hacia esta etapa final
COPY --from=build /app/target/*.jar app.jar

# Exponemos el puerto 8080 (Render lo enlazará dinámicamente)
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]