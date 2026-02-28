
# 🚀 Subscription Management System

## 📌 Project Overview

This project is a **production-style Subscription Management System** built using Spring Boot.

It simulates how real-world subscription platforms (like Netflix, Sugarfit, stripe) handle:

* Plan-based billing
* Automatic renewals
* Payment processing (simulated)
* Grace periods
* Dunning & retry mechanism
* Coupons & discounts
* Plan upgrades and downgrades
* Add-on billing
* Transaction safety & idempotency

The system fully automates the subscription lifecycle and prevents common billing errors such as duplicate renewals or missed retries.

---

# 🛠 Tech Stack

* **Java 17**
* **Spring Boot 3.x**
* **Spring Data JPA (Hibernate)**
* **MySQL**
* **Maven**
* **JUnit 5**
* **Mockito**
* **MockMvc**
* **H2 (for repository tests)**

---

# 🏗 Architecture

Layered architecture:

Controller → Service → Repository → Database
Scheduler → Background Renewal & Dunning Jobs

### Layers

* **Controller Layer** – Handles HTTP requests and validation
* **Service Layer** – Business logic and transaction handling
* **Repository Layer** – Database interactions
* **Scheduler Layer** – Auto renewal and retry mechanisms

---

# ⚙️ Setup & Run Instructions

## 1️⃣ Clone Repository

```bash
git clone <your-repo-url>
cd subscription-system
```

---

## 2️⃣ Configure Database

Create a MySQL database:

```sql
CREATE DATABASE subscription_db;
```

Update `application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/subscription_db
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
You can set environment variable for database connection

---

## 3️⃣ Run Application

Using Maven wrapper:

```bash
./mvnw spring-boot:run
```

Or:

```bash
mvn spring-boot:run
```

Application runs at:

```
http://localhost:8080
```

---

# 🧪 Running Tests

Run all tests:

```bash
./mvnw test
```

Expected output:

```
[INFO] BUILD SUCCESS
```

Tests include:

* Unit tests (Mockito)
* Controller tests (MockMvc)
* Repository tests (@DataJpaTest with H2)

---

# 🔌 API Endpoints

## 👤 User APIs

| Method | Endpoint      | Description    |
| ------ | ------------- | -------------- |
| POST   | `/users`      | Create user    |
| GET    | `/users`      | List users     |
| GET    | `/users/{id}` | Get user by ID |

---

## 📦 Plan APIs

| Method | Endpoint      | Description    |
| ------ | ------------- | -------------- |
| POST   | `/plans`      | Create plan    |
| GET    | `/plans`      | List all plans |
| GET    | `/plans/{id}` | Get plan by ID |
| PUT    | `/plans/{id}` | Update plan    |

---

## 🎟 Coupon APIs

| Method | Endpoint          | Description        |
| ------ | ----------------- | ------------------ |
| POST   | `/coupons`        | Create coupon      |
| GET    | `/coupons`        | Create coupon      |
| GET    | `/coupons/{code}` | Get coupon by code |

---

## 📑 Subscription APIs

| Method | Endpoint                          | Description         |
| ------ | --------------------------------- | ------------------- |
| POST   | `/subscriptions`                  | Create subscription |
| GET    | `/subscriptions`                  | Get subscription    |
| GET    | `/subscriptions/{id}`             | Get subscription    |
| PUT    | `/subscriptions/{id}/cancel`      | Cancel subscription |
| PUT    | `/subscriptions/{id}/change-plan` | Upgrade/Downgrade   |

---

## 📑 Add-On APIs

| Method | Endpoint                                        | Description                   |
| ------ | ----------------------------------------------- | ----------------------------- |
| POST   | `/add-ons`                                      | Create New Add-on             |
| GET    | `/add-ons`                                      | Get AddOns                    |
| POST   | `/subscriptions/{id}/add-ons`                   | Attach add-on to subscription |
| PUT    | `/subscriptions/{id}/add-ons/{addOnId}`/usage   | Cancel subscription           |
| PUT    | `/subscriptions/{id}/add-ons`                   | Upgrade/Downgrade             |

---

## 💳 Payment APIs

| Method | Endpoint                      | Description                  |
| ------ | ----------------------------- | ---------------------------- |
| GET    | `/payments/subscription/{id}` | Get payments by subscription |
| POST   | `/payments/{id}/refund`       | Refund payment               |

---

## 🔄 Admin / Scheduler APIs

| Method | Endpoint                 | Description                  |
| ------ | ------------------------ | ---------------------------- |
| POST   | `/admin/trigger-renewal` | Manually trigger renewal job |
| POST   | `/admin/trigger-dunning` | Manually trigger retry job   |

---

# ⚙️ Configuration Reference

## Renewal Configuration

```properties
renewal.scheduler.interval-ms=3600000
renewal.scheduler.enabled=true
```

---

## Dunning Configuration

```properties
dunning.max-retries=3
dunning.grace-period-days=7
dunning.retry-intervals-hours=24,48,72
dunning.scheduler.interval-ms=1800000
```

### Dunning Logic

* If renewal payment fails:

  * Subscription → GRACE
  * Retry based on configured intervals
  * Cancel after max retries

---

# 📬 Postman / Curl Examples

## 1️⃣ Create Plan

```bash
curl -X POST http://localhost:8080/plans \
-H "Content-Type: application/json" \
-d '{
  "name": "Basic",
  "price": 1000,
  "durationDays": 30,
  "description": "Basic monthly plan"
  "price": 2000
}'
```

---

## 2️⃣ Create User

```bash
curl -X POST http://localhost:8080/users \
-H "Content-Type: application/json" \
-d '{
    "name":"Mansi",
    "email":"mansi@gmail.com",
    "password":"123456"
}'
```

---

## 3️⃣ Create Subscription

```bash
curl -X POST http://localhost:8080/subscriptions \
-H "Content-Type: application/json" \
-d '{
  "userId": 4,
  "planId": 2,
  "couponCode": "SAVE150",
  "method": "UPI",
  "type":"SUBSCRIPTION"
}'
```

---

## 4️⃣ Trigger Renewal

```bash
curl -X POST http://localhost:8080/admin/trigger-renewal
```

---

## 5️⃣ Trigger Dunning

```bash
curl -X POST http://localhost:8080/admin/trigger-dunning
```

---

Repository contains:

  * Plan module
  * User module
  * Coupon engine
  * Subscription lifecycle
  * Payment module
  * Renewal scheduler
  * Dunning logic
  * Testing layer
  * Bug fixes

---

# 🎯 Key Business Features

* Idempotent renewal
* Grace period enforcement
* Retry logic with configurable intervals
* Automatic cancellation after max retries
* Refund handling on downgrade
* Add-on billing integration
* Proper transaction management

---

# 📌 Project Status

✅ All tests passing
✅ Clean architecture
✅ Retry mechanism implemented
