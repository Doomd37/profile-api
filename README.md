# Profile API Backend

## Overview
This is a Spring Boot REST API that builds user profiles by integrating external APIs:
- Genderize API
- Agify API
- Nationalize API

It stores results in PostgreSQL and exposes CRUD endpoints.

---

## Tech Stack
- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Lombok
- WebClient

---

## API Endpoints

### Create Profile
POST /api/profiles
```json
{ "name": "ella" }
Get Profile

GET /api/profiles/{id}

Get All Profiles

GET /api/profiles?gender=male&country_id=NG&age_group=adult

Delete Profile

DELETE /api/profiles/{id}

External APIs Used
https://api.genderize.io
https://api.agify.io
https://api.nationalize.io
Setup Instructions
Clone repository
Configure PostgreSQL in application.properties
Run application:
mvn spring-boot:run

 Notes
All IDs are UUID v7
CORS enabled globally
All timestamps are UTC (ISO 8601)
Duplicate names are not stored twice (idempotent design)