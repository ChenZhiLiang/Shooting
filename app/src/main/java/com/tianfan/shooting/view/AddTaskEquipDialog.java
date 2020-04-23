package com.tianfan.shooting.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tianfan.shooting.R;

import androidx.annotation.NonNull;

/**
 * @Name：Shooting
 * @Description：添加任务器材
 * @Author：Chen
 * @Date：2020/4/18 23:38
 * 修改人：Chen
 * 修改时间：2020/4/18 23:38
 */
public class AddTaskEquipDialog extends Dialog implements View.OnClickListener {

    private EditText ed_equip_type;
    private EditText ed_equip_name;
    private EditText ed_equip_unit;
    private TextView tv_row_count;
    private TextView tv_cancle;
    private TextView tv_comfir;

    private ImageView image_add;
    private ImageView image_reduce;

    private onClickComfirInterface mOnClickComfirInterface;

    public AddTaskEquipDialog(@NonNull Context context,onClickComfirInterface mOnClickComfirInterface) {
        super(context, R.style.alert_dialog);
        this.mOnClickComfirInterface = mOnClickComfirInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_task_equip);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        ed_equip_type = findViewById(R.id.ed_equip_type);
        ed_equip_name = findViewById(R.id.ed_equip_name);
        ed_equip_unit = findViewById(R.id.ed_equip_unit);

        tv_row_count = findViewById(R.id.tv_row_count);
        image_add = findViewById(R.id.image_add);
        image_reduce = findViewById(R.id.image_reduce);
        tv_cancle = findViewById(R.id.tv_cancle);
        tv_comfir = findViewById(R.id.tv_comfir);
        image_add.setOnClickListener(this);
        image_reduce.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        tv_comfir.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v==image_add){
            int count = Integer.parseInt(tv_row_count.getText().toString());
            count++;
            tv_row_count.setText(String.valueOf(count));
        }else if (v==image_reduce){
            int count = Integer.parseInt(tv_row_count.getText().toString());
            if (count==1){
                Toast.makeText(getContext(),"器材数量不能小于1",Toast.LENGTH_SHORT).show();
            }else {
                count--;
                tv_row_count.setText(String.valueOf(count));
            }

        }else if (v==tv_comfir){
            if (TextUtils.isEmpty(ed_equip_type.getText().toString())){
                Toast.makeText(getContext(),"请输入器材类型",Toast.LENGTH_SHORT).show();
            }else  if (TextUtils.isEmpty(ed_equip_name.getText().toString())){
                Toast.makeText(getContext(),"请输入器材名称",Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(ed_equip_unit.getText().toString())){
                Toast.makeText(getContext(),"请输入器材单位",Toast.LENGTH_SHORT).show();
            }else {
                mOnClickComfirInterface.onResult(ed_equip_type.getText().toString(),ed_equip_name.getText().toString(),ed_equip_unit.getText().toString(),tv_row_count.getText().toString());
                dismiss();
            }

        }else if (v==tv_cancle){
            dismiss();
        }
    }

    public interface  onClickComfirInterface{

        void onResult(String type,String name,String unit,String count);
    }
}
