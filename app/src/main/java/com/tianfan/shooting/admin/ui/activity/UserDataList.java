package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;
import android.util.Log;
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

import com.tianfan.shooting.admin.evenmessage.UserSelectEvent;
import com.tianfan.shooting.net.BaseService;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @program: Shooting
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-17 21:16
 **/
public class UserDataList extends AppCompatActivity {
        @BindView(R.id.list_user_input)
        RecyclerView recyclerView;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        baseQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                UserSelectEvent userSelectEvent = new UserSelectEvent();
                userSelectEvent.setData(userList.get(position));
                Log.e("click---->",""+userList.get(position));
                EventBus.getDefault().post(userSelectEvent);
                finish();
            }
        });
        recyclerView.setAdapter(baseQuickAdapter);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.BaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);

        Map map = new HashMap<String, String>();
//        map.put("page", );
        Log.e("userList","=----->--start");
        Call<ResponseBody> gitHubBeanCall = service.getUserList(map);
        gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body()!=null){
                        String result = response.body().string();
                        Log.e("userList","=----->"+result);
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        if (jsonObject.getString("result").equals("ok")){
                            JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
                            initList(jsonArray);
                        }else{

                        }
                    }else{
                        Log.e("userList","=----->--nulll");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    List<JSONObject> userList = new ArrayList<>();
    private void initList(JSONArray jsonArray){
        Log.e("列表数据","----》"+jsonArray);
        for (int  i=jsonArray.size()-1;i>=0;i--){
            userList.add(jsonArray.getJSONObject(i));
        }
        baseQuickAdapter.notifyDataSetChanged();
    }

    BaseQuickAdapter baseQuickAdapter = new BaseQuickAdapter<JSONObject,BaseViewHolder>(R.layout.item_uerlist,userList) {
        @Override
        protected void convert(BaseViewHolder helper, JSONObject item) {
//            tv_task_name
//            tv_create_time
            helper.setText(R.id.tv_task_name,item.getString("task_name"));
            helper.setText(R.id.tv_create_time,item.getString("create_time"));
        }

    };

}

//getUserListn