# fintech-backend

A lightweight Spring Boot application that simulates a real fintech backend API. It serves as the protected service behind the `fintech-gateway` — providing endpoints for account management and payment processing.

---

## Table of Contents

1. [Purpose](#purpose)
2. [Tech Stack](#tech-stack)
3. [Project Structure](#project-structure)
4. [Endpoints](#endpoints)
5. [Data Models](#data-models)
6. [Business Logic](#business-logic)
7. [Configuration](#configuration)
8. [Running Locally](#running-locally)
9. [Running with Docker](#running-with-docker)
10. [Health Check](#health-check)
11. [Design Decisions](#design-decisions)
12. [Future Improvements](#future-improvements)

---

## Purpose

This service simulates what a real fintech backend looks like — account balance queries, user profile retrieval, and payment processing. It intentionally has no security logic of its own.

Security is handled entirely by `fintech-gateway` which sits in front of this service. The backend trusts that every request reaching it has already been validated, risk-scored, and deemed safe.

This separation demonstrates a core architectural principle: **cross-cutting concerns like security belong at the infrastructure layer, not inside business logic.**

In a real system, this backend would be:
- A payment processor
- A core banking system
- A lending engine
- Any microservice that processes financial transactions

---

## Tech Stack

| Technology | Version | Purpose |
|---|---|---|
| Java | 21 | Language |
| Spring Boot | 4.0.5 | Framework |
| Spring Web MVC | 7.0.6 | REST API layer |
| Spring Boot Actuator | 4.0.5 | Health endpoint |
| SpringDoc OpenAPI | 3.0.2 | Swagger UI |
| Maven | 3.x | Build tool |

No database dependency — all data is simulated with hardcoded values to keep the service focused on demonstrating the API surface without infrastructure complexity.

---

## Project Structure

```
fintech-backend/
├── src/
│   ├── main/
│   │   ├── java/com/fintech/fintech_backend/
│   │   │   ├── FintechBackendApplication.java    ← Entry point
│   │   │   │
│   │   │   ├── controller/
│   │   │   │   ├── AccountController.java        ← GET /api/account/balance
│   │   │   │   │                                    GET /api/account/profile
│   │   │   │   └── PaymentController.java        ← POST /api/payments/transfer
│   │   │   │
│   │   │   ├── model/
│   │   │   │   ├── AccountBalance.java           ← Response model
│   │   │   │   ├── UserProfile.java              ← Response model
│   │   │   │   └── PaymentRequest.java           ← Request model
│   │   │   │
│   │   │   └── service/
│   │   │       ├── AccountService.java           ← Simulated account logic
│   │   │       └── PaymentService.java           ← Simulated payment logic
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       └── java/com/fintech/fintech_backend/
│           └── FintechBackendApplicationTests.java
│
└── pom.xml
```

---

## Endpoints

### GET /api/account/balance

Returns the account balance for a given account ID.

**Request**
```
GET /api/account/balance?accountId=ACC123
```

**Response 200**
```json
{
  "accountId": "ACC123",
  "balance": 50000.0,
  "currency": "INR"
}
```

**Notes**
- `accountId` is a required query parameter
- Balance is hardcoded at 50,000 INR regardless of account ID — simulated data
- In production this would query a core banking system

---

### GET /api/account/profile

Returns the user profile for a given user ID.

**Request**
```
GET /api/account/profile?userId=USER001
```

**Response 200**
```json
{
  "userId": "USER001",
  "name": "Anup Kumar",
  "email": "anup@fintech.com",
  "trustTier": "MEDIUM"
}
```

**Notes**
- `userId` is a required query parameter
- All fields are hardcoded — simulated data
- `trustTier` field is included to preview the trust tier concept used by the gateway

---

### POST /api/payments/transfer

Processes a fund transfer between two accounts.

**Request**
```
POST /api/payments/transfer
Content-Type: application/json

{
  "fromAccountId": "ACC123",
  "toAccountId": "ACC456",
  "amount": 5000,
  "currency": "INR",
  "idempotencyKey": "PAY-UUID-001"
}
```

**Response 200**
```json
{
  "status": "SUCCESS",
  "transactionId": "TXN-1776059802163",
  "fromAccount": "ACC123",
  "toAccount": "ACC456",
  "amount": 5000.0,
  "currency": "INR"
}
```

**Notes**
- `transactionId` is generated using `System.currentTimeMillis()` — unique per call
- No actual balance deduction happens — this is a simulation
- The balance endpoint always returns 50,000 even after transfers — intentional for demo purposes
- `idempotencyKey` in the body is passed through for logging purposes; actual idempotency is enforced by the gateway before this endpoint is called

---

### GET /actuator/health

Spring Boot Actuator health endpoint. Used by the gateway's `/health` check to verify backend availability.

**Request**
```
GET /actuator/health
```

**Response 200**
```json
{
  "status": "UP"
}
```

**Notes**
- Minimal response — `show-details=never` is configured intentionally
- Exposing internal details (database status, disk space, etc.) in a health endpoint is a security risk
- This endpoint is only called by the gateway internally — never by external clients

---

## Data Models

### AccountBalance

```java
public class AccountBalance {
    private String accountId;
    private double balance;
    private String currency;
}
```

Returned by `GET /api/account/balance`. Simple value object with no setters — immutable after construction.

### UserProfile

```java
public class UserProfile {
    private String userId;
    private String name;
    private String email;
    private String trustTier;  // LOW / MEDIUM / HIGH
}
```

Returned by `GET /api/account/profile`. The `trustTier` field previews the concept used by the gateway's trust tier system.

### PaymentRequest

```java
public class PaymentRequest {
    private String fromAccountId;
    private String toAccountId;
    private double amount;
    private String currency;
    private String idempotencyKey;
}
```

Received by `POST /api/payments/transfer`. Requires both getters and setters — Spring uses setters to deserialize the incoming JSON body via Jackson.

---

## Business Logic

### AccountService

```java
public AccountBalance getBalance(String accountId) {
    return new AccountBalance(accountId, 50000.00, "INR");
}

public UserProfile getProfile(String userId) {
    return new UserProfile(userId, "Anup Kumar", "anup@fintech.com", "MEDIUM");
}
```

Simulates a database lookup. In production this would call a JPA repository or external core banking API.

### PaymentService

```java
public Map<String, Object> processPayment(PaymentRequest request) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", "SUCCESS");
    response.put("transactionId", "TXN-" + System.currentTimeMillis());
    response.put("fromAccount", request.getFromAccountId());
    response.put("toAccount", request.getToAccountId());
    response.put("amount", request.getAmount());
    response.put("currency", request.getCurrency());
    return response;
}
```

Simulates payment processing. The transaction ID is always unique because `System.currentTimeMillis()` returns the current epoch millisecond — demonstrating that without idempotency at the gateway, each retry would create a new transaction.

---

## Configuration

`src/main/resources/application.properties`

```properties
server.port=8080

# Actuator — expose health endpoint only
management.endpoints.web.exposure.include=health
management.endpoint.health.show-details=never

# Swagger
springdoc.swagger-ui.enabled=true
springdoc.api-docs.enabled=true
springdoc.swagger-ui.path=/swagger-ui.html
```

### Why Only Health Is Exposed in Actuator

Spring Boot Actuator can expose many endpoints — `env`, `beans`, `metrics`, `heapdump`, etc. These reveal internal system details and are dangerous to expose publicly.

Only `health` is exposed because the gateway needs it to verify backend availability. All other actuator endpoints are disabled by default configuration.

---

## Running Locally

### Prerequisites

- Java 21
- Maven 3.x

### Start the Service

```bash
cd fintech-backend
./mvnw spring-boot:run
```

The service starts on port 8080.

### Verify

```bash
# Health check
curl http://localhost:8080/actuator/health

# Balance
curl http://localhost:8080/api/account/balance?accountId=ACC123

# Profile
curl http://localhost:8080/api/account/profile?userId=USER001

# Payment
curl -X POST http://localhost:8080/api/payments/transfer \
  -H "Content-Type: application/json" \
  -d '{"fromAccountId":"ACC123","toAccountId":"ACC456","amount":5000,"currency":"INR","idempotencyKey":"PAY-001"}'
```

### Swagger UI

```
http://localhost:8080/swagger-ui/index.html
```

---

## Running with Docker

### Build Image

```bash
cd fintech-backend
docker build -f ../fintech-backend.Dockerfile -t fintech-backend .
```

### Run Container

```bash
docker run -d \
  --name fintech_backend \
  -p 8080:8080 \
  fintech-backend
```

### Via Docker Compose (recommended)

```bash
# From project root
docker-compose up fintech-backend
```

---

## Health Check

The backend exposes `/actuator/health` for the gateway's health monitoring system.

The gateway calls this endpoint as part of its own `/health` check:

```json
{
  "redis": "UP",
  "mysql": "UP",
  "backend": "UP",    ← determined by calling /actuator/health
  "gateway": "UP",
  "overall": "HEALTHY"
}
```

If the backend is down, the gateway health returns:
```json
{
  "backend": "DOWN",
  "overall": "DEGRADED"
}
```

And requests forwarded to the backend return:
```json
{
  "error": "Service temporarily unavailable",
  "message": "Please try again later"
}
```

---

## Design Decisions

### Why No Database?

The backend is intentionally stateless and in-memory. The goal is to demonstrate the gateway's security capabilities, not the backend's data persistence. A real database would add infrastructure complexity without adding value to what's being demonstrated.

This keeps the backend:
- Fast to start
- Easy to reset (restart = clean state)
- Focused on API shape, not data management

### Why Does Balance Never Change After Transfer?

Because there is no real state. Every call to `getBalance()` returns a new hardcoded object. This is intentional — demonstrating idempotency at the gateway level requires that the same payment sent twice produces the same transaction ID (from the cache), not that the balance actually changed.

### Why Is transactionId Time-Based?

`"TXN-" + System.currentTimeMillis()` produces a different ID on every call. This is deliberate — it proves that without idempotency, two identical payment requests would create two different transactions. The gateway's idempotency cache ensures the second call never reaches this code at all.

### Why Constructor Injection?

All service dependencies are injected via constructor:

```java
public AccountController(AccountService accountService) {
    this.accountService = accountService;
}
```

Constructor injection is preferred over field injection (`@Autowired` on a field) because:
- Dependencies are explicit and required at construction time
- Easier to test (can pass mocks directly)
- Immutable after construction (field can be `final`)
- Spring recommends it as best practice

---

## Future Improvements

### Real Database Integration

Add MySQL with Spring Data JPA:
- `accounts` table with real balances
- `transactions` table recording every payment
- Actual balance deduction on successful transfer

### Real Idempotency at Backend Level

Even with gateway-level idempotency, the backend should implement its own check using the `idempotencyKey` field in `PaymentRequest`. Defense in depth — two layers of duplicate prevention.

### Authentication

Add JWT validation — the gateway would forward the token in the `Authorization` header and the backend would validate it. Currently there is no authentication — the backend trusts all requests that reach it.

### Error Scenarios

Simulate real failure conditions:
- Insufficient balance → 422 Unprocessable Entity
- Account not found → 404 Not Found
- Service timeout → 503 Service Unavailable

These would allow the gateway's graceful degradation handling to be demonstrated more realistically.

### Real Transaction ID Generation

Replace `System.currentTimeMillis()` with UUID or a distributed ID generator (Snowflake algorithm) for production-grade unique transaction IDs that are:
- Globally unique
- Time-ordered
- Not guessable
