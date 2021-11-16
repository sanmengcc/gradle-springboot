package com.example.pdfwatermark;


import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Pdf2Image {

    /**
     * 将File PDF文件转化为每一张图片
     * @param file PDF文件
     * @return 返回每一张图片的实体对象集合
     */
    public static List<FileDTO> generate(File file) {
        try {
            Long now = System.currentTimeMillis();

            //读取PDF
            PDDocument document = PDDocument.load(file);

            //加载PDF对象
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            //存储的PDF每一张图片的路径以及索引
            List<FileDTO> dataList = new ArrayList<>();

            for (int page = 0;page<document.getNumberOfPages();page++){

                //读取PDF每一页的图片
                BufferedImage img = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

                //生成图片的本地路径
                String pathname = PdfWatermarkConstants.PDF_2_IMAGE + now + "/" + file.getName() + page + ".png";

                //创建本地文件
                File imageFile = new File(pathname);
                //创建文件夹
                imageFile.mkdirs();
                //创建文件
                imageFile.createNewFile();

                FileDTO dto = new FileDTO();
                dataList.add(dto);

                dto.setIndex(page);
                dto.setPath(pathname);

                //生成PNG格式的图片
                ImageIO.write(img, "png", imageFile);
            }
            //关闭Doc流
            document.close();

            return dataList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}