package com.taxi.mobilesafe.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by taxi01 on 2017/12/21.
 */

public class MD5Util {

    public static String encoder(String psd) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            byte[] bs = digest.digest(psd.getBytes());
            StringBuffer buffer = new StringBuffer();
            for (byte b : bs){
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2){
                    hexString = "0" + hexString;
                }
                buffer.append(hexString);
            }
            return buffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }
}
