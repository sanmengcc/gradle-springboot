package com.example.pdfwatermarkV3.util;

import com.example.pdfwatermarkV3.constants.PdfWatermarkConstants;
import com.example.pdfwatermarkV3.dto.FileDTO;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip {

    /**
     * pdf压缩为Zip
     *
     * @param fileDTOS
     * @return
     * @throws Exception
     */
    public static String pdf2Zip(List<FileDTO> fileDTOS) {
        //设置zip的路径
        String zipName = PdfWatermarkConstants.ZIP_PDF + System.currentTimeMillis();
        ZipOutputStream zipos = null;
        try {
            new File(zipName).mkdirs();

            zipName = zipName + "/" + System.currentTimeMillis() + ".zip";

            new File(zipName).createNewFile();

            zipos = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipName)));
            //设置压缩方法
            zipos.setMethod(ZipOutputStream.DEFLATED);
            DataOutputStream os = null;
            //循环将文件写入压缩流
            for (int i = 0; i < fileDTOS.size(); i++) {
                FileDTO fileDTO = fileDTOS.get(i);
                String filename = fileDTO.getPath().substring(fileDTO.getPath().lastIndexOf("/") + 1);
                InputStream inputStream = new FileInputStream(fileDTO.getPath());
                //添加ZipEntry，并ZipEntry中写入文件流
                zipos.putNextEntry(new ZipEntry(filename));
                os = new DataOutputStream(zipos);
                byte[] buff = new byte[1024 * 10];
                int len = 0;
                //循环读写
                while ((len = inputStream.read(buff)) > -1) {
                    os.write(buff, 0, len);
                }
                //关闭此文件流
                inputStream.close();
                //关闭当前ZIP项，并将流放置到写入的位置。下一个条目。
                zipos.closeEntry();
            }
            //释放资源
            os.flush();
            os.close();
            zipos.close();
            zipos.close();
            return zipName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}