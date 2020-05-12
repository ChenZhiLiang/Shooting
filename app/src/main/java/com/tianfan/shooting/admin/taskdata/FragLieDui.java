package com.tianfan.shooting.admin.taskdata;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.TaskRankListAdapter;
import com.tianfan.shooting.admin.mvp.presenter.TaskRankPresenter;
import com.tianfan.shooting.admin.mvp.presenter.TaskTeamPresenter;
import com.tianfan.shooting.admin.mvp.view.TaskRankView;
import com.tianfan.shooting.admin.mvp.view.TaskTeamView;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.bean.TaskPersonBean;
import com.tianfan.shooting.bean.TaskRankBean;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.utills.Utils;
import com.tianfan.shooting.view.AddTaskPersonDialog;
import com.tianfan.shooting.view.EditTaskPersonDialog;
import com.tianfan.shooting.view.hrecycler.HRecyclerView;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;
import me.rosuh.filepicker.config.FilePickerManager;

import static com.tianfan.shooting.admin.taskdata.FraDuiyuan.IMPORT_TASK_PERSON;

/**
 * 队列管理
 * CreateBy：lxf
 * CreateTime： 2020-02-24 11:51
 */
public class FragLieDui extends BaseFragment implements TaskTeamView , TaskRankView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.input_by_excel)
    ImageView input_by_excel;
    @BindView(R.id.iv_init_liedui)
    ImageView iv_init_liedui;
    @BindView(R.id.iv_clear)
    ImageView iv_clear;
    @BindView(R.id.hrecyclerview)
    HRecyclerView mHrecyclerview;

    private List<TaskRankBean> mTaskRankDatas = new ArrayList<>();

    private String task_id;
    private String task_name;
    private String task_rounds;
    private TaskRankPresenter mTaskRankPresenter;
    private TaskTeamPresenter mTaskTeamPresenter;
    private TaskRankListAdapter mTaskRankListAdapter;
    String[] headerListData;
    private int task_person_type = 2;
    private AddTaskPersonDialog mAddTaskPersonDialog;
    private String headUri;//头像url
    private EditTaskPersonDialog mEditTaskPersonDialog;
    public static FragLieDui getInstance(String task_id,String task_name,String task_rounds) {
        FragLieDui hf = new FragLieDui();
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
        return R.layout.fragment_lie_dui;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        tv_title.setText(task_name+"-列队管理");
        mTaskTeamPresenter = new TaskTeamPresenter(this);
        mTaskRankPresenter = new TaskRankPresenter(this);
        headerListData = new String[10];
        for (int i = 0; i < 10; i++) {
            headerListData[i] = (i+1)+ "号靶";
        }
        mHrecyclerview.setHeaderListData(headerListData);

    }

    @Override
    public void initData() {
        mTaskTeamPresenter.recordTaskPersonScore(task_id,task_rounds,true);
    }
    private void onRefresh() {
        mTaskTeamPresenter.recordTaskPersonScore(task_id,task_rounds,false);
    }


    @Override
    public void FindTaskPersonResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            JSONArray jsonArray = JSONArray.parseArray(datas);
            mTaskRankDatas.clear();
            for (int i = 0;i<jsonArray.size();i++){
                JSONObject itemsObject = JSONObject.parseObject(jsonArray.get(i).toString());
                String items = itemsObject.getString(String.valueOf(i+1));
                List<TaskPersonBean> mDatas = JSONArray.parseArray(items, TaskPersonBean.class);
                if (mDatas.size() > 0) {
                    //当前最大数
                    int maxPersonCol = mDatas.get(mDatas.size() - 1).getPerson_col();
                    for (int j = 0; j < maxPersonCol; j++) {
                        int config = j + 1;
                        if (mDatas.get(j).getPerson_col() != config) {
                            mDatas.add(j, new TaskPersonBean());
                        }
                    }
                }
                TaskRankBean mTaskRankBean = new TaskRankBean();
                mTaskRankBean.setDatas(mDatas);
                mTaskRankDatas.add(mTaskRankBean);
            }

            mTaskRankListAdapter = new TaskRankListAdapter(mActivity, mTaskRankDatas, new TaskRankListAdapter.OnItemClickListener() {
                @Override
                public void onItemChildClick(int parentPostion, int childPosition) {
                    TaskPersonBean data =  mTaskRankDatas.get(parentPostion).getDatas().get(childPosition);
                    //添加
                    if (data!=null&& TextUtils.isEmpty(data.getTask_id())){
                        int person_row  = parentPostion+1;//行数
                        int person_col  = childPosition + 1;//列数
                        mAddTaskPersonDialog = new AddTaskPersonDialog(mActivity, task_person_type,person_row,person_col, new AddTaskPersonDialog.ClickConfirInterface() {
                            @Override
                            public void onResult(String person_name, String person_idno, String person_orga, String person_role, String person_row, String person_col, String picUri) {
                                headUri = picUri;
                                //查询该分组和靶位是否有人
                                mTaskTeamPresenter.queryTaskPerson(task_id, "", person_row, person_col, new ResultCallback() {
                                    @Override
                                    public void onSuccess(Object result) {
                                        JSONObject jsonObject = JSONObject.parseObject(result.toString());
                                        int code = jsonObject.getIntValue("code");
                                        if (code == 1) {
                                            String datas = jsonObject.getString("datas");
                                            List<TaskPersonBean> mDatas = JSONArray.parseArray(datas, TaskPersonBean.class);
                                            if (mDatas.size()>0){
                                                showLoadFailMsg("该分组的靶位已有人，请重新输入分组或者靶位");
                                            }else {
                                                mTaskTeamPresenter.addTaskPerson(task_id, person_idno, person_name, person_orga, person_role, person_row, person_col);

                                            }
                                        } else if (code==2){
                                            mTaskTeamPresenter.addTaskPerson(task_id, person_idno, person_name, person_orga, person_role, person_row, person_col);

                                        }else {
                                            Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                    @Override
                                    public void onFailure(Object result) {
                                        showLoadFailMsg(result.toString());
                                    }
                                });

                            }
                        });
                        mAddTaskPersonDialog.show();
                    }else {//修改
                        mEditTaskPersonDialog = new EditTaskPersonDialog(mActivity, data,task_rounds, mTaskTeamPresenter);
                        mEditTaskPersonDialog.show();
                    }

                }
            });
            mHrecyclerview.setAdapter(mTaskRankListAdapter);
            mTaskRankListAdapter.notifyDataSetChanged();

        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void AddTaskPersonResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            //headUri 为空表示 不用上传头像
            if (TextUtils.isEmpty(headUri)) {
                if (mAddTaskPersonDialog.isShowing()) {
                    mAddTaskPersonDialog.dismiss();
                }
                showLoadFailMsg("添加成功");
                onRefresh();
            } else {
                String data = jsonObject.getString("data");
                TaskPersonBean personBean = JSONObject.parseObject(data, TaskPersonBean.class);
                if (personBean != null) {
                    File file = new File(headUri);
                    if (file.exists()) {
                        mTaskTeamPresenter.uploadTaskPersonHead(personBean.getTask_id(), personBean.getPerson_id(), file);
                    } else {
                        showLoadFailMsg("头像不存在");
                    }
                }
            }

        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void EditTaskPersonResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            if (mEditTaskPersonDialog.isShowing()) {
                mEditTaskPersonDialog.dismiss();
            }
            showLoadFailMsg("修改队员成功");
            onRefresh();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void RemoveTaskPersonResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            if (mEditTaskPersonDialog!=null&&mEditTaskPersonDialog.isShowing()) {
                mEditTaskPersonDialog.dismiss();
            }
            showLoadFailMsg("删除队员成功");
            onRefresh();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void UploadTaskPersonHeadResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            if (mAddTaskPersonDialog != null && mAddTaskPersonDialog.isShowing()) {
                mAddTaskPersonDialog.dismiss();
                showLoadFailMsg("添加成功");
            } else {
                showLoadFailMsg("队员更换头像成功");
            }
            onRefresh();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));

        }
    }

    @Override
    public void ImportTaskPersonGroupResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 0) {
            showLoadFailMsg(jsonObject.getString("message"));
            onRefresh();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));

        }
    }

    @Override
    public void SetTaskPersonStatusResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code==1){
            onRefresh();
        }else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void createTaskPersonRowcolResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            onRefresh();
        }
        showLoadFailMsg(jsonObject.getString("message"));
    }

    @Override
    public void ChangeTaskPersonRowcolResult(Object result) {

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

        Toast.makeText(mActivity,err,Toast.LENGTH_SHORT).show();
    }

    @OnClick({R.id.iv_back,R.id.iv_clear,R.id.iv_init_liedui,R.id.input_by_excel})
    public void onClick(View v) {
        if (v==iv_back){
            getActivity().finish();
        }else if (v==iv_init_liedui){
            //队员数据置入
            SweetAlertDialogTools.ShowDialog(getActivity(), "是否置入队员数据？", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    mTaskRankPresenter.createTaskPersonRowcol(task_id);
                }
            });

        }else if (v==input_by_excel){
            FilePickerManager.INSTANCE.from(this).maxSelectable(1).forResult(IMPORT_TASK_PERSON);

        }else if (v==iv_clear){


            SweetAlertDialogTools.ShowDialog(getActivity(), "是否要清空队列？", new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    List<String>personIds = new ArrayList<>();
                    for (int i = 0; i < mTaskRankDatas.size(); i++) {
                        for (int j=0;j<mTaskRankDatas.get(i).getDatas().size();j++){
                            if (!TextUtils.isEmpty(mTaskRankDatas.get(i).getDatas().get(j).getPerson_id())){
                                personIds.add(mTaskRankDatas.get(i).getDatas().get(j).getPerson_id());
                            }
                        }
                    }
                    mTaskTeamPresenter.removeTaskPerson(task_id, Utils.listToString(personIds));
                }
            });
        }
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
                        mTaskTeamPresenter.importTaskPersonGroup(task_id, tempFile);
                    }
                }
            }
        }
    }

}
