# Field-Level AES-256-GCM Encryption Specification

## 1. Overview
Customer Personally Identifiable Information (PII) including bank account numbers, IFSC codes, and PAN details are encrypted at rest using AES-256 in Galois/Counter Mode (GCM).

## 2. Cryptographic Parameters
- **Cipher:** `AES/GCM/NoPadding`.
- **Key Length:** 256 bits (derived via HKDF with SHA-256).
- **Initialization Vector (IV):** 96-bit cryptographically secure random nonce generated per field.
- **Authentication Tag:** 128-bit authentication tag ensuring confidentiality and ciphertext integrity.

## 3. JPA Attribute Converter
Implements `AttributeConverter<String, String>` to perform transparent encryption before SQL write and decryption upon entity hydration.
