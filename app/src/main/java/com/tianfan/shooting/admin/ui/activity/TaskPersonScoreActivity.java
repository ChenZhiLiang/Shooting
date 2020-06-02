package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.TaskPersonScoreAdapter;
import com.tianfan.shooting.adapter.TopTabAdpater;
import com.tianfan.shooting.bean.CommandManageBean;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.view.CustomHorizontalScrollView;
import com.tianfan.shooting.view.LoadingDialog;
import com.tianfan.shooting.view.hrecycler.ScoreRecyclerView;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 任务队员成绩表
 */
public class TaskPersonScoreActivity extends AppCompatActivity implements TaskPersonScoreAdapter.OnContentScrollListener {

    @BindView(R.id.iv_return_home)
    ImageView iv_return_home;
    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.rv_tab_right)
    RecyclerView rvTabRight;
    @BindView(R.id.hor_scrollview)
    CustomHorizontalScrollView horScrollview;
    @BindView(R.id.recycler_content)
    RecyclerView recyclerContent;
    public LoadingDialog mLoadingDialog;
    private TaskInfoBean mTaskInfoBean;
    private List<CommandManageBean.CommandManageItem> dataList = new ArrayList<>();
    private TaskPersonScoreAdapter mTaskPersonScoreAdapter;

    private List<String> topTabs = new ArrayList<>();

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

    private void initView() {
        mTaskInfoBean = getIntent().getParcelableExtra("TaskInfoBean");
        if (mTaskInfoBean!=null){
            tv_title.setText(mTaskInfoBean.getTask_name()+"-队员成绩表");
        }

        //处理顶部标题部分
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTabRight.setLayoutManager(linearLayoutManager);
        TopTabAdpater topTabAdpater = new TopTabAdpater(this);
        rvTabRight.setAdapter(topTabAdpater);
        for (int i = 0; i < 6; i++) {
            topTabs.add("第" + i + "轮");
        }
        topTabAdpater.setDatas(topTabs);



        //处理内容部分
        recyclerContent.setLayoutManager(new LinearLayoutManager(this));
        recyclerContent.setHasFixedSize(true);
        for (int i=0;i<10;i++){
            CommandManageBean.CommandManageItem bean = new CommandManageBean.CommandManageItem();
            List<CommandManageBean.CommandManageItem.PersonScoreBean> person_score = new ArrayList<>();
            for (int j=0;j<6;j++){
                CommandManageBean.CommandManageItem.PersonScoreBean scoreBean = new CommandManageBean.CommandManageItem.PersonScoreBean();
                person_score.add(scoreBean);
            }
            bean.setPerson_score(person_score);
            dataList.add(bean);
        }
        mTaskPersonScoreAdapter = new TaskPersonScoreAdapter(this);

        recyclerContent.setAdapter(mTaskPersonScoreAdapter);
        mTaskPersonScoreAdapter.notifyDataSetChanged();
        mTaskPersonScoreAdapter.setDatas(dataList);
        mTaskPersonScoreAdapter.setOnContentScrollListener(this);
        recyclerContent.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                List<TaskPersonScoreAdapter.ItemViewHolder> viewHolderCacheList = mTaskPersonScoreAdapter.getViewHolderCacheList();
                if (null != viewHolderCacheList) {
                    int size = viewHolderCacheList.size();
                    for (int i = 0; i < size; i++) {
                        viewHolderCacheList.get(i).horItemScrollview.scrollTo(mTaskPersonScoreAdapter.getOffestX(), 0);
                    }
                }

            }
        });

        //同步顶部tab的横向scroll和内容页面的横向滚动
        //同步滚动顶部tab和内容
        horScrollview.setOnCustomScrollChangeListener(new CustomHorizontalScrollView.OnCustomScrollChangeListener() {
            @Override
            public void onCustomScrollChange(CustomHorizontalScrollView listener, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                //代码重复,可以抽取/////
                List<TaskPersonScoreAdapter.ItemViewHolder> viewHolderCacheList = mTaskPersonScoreAdapter.getViewHolderCacheList();
                if (null != viewHolderCacheList) {
                    int size = viewHolderCacheList.size();
                    for (int i = 0; i < size; i++) {
                        viewHolderCacheList.get(i).horItemScrollview.scrollTo(scrollX, 0);
                    }
                }
            }

        });
    }

    @OnClick({R.id.iv_return_home})
    public void onClick(View v) {
        if (v == iv_return_home) {
            finish();
        }
    }

    @Override
    public void onScroll(int offestX) {
        //处理单个item滚动时,顶部tab需要联动
        if (null != horScrollview) horScrollview.scrollTo(offestX, 0);
    }
}
