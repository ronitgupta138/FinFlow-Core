package com.financeflow.mesh;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MeshChecksumVerifierTest {
    @Test
    void testChecksumValid() {
        String packet = "MESH_PAYLOAD:sender=alice;receiver=bob;amount=500.00;token=xyz";
        long crc = MeshChecksumVerifier.computeChecksum(packet);
        assertTrue(crc > 0);
        assertTrue(MeshChecksumVerifier.verify(packet, crc));
    }

    @Test
    void testTamperedPacketRejected() {
        String packet = "MESH_PAYLOAD:sender=alice;receiver=bob;amount=500.00";
        long crc = MeshChecksumVerifier.computeChecksum(packet);
        String tampered = "MESH_PAYLOAD:sender=alice;receiver=bob;amount=9999.00";
        assertFalse(MeshChecksumVerifier.verify(tampered, crc));
    }
}
