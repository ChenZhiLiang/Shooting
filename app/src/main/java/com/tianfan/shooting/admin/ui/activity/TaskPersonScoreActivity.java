package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.view.LoadingDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
* 任务队员成绩表
* */
public class TaskPersonScoreActivity extends AppCompatActivity {

    @BindView(R.id.iv_return_home)
    ImageView iv_return_home;
    @BindView(R.id.tv_title)
    TextView tv_title;
    public LoadingDialog mLoadingDialog;

    private TaskInfoBean mTaskInfoBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_person_score);
        ButterKnife.bind(this);
        mLoadingDialog = new LoadingDialog(this, R.style.TransparentDialog);
        mLoadingDialog.setTitle("加载中");
        mLoadingDialog.setCancelable(false);
        initView();
    }

    private void initView(){
        mTaskInfoBean = getIntent().getParcelableExtra("TaskInfoBean");
        if (mTaskInfoBean!=null){
            tv_title.setText(mTaskInfoBean.getTask_name()+"-队员成绩表");
        }
    }
    @OnClick({R.id.iv_return_home})
    public void onClick(View v){
        if (v == iv_return_home) {
            finish();
        }
    }
}
