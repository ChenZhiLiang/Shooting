package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
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
import com.tianfan.shooting.admin.mvp.view.TaskTeamView;
import com.tianfan.shooting.bean.TaskPersonBean;
import com.tianfan.shooting.bean.TaskRankBean;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.view.AddTaskPersonDialog;
import com.tianfan.shooting.view.EditTaskPersonDialog;
import com.tianfan.shooting.view.LoadingDialog;
import com.tianfan.shooting.view.hrecycler.HRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Name：Shooting
 * @Description：点名
 * @Author：Chen
 * @Date：2020/4/28 23:51
 * 修改人：Chen
 * 修改时间：2020/4/28 23:51
 */
public class CompleteNameActivity extends AppCompatActivity implements TaskTeamView {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.hrecyclerview)
    HRecyclerView mHrecyclerview;

    private List<TaskRankBean> mTaskRankDatas = new ArrayList<>();

    private String task_id;
    private String task_name;
    private TaskTeamPresenter mTaskTeamPresenter;
    private TaskRankListAdapter mTaskRankListAdapter;
    String[] headerListData;
    private EditTaskPersonDialog mEditTaskPersonDialog;
    public LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_name);
        ButterKnife.bind(this);
        mLoadingDialog = new LoadingDialog(this, R.style.TransparentDialog);
        mLoadingDialog.setTitle("加载中");
        mLoadingDialog.setCancelable(false);
        initView();
        initData();
    }

    private void initView(){
        mTaskTeamPresenter = new TaskTeamPresenter(this);
        headerListData = new String[10];
        for (int i = 0; i < 10; i++) {
            headerListData[i] = (i+1)+ "号靶";
        }
        mHrecyclerview.setHeaderListData(headerListData);
        task_id = getIntent().getStringExtra("task_id");
        task_name = getIntent().getStringExtra("task_name");
        tv_title.setText(task_name+"-点名");
    }

    private void initData(){
        mTaskTeamPresenter.recordTaskPersonScore(task_id,"1",true);
    }

    @OnClick({R.id.iv_back})
    public void onClick(View v){
        if (v==iv_back){
            finish();
        }
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

            mTaskRankListAdapter = new TaskRankListAdapter(this, mTaskRankDatas, new TaskRankListAdapter.OnItemClickListener() {
                @Override
                public void onItemChildClick(int parentPostion, int childPosition) {
                    TaskPersonBean data =  mTaskRankDatas.get(parentPostion).getDatas().get(childPosition);
                    mEditTaskPersonDialog = new EditTaskPersonDialog(CompleteNameActivity.this, data, "1",mTaskTeamPresenter);
                    mEditTaskPersonDialog.show();
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
            mTaskTeamPresenter.recordTaskPersonScore(task_id,"1",false);
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
            mTaskTeamPresenter.recordTaskPersonScore(task_id,"1",false);
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void UploadTaskPersonHeadResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            showLoadFailMsg("队员更换头像成功");
            mTaskTeamPresenter.recordTaskPersonScore(task_id,"1",false);
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void ImportTaskPersonGroupResult(Object result) {

    }

    @Override
    public void SetTaskPersonStatusResult(Object result) {

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

        Toast.makeText(this,err,Toast.LENGTH_SHORT).show();
    }
}
