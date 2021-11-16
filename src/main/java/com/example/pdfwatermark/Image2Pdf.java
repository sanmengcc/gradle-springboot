package com.example.pdfwatermark;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class Image2Pdf {

    /**
     * 将图片集合合并成为PDF
     *
     * @param dataList
     * @param baseFileName
     */
    public static String image2Pdf(List<FileDTO> dataList, String baseFileName) {
        String pdfPath = PdfWatermarkConstants.BASE_PDF + System.currentTimeMillis() + "/";
        try {
            //创建文件夹
            new File(pdfPath).mkdirs();
            //创建文件
            pdfPath = pdfPath + baseFileName;
            File file = new File(pdfPath);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(pdfPath);
            // 创建文档
            Document doc = new Document(null, 0, 0, 0, 0);
            // 写入PDF文档
            PdfWriter.getInstance(doc, fos);
            // 读取图片流
            BufferedImage img = null;
            // 实例化图片
            Image image = null;
            // 循环获取图片文件夹内的图片
            for (FileDTO fileDTO : dataList) {
                //读取图片流
                img = ImageIO.read(new File(fileDTO.getPath()));
                //根据图片大小设置文档大小
                doc.setPageSize(new Rectangle(img.getWidth(), img.getHeight()));
                //实例化图片
                image = Image.getInstance(fileDTO.getPath());
                //添加图片到文档
                doc.open();
                doc.add(image);
            }
            // 关闭文档
            doc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pdfPath;
    }

}