package com.haiphamcoder.utility;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

        String cipherText = AESUtils.encrypt(content.getBytes(), secretKey);
        String encryptedSecretKey = RSAUtils.encrypt(secretKey.getEncoded(), RSAUtils.getPublicKey(base64EncodedPublicKey));
        writeFile(filePath + ".encrypted", encryptedSecretKey + "\n" + cipherText);
    }

    public static void decryptFile(String inputFilePath, String base64EncodedPrivateKey, String outputFilePath) throws Exception {
        String content = readFile(inputFilePath);
        String[] lines = content.split("\n");
        String encryptedSecretKey = lines[0];
        String encryptedContent = lines[1];

        byte[] decryptedSecretKey = RSAUtils.decrypt(encryptedSecretKey.getBytes(), RSAUtils.getPrivateKey(base64EncodedPrivateKey));
        SecretKey originalSecretKey = new SecretKeySpec(decryptedSecretKey, 0, decryptedSecretKey.length, "AES");
        String decryptedContentString = AESUtils.decrypt(encryptedContent, originalSecretKey);
        writeFile(outputFilePath, decryptedContentString);
    }
}
