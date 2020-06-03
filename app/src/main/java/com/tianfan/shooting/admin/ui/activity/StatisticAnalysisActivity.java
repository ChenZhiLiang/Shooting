package com.tianfan.shooting.admin.ui.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.StatisticAnalysisTaskListAdapter;
import com.tianfan.shooting.admin.mvp.presenter.StatisticAnalysisPersenter;
import com.tianfan.shooting.admin.mvp.view.StatisticAnalysisView;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.view.LoadingDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

    @BindView(R.id.edit_task_name)
    EditText edit_task_name;
    @BindView(R.id.edit_task_place)
    EditText edit_task_place;
    @BindView(R.id.tv_select_time)
    TextView tv_select_time;
    @BindView(R.id.sp_select_type)
    Spinner sp_select_type;
    @BindView(R.id.btn_reset)
    Button btn_reset;
    @BindView(R.id.btn_query_task)
    Button btn_query_task;
    @BindView(R.id.recycler_task)
    RecyclerView recyclerTask;
    public LoadingDialog mLoadingDialog;
    private StatisticAnalysisPersenter mStatisticAnalysisPersenter;
    private StatisticAnalysisTaskListAdapter mStatisticAnalysisTaskListAdapter;
    List<TaskInfoBean> mTaskInfos = new ArrayList();
    private ArrayAdapter  adapter;
    private String selectType;

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
        //将可选内容与ArrayAdapter连接起来
        adapter = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        sp_select_type.setAdapter(adapter);
        sp_select_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectType = adapter.getItem(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        mStatisticAnalysisPersenter = new StatisticAnalysisPersenter(this);
        recyclerTask.setLayoutManager(new LinearLayoutManager(this));
        mStatisticAnalysisTaskListAdapter = new StatisticAnalysisTaskListAdapter(mTaskInfos);
        mStatisticAnalysisTaskListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TaskInfoBean mTaskInfoBean = mTaskInfos.get(position);
                startActivity(new Intent(StatisticAnalysisActivity.this,TaskPersonScoreActivity.class).putExtra("TaskInfoBean",mTaskInfoBean));
            }
        });
        recyclerTask.setAdapter(mStatisticAnalysisTaskListAdapter);
        mStatisticAnalysisPersenter.findTaskInfo();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @OnClick({R.id.iv_return_home,R.id.tv_select_time,R.id.btn_reset,R.id.btn_query_task})
    public void onClick(View v) {
        if (v == iv_return_home) {
            finish();
        }else if (v==tv_select_time){
            DatePickerDialog datePicker = new DatePickerDialog(StatisticAnalysisActivity.this);
            datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String result = "" + year + "-" + (month + 1) + "-" + dayOfMonth;
                    tv_select_time.setText(result);
                }
            });
            datePicker.show();
        }else if (v==btn_reset){
            edit_task_name.setText("");
            edit_task_place.setText("");
            selectType = "";
            tv_select_time.setText("");
        }else if (v==btn_query_task){
            int task_target_type;
            if (TextUtils.isEmpty(selectType)||TextUtils.equals(selectType,"全部类型")){
                task_target_type = 0;
            }else if (TextUtils.equals(selectType,"胸环靶")){
                task_target_type = 1;
            }else {
                task_target_type = 2;
            }
            mStatisticAnalysisPersenter.findTaskInfo(edit_task_name.getText().toString(),edit_task_place.getText().toString(),task_target_type,tv_select_time.getText().toString());
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

        } else if (code==2){
            if (mTaskInfos.size() > 0) {
                mTaskInfos.clear();
            }
            mStatisticAnalysisTaskListAdapter.notifyDataSetChanged();
            showLoadFailMsg(jsonObject.getString("message"));

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

        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }
}
