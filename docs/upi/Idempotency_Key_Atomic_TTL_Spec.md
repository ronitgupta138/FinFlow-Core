# UPI Idempotency Key Atomic TTL Cache Specification

## 1. Concurrency Problem
Payment requests submitted over mobile networks often encounter connection drops, causing client retries that risk duplicate ledger deductions.

## 2. Distributed Atomic Lock & Cache
FinFlow-Core enforces two-phase distributed idempotency using Redis:
```
SET idempotency:key:<uuid> "PROCESSING" NX EX 120
```
- **NX (Not Exists):** Atomic test-and-set ensures only the primary request thread acquires the execution token.
- **EX 120:** 120-second TTL guarantees automatic release if the primary worker crashes.

## 3. Response Caching
Upon successful ledger commitment, status transitions from `PROCESSING` to the final serialized JSON payment receipt with a 24-hour retention window.
