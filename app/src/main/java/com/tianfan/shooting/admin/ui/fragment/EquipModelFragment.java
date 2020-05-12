package com.tianfan.shooting.admin.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.EquipModelAdapter;
import com.tianfan.shooting.adapter.EquipModelListAdapter;
import com.tianfan.shooting.adapter.TaskEquipModelAdapter;
import com.tianfan.shooting.admin.mvp.presenter.TaskEquipModelPersenter;
import com.tianfan.shooting.admin.mvp.view.TaskEquipModelView;
import com.tianfan.shooting.admin.ui.activity.EquipModeDetailActivity;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.bean.EquipModelBean;
import com.tianfan.shooting.bean.TaskEquipBean;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.view.AddTaskEquipDialog;
import com.tianfan.shooting.view.EquipModelDialog;
import com.tianfan.shooting.view.MyPopWindow;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Name：Shooting
 * @Description：器材模板
 * @Author：Chen
 * @Date：2020/4/15 0:23
 * 修改人：Chen
 * 修改时间：2020/4/15 0:23
 */
public class EquipModelFragment extends BaseFragment implements TaskEquipModelView {

    @BindView(R.id.recycler_equip_mode)
    RecyclerView recycler_equip_mode;

    private EquipModelListAdapter mEquipModelListAdapter;
    private TaskEquipModelPersenter mTaskEquipModelPersenter;
    private List<EquipModelBean> mEquipModelDatas = new ArrayList<>();

    public static EquipModelFragment getInstance() {
        EquipModelFragment hf = new EquipModelFragment();
        return hf;
    }

    @Override
    public void loadingData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_equip_model;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mTaskEquipModelPersenter = new TaskEquipModelPersenter(this);

        recycler_equip_mode.setLayoutManager(new LinearLayoutManager(mActivity));
        mEquipModelListAdapter = new EquipModelListAdapter(mEquipModelDatas);
        recycler_equip_mode.setAdapter(mEquipModelListAdapter);
        mEquipModelListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                EquipModelBean mEquipModelBean = mEquipModelDatas.get(position);
                startActivity(new Intent(mActivity, EquipModeDetailActivity.class).putExtra("equip_model_type_id", mEquipModelBean.getEquip_model_type_id()));
            }
        });
    }

    @Override
    public void initData() {
        mTaskEquipModelPersenter.findEquipModelType();
    }

    @OnClick({R.id.tv_equip_model, R.id.btn_add_task_equip, R.id.iv_return_home, R.id.iv_create, R.id.iv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_home:
                getActivity().finish();
                break;
            case R.id.iv_create:
                EquipModelDialog mEquipModelDialog = new EquipModelDialog(getContext(), new EquipModelDialog.onClickComfirInterface() {
                    @Override
                    public void onResult(String name, String desc) {
                        mTaskEquipModelPersenter.addEquipModelType(name, desc);

                    }
                });
                mEquipModelDialog.show();
                break;
            case R.id.iv_delete:

                if (mEquipModelListAdapter.getSelectedPos() == -1) {
                    Toast.makeText(getContext(), "请选择要删除的器材模板", Toast.LENGTH_SHORT).show();
                } else {
                    SweetAlertDialogTools.ShowDialog(mActivity, "确定删除选中的器材模板吗？", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            mTaskEquipModelPersenter.removeEquipModelType(mEquipModelDatas.get(mEquipModelListAdapter.getSelectedPos()).getEquip_model_type_id());
                        }
                    });
                }
                break;
        }
    }

    /**
     * @author
     * @time
     * @describe 查找器材模板类型
     */
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
            mEquipModelListAdapter.notifyDataSetChanged();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    /**
     * @author
     * @time
     * @describe 添加器材模板
     */
    @Override
    public void addEquipModelTypeResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("添加成功");
            mTaskEquipModelPersenter.findEquipModelType();

        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    /**
     * @author
     * @time
     * @describe 删除器材模板
     */
    @Override
    public void removeEquipModelTypeResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("删除成功");
            mTaskEquipModelPersenter.findEquipModelType();
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
}
