package com.tianfan.shooting.admin.ui.mdziliao;

import com.alibaba.fastjson.JSONObject;

/**
 * CreateBy：lxf
 * CreateTime： 2020-03-04 07:31
 */
public class AddUserEven {
    JSONObject jsonObject;

    public JSONObject getJsonObject() {
        return jsonObject;
    }

    public void setJsonObject(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public AddUserEven(JSONObject jsonObject) {
        this.jsonObject = jsonObject;
    }
}
