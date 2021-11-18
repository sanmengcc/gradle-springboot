package com.example.pdfwatermarkV3.service.impl;


import com.example.pdfwatermarkV3.dto.FileDTO;
import com.example.pdfwatermarkV3.dto.MarkDTO;
import com.example.pdfwatermarkV3.service.IPdfMarkService;
import com.example.pdfwatermarkV3.util.PdfWatermarkUtil;
import com.example.pdfwatermarkV3.util.Zip;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class PdfMarkService implements IPdfMarkService {

    @Override
    public String pdfMark(List<File> dataFiles, MarkDTO markDTO) {

        //java8特性stream进行批量处理PDF
        List<CompletableFuture<FileDTO>> taskList = dataFiles
                .stream()
                .map(file -> process(file, markDTO))
                .collect(Collectors.toList());

        //获取所有的并行任务
        CompletableFuture<Void> allFutures =
                CompletableFuture
                        .allOf(taskList.toArray(new CompletableFuture[taskList.size()]));


        //等待每一个子线程完成水印
        CompletableFuture<List<FileDTO>> finalResults = allFutures
                .thenApply(v -> taskList
                        .stream()
                        .map(future -> future.join())
                        .collect(Collectors.toList()));

        try {
            //获取任务结果
            List<FileDTO> fileDTOList = finalResults.get();
            //Zip打包
            return Zip.pdf2Zip(fileDTOList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * CompletableFuture进行水印
     *
     * @param pdfFile
     * @param markDTO
     * @return
     */
    private CompletableFuture<FileDTO> process(File pdfFile, MarkDTO markDTO) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                //水印后的PDF
                String watermarkFile = PdfWatermarkUtil.watermark(markDTO.getLocation(),
                        markDTO.getPassword(), pdfFile, markDTO.getDesc(), markDTO.getImageUrl());
                FileDTO fileDTO = new FileDTO();
                fileDTO.setPath(watermarkFile);
                return fileDTO;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}