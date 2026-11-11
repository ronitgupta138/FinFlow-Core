# Role-Based Access Control (RBAC) Permission Hierarchy

## 1. Role Matrix
- `ROLE_USER`: Read/write personal bank accounts, execute personal transactions.
- `ROLE_AUDITOR`: Read-only access to anonymized ledger records and compliance logs.
- `ROLE_ADMIN`: User management, tenant onboarding, emergency fund locks.
- Method-level security enforced via `@PreAuthorize("hasRole('ADMIN')")`.
