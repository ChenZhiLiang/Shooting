package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.EquipModelAdapter;
import com.tianfan.shooting.admin.mvp.presenter.EquipModeDetailPersenter;
import com.tianfan.shooting.admin.mvp.view.EquipModeDetailView;
import com.tianfan.shooting.bean.TaskEquipBean;
import com.tianfan.shooting.view.AddTaskEquipDialog;
import com.tianfan.shooting.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Name：Shooting
 * @Description：器材模板详情
 * @Author：Chen
 * @Date：2020/4/24 23:45
 * 修改人：Chen
 * 修改时间：2020/4/24 23:45
 */
public class EquipModeDetailActivity extends AppCompatActivity implements EquipModeDetailView {

    @BindView(R.id.iv_return_home)
    ImageView iv_return_home;
    @BindView(R.id.recycler_equip_mode)
    RecyclerView recycler_equip_mode;
    @BindView(R.id.btn_add_equip_item)
    Button btn_add_equip_item;

    private String equip_model_type_id;
    private List<TaskEquipBean> mTaskEquipDatas = new ArrayList<>();
    private EquipModelAdapter mEquipModelAdapter;
    private EquipModeDetailPersenter mEquipModeDetailPersenter;

    public LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equip_mode_detail);
        ButterKnife.bind(this);
        equip_model_type_id = getIntent().getStringExtra("equip_model_type_id");
        mLoadingDialog = new LoadingDialog(this, R.style.TransparentDialog);
        mLoadingDialog.setTitle("加载中");
        mLoadingDialog.setCancelable(false);
        initView();
        initData();
    }

    private void initView() {
        recycler_equip_mode.setLayoutManager(new LinearLayoutManager(this));
        mEquipModelAdapter = new EquipModelAdapter(mTaskEquipDatas);
        recycler_equip_mode.setAdapter(mEquipModelAdapter);
        mEquipModeDetailPersenter = new EquipModeDetailPersenter(this);
        mEquipModelAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TaskEquipBean mTaskEquipBean = mTaskEquipDatas.get(position);
                int count = mTaskEquipBean.getEquip_count();
                switch (view.getId()) {
                    case R.id.img_add:
                        count++;
                        mEquipModeDetailPersenter.editEquipModelItem(mTaskEquipBean.getEquip_model_type_id(), mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_type(),
                                mTaskEquipBean.getEquip_name(), mTaskEquipBean.getEquip_unit(), count, "1");
                        break;
                    case R.id.img_reduce:

                        if (count == 0) {
                            showLoadFailMsg("数量不能小于0");
                        } else {
                            count--;
                            mEquipModeDetailPersenter.editEquipModelItem(mTaskEquipBean.getEquip_model_type_id(), mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_type(),
                                    mTaskEquipBean.getEquip_name(), mTaskEquipBean.getEquip_unit(), count, "1");
                        }
                        break;
                    case R.id.btn_delete:
                        mEquipModeDetailPersenter.removeEquipModelItem(mTaskEquipBean.getEquip_model_item_id());
                        break;
                }
            }
        });
    }

    private void initData() {
        mEquipModeDetailPersenter.findEquipModelItem(equip_model_type_id);
    }

    @OnClick({R.id.iv_return_home,R.id.btn_add_equip_item})
    public void onClick(View v) {
        if (v==iv_return_home){
            finish();
        }if (v == btn_add_equip_item) {
            AddTaskEquipDialog dialog = new AddTaskEquipDialog(this, new AddTaskEquipDialog.onClickComfirInterface() {
                @Override
                public void onResult(String type, String name, String unit, String count) {
                    mEquipModeDetailPersenter.addEquipModelItem(equip_model_type_id, type, name, unit, count);
                }
            });
            dialog.show();
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
            mEquipModelAdapter.notifyDataSetChanged();
        } else if (code == 2) {//无数据
            mTaskEquipDatas.clear();
            mEquipModelAdapter.notifyDataSetChanged();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void addEquipModelItemResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("添加成功");
            initData();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void removeEquipModelItemResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("删除成功");
            initData();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void EditEquipModelItemResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            initData();
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

        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }
}
