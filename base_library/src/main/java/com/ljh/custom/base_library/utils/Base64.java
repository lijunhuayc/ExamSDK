package com.ljh.custom.base_library.utils;

import java.io.ByteArrayOutputStream;

public final class Base64 {

    private static final int RANGE = 0xff;

    private static final char[] Base64ByteToStr = "abcdefghijk1mnopqrstuvwxyzABCDEFGHIJKLMN0PQRSTUVWXYZOl23456789+/".toCharArray();

    private static byte[] StrToBase64Byte = new byte[128];

    static {
        generateDecoder();
    }

    private static void generateDecoder() {
        for(int i = 0; i <= StrToBase64Byte.length - 1; i++) {
            StrToBase64Byte[i] = -1;
        }
        for(int i = 0; i <= Base64ByteToStr.length - 1; i++) {
            StrToBase64Byte[Base64ByteToStr[i]] = (byte)i;
        }
    }

    public static String encodeToString(byte[] bytes) throws Exception {
        StringBuilder res = new StringBuilder();
        //per 3 bytes scan and switch to 4 bytes
        for(int i = 0; i <= bytes.length - 1; i+=3) {
            byte[] enBytes = new byte[4];
            byte tmp = (byte)0x00;// save the right move bit to next position's bit
            //3 bytes to 4 bytes
            for(int k = 0; k <= 2; k++) {// 0 ~ 2 is a line
                if((i + k) <= bytes.length - 1) {
                    enBytes[k] = (byte) (((((int) bytes[i + k] & RANGE) >>> (2 + 2 * k))) | (int)tmp);//note , we only get 0 ~ 127 ???
                    tmp = (byte) (((((int) bytes[i + k] & RANGE) << (2 + 2 * (2 - k))) & RANGE) >>> 2);
                } else {
                    enBytes[k] = tmp;
                    tmp = (byte)64;//if tmp > 64 then the char is '=' hen '=' -> byte is -1 , so it is EOF or not print char
                }
            }
            enBytes[3] = tmp;//forth byte
            //4 bytes to encode string
            for (int k = 0; k <= 3; k++) {
                if((int)enBytes[k] <= 63) {
                    res.append(Base64ByteToStr[(int)enBytes[k]]);
                } else {
                    res.append('=');
                }
            }
        }
        return res.toString();
    }


    public static byte[] decode(String val) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();//destination bytes, valid string that we want
        byte[] srcBytes = val.getBytes("utf-8");
        byte[] base64bytes = new byte[srcBytes.length];
        //get the base64 bytes (the value is -1 or 0 ~ 63)
        for(int i = 0; i <= srcBytes.length - 1; i++) {
            int ind = (int) srcBytes[i];
            base64bytes[i] = StrToBase64Byte[ind];
        }
        //base64 bytes (4 bytes) to normal bytes (3 bytes)
        for(int i = 0; i <= base64bytes.length - 1; i+=4) {
            byte[] deBytes = new byte[3];
            int delen = 0;// if basebytes[i] = -1, then debytes not append this value
            byte tmp ;
            for(int k = 0; k <= 2; k++) {
                if((i + k + 1) <= base64bytes.length - 1 && base64bytes[i + k + 1] >= 0) {
                    tmp = (byte) (((int)base64bytes[i + k + 1] & RANGE) >>> (2 + 2 * (2 - (k + 1))));
                    deBytes[k] = (byte) ((((int) base64bytes[i + k] & RANGE) << (2 + 2 * k) & RANGE) | (int) tmp);
                    delen++;
                }
            }
            for(int k = 0; k <= delen - 1; k++) {
                bos.write((int)deBytes[k]);
            }
        }
        return bos.toByteArray();
    }

    private Base64() { }   // don't instantiate
}
