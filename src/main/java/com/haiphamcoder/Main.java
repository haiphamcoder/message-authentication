package com.haiphamcoder;

import com.haiphamcoder.utility.FileUtils;
import com.haiphamcoder.utility.RSAKeyPairGenerator;

public class Main {
    public static void main(String[] args) throws Exception {
        RSAKeyPairGenerator rsaKeyPairGenerator = new RSAKeyPairGenerator();
        String publicKey = rsaKeyPairGenerator.getPublicKey();
        String privateKey = rsaKeyPairGenerator.getPrivateKey();
    }
}