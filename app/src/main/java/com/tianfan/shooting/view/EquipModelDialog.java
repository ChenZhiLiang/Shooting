package com.tianfan.shooting.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tianfan.shooting.R;

import androidx.annotation.NonNull;

/**
 * @Name：Shooting
 * @Description：器材模板
 * @Author：Chen
 * @Date：2020/4/16 0:02
 * 修改人：Chen
 * 修改时间：2020/4/16 0:02
 */
public class EquipModelDialog extends Dialog implements View.OnClickListener {


    private TextView tv_cancle;
    private TextView tv_comfir;
    public EquipModelDialog(@NonNull Context context) {
        super(context, R.style.alert_dialog);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_equip_model);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        tv_cancle = findViewById(R.id.tv_cancle);
        tv_comfir = findViewById(R.id.tv_comfir);
        tv_cancle.setOnClickListener(this);
        tv_comfir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
