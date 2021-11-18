package com.example.pdfwatermarkV3.api;

import com.example.pdfwatermark.PdfWatermarkConstants;
import com.example.pdfwatermarkV3.dto.MarkDTO;
import com.example.pdfwatermarkV3.dto.R;
import com.example.pdfwatermarkV3.service.IPdfMarkService;
import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PdfWatermarkV3Api {

    /**
     * Spring的方式注入接口
     */
    @Resource
    private IPdfMarkService pdfMarkService;

    /**
     * 文件上传控制器
     *
     * @param filenames 批量上传的文件集合
     * @return
     */
    @PostMapping(value = "/pdf/watermark/upload/v3")
    public R<String> upload(MultipartFile[] filenames) {

        List<File> files = new ArrayList<>();

        Long now = System.currentTimeMillis();

        for (MultipartFile multipartFile : filenames) {
            try {
                //指定本地的PDF文件路径以及名称
                String pathname = PdfWatermarkConstants.BASE + now + "/" + multipartFile.getOriginalFilename();
                //创建本地的PDF
                File pdfFile = new File(pathname);
                //将网络流 MultipartFile 转化为本地 PDF文件
                FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), pdfFile);

                files.add(pdfFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //调用接口
        MarkDTO markDTO = new MarkDTO();
        //MarkDTO设置了默认参数这里只需要new就行了、如果需要更改可以自己进行set
        String pdfMark = pdfMarkService.pdfMark(files, markDTO);

        System.out.println("PDF 完成后的ZIP地址：" + pdfMark);
        return R.success(pdfMark);
    }

}