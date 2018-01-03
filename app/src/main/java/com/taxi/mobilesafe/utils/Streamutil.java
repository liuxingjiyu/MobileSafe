package com.taxi.mobilesafe.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by taxi01 on 2017/12/15.
 */

public class Streamutil {

    /**
     * @param is 流对象
     * @return  字符串
     */
    public static String stream2Sring(InputStream is) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int temp = -1;
        try {
            while ((temp = is.read(buffer)) != -1){
                bos.write(buffer,0,temp);
                return bos.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
