🚀 Subscription Management System (Spring Boot)

A production-style SaaS Subscription Management System built with Spring Boot, supporting:

Subscription lifecycle

Coupon engine

Auto-renewal

Dunning & retry mechanism

Add-ons

Payment processing simulation

Scheduler-based billing automation

Designed with clean architecture and fully testable service layers.

🏗 Tech Stack

Java 17+

Spring Boot

Spring Data JPA

Hibernate

MySQL

H2 (Testing)

Mockito + JUnit 5

MockMvc

Lombok

📦 Core Features
👤 User Management

Create user

List users

Get by ID

📦 Plan Management

Create plan

Update plan

Delete plan

List available plans

Each plan includes:

Name

Price

Duration (days)

🎟 Coupon Engine

Supports:

Percentage discounts

Fixed amount discounts

Expiry validation

Global usage limit

Per-user usage limit

Active / inactive state

Validations:

Expired coupon

Inactive coupon

Limit exceeded

Duplicate usage

📑 Subscription Lifecycle

Create subscription (with/without coupon)

Cancel subscription

Change plan (upgrade/downgrade)

Proration logic

Status handling:

ACTIVE

GRACE

CANCELLED

💳 Payment Module

Simulated payment gateway.

Supports:

INITIATION

RENEWAL

UPGRADE

REFUND

Payment statuses:

SUCCESS

FAILED

REFUNDED

Includes idempotency checks.

🔄 Auto Renewal

Scheduled job:

renewal.scheduler.interval-ms

Renewal logic:

Runs when subscription expires

Skips already renewed subscriptions

Extends end date on success

Moves to GRACE on failure

⚠ Dunning System

Handles failed renewals.

Configuration:

dunning.max-retries=3
dunning.grace-period-days=7
dunning.retry-intervals-hours=24,48,72

Flow:

Renewal fails

Status → GRACE

Retry at configured intervals

Cancel after max retries

Log every attempt

➕ Add-On Module

Attach add-ons to subscription

Track usage

Include billing during renewal

📊 Architecture

Layered Architecture:

Controller → Service → Repository → Database

Separation of:

Business logic (Service layer)

HTTP layer (Controller)

Persistence (Repository)

Background jobs (Scheduler)

🧪 Testing Strategy
✅ Unit Tests (Mockito)

SubscriptionService

CouponService

RenewalService

DunningService

PaymentService

🌐 Controller Tests (MockMvc)

Request validation

Status codes

Error mapping

🗄 Repository Tests (@DataJpaTest)

Custom query validation

Renewal and dunning queries

Over 40+ business rule test cases.

⚙ Configuration

Example application.properties:

spring.datasource.url=jdbc:mysql://localhost:3306/subscription
spring.datasource.username=root
spring.datasource.password=password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

renewal.scheduler.interval-ms=3600000

dunning.max-retries=3
dunning.grace-period-days=7
dunning.retry-intervals-hours=24,48,72
dunning.scheduler.interval-ms=1800000
▶ Running The Application

Clone repository

Configure MySQL

Run:

mvn spring-boot:run

Or from IDE.

🔬 Testing

Run all tests:

mvn test

H2 database is used for repository tests.

📌 Important Business Scenarios Covered

Prevent duplicate active subscription

Prevent double renewal (idempotency)

Coupon validation matrix

Grace period enforcement

Retry logic with configurable intervals

Auto cancellation after failed retries

Refund handling during downgrade

🧠 Key Concepts Implemented

Transaction management (REQUIRES_NEW)

Lazy loading handling

Scheduler-based automation

Dunning retry strategies

Validation using @Valid

Global exception handling

Clean separation of concerns

🎯 Future Improvements

Stripe/Razorpay integration

Webhook support

Invoice generation (PDF)

Multi-currency support

Event-driven billing (Kafka)

Admin dashboard UI

👨‍💻 Author

Developed as a complete backend SaaS billing system for learning and production-style architecture practice.
