package com.haiphamcoder.utility;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Base64;

public class FileUtils {
    private FileUtils() {

    }

    public static String readFile(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        String currentLine;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            while ((currentLine = bufferedReader.readLine()) != null) {
                contentBuilder.append(currentLine).append("\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return contentBuilder.toString();
    }

    public static boolean writeFile(String filePath, String content) {
        boolean result = false;
        try {
            FileWriter fileWriter = new FileWriter(filePath);
            fileWriter.write(content);
            fileWriter.close();
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void encryptFile(String filePath, String base64EncodedPublicKey) throws Exception {
        String content = readFile(filePath);

        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128);
        SecretKey secretKey = keyGenerator.generateKey();

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        String cipherText = Base64.getEncoder().encodeToString(cipher.doFinal(content.getBytes()));

        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.PUBLIC_KEY, RSAUtils.getPublicKey(base64EncodedPublicKey));
        String encryptedSecretKey = Base64.getEncoder().encodeToString(cipher.doFinal(secretKey.getEncoded()));

        writeFile(filePath + ".encrypted", encryptedSecretKey + "\n" + cipherText);
    }

    public static void decryptFile(String filePath, String base64EncodedPrivateKey) throws Exception {
        String content = readFile(filePath);
        String[] lines = content.split("\n");
        String encryptedSecretKey = lines[0];
        String encryptedContent = lines[1];

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.PRIVATE_KEY, RSAUtils.getPrivateKey(base64EncodedPrivateKey));
        byte[] decryptedSecretKey = cipher.doFinal(Base64.getDecoder().decode(encryptedSecretKey.getBytes()));

        SecretKey originalSecretKey = new SecretKeySpec(decryptedSecretKey, 0, decryptedSecretKey.length, "AES");
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, originalSecretKey);
        byte[] decryptedContent = cipher.doFinal(Base64.getDecoder().decode(encryptedContent.getBytes()));
        String decryptedContentString = new String(decryptedContent);

        writeFile(filePath + ".decrypted", decryptedContentString);
    }
}
