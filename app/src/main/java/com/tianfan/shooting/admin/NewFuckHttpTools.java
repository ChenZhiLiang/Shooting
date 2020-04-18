package com.tianfan.shooting.admin;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.net.BaseService;


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @program: Shooting
 * @description: 新的网络请求工具
 * @author: lxf
 * @create: 2019-11-22 09:31
 **/
public class NewFuckHttpTools {

//    这是个老方法，得重新整
    public  void startNewTask(JSONObject data, FuckRequestCallBack baseRQCallBack, String actionType,String times,String type   ) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.BaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);
        Map map = new HashMap<String, String>();
        map.put("data",data+"");
        map.put("actionType",actionType);
        map.put("times",times);
        map.put("type",type);
        map.put("task_name",data.getString("task_name"));
        Call<ResponseBody> gitHubBeanCall = service.doResult(map);
        gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("请求回调","-----成功");
                    if (response.body()!=null){
                        String result = response.body().string();
                        JSONObject jsonObject = JSONObject.parseObject(result);
                        if (jsonObject.getString("result").equals("ok")){
                            baseRQCallBack.result(true,jsonObject,"");
                        }else{
                            baseRQCallBack.result(false,jsonObject,jsonObject.getString("msg"));
                        }
                    }else{
                        baseRQCallBack.result(false,new JSONObject(),"网络请求错误");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    baseRQCallBack.result(false,new JSONObject(),"网络请求异常");
                }
            }
            @Override
            public void onFailure(Call call, Throwable t) {
                Log.e("请求回调","-----失败---》"+t);
            }
        });
    }

}
