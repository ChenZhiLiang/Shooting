package com.tianfan.shooting.admin.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.ChooseTaskAdapter;
import com.tianfan.shooting.admin.ui.activity.HistoryDetals;
import com.tianfan.shooting.net.GetResult;
import com.tianfan.shooting.net.RequestTools;
import com.tianfan.shooting.net.RetrofitUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-12 18:05
 * @Description 历史射击成绩查看，导出等
 */
public class HistoryFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.refrash_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.list_choose_task)
    RecyclerView recyclerView;
    ChooseTaskAdapter chooseTaskAdapter;
    List<JSONObject> listData = new ArrayList();
    PromptDialog promptDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_choose_task_list, container, false);
        ButterKnife.bind(this, view);
        chooseTaskAdapter = new ChooseTaskAdapter(R.layout.item_choose_task, listData, new ChooseTaskAdapter.ClickHisCallbak() {
            @Override
            public void click(JSONObject jsonObject) {
//                点击事件，开始跳转
                //详情页数据
                Intent intent = new Intent(getActivity(),HistoryDetals.class);
                Bundle bundle = new Bundle();
                bundle.putString("data",JSONObject.toJSONString(jsonObject));
                intent.putExtra("bd",bundle);
                startActivity(intent);

            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(chooseTaskAdapter);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh();
                getHistory();
//                chooseTaskPresenter.startGetData("1");
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore();
//                chooseTaskPresenter.startGetData("1");
            }
        });
        promptDialog = new PromptDialog(getActivity());
        getHistory();
        return view;
    }
    void getHistory() {
        promptDialog.showLoading("数据加载中");
        RequestTools.doAction().getData(RetrofitUtils.getService().getHistory(new HashMap<>()),
                new GetResult<String>() {
                    @Override
                    public void fail(String msg) {
                        smartRefreshLayout.finishRefresh();
                        promptDialog.dismiss();
                    }
                    @Override
                    public void ok(String s) {
                        smartRefreshLayout.finishRefresh();
                        promptDialog.dismiss();
                        JSONArray jsonArray = JSONArray.parseArray(s);
                        if (jsonArray.size() > 0) {
                            listData.clear();
                            for (int i = jsonArray.size()-1; i >= 0; i--) {
                                listData.add(jsonArray.getJSONObject(i));
                            }
                            chooseTaskAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    //点击事件监听
    @Override
    public void onClick(View v) {

    }

}
