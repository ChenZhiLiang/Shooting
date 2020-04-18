package com.tianfan.shooting.admin.mvp.model;


import com.tianfan.shooting.admin.mvp.view.FileUpInterface;
import com.tianfan.shooting.net.BaseService;
import com.vise.log.ViseLog;

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
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-02 17:37
 * @Description 功能描述
 */
public class FileUPLoaSrvice implements FileUpInterface {
    @Override
    public void start(String fileURI, final UpResult upResult) {
        if (!fileURI.contains(".")) {
            upResult.fail("请选择Excel文档上传");
            return;
        }
//        判断上传的文件类型
        if (!((fileURI.substring(fileURI.lastIndexOf("."))).equals(".xlsx") || (fileURI.substring(fileURI.lastIndexOf("."))).equals(".xls"))) {
            upResult.fail("请选择Excel文档上传");
            return;
        }
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.UPFileBaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);
        List<MultipartBody.Part> fileList = new ArrayList<>();

        File file = new File(fileURI);
        RequestBody requestFile = RequestBody.create(MediaType.parse("application/otcet-stream"), file);
        MultipartBody.Part partBody = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
        fileList.add(partBody);
        Call<ResponseBody> call = service.uploadNew(fileList);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,
                                   Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {

                        String data = response.body().string();
                        ViseLog.e("文件上传返回的数据");
                        ViseLog.e(data);
                        upResult.onComplete("成功");
                    } else {
                        upResult.fail("服务器解析异常");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                upResult.fail(t.getMessage());
            }
        });

    }

}
