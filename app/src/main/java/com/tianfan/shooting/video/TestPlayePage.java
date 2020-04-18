package com.tianfan.shooting.video;

import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.tianfan.shooting.R;
import com.tianfan.shooting.net.GetResult;
import com.tianfan.shooting.net.RequestTools;
import com.tianfan.shooting.net.RetrofitUtils;


import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * @program: Shooting
 * @description: 说明
 * @author: lxf
 * @create: 2019-12-09 14:42
 **/
public class TestPlayePage extends FragmentActivity {

    @BindView(R.id.tv_test_task)
    TextView add;
    private int count =0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_test_pl);
        ButterKnife.bind(this);
        getFuckData();
    }

    Disposable disposable;
    private void getFuckData() {
         disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        count = count+1;
                        Log.e("MainActivity", "----------RxJava 定时轮询任务----------"+count);
                        Map map = new HashMap();
                        map.put("position", "1");
                        RequestTools.doAction().getData(RetrofitUtils.getService().getTaskStatus(map),
                                new GetResult<String>() {
                                    @Override
                                    public void fail(String msg) {
                                        dealFuckData(msg);

                                    }

                                    @Override
                                    public void ok(String s) {
                                        dealFuckData(s);
                                    }
                                });
                    }
                });
    }

    private void dealFuckData(String s){
        Log.e("deal","-----start--->");
        add.setText(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        disposable.dispose();
    }
}
