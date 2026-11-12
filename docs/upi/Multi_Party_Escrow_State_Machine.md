# Multi-Party Escrow Payment State Machine Specification

## 1. Finite State Machine (FSM) Lifecycle
FinFlow-Core governs multi-party conditional transfers through a formal state machine:
- `INITIATED`: Payment request created by payer.
- `FUNDS_LOCKED`: Payer balance deducted and placed in escrow ledger account.
- `CONDITION_VERIFIED`: Proof of service/delivery attested by validator.
- `SETTLED`: Escrow funds released to payee merchant account.
- `REFUNDED`: Timeout or dispute resolution reverts locked funds to payer.

## 2. Invariant Properties
- **Zero Sum Principle:** Sum of debit entries equals sum of credit entries across all state transitions.
- **Timeout Trigger:** `INITIATED` orders without condition satisfaction within 3600s transition automatically to `REFUNDED`.
