package com.tianfan.shooting.net;

/**
 * @program: DW_GX_ACJF
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-28 10:22
 **/
public class BaseResponse<T> {
    private String result;
    private String msg;
    private T data;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}