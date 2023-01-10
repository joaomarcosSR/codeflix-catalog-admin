package codeflix.catalog.admin.infrastructure.utils;

import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

public class HashingUtils {
    private static final HashFunction CHECKSUM = Hashing.crc32c();

    private HashingUtils() {
    }

    public static String checksum(final byte[] content) {
        return CHECKSUM.hashBytes(content).toString();
    }
}
