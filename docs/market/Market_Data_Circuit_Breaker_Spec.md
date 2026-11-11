# Resilience4j Market Ingestion Circuit Breaker Specification

## 1. Failure Thresholds
- Sliding window size: 20 calls.
- Failure rate threshold: 50%.
- Slow call duration threshold: 2,500ms.
- Fallback returns cached previous close prices when circuit trips to `OPEN`.
