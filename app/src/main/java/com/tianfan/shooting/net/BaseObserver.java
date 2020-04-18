package com.tianfan.shooting.net;

/**
 * @program: DW_GX_ACJF
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-28 10:27
 **/

import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * 创建Base抽象类实现Observer
 */
public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private static final String TAG = "BaseObserver";
    @Override
    public void onSubscribe(Disposable d) {
        Log.e(TAG, "onSubscribe: " );
    }
    @Override
    public void onNext(BaseResponse<T> response) {
        Log.e(TAG, "请求返回的数据:---> " + JSONObject.toJSONString(response));
        if(response.getResult().equals("ok")){
            Log.e(TAG, "取得的Data:---> " + JSONObject.toJSONString(response.getData()));
            onSuccess(response.getData());
        }else{
            onFailure(null,response.getMsg());
        }
    }
    @Override
    public void onError(Throwable e) {
        Log.e(TAG, "Throwable: " + e.getMessage());
        onFailure(e,e.getMessage());
    }
    @Override
    public void onComplete() {
        Log.e(TAG, "onComplete: " );
    }
    public abstract void onSuccess(T demo);
    public abstract void onFailure(Throwable e, String errorMsg);
}