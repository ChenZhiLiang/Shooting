package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-24 13:38
 * @Description 功能描述
 */
public class CheckDetials extends AppCompatActivity {

//    list_check_details
    @BindView(R.id.list_check_details)
    RecyclerView recyclerView;
    CheckDetialsAdapter checkDetialsAdapter;
    List<JSONObject> list = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_details);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getBundleExtra("data");
        Log.e("详情页数据","---"+bundle.getString("data"));
        JSONObject reuslt = JSONObject.parseObject(bundle.getString("data"));
        JSONObject jsonObject = reuslt.getJSONObject("data");
        JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
        for (Object o:jsonArray){
            list.add((JSONObject) o);
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        checkDetialsAdapter = new CheckDetialsAdapter(R.layout.item_check_details_item,list);
        recyclerView.setAdapter(checkDetialsAdapter);
    }

    class CheckDetialsAdapter extends BaseQuickAdapter<JSONObject, BaseViewHolder>{
        public CheckDetialsAdapter(int layoutResId, @Nullable List<JSONObject> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, JSONObject item) {
                helper.setText(R.id.name,item.getString("name"));
            helper.setText(R.id.number,item.getString("number")+"/"+item.getString("type"));
        }
    }
}
