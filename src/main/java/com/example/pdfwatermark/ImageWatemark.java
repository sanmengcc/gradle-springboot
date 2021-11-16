package com.example.pdfwatermark;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;

public class ImageWatemark {

    /**
     * 图片增加图片水印
     * @param iconPath 水印的地址 http
     * @param srcImgPath 原本的图片地址 File
     * @param degree 旋转角度
     * @return
     */
    public static String markImage(String iconPath, String srcImgPath, Integer degree) {
        OutputStream os = null;
        try {
            //读取原图片文件
            Image srcImg = ImageIO.read(new File(srcImgPath));

            //加载图片
            BufferedImage buffImg = new BufferedImage(srcImg.getWidth(null), srcImg.getHeight(null),
                    BufferedImage.TYPE_INT_RGB);

            //得到画笔对象
            Graphics2D g = buffImg.createGraphics();

            //设置对线段的锯齿状边缘处理
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.drawImage(srcImg.getScaledInstance(srcImg.getWidth(null),
                            srcImg.getHeight(null),
                            Image.SCALE_SMOOTH),
                            0,
                            0,
                    null);

            if (null != degree) {
                //设置水印旋转
                g.rotate(Math.toRadians(degree), (double) buffImg.getWidth() / 2, (double) buffImg.getHeight() / 2);
            }

            //水印图象
            ImageIcon imgIcon = new ImageIcon(new URL(iconPath));

            //得到Image对象
            Image img = imgIcon.getImage();

            //设置透明度
            float alpha = 0.5f;
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, alpha));

            //设置水印图片坐标
            g.drawImage(img, 150, 300, null);
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER));
            g.dispose();

            //生成新的图片

            String targerPath = srcImgPath + "_image_mark.png";
            os = new FileOutputStream(targerPath);
            ImageIO.write(buffImg, "png", os);
            return targerPath;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return null;
    }


    /**
     * 图片添加文字水印
     * @param srcImgPath 原图片地址 File
     * @param markContentColor 水印文字颜色
     * @param waterMarkContent 水印文字
     */
    public static String mark(String srcImgPath,Color markContentColor, String waterMarkContent) {
        try {
            //读取原图片信息
            File srcImgFile = new File(srcImgPath);
            Image srcImg = ImageIO.read(srcImgFile);

            //读取原图片高度、宽度
            int srcImgWidth = srcImg.getWidth(null);
            int srcImgHeight = srcImg.getHeight(null);


            //设置图片水印
            BufferedImage bufImg = new BufferedImage(srcImgWidth, srcImgHeight, BufferedImage.TYPE_INT_RGB);
            Graphics2D g = bufImg.createGraphics();
            g.drawImage(srcImg, 0, 0, srcImgWidth, srcImgHeight, null);
            Font font = new Font("宋体", Font.PLAIN, 20);
            g.setColor(markContentColor);

            g.setFont(font);
            int x = srcImgWidth - getWatermarkLength(waterMarkContent, g) - 3;
            int y = srcImgHeight - 3;
            g.drawString(waterMarkContent, x, y);
            g.dispose();
            // 输出图片
            String outImgPath = srcImgPath + "_txt_water.png";
            FileOutputStream outImgStream = new FileOutputStream(outImgPath);
            ImageIO.write(bufImg, "jpg", outImgStream);
            outImgStream.flush();
            outImgStream.close();

            return outImgPath;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取水印文字的长度
     *
     * @param waterMarkContent
     * @param g
     * @return
     */
    private static int getWatermarkLength(String waterMarkContent, Graphics2D g) {
        return g.getFontMetrics(g.getFont()).charsWidth(waterMarkContent.toCharArray(), 0, waterMarkContent.length());
    }
}