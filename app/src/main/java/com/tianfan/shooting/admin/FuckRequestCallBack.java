package com.tianfan.shooting.admin;

import com.alibaba.fastjson.JSONObject;

/**
 * @program: Shooting
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-25 08:06
 **/
public interface FuckRequestCallBack {
    public void result(boolean flag, JSONObject result, String msg);
}
