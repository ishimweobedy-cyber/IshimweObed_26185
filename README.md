# Kigali Clinic Management System

This is a backend REST API built with **Spring Boot** and **PostgreSQL** for managing a clinic's doctors, patients, appointments, prescriptions, offices, and specializations.

---

## In this system it allows a clinic to:

- Register and manage doctors and patients
- Book, reschedule, and cancel appointments
- Write and track prescriptions
- Assign doctors to offices and specializations
- View a clinic dashboard summary

---

## Technologies that I have Used

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- PostgreSQL

---

## Database Configuration

Before running this project, I made sure PostgreSQL is installed and that I have created my clinic_db database.

**Database Details:**

| Field         | Value       |
|---------------|-------------|
| Database Name | clinic_db   |
| Username      | postgres    |
| Password      | admin       |
| Port          | 5432        |

**Creating database:**

```sql
psql -U postgres
CREATE DATABASE clinic_db;
\q
```

These settings are found in `src/main/resources/application.properties`.

---

## How to Run:

**Step 1 — Make sure you have:**
- Java 17 installed
- PostgreSQL installed and running
- The database `clinic_db` created

**Step 2 — Run the application:**
```bash
./mvnw spring-boot:run
```
On Windows:
```cmd
mvnw.cmd spring-boot:run
```

**Step 3 — The server starts at:**
```
http://localhost:8080
```

---

## API Endpoints (Used in Postman)

Base URL: `http://localhost:8080`

---

### Specializations

| Method | Endpoint                          | Description                  |
|--------|-----------------------------------|------------------------------|
| GET    | /api/specializations              | Get all specializations      |
| GET    | /api/specializations/{id}         | Get specialization by ID     |
| POST   | /api/specializations/save         | Create a new specialization  |
| DELETE | /api/specializations/delete/{id}  | Delete a specialization      |

**POST /api/specializations/save — Body (JSON):**
```json
{
  "name": "Cardiology"
}
```

---

### Offices

| Method | Endpoint                  | Description              |
|--------|---------------------------|--------------------------|
| GET    | /api/offices              | Get all offices          |
| GET    | /api/offices/{id}         | Get office by ID         |
| GET    | /api/offices/free         | Get offices with no doctor assigned |
| POST   | /api/offices/save         | Create a new office      |
| DELETE | /api/offices/delete/{id}  | Delete an office         |

**POST /api/offices/save — Body (JSON):**
```json
{
  "officeNumber": 101,
  "floor": 1
}
```

---

### Doctors

| Method | Endpoint                                          | Description                        |
|--------|---------------------------------------------------|------------------------------------|
| GET    | /api/doctors                                      | Get all doctors                    |
| GET    | /api/doctors/{id}                                 | Get doctor by ID                   |
| GET    | /api/doctors/search?name=John                     | Search doctors by name             |
| GET    | /api/doctors/{doctorId}/agenda?date=2026-10-15    | Get doctor's appointments for a day|
| GET    | /api/doctors/workload?start=2026-10-01&end=2026-10-31 | Get doctor workload report     |
| POST   | /api/doctors/save                                 | Create a new doctor                |
| PUT    | /api/doctors/update/{id}                          | Update doctor details              |
| PUT    | /api/doctors/{doctorId}/specializations/{specializationId} | Add specialization to doctor |
| DELETE | /api/doctors/{doctorId}/specializations/{specializationId} | Remove specialization from doctor |
| PATCH  | /api/doctors/{doctorId}/office?officeNumber=101   | Move doctor to another office      |
| DELETE | /api/doctors/delete/{id}                          | Delete a doctor                    |

**POST /api/doctors/save — Body (JSON):**
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@clinic.com",
  "phone": "0780000001",
  "office": { "id": 1 }
}
```

---

### Patients

| Method | Endpoint                              | Description                        |
|--------|---------------------------------------|------------------------------------|
| GET    | /api/patients                         | Get all patients                   |
| GET    | /api/patients/{id}                    | Get patient by ID                  |
| GET    | /api/patients/{ssNumber}/upcoming     | Get upcoming appointments for a patient |
| POST   | /api/patients/save                    | Register a new patient             |
| PUT    | /api/patients/update/{id}             | Update patient details             |
| DELETE | /api/patients/delete/{id}             | Delete a patient                   |

**POST /api/patients/save — Body (JSON):**
```json
{
  "firstName": "Jane",
  "lastName": "Smith",
  "ssNumber": "SS123456",
  "dateOfBirth": "1990-05-20",
  "phone": "0780000002"
}
```

---

### Appointments

| Method | Endpoint                                          | Description                          |
|--------|---------------------------------------------------|--------------------------------------|
| GET    | /api/appointments                                 | Get all appointments                 |
| GET    | /api/appointments/{id}                            | Get appointment by ID                |
| POST   | /api/appointments/save                            | Book a new appointment               |
| PUT    | /api/appointments/update/{id}                     | Update appointment details           |
| PATCH  | /api/appointments/{id}/status?status=COMPLETED    | Update appointment status            |
| PATCH  | /api/appointments/{id}/reschedule?newDate=2026-11-01 | Reschedule an appointment         |
| DELETE | /api/appointments/delete/{id}                     | Delete an appointment                |

**Appointment Status values:** `PENDING`, `COMPLETED`, `CANCELLED`

**POST /api/appointments/save — Body (JSON):**
```json
{
  "appointmentDate": "2026-11-15",
  "doctor": { "id": 1 },
  "patient": { "id": 1 }
}
```

---

### Prescriptions

| Method | Endpoint                                          | Description                              |
|--------|---------------------------------------------------|------------------------------------------|
| GET    | /api/prescriptions/appointment/{appointmentId}    | Get prescriptions for an appointment     |
| GET    | /api/prescriptions/patient/{ssNumber}             | Get prescription history of a patient   |
| GET    | /api/prescriptions/stats/top-medicines            | Get most prescribed medicines            |
| POST   | /api/prescriptions/save?appointmentId=1           | Write a prescription for an appointment |
| PUT    | /api/prescriptions/update/{id}                    | Update a prescription                    |
| DELETE | /api/prescriptions/delete/{id}                    | Delete a prescription                    |

> Note: A prescription can only be written for an appointment with status **COMPLETED**.

**POST /api/prescriptions/save?appointmentId=1 — Body (JSON):**
```json
{
  "medicineName": "Amoxicillin",
  "dosage": "500mg",
  "instructions": "Take twice a day after meals"
}
```

---

### Dashboard

| Method | Endpoint               | Description                                      |
|--------|------------------------|--------------------------------------------------|
| GET    | /api/dashboard/summary | Get total counts of doctors, patients, appointments |

---

## Important Rules

- You **cannot book an appointment** in the past
- A prescription can **only be written** for a **COMPLETED** appointment
- You **cannot delete an appointment** that has prescriptions linked to it
- An appointment can **only be rescheduled** if it is still **PENDING**
- A doctor **cannot be assigned** to an office that is already taken

---

## Authored By

**Ishimwe Obed**
Student ID: 26185
AUCA — Web Technology Quiz02
