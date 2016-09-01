package com.example.luxiansheng.query.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by luxiansheng on 16/8/13.
 */
public class GetHtml {
    public static String GetHtml(InputStream in,String code) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        in.close();
        return new String(bos.toByteArray(),code );
    }
}
