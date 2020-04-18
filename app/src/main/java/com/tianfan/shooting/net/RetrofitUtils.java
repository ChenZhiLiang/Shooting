package com.tianfan.shooting.net;

/**
 * @program: DW_GX_ACJF
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-28 10:45
 **/

import android.util.Log;

import androidx.annotation.NonNull;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tianfan.shooting.Login;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit封装
 */
public class RetrofitUtils {

    private static RxBaseService mRxBaseService;
    /**
     * 单例模式
     */
    public static RxBaseService getService() {
//        if (mRxBaseService == null) {
//            synchronized (RetrofitUtils.class) {
//                if (mRxBaseService == null) {
//                    mRxBaseService = new RetrofitUtils().getRetrofit();
//                }
//            }
//        }
        return   new RetrofitUtils().getRetrofit();
    }
    private RetrofitUtils(){}
    public RxBaseService getRetrofit() {
        return initRetrofit(initOkHttp()) .create(RxBaseService.class);
    }

    /**
     * 初始化Retrofit
     */
    @NonNull
    private Retrofit initRetrofit(OkHttpClient client) {

        return new Retrofit.Builder()
                .client(client)
                .baseUrl(RxBaseService.BaseURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    /**
     * 初始化okhttp
     */
    @NonNull
    private OkHttpClient initOkHttp() {
        return new OkHttpClient().newBuilder()
                .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                .connectTimeout(10, TimeUnit.SECONDS)//设置请求超时时间
                .writeTimeout(30, TimeUnit.SECONDS)//设置写入超时时间
//                .addInterceptor(new LogInterceptor())//添加打印拦截器
                .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                .build();
    }
}
