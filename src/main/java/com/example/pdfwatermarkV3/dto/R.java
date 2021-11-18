package com.example.pdfwatermarkV3.dto;


public class R<T> {

    /**
     * 数据响应码
     */
    private String code = "200";

    /**
     * 数据响应描述
     */
    private String desc = "success";

    /**
     * 响应数据
     */
    private T data;

    public R() {
    }

    public static <T> R success(T t) {
        R r = new R();
        r.setData(t);
        return r;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}