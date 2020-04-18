package com.tianfan.shooting.admin;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.elvishew.xlog.XLog;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.mvp.view.FileUPLoadView;
import com.tianfan.shooting.admin.mvp.presenter.FileUpLoadPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import me.rosuh.filepicker.config.FilePickerManager;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-02 17:18
 * @Description 上传任务文件页面
 */
public class UPTaskFileActivity extends AppCompatActivity implements View.OnClickListener, FileUPLoadView {

    @BindView(R.id.bt)
    Button upBT;
    PromptDialog promptDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_task_file);
        ButterKnife.bind(this);
        upBT.setOnClickListener(this);
        promptDialog = new PromptDialog(this);
    }

    //点击事件监听
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt) {
            FilePickerManager.INSTANCE.from(this)
                    .maxSelectable(1).forResult(1123);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //文件选择返回
        if (requestCode == 1123) {
            if (FilePickerManager.INSTANCE.obtainData().size() > 0) {
                String result = FilePickerManager.INSTANCE.obtainData().get(0);
                XLog.e("选中文件------>："+result);
                int position = result.lastIndexOf(".");
                if (position != -1) {
                    String check = result.substring(position + 1);
                    if (check.equals("xls")) {
                        promptDialog.showLoading("正在上传");
                        new FileUpLoadPresenter(this).doUP(result);
                    } else {
                        promptDialog.showWarnAlert("只支持XLS格式的Excel文档", new PromptButton("确定", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
                            }
                        }));
                    }
                } else {

                    promptDialog.showWarnAlert("不支持的文件类型", new PromptButton("确定", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {
                        }
                    }));
                }
            }
        }
    }

    //文件上传成功
    @Override
    public void FileUpSuccess() {
        promptDialog.dismiss();
    }

    //文件上传失败
    @Override
    public void FileUpError(String reason) {
//        promptDialog.dismiss();
//        promptDialog.showError(reason);
        promptDialog.showWarnAlert(reason, new PromptButton("确定", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton button) {
            }
        }));
    }
}
