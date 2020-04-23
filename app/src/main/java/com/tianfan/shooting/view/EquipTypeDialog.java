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
import com.tianfan.shooting.bean.EquipTypeBean;

import androidx.annotation.NonNull;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/15 23:16
 * 修改人：Chen
 * 修改时间：2020/4/15 23:16
 */
public class EquipTypeDialog extends Dialog implements View.OnClickListener {
    private TextView tv_title;
    private EditText ed_name;
    private EditText ed_desc;
    private TextView tv_cancle;
    private TextView tv_comfir;
    private Context context;
    private  EquipTypeBean mEquipTypeBean;

    private onClickInterface mOnClickInterface;
    public EquipTypeDialog(@NonNull Context context,onClickInterface mOnClickInterface) {
        super(context, R.style.alert_dialog);
        this.context = context;
        this.mOnClickInterface = mOnClickInterface;
    }

    public EquipTypeDialog(@NonNull Context context, EquipTypeBean mEquipTypeBean, onClickInterface mOnClickInterface) {
        super(context, R.style.alert_dialog);
        this.context = context;
        this.mOnClickInterface = mOnClickInterface;
        this.mEquipTypeBean = mEquipTypeBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_equip_type);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        tv_title = findViewById(R.id.tv_title);
        ed_name = findViewById(R.id.ed_name);
        ed_desc = findViewById(R.id.ed_desc);
        tv_cancle = findViewById(R.id.tv_cancle);
        tv_comfir = findViewById(R.id.tv_comfir);
        tv_cancle.setOnClickListener(this);
        tv_comfir.setOnClickListener(this);

        if (mEquipTypeBean!=null){
            tv_title.setText("修改器材类型");
            ed_name.setText(mEquipTypeBean.getEquip_model_type_name());
            ed_desc.setText(mEquipTypeBean.getEquip_model_type_desc());
        }
    }

    @Override
    public void onClick(View v) {
        if (v==tv_comfir){
            if (TextUtils.isEmpty(ed_name.getText().toString())){
                Toast.makeText(context,"请输入器材类型名称",Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(ed_desc.getText().toString())){
                Toast.makeText(context,"请输入器材类型描述",Toast.LENGTH_SHORT).show();
            }else {
                mOnClickInterface.onClick(ed_name.getText().toString(),ed_desc.getText().toString());
            }
        }
        dismiss();
    }

    public interface onClickInterface{
        void onClick(String name,String desc);
    }
}
