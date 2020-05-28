package com.tianfan.shooting.admin.taskdata;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.TaskEquipListAdapter;
import com.tianfan.shooting.adapter.TaskEquipModelAdapter;
import com.tianfan.shooting.admin.mvp.presenter.TaskEquipPresenter;
import com.tianfan.shooting.admin.mvp.view.TaskEquipView;
import com.tianfan.shooting.admin.ui.activity.EquipModeDetailActivity;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.bean.EquipModelBean;
import com.tianfan.shooting.bean.EquipTypeBean;
import com.tianfan.shooting.bean.TaskEquipBean;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.utills.Utils;
import com.tianfan.shooting.view.AddTaskEquipDialog;
import com.tianfan.shooting.view.EditTaskEquipDialog;
import com.tianfan.shooting.view.MyPopWindow;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.rosuh.filepicker.config.FilePickerManager;

import static com.tianfan.shooting.admin.taskdata.FraDuiyuan.IMPORT_TASK_PERSON;

/**
 * CreateBy：lxf
 * CreateTime： 2020-02-24 11:52
 */
public class FragQiCai extends BaseFragment implements TaskEquipView{

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.input_by_excel)
    ImageView input_by_excel;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.image_equip_prepare)
    ImageView image_equip_prepare;
    @BindView(R.id.image_equip_ture)
    ImageView image_equip_ture;
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
    private String task_rounds;
    private TaskEquipPresenter mTaskEquipPresenter;
    private TaskEquipListAdapter mTaskEquipListAdapter;
    private List<TaskEquipBean> mTaskEquipDatas;

    private View popupView_view;
    private RecyclerView recycler_model;
    private TaskEquipModelAdapter mTaskEquipModelAdapter;
    private List<EquipModelBean> mEquipModelDatas = new ArrayList<>();
    private MyPopWindow window_equip_model;

    private EquipModelBean mEquipModelBean;
    private TaskInfoBean mTaskInfoBean;

    public static FragQiCai getInstance(String task_id,String task_name,String task_rounds) {
        FragQiCai hf = new FragQiCai();
        hf.task_id = task_id;
        hf.task_name = task_name;
        hf.task_rounds = task_rounds;
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
        mRecyclerEquip.setLayoutManager(new LinearLayoutManager(getContext()));
        mTaskEquipDatas = new ArrayList<>();
        mTaskEquipListAdapter = new TaskEquipListAdapter(mTaskEquipDatas);
        mTaskEquipListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (TextUtils.equals(mTaskInfoBean.getTask_equips(),"2")){
                    showLoadFailMsg("器材已确认装车，不可修改");
                }else {
                    TaskEquipBean mTaskEquipBean = mTaskEquipDatas.get(position);
                    int count = mTaskEquipBean.getEquip_count();
                    int count_take = mTaskEquipBean.getEquip_count_take();
                    if (view.getId() == R.id.img_add) {
                        count++;
                        mTaskEquipPresenter.changeTaskEquipCount(task_id, mTaskEquipBean.getEquip_model_item_id(), count,count_take);
                    } else if (view.getId() == R.id.img_reduce) {
                        if (count==0){
                            showLoadFailMsg("应带数量不能小于0");
                        }else {
                            count--;
                            mTaskEquipPresenter.changeTaskEquipCount(task_id, mTaskEquipBean.getEquip_model_item_id(), count,count_take);
                        }
                    }else if (view.getId() == R.id.img_add_take_count){
                        count_take++;
                        mTaskEquipPresenter.changeTaskEquipCount(task_id, mTaskEquipBean.getEquip_model_item_id(), count,count_take);
                    }else if (view.getId() == R.id.img_reduce_take_count){
                        if (count_take==0){
                            showLoadFailMsg("实带数量不能小于0");
                        }else {
                            count_take--;
                            mTaskEquipPresenter.changeTaskEquipCount(task_id, mTaskEquipBean.getEquip_model_item_id(), count,count_take);
                        }
                    }else if (view.getId()==R.id.tv_equip_count){
                        new EditTaskEquipDialog(getContext(), mTaskEquipBean, new EditTaskEquipDialog.onClickComfirInterface() {
                            @Override
                            public void onResult(TaskEquipBean bean, String type, String name, String unit, String count, String count_take) {
                                mTaskEquipPresenter.changeTaskEquipCount(task_id, bean.getEquip_model_item_id(),
                                        Integer.parseInt(count),Integer.parseInt(count_take));
                            }
                        }).show();
                    }else if (view.getId()==R.id.tv_equip_take_count){
                        new EditTaskEquipDialog(getContext(), mTaskEquipBean, new EditTaskEquipDialog.onClickComfirInterface() {
                            @Override
                            public void onResult(TaskEquipBean bean, String type, String name, String unit, String count, String count_take) {
                                mTaskEquipPresenter.changeTaskEquipCount(task_id, bean.getEquip_model_item_id(),
                                       Integer.parseInt(count),Integer.parseInt(count_take));
                            }
                        }).show();
                    }
                }
            }
        });
        mRecyclerEquip.setAdapter(mTaskEquipListAdapter);
        initPopMenu();
    }

    @Override
    public void initData() {
        mTaskEquipPresenter.findTaskInfo(task_id);
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

                SweetAlertDialogTools.ShowDialog(getActivity(), "确定通过器材模板生成新的任务器材？", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mEquipModelBean = mEquipModelDatas.get(position);
                        tv_equip_model.setText(mEquipModelBean.getEquip_model_type_name());
                        mTaskEquipPresenter.createTaskEquip(task_id,mEquipModelBean.getEquip_model_type_id());
                        window_equip_model.dismiss();
                    }
                });
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
        mTaskEquipPresenter.findEquipModelType();
    }

    @OnClick({R.id.tv_equip_model, R.id.iv_back,R.id.input_by_excel, R.id.iv_clear,R.id.btn_add_task_equip,R.id.image_equip_prepare,R.id.image_equip_ture})
    public void onClick(View v) {
        if (v == tv_equip_model) {
            WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
            lp.alpha = 0.7f;
            getActivity().getWindow().setAttributes(lp);
            window_equip_model.showAsDropDown(tv_equip_model, 0, 20);
        } else if (v == iv_back) {
            getActivity().finish();
        } else if (v==input_by_excel){
            FilePickerManager.INSTANCE.from(this).maxSelectable(1).forResult(IMPORT_TASK_PERSON);
        }else if (v == iv_clear) {
            SweetAlertDialogTools.ShowDialog(getActivity(), "是否要清空器材？", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    if (TextUtils.equals(mTaskInfoBean.getTask_equips(),"2")){
                        showLoadFailMsg("器材已确认装车，不可清空");
                    }else {
                        mTaskEquipDatas.clear();
                        mTaskEquipListAdapter.notifyDataSetChanged();
                        tv_equip_model.setText("请选择器材模板");
                    }
                }
            });
        }else if (v==btn_add_task_equip){
            if (mEquipModelBean==null&&mTaskEquipDatas.size()==0){
                showLoadFailMsg("请选择器材模板");
            }else {
                mTaskEquipPresenter.findEquipType();
            }

        }else if (v==image_equip_prepare){
            if (mTaskEquipDatas.size()>0){
                SweetAlertDialogTools.ShowDialog(getActivity(), "确定清点器材数量？", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mTaskEquipPresenter.editTaskInfo(task_id,"1");
                    }
                });
            }else {
                showLoadFailMsg("请先添加任务器材");
            }


        }else if (v==image_equip_ture){
            SweetAlertDialogTools.ShowDialog(getActivity(), "装车器材确认？", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    mTaskEquipPresenter.editTaskInfo(task_id,"2");
                }
            });
        }
    }


    @Override
    public void findEquipTypeResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<EquipTypeBean>mEquipTypeDatas = JSONArray.parseArray(datas, EquipTypeBean.class);
            AddTaskEquipDialog dialog = new AddTaskEquipDialog(getContext(),mEquipTypeDatas, new AddTaskEquipDialog.onClickComfirInterface() {
                @Override
                public void onResult(String type, String name, String unit, String count,String count_take) {
                    if (mEquipModelBean!=null){
                        mTaskEquipPresenter.addTaskEquip(task_id,mEquipModelBean.getEquip_model_type_id(),type,name,unit,count,count_take);
                    }else {
                        mTaskEquipPresenter.addTaskEquip(task_id,mTaskEquipDatas.get(0).getEquip_model_type_id(),type,name,unit,count,count_take);
                    }
                }
            });
            dialog.show();
        }else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    /**
     *  @author
     *  @time
     *  @describe 查询任务信息
     */
    @Override
    public void findTaskInfoResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code==1){
            String datas = jsonObject.getString("datas");
            List<TaskInfoBean> mDatas = JSONArray.parseArray(datas, TaskInfoBean.class);
            if (mDatas.size()>0){
                mTaskInfoBean = mDatas.get(0);
                if (TextUtils.equals("0",mTaskInfoBean.getTask_equips())){
                    image_equip_prepare.setEnabled(true);
                    image_equip_prepare.setBackground(getResources().getDrawable(R.mipmap.counting_equip_false));
                    image_equip_ture.setEnabled(false);
                    image_equip_ture.setBackground(getResources().getDrawable(R.mipmap.truck_equip_false));

                }else if (TextUtils.equals("1",mTaskInfoBean.getTask_equips())){
                    image_equip_prepare.setEnabled(true);
                    image_equip_prepare.setBackground(getResources().getDrawable(R.mipmap.counting_equip_true));
                    image_equip_ture.setEnabled(true);
                    image_equip_ture.setBackground(getResources().getDrawable(R.mipmap.truck_equip_false));
                }else {
                    image_equip_prepare.setEnabled(false);
                    image_equip_prepare.setBackground(getResources().getDrawable(R.mipmap.counting_equip_true));
                    image_equip_ture.setEnabled(false);
                    image_equip_ture.setBackground(getResources().getDrawable(R.mipmap.truck_equip_ture));
                    tv_equip_model.setEnabled(false);
                    btn_add_task_equip.setVisibility(View.GONE);
                }
            }
        }else {
            showLoadFailMsg(jsonObject.getString("message"));

        }
    }

    /**
     *  @author
     *  @time
     *  @describe 查找器材模板
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
     *  @describe 查找任务器材
     */
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

    /**
     *  @author
     *  @time
     *  @describe 创建任务器材
     */
    @Override
    public void createTaskEquip(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("生成任务器材成功");
            mTaskEquipPresenter.findTaskEquip(task_id);
        } else {
            showLoadFailMsg("生成任务器材失败");
        }
    }

    /**
     *  @author
     *  @time
     *  @describe 添加任务器材
     */
    @Override
    public void addTaskEquipResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            mTaskEquipPresenter.findTaskEquip(task_id);
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    /**
     *  @author
     *  @time
     *  @describe 改变器材项数量
     */
    @Override
    public void changeTaskEquipCountResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            mTaskEquipPresenter.findTaskEquip(task_id);
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    /**
     *  @author
     *  @time
     *  @describe  修改任务（器材流程）
     */
    @Override
    public void editTaskInfoResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("任务器材流程修改成功");
            mTaskEquipPresenter.findTaskInfo(task_id);
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void importTaskEquipResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg(jsonObject.getString("message"));
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
                        mTaskEquipPresenter.importTaskEquip(task_id, tempFile);
                    }
                }
            }
        }
    }
}
