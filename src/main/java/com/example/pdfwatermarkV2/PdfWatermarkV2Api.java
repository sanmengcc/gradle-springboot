package com.example.pdfwatermarkV2;

import com.example.pdfwatermark.PdfWatermarkConstants;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PdfWatermarkV2Api {

    /**
     * 文件上传控制器
     *
     * @param filenames 批量上传的文件集合
     * @return
     */
    @PostMapping(value = "/pdf/watermark/upload/v2")
    public String upload(MultipartFile[] filenames) {
        Long now = System.currentTimeMillis();

        String location = "10,10,30,30";
        String password = "123456";
        String desc = "水印文本";
        String imageUrl = "https://www.wwei.cn/yasuotu/images/logo.png";

        //水印后的FileDTO
        List<FileDTO> fileDTOList = new ArrayList<>();

        for (MultipartFile multipartFile : filenames) {
            try {
                //指定本地的PDF文件路径以及名称
                String pathname = PdfWatermarkConstants.BASE + now + "/" + multipartFile.getOriginalFilename();
                //创建本地的PDF
                File pdfFile = new File(pathname);
                //将网络流 MultipartFile 转化为本地 PDF文件
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), pdfFile);

                //水印后的PDF
                String watermarkFile = PdfWatermarkUtil.watermark(location, password, pdfFile, desc, imageUrl);

                FileDTO fileDTO = new FileDTO();
                fileDTO.setPath(watermarkFile);
                fileDTOList.add(fileDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //打包ZIP
        String pdf2Zip = Zip.pdf2Zip(fileDTOList);
        System.out.println("ZIP打包后的PDF:" + pdf2Zip);

        return "这是等待生成的下载地址";
    }
}