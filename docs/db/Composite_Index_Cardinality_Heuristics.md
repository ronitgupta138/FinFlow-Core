# Composite Index Column Cardinality & Query Optimization Heuristics

## 1. Ordering Rules for Multi-Column Indexes
In FinFlow-Core PostgreSQL schema, composite index columns are ordered strictly by cardinality and query predicate selectivity:

$$\text{Rule:} \quad \text{Index}(\text{EqualityFilter}, \text{RangeFilter}, \text{OrderBy})$$

## 2. Ledger Query Optimization
For query:
```sql
SELECT * FROM ledger_entries 
WHERE account_id = :accId AND transaction_time >= :startTime 
ORDER BY transaction_time DESC;
```
- **Optimal Index:** `CREATE INDEX idx_ledger_acc_time ON ledger_entries (account_id, transaction_time DESC);`
- **Result:** Converts expensive sequential scans and sort steps into efficient single-pass index range scans.
