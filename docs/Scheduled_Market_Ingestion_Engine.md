# Scheduled Market Ingestion Engine & ShedLock Architecture

## 1. Overview
The Market Ingestion Engine periodically pulls real-time equity quotes, FX currency rates, and index movements into PostgreSQL for portfolio valuation.

## 2. Distributed Locking with ShedLock
To prevent duplicate job execution when deployed across multi-instance container replicas:
```java
@Scheduled(cron = "0 */5 9-16 * * MON-FRI")
@SchedulerLock(name = "market_data_ingest", lockAtMostFor = "4m", lockAtLeastFor = "30s")
public void ingestMarketQuotes() {
    // High-throughput HTTP API ingestion with backoff
}
```
State is coordinated atomically via the `shedlock` database table, guaranteeing strictly one active executor per cycle.
