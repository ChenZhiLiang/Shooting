package com.tianfan.shooting.admin.ui.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.d.lib.slidelayout.SlideLayout;

import com.tianfan.shooting.R;
import com.tianfan.shooting.net.GetResult;
import com.tianfan.shooting.net.RequestTools;
import com.tianfan.shooting.net.RetrofitUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-12 18:15
 * @Description 功能描述
 */
public class SetUpFragment extends Fragment {

    @BindView(R.id.list_view)
    RecyclerView recyclerView;

    List<JSONObject> list= new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setup,container,false);
        ButterKnife.bind(this,view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.setAdapter(baseQuickAdapter);
        RequestTools.doAction().getData(RetrofitUtils.getService().getCameraList(new HashMap<>()), new GetResult<String>() {
            @Override
            public void fail(String msg) {
            }
            @Override
            public void ok(String o) {
                JSONArray jsonArray = JSONArray.parseArray(o);
                for (int i=0;i<jsonArray.size();i++){
                    list.add(jsonArray.getJSONObject(i));
                    baseQuickAdapter.setNewData(list);
                }
            }
        });

        return view;
    }

    BaseQuickAdapter baseQuickAdapter = new BaseQuickAdapter(R.layout.item_camera_list_admin, list) {
        @Override
        protected void convert(BaseViewHolder helper, Object item) {
            helper.setText(R.id.tv_name,((JSONObject)item).getString("camera_name"));
            helper.setText(R.id.tv_add,((JSONObject)item).getString("camera_add"));
            ((SlideLayout)helper.getView(R.id.slideview)).setEnable(true);

        }
    };
}
