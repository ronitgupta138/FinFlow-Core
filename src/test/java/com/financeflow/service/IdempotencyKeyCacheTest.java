package com.financeflow.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IdempotencyKeyCacheTest {
    @Test
    void testAcquireAndDuplicateRejection() {
        IdempotencyKeyCache cache = new IdempotencyKeyCache(1000L);
        assertTrue(cache.tryAcquire("key_1", 10000L));
        assertFalse(cache.tryAcquire("key_1", 10200L));
    }

    @Test
    void testExpiryAllowsReacquisition() {
        IdempotencyKeyCache cache = new IdempotencyKeyCache(1000L);
        assertTrue(cache.tryAcquire("key_2", 10000L));
        // After 1000ms TTL has passed
        assertTrue(cache.tryAcquire("key_2", 11050L));
    }
}
