package com.tianfan.shooting.admin.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.tianfan.shooting.bean.EquipTypeBean;
import com.tianfan.shooting.bean.TaskEquipBean;
import com.tianfan.shooting.utills.Utils;
import com.tianfan.shooting.view.AddTaskEquipDialog;
import com.tianfan.shooting.view.EditTaskEquipDialog;
import com.tianfan.shooting.view.LoadingDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rosuh.filepicker.config.FilePickerManager;

import static com.tianfan.shooting.admin.taskdata.FraDuiyuan.IMPORT_TASK_PERSON;

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
    @BindView(R.id.input_by_excel)
    ImageView input_by_excel;
    @BindView(R.id.recycler_equip_mode)
    RecyclerView recycler_equip_mode;
    @BindView(R.id.btn_add_equip_item)
    Button btn_add_equip_item;

    private String equip_model_type_id;
    private List<TaskEquipBean> mTaskEquipDatas = new ArrayList<>();
    private EquipModelAdapter mEquipModelAdapter;
    private EquipModeDetailPersenter mEquipModeDetailPersenter;

    public LoadingDialog mLoadingDialog;
    private List<EquipTypeBean> mEquipTypeDatas = new ArrayList<>();

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
        mEquipModeDetailPersenter = new EquipModeDetailPersenter(this);

        recycler_equip_mode.setLayoutManager(new LinearLayoutManager(this));
        mEquipModelAdapter = new EquipModelAdapter(mTaskEquipDatas);
        recycler_equip_mode.setAdapter(mEquipModelAdapter);
        mEquipModelAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TaskEquipBean mTaskEquipBean = mTaskEquipDatas.get(position);
                int count = mTaskEquipBean.getEquip_count();
                int count_take = mTaskEquipBean.getEquip_count_take();
                switch (view.getId()) {
                    case R.id.tv_equip_count:
                        new EditTaskEquipDialog(EquipModeDetailActivity.this, mTaskEquipBean, new EditTaskEquipDialog.onClickComfirInterface() {
                            @Override
                            public void onResult(TaskEquipBean bean, String type, String name, String unit, String count, String count_take) {
                                mEquipModeDetailPersenter.editEquipModelItem(bean.getEquip_model_type_id(), bean.getEquip_model_item_id(), type,
                                        name, unit, Integer.parseInt(count),Integer.parseInt(count_take) , "1");
                            }
                        }).show();
                        break;
                    case R.id.img_add:
                        count++;
                        mEquipModeDetailPersenter.editEquipModelItem(mTaskEquipBean.getEquip_model_type_id(), mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_type(),
                                mTaskEquipBean.getEquip_name(), mTaskEquipBean.getEquip_unit(), count, count_take, "1");
                        break;
                    case R.id.img_reduce:

                        if (count == 0) {
                            showLoadFailMsg("数量不能小于0");
                        } else {
                            count--;
                            mEquipModeDetailPersenter.editEquipModelItem(mTaskEquipBean.getEquip_model_type_id(), mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_type(),
                                    mTaskEquipBean.getEquip_name(), mTaskEquipBean.getEquip_unit(), count, count_take, "1");
                        }
                        break;


                    case R.id.tv_equip_count_take:
                        new EditTaskEquipDialog(EquipModeDetailActivity.this, mTaskEquipBean, new EditTaskEquipDialog.onClickComfirInterface() {
                            @Override
                            public void onResult(TaskEquipBean bean, String type, String name, String unit, String count, String count_take) {
                                mEquipModeDetailPersenter.editEquipModelItem(bean.getEquip_model_type_id(), bean.getEquip_model_item_id(), type,
                                        name, unit, Integer.parseInt(count),Integer.parseInt(count_take) , "1");
                            }
                        }).show();
                        break;
                    case R.id.img_count_take_add:

                        count_take++;
                        mEquipModeDetailPersenter.editEquipModelItem(mTaskEquipBean.getEquip_model_type_id(), mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_type(),
                                mTaskEquipBean.getEquip_name(), mTaskEquipBean.getEquip_unit(), count, count_take, "1");
                        break;
                    case R.id.img_count_take_reduce:
                        if (count_take == 0) {
                            showLoadFailMsg("数量不能小于0");
                        } else {
                            count--;
                            mEquipModeDetailPersenter.editEquipModelItem(mTaskEquipBean.getEquip_model_type_id(), mTaskEquipBean.getEquip_model_item_id(), mTaskEquipBean.getEquip_type(),
                                    mTaskEquipBean.getEquip_name(), mTaskEquipBean.getEquip_unit(), count, count_take, "1");
                        }
                    case R.id.btn_delete:
                        mEquipModeDetailPersenter.removeEquipModelItem(mTaskEquipBean.getEquip_model_item_id());
                        break;
                }
            }
        });
    }

    private void initData() {
        mEquipModeDetailPersenter.findEquipType();

        mEquipModeDetailPersenter.findEquipModelItem(equip_model_type_id);
    }

    @OnClick({R.id.iv_return_home, R.id.btn_add_equip_item, R.id.input_by_excel})
    public void onClick(View v) {
        if (v == iv_return_home) {
            finish();
        } else if (v == btn_add_equip_item) {
            AddTaskEquipDialog dialog = new AddTaskEquipDialog(this, mEquipTypeDatas, new AddTaskEquipDialog.onClickComfirInterface() {
                @Override
                public void onResult(String type, String name, String unit, String count,String count_take) {
                    mEquipModeDetailPersenter.addEquipModelItem(equip_model_type_id, type, name, unit, count,count_take);
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    btn_add_equip_item.setVisibility(View.VISIBLE);
                }
            });
            btn_add_equip_item.setVisibility(View.GONE);
            dialog.show();
        } else if (v == input_by_excel) {
            FilePickerManager.INSTANCE.from(this).maxSelectable(1).forResult(IMPORT_TASK_PERSON);
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
    public void findEquipTypeResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            mEquipTypeDatas = JSONArray.parseArray(datas, EquipTypeBean.class);
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
    public void ImportEquipModelItemResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg(jsonObject.getString("message"));
            mEquipModeDetailPersenter.findEquipModelItem(equip_model_type_id);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("requestCode", "----" + requestCode);
        Log.e("resultCode", "----" + resultCode);
        if (requestCode == IMPORT_TASK_PERSON) {
            if (FilePickerManager.INSTANCE.obtainData().size() > 0) {
                String result = FilePickerManager.INSTANCE.obtainData().get(0);
                File file = new File(result);
                if (file.exists()) {
                    File tempFile = Utils.nioTransferCopy(file);
                    if (tempFile.exists()) {
                        mEquipModeDetailPersenter.importEquipModelItem(equip_model_type_id, tempFile);
                    }
                }
            }
        }
    }
}
