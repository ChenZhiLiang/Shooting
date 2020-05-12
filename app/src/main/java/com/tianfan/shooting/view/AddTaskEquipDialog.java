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
import com.tianfan.shooting.adapter.TaskRoundsAdapter;
import com.tianfan.shooting.admin.ui.activity.CommandManageActivity;
import com.tianfan.shooting.bean.EquipTypeBean;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Name：Shooting
 * @Description：添加任务器材
 * @Author：Chen
 * @Date：2020/4/18 23:38
 * 修改人：Chen
 * 修改时间：2020/4/18 23:38
 */
public class AddTaskEquipDialog extends Dialog implements View.OnClickListener {

    private TextView tv_equip_type;
    private TextView tv_equip_name;
    private TextView tv_equip_unit;
    private TextView tv_row_count;
    private TextView tv_cancle;
    private TextView tv_comfir;

    private ImageView image_add;
    private ImageView image_reduce;

    private onClickComfirInterface mOnClickComfirInterface;
    private List<EquipTypeBean> mEquipTypeDatas;
    private View popupView_view;
    private RecyclerView recycler_model;
    private AddEquipTypeAdapter mAddEquipTypeAdapter;
    private MyPopWindow window_equip_model;
    public AddTaskEquipDialog(@NonNull Context context, List<EquipTypeBean> mEquipTypeDatas, onClickComfirInterface mOnClickComfirInterface) {
        super(context, R.style.alert_dialog);
        this.mOnClickComfirInterface = mOnClickComfirInterface;
        this.mEquipTypeDatas = mEquipTypeDatas;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_task_equip);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        tv_equip_type = findViewById(R.id.tv_equip_type);
        tv_equip_name = findViewById(R.id.tv_equip_name);
        tv_equip_unit = findViewById(R.id.tv_equip_unit);

        tv_row_count = findViewById(R.id.tv_row_count);
        image_add = findViewById(R.id.image_add);
        image_reduce = findViewById(R.id.image_reduce);
        tv_cancle = findViewById(R.id.tv_cancle);
        tv_comfir = findViewById(R.id.tv_comfir);
        tv_equip_type.setOnClickListener(this);
        image_add.setOnClickListener(this);
        image_reduce.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        tv_comfir.setOnClickListener(this);

    }
    private void initPopMenu() {
        popupView_view = getLayoutInflater().inflate(R.layout.layout_popupwindow_equip, null);
        recycler_model = popupView_view.findViewById(R.id.recycler_model);
        recycler_model.setLayoutManager(new LinearLayoutManager(getContext()));
        //默认第一轮
        mAddEquipTypeAdapter = new AddEquipTypeAdapter(mEquipTypeDatas);
        mAddEquipTypeAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                EquipTypeBean mEquipTypeBean = mEquipTypeDatas.get(position);
                tv_equip_type.setText(mEquipTypeBean.getEquip_type_name());
                tv_equip_name.setText(mEquipTypeBean.getEquip_name());
                tv_equip_unit.setText(mEquipTypeBean.getEquip_unit());
                window_equip_model.dismiss();
            }
        });
        recycler_model.setAdapter(mAddEquipTypeAdapter);
        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        window_equip_model = new MyPopWindow(popupView_view, tv_equip_type.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
        window_equip_model.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        window_equip_model.showAsDropDown(tv_equip_type, 0, 0);
    }

    @Override
    public void onClick(View v) {

        if (v==tv_equip_type){
            initPopMenu();
        }else if (v == image_add) {
            int count = Integer.parseInt(tv_row_count.getText().toString());
            count++;
            tv_row_count.setText(String.valueOf(count));
        } else if (v == image_reduce) {
            int count = Integer.parseInt(tv_row_count.getText().toString());
            if (count == 1) {
                Toast.makeText(getContext(), "器材数量不能小于1", Toast.LENGTH_SHORT).show();
            } else {
                count--;
                tv_row_count.setText(String.valueOf(count));
            }

        } else if (v == tv_comfir) {
            if (TextUtils.isEmpty(tv_equip_type.getText().toString())) {
                Toast.makeText(getContext(), "请选择类型", Toast.LENGTH_SHORT).show();
            }else {
                mOnClickComfirInterface.onResult(tv_equip_type.getText().toString(), tv_equip_name.getText().toString(), tv_equip_unit.getText().toString(), tv_row_count.getText().toString());
                dismiss();
            }

        } else if (v == tv_cancle) {
            dismiss();
        }
    }

    public interface onClickComfirInterface {

        void onResult(String type, String name, String unit, String count);
    }
}
