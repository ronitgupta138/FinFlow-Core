package com.financeflow.audit;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

/**
 * Computes sequential cryptographic SHA-256 chain hashes for immutable audit journals.
 */
public class AuditLogEntryHasher {
    public static String computeChainHash(String previousHash, String logPayload) {
        String prev = (previousHash != null) ? previousHash : "0000000000000000000000000000000000000000000000000000000000000000";
        String content = prev + ":" + (logPayload != null ? logPayload : "");
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm missing", e);
        }
    }
}
