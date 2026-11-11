# Change Data Capture (CDC) Audit Streaming Architecture

## 1. Debezium Ingestion Pipeline
- Attaches to PostgreSQL `pgoutput` logical replication slot.
- Emits atomic insert/update/delete events directly into Apache Kafka without double-write overhead.
