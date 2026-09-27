# ==========================================================
# MiniERP Backend - Docker Image
# Multi-Stage-Build: Maven Build -> Java Runtime
# ==========================================================

# ----------------------------------------------------------
# Stage 1: Spring-Boot-Anwendung bauen
# ----------------------------------------------------------

FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

# Maven-Projektbeschreibung zuerst kopieren
COPY pom.xml .

# Abhängigkeiten vorab herunterladen
RUN mvn -B dependency:go-offline

# Java-Quellcode kopieren
COPY src ./src

# Anwendung bauen; 
RUN mvn -B clean package -DskipTests


# ----------------------------------------------------------
# Stage 2: Anwendung ausführen
# ----------------------------------------------------------

FROM eclipse-temurin:21-jre

WORKDIR /app

# Fertiges Spring-Boot-JAR aus der Build-Stage übernehmen
COPY --from=build /app/target/*.jar app.jar

# Spring Boot läuft auf Port 8080
EXPOSE 8080

# Backend beim Containerstart ausführen
ENTRYPOINT ["java", "-jar", "app.jar"]
