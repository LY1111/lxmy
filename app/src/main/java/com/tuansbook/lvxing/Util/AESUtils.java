package com.tuansbook.lvxing.Util;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.util.Random;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Administrator on 2017/3/17.
 * 按接口文档中的规则编写
 * AES加密解密 除上传文件外 第四部分不传值(SVN接口文档) 上传文件时第四部分传文件流 不用加密 第一部分传整个第三部分的长度 除此之外第一部分传随机数
 */
public class AESUtils {

    private static int[] sec = {8,10,19,12,16,12,20,9,15,13,11,14,12,11,10,6};
    private static int[] iv = {15,12,7,16,19,9,8,13,18,14,13,9,11,19,20,8};

    /**
     * 加密
     * @param str 要加密的字符串
     * @return String
     */
    public static String encrypt(String str) {

        StringBuffer res = new StringBuffer();

        try {

            if(TextUtils.isEmpty(str)){
                return null;
            }

            res.append(getRandom(5)); // 第一部分

            // 私有秘钥和初始化向量都要根据randomKey生成 16位
            String random = getRandom(15); // 随机key 做公有秘钥 最后在url中传入
            Log.i("加密随机key：",random);
            StringBuffer stringBuffersec = new StringBuffer();
            StringBuffer stringBufferiv = new StringBuffer();

            for (int i = 0; i < sec.length; i++) {
                stringBuffersec.append(random.charAt(sec[i] - 6));
                stringBufferiv.append(random.charAt(iv[i] - 6));
            }

            // SecretKeySpec 私有秘钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(stringBuffersec.toString().getBytes(),"AES");
            // 初始化向量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(stringBufferiv.toString().getBytes());

            // 加密初始化
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,secretKeySpec,ivParameterSpec);

            // 加密后byte[]
            byte[] encrypted = cipher.doFinal(str.getBytes("UTF-8"));

            // 对结果进行Base64加密
            res.append(random);
            res.append(Base64.encodeToString(encrypted,Base64.DEFAULT));

        }catch (Exception e){
            e.printStackTrace();
        }

        return res.toString();
    }

    /**
     * 解密
     * @param data 要解密的字符串
     * @return String
     */
    public static String decrypt(String data){

        String res = "";

        try {

            if(TextUtils.isEmpty(data)){
                return null;
            }

            // 取随机key
            String randomKey = data.substring(5,20);
            Log.i("解密随机key：",randomKey);

            // 计算出私有秘钥和初始化向量
            StringBuffer stringBuffersec = new StringBuffer();
            StringBuffer stringBufferiv = new StringBuffer();

            for (int i = 0; i < sec.length; i++) {
                stringBuffersec.append(randomKey.charAt(sec[i] - 6));
                stringBufferiv.append(randomKey.charAt(iv[i] - 6));
            }

            // SecretKeySpec 私有秘钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(stringBuffersec.toString().getBytes(),"AES");
            // 初始化向量
            IvParameterSpec ivParameterSpec = new IvParameterSpec(stringBufferiv.toString().getBytes());

            // 加密初始化
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE,secretKeySpec,ivParameterSpec);

            // Base64解密 注意加密结果中字符串的构成
            byte[] base64 = Base64.decode(data.substring(20).getBytes(),Base64.DEFAULT);

            // Cipher解密
            res = new String(cipher.doFinal(base64));
            Log.i("解密出的字符串是：",res);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * 取随机数
     */
    public static String getRandom(int length){

        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();

        char[] chars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

        for (int i = 0; i < length; i++) {
            stringBuffer.append(chars[random.nextInt(62)]);
        }

        return stringBuffer.toString();
    }
}
