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
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.TaskPersonListAdapter;
import com.tianfan.shooting.admin.mvp.presenter.TaskTeamPresenter;
import com.tianfan.shooting.admin.mvp.view.TaskTeamView;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.bean.TaskPersonBean;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.utills.Utils;
import com.tianfan.shooting.view.AddTaskPersonDialog;
import com.tianfan.shooting.view.EditTaskPersonDialog;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import me.rosuh.filepicker.config.FilePickerManager;

/**
 * 队员管理
 * CreateBy：lxf
 * CreateTime： 2020-02-24 11:51
 */
public class FraDuiyuan extends BaseFragment implements TaskTeamView {
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_return_home)
    ImageView iv_return_home;
    @BindView(R.id.iv_create)
    ImageView iv_create;
    @BindView(R.id.iv_ed_user)
    ImageView iv_ed_user;
    @BindView(R.id.input_by_excel)
    ImageView input_by_excel;
    @BindView(R.id.iv_delete)
    ImageView iv_delete;
    @BindView(R.id.duiyuan_listview)
    RecyclerView recyclerView;
    @BindView(R.id.smrefresh)
    SmartRefreshLayout smrefresh;
    @BindView(R.id.tv_slect)
    TextView tv_slect;
    private TaskTeamPresenter mTaskTeamPresenter;
    private TaskPersonListAdapter mTaskPersonListAdapter;
    private String task_id;
    private String task_name;
    private String task_rounds;
    private List<TaskPersonBean> mTaskPersonDatas = new ArrayList<>();
    private AddTaskPersonDialog mAddTaskPersonDialog;
    private String headUri;//头像url
    private EditTaskPersonDialog mEditTaskPersonDialog;

    public static final int IMPORT_TASK_PERSON = 1123;
    private int task_person_type = 2;

    public static FraDuiyuan getInstance(String task_id,String task_name,String task_rounds) {
        FraDuiyuan hf = new FraDuiyuan();
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
        return R.layout.frag_zl_dy;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        tv_title.setText(task_name+"-队员管理");
        smrefresh.setEnableLoadMore(false);
        smrefresh.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smrefresh.finishRefresh();
                mTaskTeamPresenter.findTaskPerson(task_id,task_rounds,false);
            }
        });

        mTaskPersonListAdapter = new TaskPersonListAdapter(mTaskPersonDatas);
        mTaskPersonListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TaskPersonBean mTaskPersonBean = mTaskPersonDatas.get(position);
                if (mTaskPersonBean.isSelect()) {
                    mTaskPersonBean.setSelect(false);
                } else {
                    mTaskPersonBean.setSelect(true);
                }
                mTaskPersonListAdapter.notifyDataSetChanged();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(mTaskPersonListAdapter);
        mTaskTeamPresenter = new TaskTeamPresenter(this);
    }

    @Override
    public void initData() {
        mTaskTeamPresenter.findTaskPerson(task_id,task_rounds,true);
    }

    private void onRefresh() {
        mTaskTeamPresenter.findTaskPerson(task_id,task_rounds,false);
    }

    @OnClick({R.id.iv_return_home, R.id.iv_create, R.id.iv_ed_user, R.id.input_by_excel, R.id.iv_delete})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_return_home:
                getActivity().finish();
                break;
            case R.id.iv_create:
                mAddTaskPersonDialog = new AddTaskPersonDialog(mActivity, new AddTaskPersonDialog.ClickConfirInterface() {
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
                                } else if (code==2){//表示该靶位无人 直接新增队员
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
                break;
            case R.id.iv_ed_user:
                int seletCount = 0;
                int seletPosition = 0;
                for (int i = 0; i < mTaskPersonDatas.size(); i++) {
                    if (mTaskPersonDatas.get(i).isSelect()) {
                        seletPosition = i;
                        seletCount++;
                    }
                }
                if (seletCount == 0) {
                    showLoadFailMsg("请选择队员");
                } else if (seletCount > 1) {
                    showLoadFailMsg("一次只能修改一个队员信息");
                } else {
                    mEditTaskPersonDialog = new EditTaskPersonDialog(mActivity, mTaskPersonDatas.get(seletPosition),task_rounds, mTaskTeamPresenter);
                    mEditTaskPersonDialog.show();
                }
                break;
            case R.id.input_by_excel:
                FilePickerManager.INSTANCE.from(this).maxSelectable(1).forResult(IMPORT_TASK_PERSON);
                break;
            case R.id.iv_delete:

                List<String> personIds = new ArrayList<>();
                for (int i = 0; i < mTaskPersonDatas.size(); i++) {
                    if (mTaskPersonDatas.get(i).isSelect()) {
                        personIds.add(mTaskPersonDatas.get(i).getPerson_id());
                    }
                }
                if (personIds.size() == 0) {
                    showLoadFailMsg("请选择队员");
                } else {
                    SweetAlertDialogTools.ShowDialog(mActivity, "确定删除选中的队员吗？", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            mTaskTeamPresenter.removeTaskPerson(task_id, Utils.listToString(personIds));

                        }
                    });
                }
                break;
        }
    }


    @Override
    public void FindTaskPersonResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<TaskPersonBean> mDatas = JSONArray.parseArray(datas, TaskPersonBean.class);
            if (mTaskPersonDatas.size() > 0) {
                mTaskPersonDatas.clear();
            }
            mTaskPersonDatas.addAll(mDatas);
            mTaskPersonListAdapter.notifyDataSetChanged();
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
                        mTaskTeamPresenter.importTaskPersonGroup(task_id, tempFile);
                    }
                }
            }
        }
    }

    public interface AddUserCB {
        void cbData(JSONObject jsonObject);
    }
}
