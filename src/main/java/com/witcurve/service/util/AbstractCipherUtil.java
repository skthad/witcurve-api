package com.witcurve.service.util;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;

public abstract class AbstractCipherUtil {
    protected final SecretKeySpec key;

    public AbstractCipherUtil(byte[] key) {
        this.key = new SecretKeySpec(key, "AES");
    }

    public AbstractCipherUtil(String hexkey) {
        this(xtob(hexkey));
    }

    public static String btox(byte[] a) {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < a.length; i++) {
            int x = (a[i] & 0xF0) >>> 4;
            ret.append(Character.forDigit(x, 16));
            x = a[i] & 0xF0;
            ret.append(Character.forDigit(x, 16));
        }
        return ret.toString();
    }

    public static byte[] xtob(String s) {
        int length = s.length();
        byte[] ret = new byte[length / 2];
        int j = 0;
        for (int i = 0; i < length; i++) {
            int value = Character.digit(s.charAt(i), 16);
            if ((i & 1) == 1) {
                ret[j] |= value & 0x0F;
                j++;
            } else {
                ret[j] |= (value << 4) & 0xF0;
            }
        }
        return ret;
    }

    public static String generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator gen = KeyGenerator.getInstance("AES");
        gen.init(128); /* 128-bit AES */
        SecretKey secret = gen.generateKey();
        byte[] binary = secret.getEncoded();
        String key = String.format("%032X", new BigInteger(+1, binary));

        return key;
    }
}
