# Neighborhood Security Alert System Backend

**Student:** IZABAYO Parfait  
**Student ID:** 27144  
**Course:** INSY 8322 Web Technology and Internet  
**Project:** Neighborhood Security Alert System

Spring Boot REST API using Spring Web, Spring Data JPA and PostgreSQL. This project follows the supplied examination specification for the domain model, CRUD, derived queries, JPQL queries, business rules, response actions, reports, bulk updates and dashboard/page bonus endpoints.

## Requirements
- Java 17+
- PostgreSQL
- Maven 3.9+ (or `mvnw` if you add the Maven wrapper)

## Database setup
Create the database in PostgreSQL:
```sql
CREATE DATABASE neighborhood_security_alert_system;
```
Set the password in `src/main/resources/application.properties` or use environment variable `DB_PASSWORD`. The defaults are user `postgres` and password `Parfait1@`; change the password to your local PostgreSQL password before running.

## Run
From this directory:
```bash
mvn spring-boot:run
```
API base URL: `http://localhost:8080`. Hibernate creates/updates the tables with `spring.jpa.hibernate.ddl-auto=update`.

## Main endpoints
- `/api/incident-types`
- `/api/patrol-zones`
- `/api/security-guards`
- `/api/residents`
- `/api/incident-reports`
- `/api/response-actions`
- `/api/dashboard/summary`

Each main entity supports `/save`, `/all`, `/{id}`, `/update/{id}`, and `/delete/{id}`. Consult the examination paper for exact query/business-rule paths and required Postman demonstrations.

## Important
Use the save endpoints to add related entities in this order: incident types and patrol zones, then guards, residents, incident reports, and finally response actions for resolved reports. For foreign keys, use the UUIDs returned by earlier API responses. Seed data is provided in `database/seed-guide.md`; it is intentionally entered through the API as the examination requires.
