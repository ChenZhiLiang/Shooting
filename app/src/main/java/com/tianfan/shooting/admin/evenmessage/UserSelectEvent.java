package com.tianfan.shooting.admin.evenmessage;

import com.alibaba.fastjson.JSONObject;

/**
 * @program: Shooting
 * @description: 用户选择回调
 * @author: lxf
 * @create: 2019-11-17 23:34
 **/
public class UserSelectEvent {
    public JSONObject data;


    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
