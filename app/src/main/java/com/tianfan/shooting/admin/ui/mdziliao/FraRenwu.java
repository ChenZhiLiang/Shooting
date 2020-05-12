package com.tianfan.shooting.admin.ui.mdziliao;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.TaskHistoryAdapter;
import com.tianfan.shooting.admin.mvp.presenter.TaskPresenter;
import com.tianfan.shooting.admin.mvp.view.TaskView;
import com.tianfan.shooting.admin.taskdata.TaskDataActivity;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.network.okhttp.request.RequestParams;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.view.CreateTaskDialog;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * 任务管理
 * CreateBy：lxf
 * CreateTime： 2020-02-24 11:51
 */
public class FraRenwu extends BaseFragment implements TaskView {
    @BindView(R.id.smrefresh)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rw_listview)
    RecyclerView recyclerView;
    List<TaskInfoBean> mTaskInfos = new ArrayList();
    TaskHistoryAdapter historyAdapter;
    private TaskPresenter mTaskPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.frag_zl_rw;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        smartRefreshLayout.setEnableLoadMore(false);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                loadingData();
                smartRefreshLayout.finishRefresh();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        historyAdapter = new TaskHistoryAdapter(mTaskInfos);
        recyclerView.setAdapter(historyAdapter);
        historyAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TaskInfoBean bean = mTaskInfos.get(position);
                Intent intent = new Intent(getContext(), TaskDataActivity.class);
                intent.putExtra("task_id", bean.getTask_id());
                intent.putExtra("task_name",bean.getTask_name());
                intent.putExtra("task_rounds",bean.getTask_rounds());
                startActivity(intent);
            }
        });
        mTaskPresenter = new TaskPresenter(this);
    }

    @Override
    public void initData() {
        mTaskPresenter.findTaskInfo();

    }

    @Override
    public void loadingData() {
    }

    @OnClick({R.id.img_back, R.id.iv_create, R.id.iv_delete, R.id.iv_edtor})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_back:
                getActivity().finish();
                break;
            case R.id.iv_create:
                CreateTaskDialog dialogCreateTask = new CreateTaskDialog(getActivity(), new CreateTaskDialog.CreateCallBack() {
                    @Override
                    public void result(RequestParams params) {
                        mTaskPresenter.addTaskInfo(params);
                    }
                });

                dialogCreateTask.show();
                break;
            case R.id.iv_delete:
                if (historyAdapter.getSelectedPos() == -1) {
                    Toast.makeText(getContext(), "请选择一条数据删除", Toast.LENGTH_SHORT).show();
                } else {
                    SweetAlertDialogTools.ShowDialog(getActivity(), "确定删除选中的任务吗？", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            mTaskPresenter.deleteTaskInfo(mTaskInfos.get(historyAdapter.getSelectedPos()).getTask_id());
                        }
                    });
                }
                break;
            case R.id.iv_edtor:
                if (historyAdapter.getSelectedPos() == -1) {
                    Toast.makeText(getContext(), "请选择一条数据进行操作", Toast.LENGTH_SHORT).show();
                } else {
                    CreateTaskDialog editDialogCreateTask = new CreateTaskDialog(getActivity(),mTaskInfos.get(historyAdapter.getSelectedPos()), new CreateTaskDialog.CreateCallBack() {
                        @Override
                        public void result(RequestParams params) {
                            mTaskPresenter.editTaskInfo(params);
                        }
                    });
                    editDialogCreateTask.show();
                }
                break;
        }
    }

    @Override
    public void FindTaskInfoResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<TaskInfoBean> mDatas = JSONArray.parseArray(datas, TaskInfoBean.class);
            if (mTaskInfos.size() > 0) {
                mTaskInfos.clear();
            }
            if (mDatas != null && mDatas.size() > 0) {
                for (int i = mDatas.size() - 1; i >= 0; i--) {
                    mTaskInfos.add(mDatas.get(i));
                }
            }
            historyAdapter.notifyDataSetChanged();

        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void AddTaskInfoResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("创建成功");
            initData();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void EditTaskInfoResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("修改成功");
            initData();
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void RemoveTaskInfoResult(Object result) {
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
    public void showProgress() {
        mLoadingDialog.show();
    }

    @Override
    public void hideProgress() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showLoadFailMsg(String err) {
        Toast.makeText(getContext(), err, Toast.LENGTH_SHORT).show();
    }
}
