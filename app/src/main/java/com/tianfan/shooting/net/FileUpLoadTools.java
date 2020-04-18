package com.tianfan.shooting.net;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @program: Shooting
 * @description: 文件上传工具
 * @author: lxf
 * @create: 2020-01-07 10:53
 **/
public class FileUpLoadTools {
    public interface FileUpCallBack{
        void result(boolean result, String msg);
    }

    public static void doUpLoadPic(final FileUpCallBack fileUpCallBack, File file,JSONObject jsonObject){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RxBaseService.BaseURL)
                .build();
        RxBaseService service = retrofit.create(RxBaseService.class);
        List<MultipartBody.Part> fileList = new ArrayList<>();
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part partBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        fileList.add(partBody);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), jsonObject+"");
        Call<ResponseBody> call = service.uploadPic(fileList,description);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String data = response.body().string();
                        fileUpCallBack.result(true,"成功！");
                    } else {
                        fileUpCallBack.result(false,"异常");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    fileUpCallBack.result(false,"异常："+e.getMessage());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                fileUpCallBack.result(false,"异常："+t.getMessage());
            }
        });
    }
}
