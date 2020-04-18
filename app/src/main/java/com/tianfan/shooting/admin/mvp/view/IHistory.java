package com.tianfan.shooting.admin.mvp.view;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-16 10:05
 * @Description 功能描述
 */
public interface IHistory {
    void start(String page, IHistory.IChooseTaskResult result);
    interface IChooseTaskResult{
        void ok(String data);
        void bullshit(String msg);

    }
}
