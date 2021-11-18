package com.example.pdfwatermarkV3.service;

import com.example.pdfwatermarkV3.dto.MarkDTO;

import java.io.File;
import java.util.List;

public interface IPdfMarkService {

    /**
     * pdf水印
     * @param dataFiles 需要水印的PDF集合
     * @param markDTO 水印参数
     * @return 返回ZIP后的地址
     */
    String pdfMark(List<File> dataFiles, MarkDTO markDTO);
}