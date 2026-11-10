# Spring Security 6 Stateless JWT Authentication Architecture

## 1. Security Filter Chain
- Standard HTTP session creation is completely disabled (`SessionCreationPolicy.STATELESS`).
- `JwtAuthenticationFilter` intercepts requests, extracts Bearer token, verifies HMAC-SHA256 signature, and populates `SecurityContextHolder`.

## 2. Token Lifecycle & Revocation
- Short-lived Access Tokens (15 minutes).
- Refresh Tokens stored in Redis with TTL for silent renewal.
- Immediate revocation supported via Redis bloom filter / token blacklist lookup on logout.
