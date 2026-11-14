package com.financeflow.mesh;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

/**
 * Validates CRC32 checksums for offline peer-to-peer mesh payment packets.
 */
public class MeshChecksumVerifier {

    public static long computeChecksum(byte[] payload) {
        if (payload == null) {
            return 0L;
        }
        CRC32 crc = new CRC32();
        crc.update(payload);
        return crc.getValue();
    }

    public static long computeChecksum(String payload) {
        if (payload == null) {
            return 0L;
        }
        return computeChecksum(payload.getBytes(StandardCharsets.UTF_8));
    }

    public static boolean verify(String payload, long expectedCrc) {
        return computeChecksum(payload) == expectedCrc;
    }
}
