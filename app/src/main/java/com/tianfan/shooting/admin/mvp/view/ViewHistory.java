package com.tianfan.shooting.admin.mvp.view;


import com.alibaba.fastjson.JSONObject;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-16 10:03
 * @Description 功能描述
 */
public interface ViewHistory {
    void ok(String data);
    void bullshit(String data);
    void getMore(String data);
     void ItemClick(JSONObject jsonObject);
}
