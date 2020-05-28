package com.tianfan.shooting.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.AddEquipTypeAdapter;
import com.tianfan.shooting.bean.EquipTypeBean;
import com.tianfan.shooting.bean.TaskEquipBean;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/5/29 0:26
 * 修改人：Chen
 * 修改时间：2020/5/29 0:26
 */
public class EditTaskEquipDialog extends Dialog implements View.OnClickListener {

    private TextView tv_equip_type;
    private TextView tv_equip_name;
    private TextView tv_equip_unit;
    private EditText edit_row_count;
    private TextView tv_cancle;
    private TextView tv_comfir;

    private ImageView image_add;
    private ImageView image_reduce;

    private ImageView image_count_take_add;
    private EditText edit_row_count_take;
    private ImageView image_count_take_reduce;
    private onClickComfirInterface mOnClickComfirInterface;
    private TaskEquipBean mTaskEquipBean;
    public EditTaskEquipDialog(@NonNull Context context, TaskEquipBean mTaskEquipBean, onClickComfirInterface mOnClickComfirInterface) {
        super(context, R.style.alert_dialog);
        this.mOnClickComfirInterface = mOnClickComfirInterface;
        this.mTaskEquipBean = mTaskEquipBean;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_task_equip);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        tv_equip_type = findViewById(R.id.tv_equip_type);
        tv_equip_name = findViewById(R.id.tv_equip_name);
        tv_equip_unit = findViewById(R.id.tv_equip_unit);

        edit_row_count = findViewById(R.id.edit_row_count);
        image_add = findViewById(R.id.image_add);

        image_count_take_add = findViewById(R.id.image_count_take_add);
        edit_row_count_take = findViewById(R.id.edit_row_count_take);
        image_count_take_reduce = findViewById(R.id.image_count_take_reduce);
        image_reduce = findViewById(R.id.image_reduce);
        tv_cancle = findViewById(R.id.tv_cancle);
        tv_comfir = findViewById(R.id.tv_comfir);
        image_add.setOnClickListener(this);
        image_reduce.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        tv_comfir.setOnClickListener(this);
        image_count_take_add.setOnClickListener(this);
        image_count_take_reduce.setOnClickListener(this);

        if (mTaskEquipBean!=null){
            tv_equip_type.setText(mTaskEquipBean.getEquip_type());
            tv_equip_name.setText(mTaskEquipBean.getEquip_name());
            tv_equip_unit.setText(mTaskEquipBean.getEquip_unit());
            edit_row_count.setText(String.valueOf(mTaskEquipBean.getEquip_count()));
            edit_row_count_take.setText(String.valueOf(mTaskEquipBean.getEquip_count_take()));
        }

    }

    @Override
    public void onClick(View v) {

        if (v == image_add) {
            int count = Integer.parseInt(edit_row_count.getText().toString());
            count++;
            edit_row_count.setText(String.valueOf(count));
        } else if (v == image_reduce) {
            int count = Integer.parseInt(edit_row_count.getText().toString());
            if (count == 1) {
                Toast.makeText(getContext(), "器材数量不能小于1", Toast.LENGTH_SHORT).show();
            } else {
                count--;
                edit_row_count.setText(String.valueOf(count));
            }

        } else if (v==image_count_take_add){
            int count = Integer.parseInt(edit_row_count_take.getText().toString());
            count++;
            edit_row_count_take.setText(String.valueOf(count));
        }else if (v==image_count_take_reduce){
            int count = Integer.parseInt(edit_row_count_take.getText().toString());
            if (count == 1) {
                Toast.makeText(getContext(), "器材数量不能小于1", Toast.LENGTH_SHORT).show();
            } else {
                count--;
                edit_row_count_take.setText(String.valueOf(count));
            }
        }else if (v == tv_comfir) {
            if (TextUtils.isEmpty(tv_equip_type.getText().toString())) {
                Toast.makeText(getContext(), "请选择类型", Toast.LENGTH_SHORT).show();
            }else {
                mOnClickComfirInterface.onResult(mTaskEquipBean,tv_equip_type.getText().toString(), tv_equip_name.getText().toString(), tv_equip_unit.getText().toString(), edit_row_count.getText().toString(),edit_row_count_take.getText().toString());
                dismiss();
            }

        } else if (v == tv_cancle) {
            dismiss();
        }
    }

    public interface onClickComfirInterface {

        void onResult(TaskEquipBean mTaskEquipBean,String type, String name, String unit, String count,String count_take);
    }
}
