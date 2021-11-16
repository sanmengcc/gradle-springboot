package com.example.pdfwatermark;

public class FileDTO {

    /**
     * 文件序号
     */
    private Integer index;

    /**
     * 文件路径
     */
    private String path;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}