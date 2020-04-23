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


    private EditText ed_name;
    private EditText ed_desc;
    private TextView tv_cancle;
    private TextView tv_comfir;
    private  onClickComfirInterface mOnClickComfirInterface;
    public EquipModelDialog(@NonNull Context context, onClickComfirInterface mOnClickComfirInterface) {
        super(context, R.style.alert_dialog);
        this.mOnClickComfirInterface = mOnClickComfirInterface;
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
        ed_name = findViewById(R.id.ed_name);
        ed_desc = findViewById(R.id.ed_desc);
        tv_cancle = findViewById(R.id.tv_cancle);
        tv_comfir = findViewById(R.id.tv_comfir);
        tv_cancle.setOnClickListener(this);
        tv_comfir.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==tv_comfir){
            if (TextUtils.isEmpty(ed_name.getText().toString())){
                Toast.makeText(getContext(),"请输入器材模板名称",Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(ed_desc.getText().toString())){
                Toast.makeText(getContext(),"请输入器材模板描述",Toast.LENGTH_SHORT).show();
            }else {
                mOnClickComfirInterface.onResult(ed_name.getText().toString(),ed_desc.getText().toString());
                dismiss();
            }
        }else if (v==tv_cancle){
            dismiss();
        }
    }

    public interface onClickComfirInterface{
        void onResult(String name,String desc);
    }
}
