package com.tianfan.shooting.net;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * @program: HttpDemo
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-29 14:19
 **/
public class ReQCallBack<T> {
    public void getData(Observable observable, final GetResult getResult){
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseObserver<T>() {
                    @Override
                    public void onSuccess(T demo) {
                        getResult.ok(demo);
                    }
                    @Override
                    public void onFailure(Throwable e, String errorMsg) {
                        getResult.fail(errorMsg);
                    }
                });
    }
}