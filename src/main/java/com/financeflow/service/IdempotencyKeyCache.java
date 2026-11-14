package com.financeflow.service;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Sliding-window in-memory idempotency key cache with TTL timestamp tracking.
 */
public class IdempotencyKeyCache {
    private final ConcurrentHashMap<String, Long> cache = new ConcurrentHashMap<>();
    private final long ttlMillis;

    public IdempotencyKeyCache(long ttlMillis) {
        this.ttlMillis = ttlMillis;
    }

    public boolean tryAcquire(String key, long nowMillis) {
        cleanupExpired(nowMillis);
        Long existing = cache.putIfAbsent(key, nowMillis);
        if (existing == null) {
            return true;
        }
        if (nowMillis - existing >= ttlMillis) {
            cache.put(key, nowMillis);
            return true;
        }
        return false;
    }

    public void cleanupExpired(long nowMillis) {
        cache.entrySet().removeIf(entry -> nowMillis - entry.getValue() >= ttlMillis);
    }

    public int size() {
        return cache.size();
    }
}
