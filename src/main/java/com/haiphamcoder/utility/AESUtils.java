package com.haiphamcoder.utility;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class AESUtils {
    private AESUtils() {

    }

    public static SecretKey getSecretKey(String base64EncodedSecretKey) {
        return new SecretKeySpec(Base64.getDecoder().decode(base64EncodedSecretKey.getBytes()), "AES");
    }

    public static String encrypt(byte[] data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data));
    }

    public static String decrypt(String data, SecretKey secretKey) throws Exception {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes())));
    }
}
