package com.example.pdfwatermark;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PdfWatermarkApi {

    /**
     * 文件上传控制器
     *
     * @param filenames 批量上传的文件集合
     * @return
     */
    @PostMapping(value = "/pdf/watermark/upload")
    public String upload(MultipartFile[] filenames) {
        Long now = System.currentTimeMillis();

        //最终的PDF集合
        List<FileDTO> pdfDTOs = new ArrayList<>();

        for (MultipartFile multipartFile : filenames) {
            //打印数据
            System.out.println(multipartFile.getOriginalFilename() + " FileSize:" + multipartFile.getSize());

            try {
                //指定本地的PDF文件路径以及名称
                String pathname = PdfWatermarkConstants.BASE + now + "/" + multipartFile.getOriginalFilename();
                //创建本地的PDF
                File pdfFile = new File(pathname);
                //将网络流 MultipartFile 转化为本地 PDF文件
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), pdfFile);
                //PDF转化为图片
                List<FileDTO> imageDTOs = Pdf2Image.generate(pdfFile);

                //图片水印后的图片
                List<FileDTO> imageMarkDTOs = new ArrayList<>();

                //打印日志输出语句
                for (FileDTO fileDTO : imageDTOs) {
                    System.out.println(pdfFile.getName() + " -> PDF转化image：" + fileDTO.getIndex() + " 文件路径：" + fileDTO.getPath());

                    //图片水印
                    String markImage = ImageWatemark.markImage("https://www.wwei.cn/yasuotu/images/logo.png", fileDTO.getPath(), null);

                    //添加到图片水印后的图片集合
                    FileDTO newFileDTO = new FileDTO();
                    newFileDTO.setPath(markImage);
                    newFileDTO.setIndex(fileDTO.getIndex());
                    imageMarkDTOs.add(newFileDTO);
                }

                //图片文字水印后的图片集合
                List<FileDTO> imageTxtMarkDTOs = new ArrayList<>();

                //打印日志输出语句
                for (FileDTO fileDTO : imageMarkDTOs) {
                    System.out.println(pdfFile.getName() + " -> Image添加图片水印：" + fileDTO.getIndex() + " 文件路径：" + fileDTO.getPath());

                    String txtMark = ImageWatemark.mark(fileDTO.getPath(), Color.BLUE, "水印文字");
                    //添加到图片水印后的图片集合
                    FileDTO newFileDTO = new FileDTO();
                    newFileDTO.setPath(txtMark);
                    newFileDTO.setIndex(fileDTO.getIndex());
                    imageTxtMarkDTOs.add(newFileDTO);
                }

                //打印日志输出语句
                for (FileDTO fileDTO : imageTxtMarkDTOs) {
                    System.out.println(pdfFile.getName() + " -> Image添加文字水印：" + fileDTO.getIndex() + " 文件路径：" + fileDTO.getPath());
                }

                //将图片再次合并成为PDF
                String image2Pdf = Image2Pdf.image2Pdf(imageTxtMarkDTOs, multipartFile.getOriginalFilename());

                System.out.println("水印后的原始PDF文件：" + image2Pdf);

                //加密PDF
                String encryptPath = PdfEncrypt.addAttachment(image2Pdf, "123456");

                System.out.println("PDF 加密后的文件：" + encryptPath);


                //添加水印后的PDF
                FileDTO pdfDTO = new FileDTO();
                pdfDTO.setPath(encryptPath);
                pdfDTOs.add(pdfDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //进行PDF 压缩
        String pdf2Zip = Zip.pdf2Zip(pdfDTOs);
        System.out.println("PDF 经过ZIP压缩后的地址:" + pdf2Zip);

        return "这是等待生成的下载地址";
    }

}