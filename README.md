# Profile API Backend

## Overview
This is a Spring Boot REST API that builds user profiles by integrating external APIs:
- Genderize API
- Agify API
- Nationalize API

It stores results in PostgreSQL and exposes CRUD endpoints.

---

## Tech Stack
- Java 17+
- Spring Boot 3.2.5
- Spring Data JPA
- PostgreSQL
- Lombok
- WebClient (Spring WebFlux)
- UUID Creator (for UUID v7)

---

## API Endpoints

### Create Profile
**POST** `/api/profiles`

**Request Body:**
```json
{
  "name": "ella"
}
```

**Response:** `201 Created` or `200 OK` (if profile already exists)

### Get Profile by ID
**GET** `/api/profiles/{id}`

**Response:** `200 OK` with profile data

### Get All Profiles
**GET** `/api/profiles`

**Query Parameters (optional):**
- `gender` - Filter by gender (e.g., male, female)
- `country_id` - Filter by country code (e.g., NG, US)
- `age_group` - Filter by age group (child, teenager, adult, senior)

**Example:** `GET /api/profiles?gender=male&country_id=NG&age_group=adult`

**Response:** `200 OK` with filtered profiles

### Delete Profile
**DELETE** `/api/profiles/{id}`

**Response:** `204 No Content`

---

## External APIs Used
- https://api.genderize.io
- https://api.agify.io
- https://api.nationalize.io

---

## Setup Instructions

### Prerequisites
- Java 17 or higher
- Maven
- PostgreSQL database

### Clone Repository
```bash
git clone https://github.com/emmanuel-40/profile-api.git
cd profile-api
```

### Local Development Setup

#### Step 1: Set up Local Database

**Option 1: Using Local PostgreSQL (Recommended)**
1. Install PostgreSQL on your machine if not already installed
2. Create a database:
```sql
CREATE DATABASE profile_api;
```
3. Create a user with appropriate credentials (or use existing postgres user)

**Option 2: Using Docker**
```bash
# Start PostgreSQL container
docker run --name profile-api-db \
  -e POSTGRES_DB=profile_api \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:15
```

#### Step 2: Run Application Locally
```bash
# Run with local profile (uses application-local.properties)
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

The API will be available at `http://localhost:8080`

### Production Deployment (Railway)

The application is configured to use Railway environment variables automatically:

**Railway Environment Variables to Set:**
- `DATABASE_URL` - Your Railway PostgreSQL connection URL
- `DATABASE_USERNAME` - Database username (typically `postgres`)
- `DATABASE_PASSWORD` - Your Railway database password

**Note:** The `application.properties` file uses these environment variables with Railway as defaults. When deployed to Railway, these variables are automatically provided by the platform.

### Configuration Files

- `application.properties` - Production configuration (uses environment variables)
- `application-local.properties` - Local development configuration (gitignored)
- `application-local.properties.example` - Example template for local configuration
- The local file contains your local database credentials and won't be pushed to GitHub

**To set up local configuration:**
```bash
# Copy the example file
cp src/main/resources/application-local.properties.example src/main/resources/application-local.properties

# Edit with your local database credentials
# Then run with local profile
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

---

## Technical Notes
- All IDs are UUID v7 (time-ordered)
- CORS enabled globally for cross-origin requests
- All timestamps are UTC (ISO 8601 format)
- Duplicate names are not stored twice (idempotent design)
- Input validation: Names must contain only letters (a-z, A-Z)
- Age groups: child (≤12), teenager (13-19), adult (20-59), senior (60+)

---

## Author
**Eze Emmanuel** - [GitHub](https://github.com/emmanuel-40)