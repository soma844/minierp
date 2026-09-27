\# Architektur



\## 1. Überblick



MiniERP ist als Full-Stack-Anwendung aufgebaut.



Die wesentlichen Komponenten sind:



\- Vue 3 / TypeScript Frontend

\- Nginx als Webserver und Reverse Proxy

\- Spring Boot / Java Backend

\- PostgreSQL

\- Flyway für Datenbankmigrationen

\- Docker

\- GitLab CI/CD

\- Kubernetes



Die Anwendung bildet einen durchgängigen Geschäftsprozess von der

Kundenverwaltung über die technische Produktkonfiguration bis zur Produktion ab.





\## 2. Systemarchitektur





Der Browser kommuniziert ausschließlich mit dem Frontend.



Nginx liefert die statischen Dateien der Vue-Anwendung aus.



REST-Aufrufe unter `/api/` werden von Nginx an das Spring-Boot-Backend

weitergeleitet.



Das Backend verarbeitet die Geschäftslogik und speichert persistente Daten

in PostgreSQL.



Rest Controller

DTO\[Request / Response DTO]

Mapper\[DTO Mapper]

Service\[Application Service]

Domain\[Domain Model]

Repository\[Repository]

Entity\[JPA Entity]

Database\[(PostgreSQL)]



Controller --> DTO

DTO --> Mapper

Mapper --> Service

Service --> Domain

Service --> Repository

Repository --> Entity

Entity --> Database



\## 3. Backend-Schichten



Das Backend verwendet eine klare Trennung der Verantwortlichkeiten.



\### REST Controller



Die Controller bilden die HTTP-Schnittstelle der Anwendung.



Beispiele:



KundeController

LichtkuppelKonfigurationController

AuftragController

ProduktionsAuftragController



Die Controller sollen möglichst wenig Geschäftslogik enthalten.



Ihre Hauptaufgaben sind:



1\. Request entgegennehmen

2\. Eingaben validieren

3\. Application Service aufrufen

4\. Ergebnis als DTO zurückgeben



\## 4. DTOs



REST-Daten werden nicht direkt als Domainobjekte veröffentlicht.

Dafür existieren Request- und Response-DTOs.

Dadurch bleibt die externe REST-Schnittstelle von der internen

Domainmodellierung getrennt.

\## 5. Application Services

Services koordinieren fachliche Use Cases.

Beispiele:

Kunde anlegen

Konfiguration anlegen

Konfiguration prüfen

Konfiguration freigeben

Auftrag anlegen

Auftrag freigeben

Produktionsauftrag erzeugen

Produktion starten

Produktion abschließen

```



Ein Service orchestriert Domainobjekte und Repositories.



ENTWURF

   |

   | technische Prüfung erfolgreich

   v

FREIGABEBEREIT

   |

   | Freigabe

   v

FREIGEGEBEN



Er soll jedoch fachliche Regeln nicht unnötig selbst duplizieren.



\## 6. Domain Model



Die fachlich wichtigen Regeln befinden sich im Domainmodell.



Ein Beispiel ist der technische Status einer Konfiguration:



FREIGABEBEREIT

   |

   | Freigabe

   v

FREIGEGEBEN



Ein Auftrag darf nur freigegeben werden, wenn:



\- mindestens eine Auftragsposition existiert

\- alle verwendeten Konfigurationen freigegeben sind



Ein Produktionsauftrag darf nur aus einem bereits freigegebenen Auftrag

erstellt werden.



Der Produktionsstatus folgt dem Ablauf:



GEPLANT

   |

   v

IN PRODUKTION

   |

   v

PRODUZIERT

   |

   v

ABGESCHLOSSEN



Ungültige Statusübergänge führen zu einer Geschäftsregelverletzung.



\## 7. Persistence



Die Persistenzschicht verwendet Spring Data JPA und Hibernate.



Domain Model

   |

   v

Persistence Mapper

   |

   v

JPA Entity

   |

   v

Spring Data Repository

   |

   v

PostgreSQL



Domainobjekte und JPA-Entities werden bewusst voneinander getrennt.



Damit ist das Domainmodell nicht direkt von JPA abhängig.



\## 8. Datenbankschema



PostgreSQL ist die persistente Datenbank der produktionsnahen Umgebung.



Das Schema wird mit Flyway versioniert.



Aktuell existieren Migrationen:



V1

V2

V3



Hibernate arbeitet mit:



properties

spring.jpa.hibernate.ddl-auto=validate



Damit prüft Hibernate das vorhandene Schema, verändert es jedoch nicht

automatisch.



Schemaänderungen werden stattdessen kontrolliert über Flyway vorgenommen.



\## 9. Frontend



Das Frontend basiert auf:



\- Vue 3

\- TypeScript

\- Vite

\- Vue Router



Die Oberfläche ist fachlich in vier Hauptbereiche gegliedert:



Kunden

Konfigurationen

Aufträge

Produktion



Der Zugriff auf das Backend erfolgt über eine zentrale HTTP-Abstraktion.



Vue View

   |

   v

API Client

   |

   v

HTTP / REST

   |

   v

Spring Boot



Dadurch wird die HTTP-Kommunikation nicht in jeder View neu implementiert.



\## 10. Fehlerbehandlung



Das Backend verwendet eine zentrale Fehlerbehandlung.



Beispiele:



DomainValidationException       -> HTTP 400

IllegalArgumentException        -> HTTP 400

GeschaeftsregelException        -> HTTP 409

IllegalStateException           -> HTTP 409

RessourceNichtGefundenException -> HTTP 404



REST-Fehler werden in einer konsistenten HTTP-Antwort dargestellt.



Damit werden technische Fehler und fachliche Fehler sauber voneinander

unterschieden.



\## 11. Docker-Architektur



Für Backend und Frontend werden Multi-Stage-Docker-Builds eingesetzt.



\### Backend



Maven + JDK

   |

   | mvn package

   v

Spring Boot JAR

   |

   v

JRE Runtime Image



Die Build-Werkzeugen müssen dadurch nicht im Runtime-Container enthalten sein.



\### Frontend



Node.js

   |

   | npm build

   v

HTML / CSS / JavaScript

   |

   v

Nginx Runtime Image



Node.js wird nur zum Erzeugen des Vue-Builds benötigt.



Im produktiven Frontend-Container läuft nur Nginx.



\## 12. Docker Compose



Docker Compose verbindet lokal drei Services:





   Browser --> Frontend

   Frontend --> Backend

   Backend --> DB



   Frontend\[frontend Vue + Nginx]

   Backend\[backend Spring Boot]

   DB\[(db PostgreSQL)]





Die Service-Namen werden innerhalb des Docker-Netzwerks als DNS-Namen

verwendet.



Das Backend verbindet sich deshalb beispielsweise mit:



jdbc:postgresql://db:5432/minierp



und nicht mit `localhost`.



\## 13. Kubernetes-Architektur



Für Kubernetes werden folgende Ressourcen eingesetzt:



\- Namespace

\- Deployments

\- Services

\- Secrets

\- PersistentVolumeClaim

\- Readiness Probes

\- Liveness Probes

\- Image Pull Secret



   User\[Browser]

   kubectl port-forward



   frontend Service

   Vue / Nginx Pod



   backend Service

   Spring Boot Pod



   db Service

   PostgreSQL Pod

   Persistent Volume Claim





Alle Ressourcen befinden sich im Namespace:



minierp







\## 14. Kubernetes Services



Die Anwendung verwendet drei interne Services.



\### db





db:5432



Der Backend-Pod greift über diesen stabilen DNS-Namen auf PostgreSQL zu.



\### backend



backend:8080



Das Frontend beziehungsweise Nginx leitet `/api/` auf diesen Service weiter.



\### frontend



frontend:80



Für den lokalen Zugriff kann Port Forwarding verwendet werden:



powershell

kubectl port-forward -n minierp service/frontend 8081:80



Die Anwendung ist anschließend erreichbar unter:



http://localhost:8081



\## 15. Persistenz unter Kubernetes



PostgreSQL verwendet einen PersistentVolumeClaim:



postgres-data





Damit werden Daten nicht ausschließlich im Lebenszyklus eines einzelnen Pods

gespeichert.



Ein PostgreSQL-Pod kann ersetzt werden, während das Persistent Volume bestehen

bleibt.





\## 16. CI/CD



Die GitLab-Pipeline enthält die Stages:



test

build

image

e2e



Der Ablauf ist:


   A\[Git Push]

   B\[Backend Tests]

   C\[Frontend Tests]

   D\[Frontend Build]

   E\[Docker Build]

   F\[GitLab Registry]

   G\[Playwright E2E]



   A --> B

   A --> C

   C --> D

   B --> E

   D --> E

   E --> F

   F --> G



Die Docker-Images werden unter anderem mit dem Commit-Identifier versioniert

und zusätzlich für den aktuellen Hauptstand als `latest` veröffentlicht.



\## 17. Teststrategie



Die Anwendung wird auf mehreren Ebenen getestet.



Domain / Unit Tests

       |

       v

Service Tests

       |

       v

Controller Tests

       |

       v

Vue Component Tests

       |

       v

Playwright End-to-End



Der Playwright-Test validiert den vollständigen Geschäftsprozess vom Kunden

bis zum abgeschlossenen Produktionsauftrag.



\## 18. Geplanter Ausbau



Eine spätere Version kann die ERP-Funktionalität erweitern um:



\- Artikelstamm

\- Komponenten

\- Stücklisten

\- Lagerorte

\- Bestände

\- Lagerbewegungen

\- Materialbedarf

\- Produktionsmaterial

\- Hochregallager

\- Wareneingang

\- Warenausgang



Diese Funktionen sind bewusst nicht Bestandteil von MiniERP v1.

