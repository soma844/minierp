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



&#x20;   Rest Controller

&#x20;   DTO\[Request / Response DTO]

&#x20;   Mapper\[DTO Mapper]

&#x20;   Service\[Application Service]

&#x20;   Domain\[Domain Model]

&#x20;   Repository\[Repository]

&#x20;   Entity\[JPA Entity]

&#x20;   Database\[(PostgreSQL)]



&#x20;   Controller --> DTO

&#x20;   DTO --> Mapper

&#x20;   Mapper --> Service

&#x20;   Service --> Domain

&#x20;   Service --> Repository

&#x20;   Repository --> Entity

&#x20;   Entity --> Database



\## 3. Backend-Schichten



Das Backend verwendet eine klare Trennung der Verantwortlichkeiten.



\### REST Controller



Die Controller bilden die HTTP-Schnittstelle der Anwendung.



Beispiele:



\- `KundeController`

\- `LichtkuppelKonfigurationController`

\- `AuftragController`

\- `ProduktionsAuftragController`



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

&#x20;   |

&#x20;   | technische Prüfung erfolgreich

&#x20;   v

FREIGABEBEREIT

&#x20;   |

&#x20;   | Freigabe

&#x20;   v

FREIGEGEBEN



Er soll jedoch fachliche Regeln nicht unnötig selbst duplizieren.



\## 6. Domain Model



Die fachlich wichtigen Regeln befinden sich im Domainmodell.



Ein Beispiel ist der technische Status einer Konfiguration:



FREIGABEBEREIT

&#x20;   |

&#x20;   | Freigabe

&#x20;   v

FREIGEGEBEN



Ein Auftrag darf nur freigegeben werden, wenn:



\- mindestens eine Auftragsposition existiert

\- alle verwendeten Konfigurationen freigegeben sind



Ein Produktionsauftrag darf nur aus einem bereits freigegebenen Auftrag

erstellt werden.



Der Produktionsstatus folgt dem Ablauf:



GEPLANT

&#x20;   |

&#x20;   v

IN\_PRODUKTION

&#x20;   |

&#x20;   v

PRODUZIERT

&#x20;   |

&#x20;   v

ABGESCHLOSSEN



Ungültige Statusübergänge führen zu einer Geschäftsregelverletzung.



\## 7. Persistence



Die Persistenzschicht verwendet Spring Data JPA und Hibernate.



Domain Model

&#x20;   |

&#x20;   v

Persistence Mapper

&#x20;   |

&#x20;   v

JPA Entity

&#x20;   |

&#x20;   v

Spring Data Repository

&#x20;   |

&#x20;   v

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

&#x20;   |

&#x20;   v

API Client

&#x20;   |

&#x20;   v

HTTP / REST

&#x20;   |

&#x20;   v

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

&#x20;   |

&#x20;   | mvn package

&#x20;   v

Spring Boot JAR

&#x20;   |

&#x20;   v

JRE Runtime Image



Die Build-Werkzeugen müssen dadurch nicht im Runtime-Container enthalten sein.



\### Frontend



Node.js

&#x20;   |

&#x20;   | npm build

&#x20;   v

HTML / CSS / JavaScript

&#x20;   |

&#x20;   v

Nginx Runtime Image



Node.js wird nur zum Erzeugen des Vue-Builds benötigt.



Im produktiven Frontend-Container läuft nur Nginx.



\## 12. Docker Compose



Docker Compose verbindet lokal drei Services:





&#x20;   Browser --> Frontend

&#x20;   Frontend --> Backend

&#x20;   Backend --> DB



&#x20;   Frontend\[frontend Vue + Nginx]

&#x20;   Backend\[backend Spring Boot]

&#x20;   DB\[(db PostgreSQL)]





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



&#x20;   User\[Browser]

&#x20;   kubectl port-forward



&#x20;   frontend Service

&#x20;   Vue / Nginx Pod



&#x20;   backend Service

&#x20;   Spring Boot Pod



&#x20;   db Service

&#x20;   PostgreSQL Pod

&#x20;   Persistent Volume Claim





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



&#x20;   A\[Git Push]

&#x20;   B\[Backend Tests]

&#x20;   C\[Frontend Tests]

&#x20;   D\[Frontend Build]

&#x20;   E\[Docker Build]

&#x20;   F\[GitLab Registry]

&#x20;   G\[Playwright E2E]



&#x20;   A --> B

&#x20;   A --> C

&#x20;   C --> D

&#x20;   B --> E

&#x20;   D --> E

&#x20;   E --> F

&#x20;   F --> G



Die Docker-Images werden unter anderem mit dem Commit-Identifier versioniert

und zusätzlich für den aktuellen Hauptstand als `latest` veröffentlicht.



\## 17. Teststrategie



Die Anwendung wird auf mehreren Ebenen getestet.



Domain / Unit Tests

&#x20;       |

&#x20;       v

Service Tests

&#x20;       |

&#x20;       v

Controller Tests

&#x20;       |

&#x20;       v

Vue Component Tests

&#x20;       |

&#x20;       v

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

