package com.financeflow.service;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MerkleAuditTreeTest {
    @Test
    void testSingleLeafRoot() {
        String hash = MerkleAuditTree.sha256("tx_1");
        String root = MerkleAuditTree.calculateMerkleRoot(List.of(hash));
        assertEquals(hash, root);
    }

    @Test
    void testDeterministicMerkleRoot() {
        List<String> leaves = List.of(
            MerkleAuditTree.sha256("tx_1"),
            MerkleAuditTree.sha256("tx_2"),
            MerkleAuditTree.sha256("tx_3")
        );
        String root1 = MerkleAuditTree.calculateMerkleRoot(leaves);
        String root2 = MerkleAuditTree.calculateMerkleRoot(leaves);
        assertNotNull(root1);
        assertEquals(root1, root2);
    }

    @Test
    void testTamperDetectionChangesRoot() {
        List<String> leavesOriginal = List.of(
            MerkleAuditTree.sha256("tx_1"),
            MerkleAuditTree.sha256("tx_2")
        );
        List<String> leavesTampered = List.of(
            MerkleAuditTree.sha256("tx_1"),
            MerkleAuditTree.sha256("tx_2_modified")
        );
        String rootOriginal = MerkleAuditTree.calculateMerkleRoot(leavesOriginal);
        String rootTampered = MerkleAuditTree.calculateMerkleRoot(leavesTampered);
        assertNotEquals(rootOriginal, rootTampered);
    }
}
