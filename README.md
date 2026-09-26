\# MiniERP



MiniERP ist eine Full-Stack-Anwendung zur technischen Konfiguration,

Auftragsbearbeitung und Produktionssteuerung eines konfigurierbaren

Industrieprodukts, es ist ein Demo Projekt.



Das Projekt wurde als Portfolio- und Lernprojekt umgesetzt und verbindet

klassische ERP-Domänenlogik mit moderner Softwarearchitektur, automatisierten

Tests, Containerisierung, CI/CD und Kubernetes.



\---



\## Fachlicher Ablauf



Der zentrale Geschäftsprozess ist:



```mermaid

flowchart LR

&#x20;   A\[Kunde] --> B\[Konfiguration]

&#x20;   B --> C\[Technische Prüfung]

&#x20;   C --> D\[Freigabe]

&#x20;   D --> E\[Auftrag]

&#x20;   E --> F\[Auftragsfreigabe]

&#x20;   F --> G\[Produktionsauftrag]

&#x20;   G --> H\[IN\_PRODUKTION]

&#x20;   H --> I\[PRODUZIERT]

&#x20;   I --> J\[ABGESCHLOSSEN]



Eine technische Konfiguration muss geprüft und freigegeben sein,

bevor sie in einem Auftrag verwendet werden kann.

Ein Produktionsauftrag darf wiederum nur aus einem freigegebenen Auftrag

erzeugt werden.



Funktionen



Kundenverwaltung



Unterstützt werden:

\- Kunde anlegen

\- Kunde anzeigen

\- Kunde bearbeiten

\- Kunde löschen

\- Liste aller Kunden

Ein Kunde kann nicht gelöscht werden, wenn bereits Aufträge zugeordnet sind.



Produktkonfiguration



Aktuell werden unter anderem folgende Produktausführungen unterstützt:

\- FEST

\- LUEFTUNG



Die Konfiguration enthält technische Daten, beispielsweise:

\- Projektabmessungen

\- Aufsetzkranz

\- Rahmen

\- Material

\- Füllung

\- Öffnungsrahmen

\- Antrieb

\- Steuerung

\- Versorgungsspannung



Auftragsverwaltung



Ein Auftrag besteht aus:

\- Auftragsnummer

\- Kunde

\- einer oder mehreren Positionen

\- freigegebenen Produktkonfigurationen

\- Preis

\- Rabatt

\- Gesamtwert

\- Auftragsstatus

Eine Auftragsfreigabe ist nur möglich, wenn:

1\. mindestens eine Position existiert

2\. alle verwendeten Konfigurationen technisch freigegeben sind



Architektur



Die Anwendung besteht aus einem Vue-Frontend, einem Spring-Boot-Backend und

einer PostgreSQL-Datenbank.



Backend



Technologien:

\- Java 21

\- Spring Boot 4

\- Spring Web

\- Spring Data JPA

\- Hibernate

\- PostgreSQL

\- H2 für Tests

\- Flyway

\- Bean Validation

\- OpenAPI / Swagger

\- Maven



Geschäftslogik befindet sich im Domain- und Service-Layer.



Frontend



Technologien:

\- Vue 3

\- TypeScript

\- Vite

\- Vue Router

\- Fetch API

\- Vitest

\- Vue Test Utils

\- Playwright



End-to-End Test



Zusätzlich existiert ein Playwright-End-to-End-Test.



Docker



Backend und Frontend besitzen eigene Multi-Stage-Docker-Builds.



Docker Compose



Der vollständige Stack kann lokal mit Docker Compose gestartet werden.



CI/CD



Das Repository verwendet GitLab CI/CD.



Die CI-Pipeline:



\- führt Backendtests aus

\- führt Frontendtests aus

\- baut das Vue-Frontend

\- baut Docker-Images

\- veröffentlicht die Images in der privaten GitLab Container Registry

\- startet einen vollständigen Container-Stack

\- führt den Playwright-E2E-Test gegen diesen Stack aus



Container Registry



Die CI-Pipeline veröffentlicht zwei Images:



registry.gitlab.com/.../minierp/backend

registry.gitlab.com/.../minierp/frontend



Kubernetes



Der Stack kann man auch unter Kubernetes betreiben.



Aktueller Scope



MiniERP v1 konzentriert sich bewusst auf:

\- Produktkonfiguration

\- technische Prüfung

\- technische Freigabe

\- Kundenverwaltung

\- Auftragsverwaltung

\- Produktionsauftrag

\- Produktionsstatus

Noch nicht Bestandteil von v1 sind beispielsweise:

\- Artikelstamm

\- Stücklisten

\- Lagerverwaltung

\- Materialbewegungen

\- Materialbedarfsplanung

\- Hochregallager

\- Benutzer- und Rollenverwaltung



Diese Funktionen können in einer späteren Ausbaustufe ergänzt werden.

















