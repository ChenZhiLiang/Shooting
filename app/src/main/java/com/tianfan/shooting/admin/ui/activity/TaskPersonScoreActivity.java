package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.TaskPersonScoreAdapter;
import com.tianfan.shooting.adapter.TopTabAdpater;
import com.tianfan.shooting.admin.mvp.presenter.TaskPersonScorePerson;
import com.tianfan.shooting.admin.mvp.view.TaskPersonScoreView;
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
public class TaskPersonScoreActivity extends AppCompatActivity implements TaskPersonScoreAdapter.OnContentScrollListener, TaskPersonScoreView {
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
    private TopTabAdpater topTabAdpater;
    private TaskPersonScoreAdapter mTaskPersonScoreAdapter;

    private List<String> topTabs = new ArrayList<>();
    private TaskPersonScorePerson mTaskPersonScorePerson;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_person_score);
        ButterKnife.bind(this);
        mLoadingDialog = new LoadingDialog(this, R.style.TransparentDialog);
        mLoadingDialog.setTitle("加载中");
        mLoadingDialog.setCancelable(false);
        initView();
        initData();
    }

    private void initView() {
        mTaskInfoBean = getIntent().getParcelableExtra("TaskInfoBean");
        if (mTaskInfoBean!=null){
            tv_title.setText(mTaskInfoBean.getTask_name()+"-队员成绩表");
        }

        mTaskPersonScorePerson = new TaskPersonScorePerson(this);
        //处理顶部标题部分
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvTabRight.setLayoutManager(linearLayoutManager);
         topTabAdpater = new TopTabAdpater(this);
        rvTabRight.setAdapter(topTabAdpater);

        //处理内容部分
        recyclerContent.setLayoutManager(new LinearLayoutManager(this));
        recyclerContent.setHasFixedSize(true);

        mTaskPersonScoreAdapter = new TaskPersonScoreAdapter(this);
        recyclerContent.setAdapter(mTaskPersonScoreAdapter);
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

    private void initData(){
        mTaskPersonScorePerson.findTaskPersonScore(mTaskInfoBean.getTask_id(),true);
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

    @Override
    public void FindTaskPersonScoreResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            int max_round = 0;
            String datas = jsonObject.getString("datas");
            JSONArray jsonArray = JSONArray.parseArray(datas);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject itemsObject = JSONObject.parseObject(jsonArray.get(i).toString());
                String items = itemsObject.getString(String.valueOf(i + 1));
                List<CommandManageBean.CommandManageItem> mDatas = JSONArray.parseArray(items, CommandManageBean.CommandManageItem.class);
                for (int j = 0;j<mDatas.size();j++){
                    if (!TextUtils.isEmpty(mDatas.get(j).getPerson_id())){
                        if (mDatas.get(j).getPerson_score()!=null&&mDatas.get(j).getPerson_score().size()>max_round){
                            max_round = mDatas.get(j).getPerson_score().size();
                        }
                        dataList.add(mDatas.get(j));
                    }
                }
                Log.i("CommandManageBean",mDatas.size()+"max_round:"+max_round);
            }

            topTabs.add("总成绩");
            for (int i = 0; i < max_round; i++) {
                topTabs.add("第" + (i+1) + "轮");
            }
            topTabAdpater.setDatas(topTabs);
            mTaskPersonScoreAdapter.setDatas(dataList);


//            for (int i=0;i<10;i++){
//                CommandManageBean.CommandManageItem bean = new CommandManageBean.CommandManageItem();
//                List<CommandManageBean.CommandManageItem.PersonScoreBean> person_score = new ArrayList<>();
//                for (int j=1;j<6;j++){
//                    CommandManageBean.CommandManageItem.PersonScoreBean scoreBean = new CommandManageBean.CommandManageItem.PersonScoreBean();
//                    person_score.add(scoreBean);
//                }
//                bean.setPerson_score(person_score);
//                dataList.add(bean);
//            }

        }
    }

    @Override
    public void showProgress() {

        mLoadingDialog.show();
    }

    @Override
    public void hideProgress() {
        mLoadingDialog.hide();
    }

    @Override
    public void showLoadFailMsg(String err) {

        Toast.makeText(this,err,Toast.LENGTH_SHORT).show();
    }
}
