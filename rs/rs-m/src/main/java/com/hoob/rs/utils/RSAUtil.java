package com.hoob.rs.utils;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RSAUtil {

    public static final String PUBLIC_KEY = "public_key";
    public static final String PRIVATE_KEY = "private_key";
    public static final Map<String, Map<String, RSAKey>> RSA_KEYS = new HashMap<>();

    /**
     * 根据userId获取密钥对
     * @param userId
     * @return
     */
    public static Map<String, RSAKey> getByUser(String userId) {
        return RSA_KEYS.get(userId);
    }

    /**
     * 根据userId添加密钥对
     * @param userId
     * @param keys
     */
    public static void addByUser(String userId, Map<String, RSAKey> keys) {
        RSA_KEYS.put(userId, keys);
    }

    /**
     * 生成公钥和私钥
     *
     * @throws NoSuchAlgorithmException exception
     */
    public static Map<String, RSAKey> getKeys() throws NoSuchAlgorithmException {
        Map<String, RSAKey> keyMap = new HashMap<>();
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    /**
     * RSA公钥加密
     *
     * @param data      需要加密的字符串
     * @param publicKey 公钥
     * @return 公钥加密后的内容
     * @throws Exception
     */
    public static String encrypt(String data, RSAPublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String outStr = Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8")));
        return outStr;
    }
    /***/
    public static String decrypt(String data, RSAPrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        String outStr = new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes("UTF-8"))));
        return outStr;
    }
    /***/
    public static void main(String[] args) throws Exception {
        String message = "李雷，how do you do!";
        Map<String, RSAKey> keyMap = getKeys();
        RSAPublicKey publicKey = (RSAPublicKey) keyMap.get(PUBLIC_KEY);
        RSAPrivateKey privateKey = (RSAPrivateKey) keyMap.get(PRIVATE_KEY);
        String pubKeyStr = new String(Base64.getEncoder().encode(publicKey.getEncoded()));
        String priKeyStr = new String(Base64.getEncoder().encode(privateKey.getEncoded()));
        System.out.println("pubKeyStr >>" + pubKeyStr);
        System.out.println("priKeyStr >>" + priKeyStr);
        //
        String encryptMsg = encrypt(message, publicKey);
        String decryptMsg = decrypt(encryptMsg, privateKey);
        System.out.println("encryptStr >>" + encryptMsg);
        System.out.println("decryptStr >>" + decryptMsg);
    }

}
