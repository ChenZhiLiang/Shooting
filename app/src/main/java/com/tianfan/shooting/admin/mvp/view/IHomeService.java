package com.tianfan.shooting.admin.mvp.view;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-12 11:00
 * @Description 功能描述
 */
public interface IHomeService {
    void start(String page, HomeTaskResult result);
    interface HomeTaskResult {
        void ok(String data);
        void bullshit(String msg);

    }
}
