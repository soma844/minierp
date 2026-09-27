# ==========================================================
# MiniERP Frontend - Docker Image
#
# Multi-Stage-Build:
# 1. Vue/Vite-Anwendung mit Node.js bauen
# 2. Erzeugte statische Dateien über Nginx ausliefern
# ==========================================================


# ----------------------------------------------------------
# Stage 1: Frontend bauen
# ----------------------------------------------------------

FROM node:22-alpine AS build

# Arbeitsverzeichnis innerhalb des Build-Containers
WORKDIR /app

# Paketdefinitionen zuerst kopieren, damit Docker den
# Dependency-Layer bei unveränderten Dateien wiederverwenden kann.
COPY package*.json ./

# Reproduzierbare Installation anhand des package-lock.json
RUN npm ci

# Restlichen Frontend-Quellcode kopieren
COPY . .

# Produktionsbuild mit Vite erzeugen.
# Das Ergebnis befindet sich anschließend unter /app/dist.
RUN npm run build


# ----------------------------------------------------------
# Stage 2: Produktions-Webserver
# ----------------------------------------------------------

FROM nginx:1.27-alpine

# Projektspezifische Nginx-Konfiguration übernehmen
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Nur das fertige Frontend aus der Build-Stage übernehmen.
# Node.js und der Quellcode werden im Runtime-Image nicht gebraucht.
COPY --from=build /app/dist /usr/share/nginx/html

# Nginx stellt das Frontend über HTTP bereit
EXPOSE 80
