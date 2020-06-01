package com.tianfan.shooting.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.AddEquipTypeAdapter;
import com.tianfan.shooting.adapter.ChangeTaskPersonRowcolAdapter;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.bean.CommandManageBean;
import com.tianfan.shooting.bean.EquipTypeBean;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Name：Shooting
 * @Description：更新队员的分组及靶位
 * @Author：Chen
 * @Date：2020/5/26 20:02
 * 修改人：Chen
 * 修改时间：2020/5/26 20:02
 */
public class ChangeTaskPersonRowcolDialog extends Dialog implements View.OnClickListener {

    private TextView tv_name;
    private TextView tv_id_number;
    private TextView tv_work_unit;
    private TextView tv_user_type;
    private TextView tv_group;
    private TextView tv_position;
    private RoundedImageView iv_user_icon;
    private TextView tv_cancle;
    private TextView tv_comfir;
    private View popupView_view;
    private RecyclerView recycler_model;
    private ChangeTaskPersonRowcolAdapter mChangeTaskPersonRowcolAdapter;
    private MyPopWindow window_equip_model;
    private List<CommandManageBean.CommandManageItem> personDatas;
    private CommandManageBean.CommandManageItem mCommandManageItem;//当前靶位
    private Context mContext;
    private int person_row ;//行数
    private int person_col ;//列数
    private String currentRounds;
    private String task_id;
    private String person_id;
    private onResultInterface mOnResultInterface;
    public ChangeTaskPersonRowcolDialog(@NonNull Context context, List<CommandManageBean.CommandManageItem> personDatas,CommandManageBean.CommandManageItem mCommandManageItem,
                                        int person_row, int person_col,onResultInterface mOnResultInterface) {
        super(context, R.style.alert_dialog);
        this.personDatas = personDatas;
        this.mCommandManageItem = mCommandManageItem;
        this.mContext = context;
        this.person_col = person_col;
        this.person_row = person_row;
        this.mOnResultInterface = mOnResultInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_change_task_person_rowcol);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){

        tv_name = findViewById(R.id.tv_name);
        tv_id_number = findViewById(R.id.tv_id_number);
        tv_work_unit = findViewById(R.id.tv_work_unit);
        tv_user_type = findViewById(R.id.tv_user_type);
        tv_group = findViewById(R.id.tv_group);
        tv_position = findViewById(R.id.tv_position);
        iv_user_icon = findViewById(R.id.iv_user_icon);
        tv_cancle = findViewById(R.id.tv_cancle);
        tv_comfir = findViewById(R.id.tv_comfir);
        tv_group.setText(String.valueOf(person_row));
        tv_position.setText(String.valueOf(person_col));
        tv_name.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);
        tv_comfir.setOnClickListener(this);

    }
    private void initPopMenu() {
        popupView_view = getLayoutInflater().inflate(R.layout.layout_popupwindow_equip, null);
        recycler_model = popupView_view.findViewById(R.id.recycler_model);
        recycler_model.setLayoutManager(new LinearLayoutManager(getContext()));
        //默认第一轮
        mChangeTaskPersonRowcolAdapter = new ChangeTaskPersonRowcolAdapter(personDatas);
        mChangeTaskPersonRowcolAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CommandManageBean.CommandManageItem mCommandManageItem = personDatas.get(position);
                task_id = mCommandManageItem.getTask_id();
                person_id = mCommandManageItem.getPerson_id();
                currentRounds = mCommandManageItem.getTask_rounds();
                tv_name.setText(mCommandManageItem.getPerson_name());
                tv_id_number.setText(mCommandManageItem.getPerson_idno());
                tv_work_unit.setText(mCommandManageItem.getPerson_orga());
                tv_user_type.setText(mCommandManageItem.getPerson_role());
                Glide.with(mContext).load(mCommandManageItem.getPerson_head()).error(R.drawable.user_icon).placeholder(R.drawable.user_icon).into(iv_user_icon);
                window_equip_model.dismiss();
            }
        });
        recycler_model.setAdapter(mChangeTaskPersonRowcolAdapter);
        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        window_equip_model = new MyPopWindow(popupView_view, tv_name.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);
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
        window_equip_model.showAsDropDown(tv_name, 0, 0);
    }


    @Override
    public void onClick(View v) {
        if (v==tv_name){
            initPopMenu();
        }else if (v==tv_cancle){
            dismiss();
        }else if (v==tv_comfir){
            if (TextUtils.isEmpty(tv_name.getText().toString())){
                Toast.makeText(mContext,"请选择要调整的队员",Toast.LENGTH_SHORT).show();
            }else {
                if (TextUtils.isEmpty(mCommandManageItem.getTask_id())){
                    changeTaskPersonRowcol();
                }else {
                    exChangeTaskPersonRowcol();
                }
            }
        }
    }

    //调换组员与靶位
    public void exChangeTaskPersonRowcol(){
        String url = ApiUrl.TaskPersonApi.ExChangeTaskPersonRowcol;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("task_rounds",currentRounds);
        params.put("person_id",person_id);
        params.put("person_row",String.valueOf(person_row));
        params.put("person_col",String.valueOf(person_col));
        new BaseMode().GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mOnResultInterface.onSuccess(result);
                dismiss();
            }

            @Override
            public void onFailure(Object result) {
                mOnResultInterface.onFailure(result);
            }
        });
    }


    private void changeTaskPersonRowcol(){
        String url = ApiUrl.TaskPersonApi.ChangeTaskPersonRowcol;
        RequestParams params  = new RequestParams();
        params.put("task_id",task_id);
        params.put("task_rounds",currentRounds);
        params.put("person_id",person_id);
        params.put("person_row",String.valueOf(person_row));
        params.put("person_col",String.valueOf(person_col));
        new BaseMode().GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mOnResultInterface.onSuccess(result);
                dismiss();
            }

            @Override
            public void onFailure(Object result) {
                mOnResultInterface.onFailure(result);
            }
        });
    }

    public interface onResultInterface{

        void onSuccess(Object result);
        void onFailure(Object result);
    }
}
