# PostgreSQL 16 Table Partitioning and Index Tuning Guide

## 1. Table Partitioning Scheme
- Large-volume `transactions` table is declaratively partitioned by RANGE on `timestamp` (monthly partitions):
  `PARTITION BY RANGE (timestamp)`
- Outdated partitions (> 3 years) can be detached and archived to cold Parquet/S3 storage without table locks.

## 2. Partial Indexing
To accelerate unsettled pending transactions:
`CREATE INDEX idx_tx_pending ON transactions (user_id, status) WHERE status = 'PENDING';`
Index contains only active unsettled rows, maintaining tiny cache footprint.
