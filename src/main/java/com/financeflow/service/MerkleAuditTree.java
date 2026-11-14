package com.financeflow.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;

/**
 * SHA-256 Merkle Tree computation engine for verifying immutable transaction block ledgers.
 */
public class MerkleAuditTree {

    public static String calculateMerkleRoot(List<String> transactionHashes) {
        if (transactionHashes == null || transactionHashes.isEmpty()) {
            return "";
        }
        List<String> currentLevel = new ArrayList<>(transactionHashes);

        while (currentLevel.size() > 1) {
            List<String> nextLevel = new ArrayList<>();
            for (int i = 0; i < currentLevel.size(); i += 2) {
                String left = currentLevel.get(i);
                String right = (i + 1 < currentLevel.size()) ? currentLevel.get(i + 1) : left;
                nextLevel.add(sha256(left + right));
            }
            currentLevel = nextLevel;
        }

        return currentLevel.get(0);
    }

    public static String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not available", e);
        }
    }
}
