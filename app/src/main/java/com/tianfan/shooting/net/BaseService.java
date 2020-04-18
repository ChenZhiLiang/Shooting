package com.tianfan.shooting.net;

import com.tianfan.shooting.Login;
import com.tianfan.shooting.utills.NewSPTools;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface BaseService {
    public String BaseURL = "http://192.168.88.136:8971/ShootingService/";
    String UPFileBaseURL = "http://192.168.88.136:8971/ShootingService/";

    //增加装备
    @FormUrlEncoded
    @POST("Equipment/newAddlEquipment")
    Call<ResponseBody> addEquipment(@FieldMap Map<String, String> params);

    //增加装备
    @FormUrlEncoded
    @POST("Equipment/getAllEquipment")
    Call<ResponseBody> getAllEquipment(@FieldMap Map<String, String> params);

    //文件上传
    @Multipart
    @POST("Task/uploadNew")
    Call<ResponseBody> uploadNew(@Part List<MultipartBody.Part> file);

    @Multipart
    @POST("Task/uploadTest")
    Call<ResponseBody> uploadTest(@Part List<MultipartBody.Part> file);


    //    获取尚未完成的数据
    @FormUrlEncoded
    @POST("Task/getUnDoTask")
    Call<ResponseBody> getUnDoTask(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Task/getUserList")
    Call<ResponseBody> getUserList(@FieldMap Map<String, String> params);

    //    更新任务状态
    @FormUrlEncoded
    @POST("Task/updateTask")
    Call<ResponseBody> updateTask(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Task/doBaseAcition")
    Call<ResponseBody> doResult(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Task/getFinishTask")
    Call<ResponseBody> getFinishTask(@FieldMap Map<String, String> params);

    //    getFinishTask
    @FormUrlEncoded
    @POST("Task/startNewTask")
    Call<ResponseBody> startTask(@FieldMap Map<String, String> params);

    //    getFinishTask
    @FormUrlEncoded
    @POST("Task/finishTask")
    Call<ResponseBody> finishTask(@FieldMap Map<String, String> params);


    //    endTimesTask
    @FormUrlEncoded
    @POST("Task/endTimesTask")
    Call<ResponseBody> fiendTimesTasknishTask(@FieldMap Map<String, String> params);


    //    startTimesTask
    @FormUrlEncoded
    @POST("Task/startTimesTask")
    Call<ResponseBody> startTimesTask(@FieldMap Map<String, String> params);







}

