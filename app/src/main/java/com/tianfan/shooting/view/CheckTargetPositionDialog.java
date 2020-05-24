package com.tianfan.shooting.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.AddEquipTypeAdapter;
import com.tianfan.shooting.adapter.CheckTargetPositionAdapter;
import com.tianfan.shooting.bean.EquipTypeBean;
import com.tianfan.shooting.scorer.ui.activity.ScorerActivity;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Name：Shooting
 * @Description：选择靶位
 * @Author：Chen
 * @Date：2020/5/24 21:39
 * 修改人：Chen
 * 修改时间：2020/5/24 21:39
 */
public class CheckTargetPositionDialog extends Dialog implements View.OnClickListener {

    private TextView tv_target_position;
    private TextView id_cancle;
    private TextView comfir;
    private MyPopWindow window_equip_model;
    private View popupView_view;
    private RecyclerView recycler_model;
    private CheckTargetPositionAdapter mCheckTargetPositionAdapter;
    private List<Integer>targetPositionData = new ArrayList<>();
    private int checkTarget = 0;
    private Context mContext;
    public CheckTargetPositionDialog(@NonNull Context context) {
        super(context, R.style.alert_dialog);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_check_target_position);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        tv_target_position = findViewById(R.id.tv_target_position);
        tv_target_position.setOnClickListener(this);
        id_cancle = findViewById(R.id.id_cancle);
        comfir = findViewById(R.id.comfir);
        id_cancle.setOnClickListener(this);
        comfir.setOnClickListener(this);
        for (int i=1;i<=10;i++){
            targetPositionData.add(i);
        }

    }

    private void initPopMenu() {
        popupView_view = getLayoutInflater().inflate(R.layout.layout_popupwindow_equip, null);
        recycler_model = popupView_view.findViewById(R.id.recycler_model);
        recycler_model.setLayoutManager(new LinearLayoutManager(getContext()));
        //默认第一轮
        mCheckTargetPositionAdapter = new CheckTargetPositionAdapter(targetPositionData);
        mCheckTargetPositionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Integer integer = targetPositionData.get(position);
                checkTarget = integer;
                tv_target_position.setText(integer+"号靶");
                window_equip_model.dismiss();
            }
        });
        recycler_model.setAdapter(mCheckTargetPositionAdapter);
        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        window_equip_model = new MyPopWindow(popupView_view, tv_target_position.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
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
        window_equip_model.showAsDropDown(tv_target_position, 0, 0);
    }


    @Override
    public void onClick(View v) {
        if (v==tv_target_position){
            initPopMenu();
        }else if (v==id_cancle){
            dismiss();
        }else if (v==comfir){
            if (checkTarget==0){
                Toast.makeText(mContext,"请选择靶位",Toast.LENGTH_SHORT).show();
            }else {
                mContext.startActivity(new Intent(mContext, ScorerActivity.class).putExtra("checkTarget",checkTarget));
                dismiss();
            }
        }
    }
}
