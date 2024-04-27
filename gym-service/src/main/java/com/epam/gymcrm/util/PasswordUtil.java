package com.epam.gymcrm.util;

import java.util.Random;

public class PasswordUtil {

    private PasswordUtil() { }

    private static final String PASSWORD_ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890_";

    private static final Random random = new Random();

    public static String getRandomPassword(int length) {
        char[] password = new char[length];
        for (int i = 0; i < length; i++) {
            password[i] = PASSWORD_ALPHABET.charAt(random.nextInt(PASSWORD_ALPHABET.length()));
        }
        return new String(password);
    }
}
