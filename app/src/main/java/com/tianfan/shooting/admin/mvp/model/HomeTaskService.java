package com.tianfan.shooting.admin.mvp.model;

import com.alibaba.fastjson.JSONObject;

import com.tianfan.shooting.admin.mvp.view.IHomeService;
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
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-12 10:54
 * @Description 功能描述
 */
public class HomeTaskService {
    public void getUnDoTask(String page, final IHomeService.HomeTaskResult iChooseTaskResult){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.BaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);

        Map map = new HashMap<String, String>();
        map.put("page", page);
        Call<ResponseBody> gitHubBeanCall = service.getUnDoTask(map);
        gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body()!=null){
                        String result = response.body().string();
                        iChooseTaskResult.ok(result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    iChooseTaskResult.bullshit(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                iChooseTaskResult.bullshit(t.getMessage());
            }
        });

    }

    public void startGetFinish(String page, final IHomeService.HomeTaskResult iChooseTaskResult){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.BaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);

        Map map = new HashMap<String, String>();
        map.put("page", page);
        Call<ResponseBody> gitHubBeanCall = service.getFinishTask(map);
        gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    iChooseTaskResult.ok(result);
                } catch (IOException e) {
                    e.printStackTrace();
                    iChooseTaskResult.bullshit(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                iChooseTaskResult.bullshit(t.getMessage());
            }
        });
    }

    void updataTaskStatus(String id, final IHomeService.HomeTaskResult iChooseTaskResult){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.BaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);

        Map map = new HashMap<String, String>();
        map.put("id", id);
        Call<ResponseBody> gitHubBeanCall = service.updateTask(map);
        gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    iChooseTaskResult.ok(result);
                } catch (IOException e) {
                    e.printStackTrace();
                    iChooseTaskResult.bullshit(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                iChooseTaskResult.bullshit(t.getMessage());
            }
        });
    }

    public void startTask(String id, final IHomeService.HomeTaskResult iChooseTaskResult){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.BaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);

        Map map = new HashMap<String, String>();
        map.put("id", id);
        Call<ResponseBody> gitHubBeanCall = service.startTask(map);
        gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    iChooseTaskResult.ok(result);
                } catch (IOException e) {
                    e.printStackTrace();
                    iChooseTaskResult.bullshit(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                iChooseTaskResult.bullshit(t.getMessage());
            }
        });
    }

    public void finshTask(String id, final IHomeService.HomeTaskResult iChooseTaskResult){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.BaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);

        Map map = new HashMap<String, String>();
        map.put("id", id);
        Call<ResponseBody> gitHubBeanCall = service.finishTask(map);
        gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    iChooseTaskResult.ok(result);
                } catch (IOException e) {
                    e.printStackTrace();
                    iChooseTaskResult.bullshit(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                iChooseTaskResult.bullshit(t.getMessage());
            }
        });

    }
    public void startTimesTask(String id, String groupID,JSONObject userData, final IHomeService.HomeTaskResult iChooseTaskResult){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.BaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);

        Map map = new HashMap<String, String>();
        map.put("id", id);
        map.put("groupID", groupID);

        map.put("data",JSONObject.toJSONString(userData));
        Call<ResponseBody> gitHubBeanCall = service.startTimesTask(map);
        gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    iChooseTaskResult.ok(result);
                } catch (IOException e) {
                    e.printStackTrace();
                    iChooseTaskResult.bullshit(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                iChooseTaskResult.bullshit(t.getMessage());
            }
        });
    }
    public void endTimesTask(String id, final IHomeService.HomeTaskResult iChooseTaskResult){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.BaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);

        Map map = new HashMap<String, String>();
        map.put("id", id);
        Call<ResponseBody> gitHubBeanCall = service.fiendTimesTasknishTask(map);
        gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    iChooseTaskResult.ok(result);
                } catch (IOException e) {
                    e.printStackTrace();
                    iChooseTaskResult.bullshit(e.getMessage());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                iChooseTaskResult.bullshit(t.getMessage());
            }
        });
    }

}
