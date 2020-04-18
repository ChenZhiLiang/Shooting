package com.tianfan.shooting.net;

import com.tianfan.shooting.Login;
import com.tianfan.shooting.utills.NewSPTools;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * @program: DW_GX_ACJF
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-28 10:26
 **/
public interface RxBaseService {


    public static String BaseURL = "http://192.168.88.136:8971/ShootingService/";

    //获取状态详情
    @FormUrlEncoded
    @POST("Task/getTaskStatus")
    Observable<BaseResponse<String>> getTaskStatus(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Task/getCameraList")
    Observable<BaseResponse<String>> getCameraList(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Scorer/upData")
    Observable<BaseResponse<String>> upShootData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("admin/getNowShoot")
    Observable<BaseResponse<String>> getNowShoot(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("admin/getHistory")
    Observable<BaseResponse<String>> getHistory(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("admin/getRecordData")
    Observable<BaseResponse<String>> getRecordData(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("admin/finishNowShoot")
    Observable<BaseResponse<String>> finishNowShoot(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("admin/upNowShoot")
    Observable<BaseResponse<String>> upNowShoot(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("admin/createFile")
    Observable<BaseResponse<String>> createFile(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("admin/checService")
    Observable<BaseResponse<String>> checService(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Scorer/upXY")
    Observable<BaseResponse<String>> upXY(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("Scorer/getXY")
    Observable<BaseResponse<String>> getXY(@FieldMap Map<String, String> params);

    //文件上传

    @Multipart
    @POST("Scorer/upload")
    Call<ResponseBody> uploadPic(@Part List<MultipartBody.Part> file, @Part("data") RequestBody description);


    //    获取用户列表
    @FormUrlEncoded
    @POST("Data/DuiYuan/getDuiYuanList")
    Observable<BaseResponse<String>> getUserList(@FieldMap Map<String, String> params);

    //    delteUser
    @FormUrlEncoded
    @POST("Data/DuiYuan/delteUser")
    Observable<BaseResponse<String>> delteUser(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("Data/DuiYuan/addUser")
    Observable<BaseResponse<String>> addUser(@FieldMap Map<String, String> params);


    //    获取列队信息
    @FormUrlEncoded
    @POST("Data/LieDui/getLDList")
    Observable<BaseResponse<String>> getLDList(@FieldMap Map<String, String> params);


    //    保存新的列队信息
    @FormUrlEncoded
    @POST("Data/LieDui/saveRange")
    Observable<BaseResponse<String>> saveRange(@FieldMap Map<String, String> params);


    //    保存新的列队信息
    @FormUrlEncoded
    @POST("Data/LieDui/clearData")
    Observable<BaseResponse<String>> clearData(@FieldMap Map<String, String> params);


}
