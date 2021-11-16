package com.example.pdfwatermark;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import java.io.FileOutputStream;

public class PdfEncrypt {

    /**
     * 新增pdf密码
     * @param src
     * @param password
     * @return
     */
    public static String addAttachment(String src,String password) {
        String dest = src + "encrypt.pdf";
        //pdf权限，值为"PdfWriter.ALLOW_PRINTING"
        int permission = 0;
        try {
            PdfReader reader = new PdfReader(src);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
            //设置密码文件打开密码文件编辑密码
            //final byte userPassword[], final byte ownerPassword[]
            stamper.setEncryption(password.getBytes(),password.getBytes(), permission, false);
            //关闭流
            stamper.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dest;
    }
}