package com.example.pdfwatermarkV3.util;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileUtil {

    /**
     * File转换字节数组
     * @param file
     * @return
     */
    public static byte[] file2bytes(File file) {
        try {
            InputStream input = new FileInputStream(file);
            byte[] bytes = new byte[input.available()];
            return bytes;
        } catch (Exception e) {
            System.out.println("文件转化失败！");
        }
        return null;
    }

    /**
     * 字节数组转化File
     * @param bytes
     * @param filePath
     * @return
     */
    public static File bytes2File(byte[] bytes,String filePath) {
        try {
            File file = new File(filePath);
            OutputStream output = new FileOutputStream(file);
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
            bufferedOutput.write(bytes);
            return file;
        } catch (Exception e) {
            System.out.println("文件转化失败！");
        }
        return null;
    }

    /**
     * 将url文件转化为字节流
     * @param urlPath
     * @return
     */
    public static ByteArrayInputStream getImageStream(String urlPath) {
        InputStream in = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            URL url = new URL(urlPath);
            URLConnection connection = url.openConnection();
            in = connection.getInputStream();
            int len = -1;
            byte[] data = new byte[1024];
            while ((len = in.read(data)) != -1) {
                out.write(data, 0, len);
            }
        } catch (MalformedURLException me) {
        } catch (IOException ie) {
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
        return new ByteArrayInputStream(out.toByteArray());

}
}