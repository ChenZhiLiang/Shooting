package com.tianfan.shooting.admin;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.ui.evendata.CameraSelectEvent;
import com.tianfan.shooting.net.GetResult;
import com.tianfan.shooting.net.RequestTools;
import com.tianfan.shooting.net.RetrofitUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @program: Shooting
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-27 00:51
 **/
public class CameraListActivity extends AppCompatActivity {
//    list_camrea
    @BindView(R.id.list_camrea)
    RecyclerView recyclerView;
    List<JSONObject> list= new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_list);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CameraSelectEvent cameraSelectEvent = new CameraSelectEvent();
                cameraSelectEvent.setResult(list.get(position).getString("camera_add"));
                cameraSelectEvent.setPositon(list.get(position).getString("position"));
                EventBus.getDefault().post(cameraSelectEvent);
                finish();
            }
        });
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
    }

    BaseQuickAdapter baseQuickAdapter = new BaseQuickAdapter(R.layout.item_camera_list, list) {
        @Override
        protected void convert(BaseViewHolder helper, Object item) {
                helper.setText(R.id.tv_name,((JSONObject)item).getString("camera_name"));
            helper.setText(R.id.tv_add,((JSONObject)item).getString("camera_add"));

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
