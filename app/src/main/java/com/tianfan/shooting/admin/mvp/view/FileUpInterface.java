package com.tianfan.shooting.admin.mvp.view;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-02 17:54
 * @Description 文件上传接口
 */
public interface FileUpInterface {
    public void start (String uri,UpResult loginResult);
    interface UpResult {
        void onComplete(String result);
        void fail(String msg);
    }

}
