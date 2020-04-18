package com.tianfan.shooting.admin.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.mvp.view.SubEQDialogView;


/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-24 12:19
 * @Description 功能描述
 */
public class SumitQEDialog extends Dialog {
    SubEQDialogView subEQDialogView;
    public SumitQEDialog(@NonNull Context context, SubEQDialogView submitResult) {
        super(context);
        this.subEQDialogView = submitResult;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_sumbmit_eq);
        Button comfir = findViewById(R.id.bt_comfir);
        final Button cancle = findViewById(R.id.bt_cancel);
        final EditText editText = findViewById(R.id.ed_task_name);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        comfir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().equals("")){
                    Toast.makeText(getContext(),"任务名称不能为空",Toast.LENGTH_SHORT).show();
                    return;
                }else{

                    subEQDialogView.submitClick(editText.getText().toString());
                    cancel();
                }
            }
        });

    }
}
