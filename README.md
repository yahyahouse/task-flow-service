# Task Management System (Mini Trello Backend)

REST API sederhana untuk mengelola task board dengan status `TODO`, `IN_PROGRESS`, dan `DONE`.
Project ini menonjolkan praktik Spring yang benar: IoC via constructor injection, perhitungan reporting via Java Stream, dan reporting query via Native SQL.

## Tech Stack

- Java 21
- Spring Boot 4.0.2
- Spring Web
- Spring Data JPA
- Jakarta Validation
- H2 Database (in-memory)
- Maven

## Struktur Singkat

```text
src/main/java/com/yahyahouse/taskflow
├── controller
├── dto
│   ├── request
│   └── response
├── util
├── mapper
├── model
│   ├── entity
│   └── enums
├── repository
│   └── projection
└── service
    └── impl
```

## Menjalankan Aplikasi

### Opsi 1: Run dari IDE

1. Buka project di IntelliJ IDEA.
2. Jalankan class `TaskFlowApplication` dengan tombol **Run**.

### Opsi 2: Maven Project Import

```bash
# Import as Maven project di IDE, lalu jalankan TaskFlowApplication.
```

API default berjalan di `http://localhost:8080`.
H2 console tersedia di `http://localhost:8080/h2-console`.

## Endpoint API

### Task Endpoints

- `POST /api/tasks` - buat task baru
- `GET /api/tasks` - list tasks + optional filters `status`, `priority`, `keyword`, `dueDateFrom`, `dueDateTo`
- `GET /api/tasks/{id}` - detail task
- `PUT /api/tasks/{id}` - update task
- `PATCH /api/tasks/{id}/status` - update status saja
- `DELETE /api/tasks/{id}` - hapus task

### Reporting Endpoints

- `GET /api/reports/summary` - summary total, count per status, completion rate
- `GET /api/reports/status-count` - jumlah task per status (Native SQL `GROUP BY`)
- `GET /api/reports/overdue` - task overdue (Native SQL)

## Contoh cURL

### 1) Create Task

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Setup authentication",
    "description": "Implement JWT login flow",
    "priority": "HIGH",
    "dueDate": "2026-03-01"
  }'
```

### 2) Get Tasks with Filter

```bash
curl "http://localhost:8080/api/tasks?status=TODO&priority=HIGH&keyword=auth&dueDateFrom=2026-02-20&dueDateTo=2026-03-10"
```

### 3) Update Task

```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Setup authentication module",
    "description": "Implement JWT login + refresh token",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "dueDate": "2026-03-05"
  }'
```

### 4) Patch Status

```bash
curl -X PATCH http://localhost:8080/api/tasks/1/status \
  -H "Content-Type: application/json" \
  -d '{
    "status": "DONE"
  }'
```

### 5) Report Summary

```bash
curl http://localhost:8080/api/reports/summary
```

### 6) Report Status Count

```bash
curl http://localhost:8080/api/reports/status-count
```

### 7) Report Overdue

```bash
curl http://localhost:8080/api/reports/overdue
```

## Catatan Desain

- **Spring IoC**: constructor injection konsisten pada `Controller -> Service -> Repository`.
- **Java Stream**: perhitungan `completionRate` dan agregasi status di endpoint summary.
- **Native SQL**: reporting `status-count` dan `overdue` menggunakan `@Query(nativeQuery = true)`.
- **Validation**: `title` wajib pada create/update, dengan global exception handler untuk response error konsisten.
- **Logging**: `common-logger` aktif via `@Loggable` pada controller dan service, plus correlation id (`X-Correlation-Id`).
