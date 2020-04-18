package com.tianfan.shooting.admin.mvp.presenter;

import com.tianfan.shooting.admin.mvp.model.FileUPLoaSrvice;
import com.tianfan.shooting.admin.mvp.view.FileUPLoadView;
import com.tianfan.shooting.admin.mvp.view.FileUpInterface;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-02 17:58
 * @Description 文件上传
 */
public class FileUpLoadPresenter {
    FileUPLoadView fileUPLoadView;
    FileUPLoaSrvice fileUPLoaSrvice = new FileUPLoaSrvice() ;

    public FileUpLoadPresenter(FileUPLoadView fileUPLoadView) {
        this.fileUPLoadView = fileUPLoadView;
    }

    public void doUP(String URI){
        fileUPLoaSrvice.start(URI, new FileUpInterface.UpResult() {
            @Override
            public void onComplete(String result) {
                fileUPLoadView.FileUpSuccess();
            }

            @Override
            public void fail(String msg) {
                fileUPLoadView.FileUpError(msg);
            }
        });
    }

}
