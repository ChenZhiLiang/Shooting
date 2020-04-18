package com.tianfan.shooting.admin.ui.evendata;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 * @program: Shooting
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-13 14:53
 **/
public class SelectUserForTaskEven {
    List<JSONObject> data ;
    JSONObject createData = new JSONObject();

    public JSONObject getCreateData() {
        return createData;
    }

    public void setCreateData(JSONObject createData) {
        this.createData = createData;
    }

    public List<JSONObject> getData() {
        return data;
    }

    public void setData(List<JSONObject> data) {
        this.data = data;
    }
}
