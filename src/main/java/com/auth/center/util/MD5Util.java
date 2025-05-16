package com.auth.center.util;

import com.auth.center.exception.CustomException;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @Desc: 简单签名工具
 * @Author: zhoujianbo
 * @Version: 1.0
 * @Date: 2024/4/24 16:35
 **/
@Slf4j
public class MD5Util {

    /**
     * @param plainText
     * @param salt 盐值
     * @return
     */
    public static String encrypt(String plainText, String salt) {

        // 获取签名算法
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("获取签名算法异常", e);
            throw new CustomException("签名算法获取异常");
        }

        // 获取MD5摘要
        byte[] digest = md.digest((plainText + salt).getBytes());

        // 将MD5摘要转换为十六进制字符串
        return byte2hexString(digest).toUpperCase();
    }

    /**
     * 将字节数组转换成十六进制字符串
     * @param bytes
     * @return
     */
    private static String byte2hexString(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        for (int i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }
}
