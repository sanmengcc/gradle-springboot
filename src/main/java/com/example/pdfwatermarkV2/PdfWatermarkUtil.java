package com.example.pdfwatermarkV2;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.*;

import java.io.*;

public class PdfWatermarkUtil {


    /**
     * PDF水印
     * @param location 图片坐标 10,10,30,30
     * @param password pdf密码
     * @param pdfFile 需要加入水印的pdf文件
     * @param desc 文本水印
     * @param imageUrl 图片水印地址
     * @return 水印后的PDF地址
     * @throws Exception
     */
    public static String watermark(String location,
                                 String password,
                                 File pdfFile,
                                 String desc,
                                 String imageUrl) throws Exception {

        String makerFile = pdfFile.getName() + "_maker.pdf";

        BufferedOutputStream outs = new BufferedOutputStream(new FileOutputStream(new File(makerFile)));


        PdfReader reader = null;
        try {
            //加载PdfReader对象
            reader = new PdfReader(pdfFile.getPath());

            //PDF是否加密
            if (reader.isEncrypted()) {
                //解密PDF
                File decryptFile = PdfPassword.unProtected(pdfFile);
                //重新加载PdfReader
                reader = new PdfReader(decryptFile.getPath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(pdfFile + "失败....");
            return null;
        }

        //加载stamper
        PdfStamper stamper = new PdfStamper(reader, outs);

        //设置水印文本字体
        BaseFont base = BaseFont.createFont("simkai.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        //加载画笔工具
        PdfGState gs = new PdfGState();

        //设置pdf密码
        stamper.setEncryption(null,
                password.getBytes(),
                com.itextpdf.text.pdf.PdfWriter.ALLOW_PRINTING,
                false);

        //加载水印坐标
        String[] coordinate = location.split(",");

        //加载图片水印
        ByteArrayInputStream imageStream = FileUtil.getImageStream(imageUrl);
        byte images[] = new byte[imageStream.available()];
        imageStream.read(images, 0, imageStream.available());

        //对每一页的页页面进行处理
        for (int i = 1; i < reader.getNumberOfPages() + 1; i++) {

            //读取水印图片
            Image image = Image.getInstance(images);
            //设置百分比
            image.scalePercent(100);
            //设置对齐方式
            image.setAlignment(Image.UNDERLYING);

            //设置图片水印坐标
            image.setAbsolutePosition(Float.valueOf(coordinate[0]), Float.valueOf(coordinate[1]));
            image.scaleAbsolute(Float.valueOf(coordinate[2]), Float.valueOf(coordinate[3]));

            //获取页面宽度
            Float width = reader.getPageSize(i).getWidth();

            //设置水印文本
            PdfContentByte content = stamper.getUnderContent(i);
            //添加图片水印
            content.addImage(image);

            //设置透明度
            gs.setFillOpacity(0.5f);

            //设置画笔工具
            content.setGState(gs);

            //开始设置文本水印
            content.beginText();
            //设置文本矩阵
            content.setTextMatrix(70, 200);
            //设置文本颜色
            content.setColorFill(BaseColor.BLACK);
            //设置文本字体大小
            content.setFontAndSize(base, 8);
            //设置水印文本未知
            content.showTextAligned(Element.ALIGN_CENTER, desc, width / 2, 10, 0);
            //结束水印文本
            content.endText();
        }
        stamper.close();

        return makerFile;
    }
}