package com.tianfan.shooting.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private CameraBean mCameraBean;

    private onClickInterface mOnClickInterface;

    public CameraDialog(@NonNull Context context, onClickInterface mOnClickInterface) {
        super(context, R.style.alert_dialog);
        this.mOnClickInterface = mOnClickInterface;

    }

    public CameraDialog(@NonNull Context context, CameraBean mCameraBean, onClickInterface mOnClickInterface) {
        super(context, R.style.alert_dialog);
        this.mCameraBean = mCameraBean;
        this.mOnClickInterface = mOnClickInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_camera);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        ed_camera_number = findViewById(R.id.ed_camera_number);
        ed_camera_name = findViewById(R.id.ed_camera_name);
        ed_camera_location = findViewById(R.id.ed_camera_location);
        tv_cancle = findViewById(R.id.tv_cancle);
        tv_comfir = findViewById(R.id.tv_comfir);
        tv_cancle.setOnClickListener(this);
        tv_comfir.setOnClickListener(this);

        if (mCameraBean != null) {
            tv_title.setText("修改器材类型");
            ed_camera_number.setText(mCameraBean.getCamera_id());
            ed_camera_name.setText(mCameraBean.getCamera_name());
            ed_camera_location.setText(String.valueOf(mCameraBean.getCamera_col()));
        }

    }

    @Override
    public void onClick(View v) {

        if (v == tv_comfir) {
            if (TextUtils.isEmpty(ed_camera_number.getText().toString())) {
                Toast.makeText(getContext(), "请输入摄像头编号", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(ed_camera_name.getText().toString())) {
                Toast.makeText(getContext(), "请输入摄像头名称", Toast.LENGTH_SHORT).show();
                return;
            } else if (TextUtils.isEmpty(ed_camera_location.getText().toString())) {
                Toast.makeText(getContext(), "请输入摄像头靶位", Toast.LENGTH_SHORT).show();
                return;
            } else {
                mOnClickInterface.onClick(ed_camera_number.getText().toString(), ed_camera_name.getText().toString(), ed_camera_location.getText().toString());
            }
        }
        dismiss();
    }

    public interface onClickInterface {
        void onClick(String camera_id, String camera_name, String camera_col);
    }
}
