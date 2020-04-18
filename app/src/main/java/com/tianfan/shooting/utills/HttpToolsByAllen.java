package com.tianfan.shooting.utills;

import androidx.annotation.MainThread;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * @program: Shooting
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-27 16:00
 **/
public class HttpToolsByAllen<T> {
    public interface MyRequestCallBack<T> {
        public void result(HttpToolsByAllen t);
    }

    public HttpToolsByAllen getFuckData(Map prameter, MyRequestCallBack myRequestCallBack, T bean) {
        Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
//                e.onNext(e);
                e.onNext("");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {

                    @Override
                    public void accept(String o) throws Exception {

                    }
                });
        return this;

    }

    public class  TestBean{
        String test;
        String data;
    }
}
