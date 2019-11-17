package com.ljh.custom.base_library.utils;

import android.text.TextUtils;

import java.security.Key;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

/**
 * @author Zhipeng.Fan
 * @类说明 3DES加密、解密工具类
 */
public class DESUtils {
    // 密钥 长度不得小于24
    private final static String secretKey = "uck23*Iksk!sk_*ckdl52Udc";
    // 向量 可有可无 终端后台也要约定
    private final static String iv = "RX@)!^rx";
    // 加解密统一使用的编码方式
    private final static String encoding = "utf-8";

    /**
     * 3DES加密
     *
     * @param plainText 普通文本
     * @return 加密字符串
     * @throws Exception
     */
    public static String encode(String plainText) {
        if(TextUtils.isEmpty(plainText)){
            return null;
        }
        String result = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            Key deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            byte[] encryptData = cipher.doFinal(plainText.getBytes(encoding));
            result = Base64.encodeToString(encryptData);
        } catch (Exception pE) {
            pE.printStackTrace();
        }
        return result;
    }

    /**
     * 3DES解密
     *
     * @param encryptText 加密文本
     * @return 明文
     * @throws Exception
     */
    public static String decode(String encryptText) {
        if(TextUtils.isEmpty(encryptText)){
            return null;
        }
        String result = null;
        try {
            DESedeKeySpec spec = new DESedeKeySpec(secretKey.getBytes());
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            Key deskey = keyfactory.generateSecret(spec);
            Cipher cipher = Cipher.getInstance("desede/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(iv.getBytes());
            cipher.init(Cipher.DECRYPT_MODE, deskey, ips);
            byte[] decryptData = cipher.doFinal(Base64.decode(encryptText));
            result = new String(decryptData, encoding);
        } catch (Exception pE) {
            pE.printStackTrace();
        }
        return result;
    }
}
