# Chaos Engineering: Database Partition & Fault Injection Scenarios

## 1. Objectives
Verify that FinFlow-Core gracefully handles infrastructure failures without financial ledger corruption or unhandled runtime exceptions.

## 2. Test Scenarios
1. **Primary Database Partition:** Simulate sudden TCP RST on HikariCP connection pool. Assert that pending transactions roll back cleanly without partial ledger commits.
2. **Redis Ingestion Outage:** Disconnect Redis cache. Assert that transaction processing falls back to pessimistic row locking on PostgreSQL.
3. **High Latency Injection:** Inject 1500ms artificial network delay on banking gateway endpoints. Assert Resilience4j timeout and circuit breaker transition to `OPEN`.

## 3. Verification Metrics
- Zero orphan debit/credit records.
- HTTP 503 / 504 status codes returned with RFC 7807 problem details.
