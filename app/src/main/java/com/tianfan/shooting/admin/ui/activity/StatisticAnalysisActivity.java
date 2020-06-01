package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.StatisticAnalysisTaskListAdapter;
import com.tianfan.shooting.admin.mvp.presenter.StatisticAnalysisPersenter;
import com.tianfan.shooting.admin.mvp.view.StatisticAnalysisView;
import com.tianfan.shooting.bean.TaskInfoBean;
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
 * @Description：统计分析
 * @Author：Chen
 * @Date：2020/6/1 23:56
 * 修改人：Chen
 * 修改时间：2020/6/1 23:56
 */
public class StatisticAnalysisActivity extends AppCompatActivity implements StatisticAnalysisView {

    @BindView(R.id.iv_return_home)
    ImageView iv_return_home;

    @BindView(R.id.recycler_task)
    RecyclerView recyclerTask;
    public LoadingDialog mLoadingDialog;
    private StatisticAnalysisPersenter mStatisticAnalysisPersenter;
    private StatisticAnalysisTaskListAdapter mStatisticAnalysisTaskListAdapter;
    List<TaskInfoBean> mTaskInfos = new ArrayList();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic_analysis);
        ButterKnife.bind(this);
        mLoadingDialog = new LoadingDialog(this, R.style.TransparentDialog);
        mLoadingDialog.setTitle("加载中");
        mLoadingDialog.setCancelable(false);
        initView();
    }

    private void initView() {
        mStatisticAnalysisPersenter = new StatisticAnalysisPersenter(this);
        recyclerTask.setLayoutManager(new LinearLayoutManager(this));
        mStatisticAnalysisTaskListAdapter = new StatisticAnalysisTaskListAdapter(mTaskInfos);
        recyclerTask.setAdapter(mStatisticAnalysisTaskListAdapter);
        mStatisticAnalysisPersenter.findTaskInfo();
    }

    @OnClick({R.id.iv_return_home})
    public void onClick(View v) {
        if (v == iv_return_home) {
            finish();
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
            mStatisticAnalysisTaskListAdapter.notifyDataSetChanged();

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
