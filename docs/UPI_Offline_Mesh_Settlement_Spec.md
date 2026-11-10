# UPI Offline Mesh Settlement Protocol Specification

## 1. Architectural Scope
Facilitates peer-to-peer and merchant retail payments in connectivity-constrained rural environments without immediate internet connectivity.

## 2. Cryptographic Vouchers & Verification
- **Transaction Token:** Ephemeral Ed25519 digital signature generated on-device with sender public key, merchant identifier, transaction nonce, and amount.
- **Offline Ledger:** Merkle tree of uncommitted offline transactions buffered on merchant device.
- **Reconciliation Invariants:** Upon network re-establishment, merchant client flushes cryptographically signed settlement envelopes. Idempotent deduplication guarantees single-settlement execution.
