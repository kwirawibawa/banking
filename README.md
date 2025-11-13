# Banking Transaction Service

A simple banking transaction service built with **Java Spring Boot** supporting **deposit, withdraw, and transfer** operations with transaction history tracking. Transfer transactions also include a direction indicator (`INCOMING` or `OUTGOING`). The system also supports **account management** with create, update, retrieve, and deactivate operations.

---

## 📚 Table of Contents

- [Features](#features)  
- [Layers Architecture](#layers-architecture)
- [Technical Stack](#technical-stack)
- [Database Schema](#database-schema)  
- [Getting Started](#getting-started)  
- [API Endpoints](#api-endpoints)  
- [Request / Response Examples](#request--response-examples)  
- [Notes](#notes)  

---

## ✨ Features

- **Account Management:** Create, update, retrieve, and deactivate accounts
- Create **Deposit** and **Withdraw** transactions
- Perform **Transfers** between accounts with **direction tracking**
- Retrieve **transaction history** by account
- **Search transactions** with filters: accountId, type, date range, and paging  
- Supports **validation** and **transactional integrity** (atomic operations)
- Uses **optimistic locking** for accounts
- Simple **REST API** responses with standard format

---

## 🧩 Layers Architecture

- **Controller Layer:** Handles HTTP requests and returns standardized responses.
- **Service Layer:** Contains business logic, validation, and **transactional control**.
- **Repository Layer:** Handles persistence using JPA repositories and **native SQL queries**.
- **Database:** Tables auto-created using src/main/resources/schema.sql, storing accounts and transactions with referential integrity.

---

## 🧰 Technical Stack

- Java 21  
- Spring Boot 3  
- Spring Data JPA / Hibernate  
- H2 (in-memory)  
- Lombok  
- Jakarta Validation  
- Java Stream
- Spring IoC / Dependency Injection

---

## Database Schema

### `account` Table

| Column          | Type          | Notes                       |
|-----------------|---------------|-----------------------------|
| id              | UUID          | Primary Key                 |
| name            | VARCHAR(100)  | Unique, not null            |
| email           | VARCHAR(150)  | Unique, not null            |
| account_number  | VARCHAR(20)   | Unique, not null            |
| balance         | DECIMAL(19,2) | Default 0                   |
| is_active       | BOOLEAN       | Default true                |
| version         | BIGINT        | Optimistic lock             |

### `transaction` Table

| Column      | Type          | Notes                                                  |
|------------ |---------------|--------------------------------------------------------|
| id          | UUID          | Primary Key                                            |
| account_id  | UUID          | FK → account(id)                                       |
| type        | VARCHAR(10)   | `DEPOSIT`, `WITHDRAW`, `TRANSFER`                      |
| direction   | VARCHAR(10)   | `INCOMING`, `OUTGOING` (nullable for deposit/withdraw) |
| amount      | DECIMAL(19,2) | Not null                                               |
| created_at  | TIMESTAMP     | Default current timestamp                              |

Note: All tables (account, transaction) are automatically created at application startup using src/main/resources/schema.sql.

---

## 🚀 Getting Started

1. Clone the repository:

```bash
git https://github.com/kwirawibawa/banking.git
cd banking
```

2. Build the project:

```bash
mvn clean install
```

3. Run the application:

```bash
mvn spring-boot:run
```

4. The service runs by default on:

```
http://localhost:8080
```

5. Database Access Point

- H2 Console → `http://localhost:8080/h2-console`  
  ```
  JDBC URL: jdbc:h2:mem:bankdb
  Username: sa
  Password:
  ```

---

## API Endpoints

### Account Management

- **Create Account**
```
POST /api/accounts
```
- **Get Account by ID**
```
GET /api/accounts/{id}
```
- **Update Account**
```
PUT /api/accounts/{id}
```
- **Deactivate Account**
```
PATCH /api/accounts/{id}/deactivate
```

### Transactions

- **Create Transaction (Deposit / Withdraw)**
```
POST /api/transactions
```
- **Transfer**
```
POST /api/transactions/transfer
```
- **Get Transactions by Account**
```
GET /api/transactions/account/{accountId}
```
- **Search Transactions**
```
POST /api/transactions/search
```
```
Filters: accountId (required), type (optional), startDate / endDate (optional), pageNumber, pageSize
```

---

## Request / Response Examples

### Create Account

```
POST /api/accounts
```

**Request Body:**

```json
{
  "name": "Wira",
  "email": "wira@gmail.com"
}
```

**Response:**

```json
{
  "code": 200,
  "message": "Account successfully created",
  "data": {
    "id": "UUID",
    "name": "Wira",
    "email": "wira@gmail.com",
    "accountNumber": "8236189429",
    "balance": 0,
    "active": true
  }
}
```

---

### Create Transaction (Deposit / Withdraw)

```
POST /api/transactions
```

**Request Body:**

```json
{
  "accountId": "UUID",
  "type": "DEPOSIT",
  "amount": 500.00
}
```

**Response:**

```json
{
  "code": 200,
  "message": "Transaction successfully processed",
  "data": {
    "id": "UUID",
    "accountId": "UUID",
    "type": "DEPOSIT",
    "amount": 500.00,
    "createdAt": "2025-11-13T00:00:00"
  }
}
```

---

### Transfer

```
POST /api/transactions/transfer
```

**Request Body:**

```json
{
  "fromAccountId": "UUID",
  "toAccountId": "UUID",
  "amount": 100.00
}
```

**Response:**

```json
{
  "code": 200,
  "message": "Transfer successfully processed",
  "data": {
    "fromAccountId": "UUID",
    "fromAccountNumber": "1019564279",
    "fromAccountName": "Kresna",
    "toAccountId": "UUID",
    "toAccountNumber": "4991904171",
    "toAccountName": "Wira",
    "amount": 100.00,
    "createdAt": "2025-11-13T00:26:48"
  }
}
```

---

### Get Transactions by Account

```
GET /api/transactions/account/{accountId}
```

**Response:**

```json
{
  "code": 200,
  "message": "Successfully retrieved transactions",
  "data": [
    {
      "id": "UUID",
      "accountId": "UUID",
      "type": "TRANSFER",
      "amount": 100.00,
      "createdAt": "2025-11-13T00:26:48",
      "direction": "INCOMING"
    },
    {
      "id": "UUID",
      "accountId": "UUID",
      "type": "DEPOSIT",
      "amount": 200.00,
      "createdAt": "2025-11-12T23:59:59",
      "direction": null
    }
  ]
}
```
### Search Transactions (with paging & filters)

```
POST /api/transactions/search
```

**Request Body:**

```json
{
  "accountId": "UUID",
  "type": "WITHDRAW",
  "startDate": "2025-11-01",
  "endDate": "2025-11-13",
  "pageNumber": 1,
  "pageSize": 1
}
```

**Response:**

```json
{
    "pageNo": 1,
    "pageSize": 1,
    "totalDataInPage": 1,
    "totalData": 2,
    "totalPages": 2,
    "startData": 1,
    "endData": 1,
    "listData": [
        {
            "id": "82da9daf-ccc7-3f38-bc16-557ca1f547bd",
            "accountId": "f1759912-ba32-30f2-a6d5-d7bcfd453481",
            "type": "WITHDRAW",
            "amount": 100.00,
            "createdAt": "2025-11-13T10:14:34.402648",
            "direction": null
        }
    ],
    "code": 200,
    "message": "Transactions search completed successfully"
}
```
---

## Notes

- All transfer transactions are split into two rows: **OUTGOING** (sender) and **INCOMING** (receiver).  
- Deposit / Withdraw transactions have `direction = null`.  
- Transaction history API **correctly reflects the transfer direction**.  
- Operations are **atomic and transactional**, ensuring consistency across related updates.  
- **Optimistic locking** is implemented in accounts via the `version` column.  
- API responses are wrapped in ApiResponse<T> or SearchResponse<T> with code, message, data, and paging info. 
- Some repository methods use **native SQL queries** while others use JPA `findById`.  
- Java Stream is used for mapping and transforming transaction lists.  
- Fully uses **Spring IoC** / dependency injection.

---

**👨‍💻 Author:** Kresna Wirawibawa

