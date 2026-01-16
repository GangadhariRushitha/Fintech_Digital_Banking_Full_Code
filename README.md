# VaultCore Financial

## Overview

**VaultCore Financial** is a production-grade simulation of a **Neo-Bank core infrastructure**, designed to demonstrate secure, scalable, and compliant financial system design. The project focuses on **account management, money transfers, and simulated stock trading**, with **security and data integrity as the highest priorities**.

A single vulnerability such as **SQL Injection or Cross-Site Scripting (XSS)** in a real-world banking system can lead to severe legal and regulatory consequences. VaultCore Financial is built to reflect these real-world constraints using modern backend, frontend, and security best practices.

---

## Key Objectives

* Enforce **financial correctness** using a strict double-entry ledger system
* Guarantee **ACID compliance** and prevent race conditions under high concurrency
* Simulate **thousands of concurrent users** using Java 21 Virtual Threads
* Detect and mitigate **fraudulent transactions** in real time
* Maintain **auditability and regulatory compliance**

---

## Core Features

### 🔐 Security-First Architecture

* Spring Security with **JWT authentication and refresh tokens**
* Strict validation against **SQL Injection and XSS attacks**
* Role-based access control (RBAC)
* OWASP-compliant secure coding practices

### 📒 Double-Entry Ledger System

* Immutable ledger entries (no updates or deletes)
* Every transaction creates **matching debit and credit records**
* Database-level **check constraints** to prevent balance corruption
* Fully **ACID-compliant** transactions

### ⚙️ Transaction Engine

* Money transfers implemented using `@Transactional(isolation = SERIALIZABLE)`
* Serialized execution to prevent dirty reads, lost updates, and race conditions
* Guarantees **non-negative balances**, even under heavy load

### 🧵 High-Concurrency with Virtual Threads

* Java 21 **Virtual Threads** used for the `Get Balance` API
* Simulates thousands of simultaneous requests without performance degradation
* Designed for high-throughput, low-latency financial workloads

### 🚨 Fraud Detection Middleware

* Interceptor / AOP-based middleware for transaction monitoring
* Automatically flags transactions exceeding a configurable threshold
* Triggers a **mock 2FA challenge** via SMS/Email for suspicious activity

### 📈 Trading & Portfolio Management

* Integration with a **mock Stock Market API**
* Simulated stock buy/sell operations
* React-based **Portfolio Dashboard** with real-time charts using Recharts
* Optimized to keep stock price update latency under **300ms**

### 🧾 Audit & Compliance

* AspectJ-based **audit logging** for every method call
* Logs input parameters, execution details, and return values
* Generates **monthly account statements in PDF format**
* Supports regulatory-style audits and traceability

---

## Technology Stack

### Backend

* Java 21
* Spring Boot
* Spring Security (JWT, Refresh Tokens)
* Spring Data JPA / Hibernate
* AspectJ (Audit Logging)
* RESTful APIs

### Frontend

* React
* Recharts (Data Visualization)
* Secure multi-step forms for transactions

### Database

* Relational Database (PostgreSQL / MySQL recommended)
* Immutable ledger tables
* Check constraints and transactional isolation

### Testing & Security

* Concurrency testing (100+ parallel threads)
* OWASP ZAP penetration testing
* Manual and automated security validation

---

## Week-Wise Implementation Plan

### Week 1 – Security & Ledger Design

* Design immutable ledger database schema
* Implement database constraints to prevent tampering
* Configure Spring Security with JWT & Refresh Tokens
* Build React Login Page

**Validation:**

* Manual attempts to modify ledger rows must fail
* Balance integrity must remain intact

---

### Week 2 – Transaction Engine

* Implement `TransferService` with SERIALIZABLE isolation
* Develop multi-step "Send Money" wizard in React

**Validation:**

* Concurrency test with 100 simultaneous withdrawals
* Final balance must be correct and never negative

---

### Week 3 – Trading & External APIs

* Integrate mock Stock Market REST API
* Build Portfolio Dashboard with Recharts

**Validation:**

* Stock price update latency under 300ms

---

### Week 4 – Audit & Compliance

* Implement AspectJ-based audit logging
* Generate monthly PDF statements
* Perform OWASP ZAP security scan

**Validation:**

* No critical or high-risk vulnerabilities detected

---

## Disclaimer

This project is a **simulation** built for educational and architectural demonstration purposes only. It does **not handle real money or real stock trades**.

---

## Author / Organization

**Zaalima Development Pvt. Ltd**
Confidential – Internal Use Only
