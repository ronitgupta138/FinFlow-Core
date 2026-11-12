# JCStress Concurrency & Memory Model Verification Specification

## 1. Overview
Java Concurrency Stress (JCStress) harnesses verify thread-safety invariants on lock-free data structures and account balance accumulation logic under JVM memory model conditions.

## 2. Invariant Under Verification
Verifies that atomic Compare-And-Swap (CAS) operations on account balances maintain serializability under 64-thread concurrent contention:

```java
@JCStressTest
@Outcome(id = "100, 100", expect = Expect.ACCEPTABLE, desc = "Both balance updates committed atomically")
@State
public class AccountBalanceStressTest { ... }
```

## 3. Target JVM Configurations
Tests executed against OpenJDK 21 64-Bit Server VM with `-XX:+UnlockDiagnosticVMOptions` to detect hardware reordering vulnerabilities on x86_64 and AArch64.
