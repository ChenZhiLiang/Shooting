package com.tianfan.shooting.admin.ui.fragment;

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
import com.tianfan.shooting.adapter.TaskEquipModelAdapter;
import com.tianfan.shooting.admin.mvp.presenter.TaskEquipModelPersenter;
import com.tianfan.shooting.admin.mvp.view.TaskEquipModelView;
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

    private String task_id;
    @BindView(R.id.tv_equip_model)
    TextView tv_equip_model;
    @BindView(R.id.recycler_equip)
    RecyclerView recycler_equip;
    @BindView(R.id.btn_add_task_equip)
    Button btn_add_task_equip;


    private List<TaskEquipBean> mTaskEquipDatas;
    private EquipModelAdapter mEquipModelAdapter;


    private View popupView_view;
    private RecyclerView recycler_model;
    private TaskEquipModelAdapter mTaskEquipModelAdapter;
    private TaskEquipModelPersenter mTaskEquipModelPersenter;
    private List<EquipModelBean> mEquipModelDatas = new ArrayList<>();
    private MyPopWindow window_equip_model;
    private EquipModelBean mEquipModelBean;

    public static EquipModelFragment getInstance(String task_id) {
        EquipModelFragment hf = new EquipModelFragment();
        hf.task_id = task_id;
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
        initPopMenu();
        recycler_equip.setLayoutManager(new LinearLayoutManager(mActivity));
        mTaskEquipDatas = new ArrayList<>();
        mEquipModelAdapter = new EquipModelAdapter(mTaskEquipDatas);
        mEquipModelAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TaskEquipBean mTaskEquipBean = mTaskEquipDatas.get(position);
                int count = mTaskEquipBean.getEquip_count();

                switch (view.getId()) {
                    case R.id.img_add:
                        count++;
                        mTaskEquipModelPersenter.editEquipModelItem(mTaskEquipBean.getEquip_model_type_id(), mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_type(),
                                mTaskEquipBean.getEquip_name(), mTaskEquipBean.getEquip_unit(), count, "1");
                        break;
                    case R.id.img_reduce:

                        if (count == 0) {
                            showLoadFailMsg("数量不能小于0");
                        } else {
                            count--;
                            mTaskEquipModelPersenter.editEquipModelItem(mTaskEquipBean.getEquip_model_type_id(), mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_type(),
                                    mTaskEquipBean.getEquip_name(), mTaskEquipBean.getEquip_unit(), count, "1");
                        }
                        break;
                    case R.id.btn_delete:
                        mTaskEquipModelPersenter.removeEquipModelItem(mTaskEquipBean.getEquip_model_item_id());
                        break;
                }
            }
        });
        recycler_equip.setAdapter(mEquipModelAdapter);
    }

    private void initPopMenu() {
        popupView_view = getLayoutInflater().inflate(R.layout.layout_popupwindow_equip, null);
        recycler_model = popupView_view.findViewById(R.id.recycler_model);
        recycler_model.setLayoutManager(new LinearLayoutManager(mActivity));
        mTaskEquipModelAdapter = new TaskEquipModelAdapter(mEquipModelDatas);
        mTaskEquipModelAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
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

    @Override
    public void initData() {
        mTaskEquipModelPersenter.findEquipModelType();
    }

    @OnClick({R.id.tv_equip_model, R.id.btn_add_task_equip, R.id.iv_return_home, R.id.iv_create, R.id.iv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_equip_model:
                WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
                lp.alpha = 0.7f;
                getActivity().getWindow().setAttributes(lp);
                window_equip_model.showAsDropDown(tv_equip_model, 0, 20);
                break;
            case R.id.iv_return_home:
                getActivity().finish();
                break;
            case R.id.iv_create:
                EquipModelDialog mEquipModelDialog = new EquipModelDialog(getContext(), new EquipModelDialog.onClickComfirInterface() {
                    @Override
                    public void onResult(String name, String desc) {
                        mTaskEquipModelPersenter.addEquipModelType(name,desc);

                    }
                });
                mEquipModelDialog.show();
                break;
            case R.id.iv_delete:
                if (mEquipModelBean == null) {
                    showLoadFailMsg("请选择器材模板");
                } else {
                    SweetAlertDialogTools.ShowDialog(mActivity, "确定删除该模板？", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            mTaskEquipModelPersenter.removeEquipModelType(mEquipModelBean.getEquip_model_type_id());
                        }
                    });
                }

                break;
            case R.id.btn_add_task_equip:
                AddTaskEquipDialog dialog = new AddTaskEquipDialog(getContext(), new AddTaskEquipDialog.onClickComfirInterface() {
                    @Override
                    public void onResult(String type, String name, String unit, String count) {
                        mTaskEquipModelPersenter.addEquipModelItem(mEquipModelBean.getEquip_model_type_id(), type, name, unit, count);
                    }
                });
                dialog.show();
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
            mTaskEquipModelAdapter.notifyDataSetChanged();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    /**
     *  @author
     *  @time
     *  @describe  添加器材模板
     */
    @Override
    public void addEquipModelTypeResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("添加成功");
            mTaskEquipModelPersenter.findEquipModelType();

        }else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    /**
     *  @author
     *  @time
     *  @describe 删除器材模板
     */
    @Override
    public void removeEquipModelTypeResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("删除成功");
            mTaskEquipModelPersenter.findEquipModelType();
            tv_equip_model.setText("请选择模板类型");
            btn_add_task_equip.setVisibility(View.GONE);
            if (mTaskEquipDatas.size() > 0) {
                mTaskEquipDatas.clear();
            }
            mEquipModelAdapter.notifyDataSetChanged();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    /**
     * @author
     * @time
     * @describe 添加器材模板项目
     */
    @Override
    public void addEquipModelItemResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("添加成功");
            mTaskEquipModelPersenter.findEquipModelItem(mEquipModelBean.getEquip_model_type_id());
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    /**
     * @author
     * @time
     * @describe 删除器材模板项目
     */
    @Override
    public void removeEquipModelItemResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("删除成功");
            mTaskEquipModelPersenter.findEquipModelItem(mEquipModelBean.getEquip_model_type_id());
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    /**
     * @author
     * @time
     * @describe 查找器材模板项目
     */
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
            btn_add_task_equip.setVisibility(View.VISIBLE);
            mTaskEquipDatas.addAll(mDatas);
            mEquipModelAdapter.notifyDataSetChanged();
        } else if (code == 2) {//无数据
            btn_add_task_equip.setVisibility(View.VISIBLE);
            mTaskEquipDatas.clear();
            mEquipModelAdapter.notifyDataSetChanged();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    /**
     * @author
     * @time
     * @describe 修改器材模板项目
     */
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
