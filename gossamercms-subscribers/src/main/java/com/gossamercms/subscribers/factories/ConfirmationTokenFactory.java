package com.gossamercms.subscribers.factories;

import java.security.SecureRandom;
import java.util.HexFormat;

public class ConfirmationTokenFactory {

    private static final SecureRandom SECURE_RANDOM =
            new SecureRandom();

    public static String generateToken() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);

        return HexFormat.of().formatHex(bytes);
    }
}
