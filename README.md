
# Global Class Offering Booking System

## Overview

This project is a **backend service for managing course offerings and bookings on a global live-learning platform.**

**Teachers can create course offerings and schedule sessions in their own timezone. Parents and students can browse available offerings, book them, and view schedules in their local timezone.**

The system also prevents parents from **booking overlapping offerings** and handles **concurrent booking requests** safely.



## Tech Stack

### Backend

* Java 21
* Spring Boot
* Spring Data JPA
* Hibernate

### Database

* MySQL 8

### Build Tool

* Maven

### Testing

* JUnit 5
* Mockito

### Utilities

* Lombok



## Core Concepts

### Course

Represents a learning program.

Examples:

* Python Coding
* Art Drawing
* Public Speaking

---

### Offering

A schedulable version of a course.

Examples:

* Saturday Batch
* Evening Batch
* Summer Camp

Each offering belongs to:

* One Course
* One Teacher

An offering can contain multiple sessions.


### Session

Represents an actual class meeting.

Example:

Saturday Batch

* Session 1 → June 7, 6 PM - 7 PM
* Session 2 → June 14, 6 PM - 7 PM

**All session times are stored in UTC in the database.**


### Booking

* Parents book an entire offering.

*  Booking an offering automatically includes all sessions within that offering.


## Database Schema Overview

### teachers

| Column   | Type    |
| -------- | ------- |
| id       | BIGINT  |
| name     | VARCHAR |
| timezone | VARCHAR |


### parents

| Column   | Type    |
| -------- | ------- |
| id       | BIGINT  |
| name     | VARCHAR |
| timezone | VARCHAR |

---

### courses

| Column      | Type    |
| ----------- | ------- |
| id          | BIGINT  |
| name        | VARCHAR |
| description | VARCHAR |

---

### offerings

| Column     | Type    |
| ---------- | ------- |
| id         | BIGINT  |
| title      | VARCHAR |
| teacher_id | BIGINT  |
| course_id  | BIGINT  |

---

### sessions

| Column         | Type      |
| -------------- | --------- |
| id             | BIGINT    |
| offering_id    | BIGINT    |
| session_order  | INT       |
| start_time_utc | TIMESTAMP |
| end_time_utc   | TIMESTAMP |

---

### bookings

| Column      | Type      |
| ----------- | --------- |
| id          | BIGINT    |
| parent_id   | BIGINT    |
| offering_id | BIGINT    |
| booked_at   | TIMESTAMP |

Unique Constraint:

**(parent_id, offering_id)**

This prevents duplicate bookings for the same offering.



## Environment Variables / Configuration

Update the following values in:

src/main/resources/application.properties

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/global_booking_db
spring.datasource.username=YOUR_USERNAME
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```


## Setup Instructions

### 1. Clone Repository

```bash
git clone https://github.com/pratap-rahul15/Global-Course-Booking-System
```

### 2. Create Database

```sql
CREATE DATABASE global_booking_db;
```

### 3. Configure Database Credentials

Update **application.properties** with your MySQL credentials.

### 4. Run Application

Using IntelliJ:

Run:

**GlobalBookingSystemApplication**

or

```bash
mvn spring-boot:run
```

Application starts on:

```text
http://localhost:8080
```


## API Documentation


### 1. Create Offering

POST

```http
http://localhost:8080/api/offerings
```

Request Body

```json
{
  "title": "Saturday Batch",
  "teacherId": 1,
  "courseId": 1
}
```

---

### 2. Add Sessions to Offering

POST

```http
http://localhost:8080/api/offerings/{offeringId}/sessions
```

Example

```json
{
  "timezone": "Asia/Kolkata",
  "sessions": [
    {
      "sessionOrder": 1,
      "startTime": "2026-06-07T18:00:00",
      "endTime": "2026-06-07T19:00:00"
    }
  ]
}
```

---

### 3. View Teacher Offerings

GET

```http
http://localhost:8080/api/offerings/teacher/{teacherId}
```

Example

```http
http://localhost:8080/api/offerings/teacher/1
```

---

### 4. View Available Offerings

GET

```http
http://localhost:8080/api/offerings/parent/{parentId}
```

Example

```http
http://localhost:8080/api/offerings/parent/1
```

---

### 5. Book Offering

POST

```http
http://localhost:8080/api/bookings
```

Request Body

```json
{
  "parentId": 1,
  "offeringId": 1
}
```

---

### 6. View Parent Bookings

GET

```http
http://localhost:8080/api/bookings/parent/{parentId}
```

Example

```http
http://localhost:8080/api/bookings/parent/1
```

---

## Timezone Handling Approach

**Teachers create sessions in their local timezone.**

Example:

```text
Teacher Timezone: Asia/Kolkata
Session Time: 2026-06-07 18:00
```

Before persisting in the DB, the application converts the session time to UTC and stores it as an Instant.

Example:

```text
2026-06-07 18:00 IST
↓
2026-06-07 12:30 UTC
```

When a parent requests available offerings or bookings, UTC timestamps are converted into the parent's timezone before being returned.

This ensures consistent scheduling across different countries and timezones.

---

## Concurrency Handling Approach

The booking flow uses:

* **@Transactional**
* **Pessimistic Database Locking**

Implementation:

```
@Lock(LockModeType.PESSIMISTIC_WRITE)
```

When a booking request is received:

1. Parent row is locked.
2. Existing bookings are loaded.
3. Session overlap validation is performed.
4. Booking is persisted.
5. Transaction is committed.

This prevents race conditions and ensures data consistency during simultaneous booking attempts.

---

## Booking Conflict Detection

Parents book an entire offering, not individual sessions.

Before creating a booking:

1. Sessions of the requested offering are loaded.
2. Sessions from all existing bookings are loaded.
3. Time overlap is checked.

Overlap condition:

```
newSession.start < existingSession.end
&&
newSession.end > existingSession.start
```

If any overlap exists, booking is rejected.

HTTP Response:

```http
409 CONFLICT
```

---

## Assumptions I have Made

* A parent books an entire offering, not individual sessions.
* Session times are stored only in UTC.
* Teacher and Parent timezones are valid IANA timezone identifiers (ex. Asia/Kolkata, America/New_York).
* Offerings may contain one or more sessions.
* Booking conflicts are evaluated across all sessions of booked offerings.

---

## Tests Implemented

BookingService Unit Tests:

* Successful Booking
* Duplicate Booking Rejection
* Overlapping Booking Rejection


# Thank You!

