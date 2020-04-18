package com.tianfan.shooting.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.CameraBean;

import androidx.annotation.NonNull;

/**
 * @Name：Shooting
 * @Description：摄像头
 * @Author：Chen
 * @Date：2020/4/15 22:49
 * 修改人：Chen
 * 修改时间：2020/4/15 22:49
 */
public class CameraDialog extends Dialog implements View.OnClickListener {

    private TextView tv_title;
    private EditText ed_camera_number;
    private EditText ed_camera_name;
    private EditText ed_camera_location;
    private TextView tv_cancle;
    private TextView tv_comfir;
    private  CameraBean mCameraBean;
    public CameraDialog(@NonNull Context context) {
        super(context, R.style.alert_dialog);
    }

    public CameraDialog(@NonNull Context context, CameraBean mCameraBean) {
        super(context, R.style.alert_dialog);
        this.mCameraBean = mCameraBean;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_camera);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        tv_title = findViewById(R.id.tv_title);
        ed_camera_number = findViewById(R.id.ed_camera_number);
        ed_camera_name = findViewById(R.id.ed_camera_name);
        ed_camera_location = findViewById(R.id.ed_camera_location);
        tv_cancle = findViewById(R.id.tv_cancle);
        tv_comfir = findViewById(R.id.tv_comfir);
        tv_cancle.setOnClickListener(this);
        tv_comfir.setOnClickListener(this);

        if (mCameraBean==null){
            tv_title.setText("新增摄像头");
        }else {
            tv_title.setText("修改摄像头");
        }

    }

    @Override
    public void onClick(View v) {
        dismiss();
    }
}
