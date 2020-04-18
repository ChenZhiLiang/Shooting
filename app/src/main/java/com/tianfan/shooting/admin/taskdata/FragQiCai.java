package com.tianfan.shooting.admin.taskdata;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.TaskEquipListAdapter;
import com.tianfan.shooting.adapter.TaskEquipModelAdapter;
import com.tianfan.shooting.admin.mvp.presenter.TaskEquipModelPersenter;
import com.tianfan.shooting.admin.mvp.presenter.TaskEquipPresenter;
import com.tianfan.shooting.admin.mvp.view.TaskEquipModelView;
import com.tianfan.shooting.admin.mvp.view.TaskEquipView;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.bean.EquipModelBean;
import com.tianfan.shooting.bean.TaskEquipBean;
import com.tianfan.shooting.utills.Utils;
import com.tianfan.shooting.view.AddTaskEquipDialog;
import com.tianfan.shooting.view.MyPopWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * CreateBy：lxf
 * CreateTime： 2020-02-24 11:52
 */
public class FragQiCai extends BaseFragment implements TaskEquipView, TaskEquipModelView {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.ll_equip)
    LinearLayout ll_equip;
    @BindView(R.id.tv_equip_model)
    TextView tv_equip_model;
    @BindView(R.id.recycler_equip)
    RecyclerView mRecyclerEquip;

    @BindView(R.id.btn_add_task_equip)
    Button btn_add_task_equip;
    private String task_id;
    private String task_name;
    private TaskEquipPresenter mTaskEquipPresenter;
    private TaskEquipListAdapter mTaskEquipListAdapter;
    private List<TaskEquipBean> mTaskEquipDatas = new ArrayList<>();

    private View popupView_view;
    private RecyclerView recycler_model;
    private TaskEquipModelAdapter mTaskEquipModelAdapter;
    private TaskEquipModelPersenter mTaskEquipModelPersenter;
    private List<EquipModelBean> mEquipModelDatas = new ArrayList<>();
    private MyPopWindow window_equip_model;

    boolean isTask = true;
    private EquipModelBean mEquipModelBean;

    public static FragQiCai getInstance(String task_id,String task_name) {
        FragQiCai hf = new FragQiCai();
        hf.task_id = task_id;
        hf.task_name = task_name;
        return hf;
    }

    @Override
    public void loadingData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.frag_zl_qc;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        tv_title.setText(task_name+"-器材管理");
        mTaskEquipPresenter = new TaskEquipPresenter(this);
        mTaskEquipModelPersenter = new TaskEquipModelPersenter(this);
        mRecyclerEquip.setLayoutManager(new LinearLayoutManager(getContext()));
        mTaskEquipListAdapter = new TaskEquipListAdapter(mTaskEquipDatas);
        mTaskEquipListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TaskEquipBean mTaskEquipBean = mTaskEquipDatas.get(position);

                if (view.getId() == R.id.img_add) {
                    int count = mTaskEquipBean.getEquip_count() + 1;
                    if (isTask) {
                        mTaskEquipPresenter.changeTaskEquipCount(task_id, mTaskEquipBean.getEquip_model_item_id(), count,mTaskEquipBean.getEquip_status());
                    } else {
                        mTaskEquipModelPersenter.editEquipModelItem(mTaskEquipBean.getEquip_model_type_id()
                                , mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_type(), mTaskEquipBean.getEquip_name()
                                , mTaskEquipBean.getEquip_unit(), count, mTaskEquipBean.getEquip_status());
                    }
                } else if (view.getId() == R.id.img_reduce) {
                    int count = mTaskEquipBean.getEquip_count() - 1;
                    if (isTask) {
                        mTaskEquipPresenter.changeTaskEquipCount(task_id, mTaskEquipBean.getEquip_model_item_id(), count,mTaskEquipBean.getEquip_status());
                    } else {
                        if (mTaskEquipBean.getEquip_count() == 0) {
                            showLoadFailMsg("数量不能小于0");
                        } else {
                            mTaskEquipModelPersenter.editEquipModelItem(mTaskEquipBean.getEquip_model_type_id()
                                    , mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_type()
                                    , mTaskEquipBean.getEquip_name(), mTaskEquipBean.getEquip_unit(), count, mTaskEquipBean.getEquip_status());
                        }
                    }
                }else if (view.getId()==R.id.check_equip_status){
                    if (isTask){
                        mTaskEquipPresenter.changeTaskEquipCount(task_id, mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_count(),TextUtils.equals(mTaskEquipBean.getEquip_status(),"0")?"1":"0");
                    }else {
                        mTaskEquipModelPersenter.editEquipModelItem(mTaskEquipBean.getEquip_model_type_id()
                                , mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_type()
                                , mTaskEquipBean.getEquip_name(), mTaskEquipBean.getEquip_unit(), mTaskEquipBean.getEquip_count(), TextUtils.equals(mTaskEquipBean.getEquip_status(),"0")?"1":"0");
                    }
                }
            }
        });
        mRecyclerEquip.setAdapter(mTaskEquipListAdapter);
        initPopMenu();
    }

    @Override
    public void initData() {
        mTaskEquipPresenter.findTaskEquip(task_id);
    }

    private void initPopMenu() {
        popupView_view = getLayoutInflater().inflate(R.layout.layout_popupwindow_equip, null);
        recycler_model = popupView_view.findViewById(R.id.recycler_model);
        recycler_model.setLayoutManager(new LinearLayoutManager(mActivity));
        mTaskEquipModelAdapter = new TaskEquipModelAdapter(mEquipModelDatas);
        mTaskEquipModelAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                isTask = false;
                mEquipModelBean = mEquipModelDatas.get(position);
                tv_equip_model.setText(mEquipModelBean.getEquip_model_type_name());
                mTaskEquipModelPersenter.findEquipModelItem(mEquipModelBean.getEquip_model_type_id());
                window_equip_model.dismiss();
            }
        });
        recycler_model.setAdapter(mTaskEquipModelAdapter);
        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        window_equip_model = new MyPopWindow(popupView_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window_equip_model.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 1f;
                getActivity().getWindow().setAttributes(lp);
            }
        });
        mTaskEquipModelPersenter.findEquipModelType();


    }

    @OnClick({R.id.tv_equip_model, R.id.iv_back, R.id.iv_clear,R.id.btn_add_task_equip})
    public void onClick(View v) {
        if (v == tv_equip_model) {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.7f;
            getActivity().getWindow().setAttributes(lp);
            window_equip_model.showAsDropDown(tv_equip_model, 0, 20);
        } else if (v == iv_back) {
            getActivity().finish();
        } else if (v == iv_clear) {
            new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE)
                    .setCancelText("取消")
                    .setConfirmText("是")
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            isTask = true;
                            mTaskEquipDatas.clear();
                            mTaskEquipListAdapter.notifyDataSetChanged();
                            tv_equip_model.setText("请选择器材模板");

                        }
                    })
                    .setTitleText("温馨提示")
                    .setContentText("是否要清空器材？")
                    .show();
        }else if (v==btn_add_task_equip){
            AddTaskEquipDialog dialog = new AddTaskEquipDialog(getContext());
            dialog.show();
        }
    }


    @Override
    public void findTaskEquipResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<TaskEquipBean> mDatas = JSONArray.parseArray(datas, TaskEquipBean.class);
            if (mTaskEquipDatas.size() > 0) {
                mTaskEquipDatas.clear();
            }
            mTaskEquipDatas.addAll(mDatas);
            mTaskEquipListAdapter.notifyDataSetChanged();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void createTaskEquip(Object result) {

    }

    @Override
    public void changeTaskEquipCount(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            mTaskEquipPresenter.findTaskEquip(task_id);
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void showProgress() {
        mLoadingDialog.show();
    }

    @Override
    public void hideProgress() {

        mLoadingDialog.dismiss();
    }

    @Override
    public void showLoadFailMsg(String err) {

        Toast.makeText(mActivity, err, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void findEquipModelTypeResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<EquipModelBean> mDatas = JSONArray.parseArray(datas, EquipModelBean.class);
            if (mEquipModelDatas.size() > 0) {
                mEquipModelDatas.clear();
            }
            mEquipModelDatas.addAll(mDatas);
            mTaskEquipModelAdapter.notifyDataSetChanged();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void findEquipModelItemResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<TaskEquipBean> mDatas = JSONArray.parseArray(datas, TaskEquipBean.class);
            if (mTaskEquipDatas.size() > 0) {
                mTaskEquipDatas.clear();
            }
            mTaskEquipDatas.addAll(mDatas);
            mTaskEquipListAdapter.notifyDataSetChanged();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void EditEquipModelItemResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            mTaskEquipModelPersenter.findEquipModelItem(mEquipModelBean.getEquip_model_type_id());
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }
}
