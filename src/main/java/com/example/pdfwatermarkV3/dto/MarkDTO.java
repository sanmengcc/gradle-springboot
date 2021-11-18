package com.example.pdfwatermarkV3.dto;


public class MarkDTO {

    /**
     * 图片水印坐标
     */
    private String location = "10,10,30,30";

    /**
     * pdf密码
     */
    private String password = "123456";

    /**
     * 文本水印
     */
    private String desc = "水印文本";

    /**
     * 图片水印
     */
    private String imageUrl = "https://www.wwei.cn/yasuotu/images/logo.png";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}