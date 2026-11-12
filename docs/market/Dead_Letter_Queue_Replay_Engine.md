# Dead Letter Queue (DLQ) Replay & Poison Pill Isolation Specification

## 1. Problem Definition
Corrupted market quote frames or schema mismatches must not block consumer worker threads or halt market ingestion pipelines.

## 2. DLQ Architecture
- **Error Routing:** Messages exceeding 3 retry attempts are redirected to `market.quotes.dlq`.
- **Error Metadata:** Header enriched with exception stack trace, failed timestamp, retry count, and original consumer host.
- **Poison Pill Quarantine:** Malformed packets are isolated into dedicated forensic storage without terminating the consumer process.

## 3. Manual & Scheduled Replay Protocol
Automated backoff replay scheduled during off-peak hours (02:00 UTC) with rate limiting to prevent overwhelming downstream pricing databases.
