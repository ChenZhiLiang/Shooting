package com.tianfan.shooting.base;

import android.app.Application;
import android.util.Log;
//
//import com.baidu.ocr.sdk.OCR;
//import com.baidu.ocr.sdk.OnResultListener;
//import com.baidu.ocr.sdk.exception.OCRError;
//import com.baidu.ocr.sdk.model.AccessToken;
import com.tianfan.shooting.utills.Density;
import com.vise.log.ViseLog;
import com.vise.log.inner.LogcatTree;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-03 17:48
 * @Description 功能描述
 */
public class ShootingApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ViseLog.getLogConfig()
                .configAllowLog(true)//是否输出日志
                .configShowBorders(true)//是否排版显示
                .configTagPrefix("ViseLog")//设置标签前缀
                .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-5}")//个性化设置标签，默认显示包名
                .configLevel(Log.VERBOSE);//设置日志最小输出级别，默认Log.VERBOSE
        ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
//
//        OCR.getInstance(getApplicationContext()).initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
//            @Override
//            public void onResult(AccessToken result) {
//                // 调用成功，返回AccessToken对象
//                String token = result.getAccessToken();
//            }
//            @Override
//            public void onError(OCRError error) {
//                // 调用失败，返回OCRError子类SDKError对象
//            }
//        }, getApplicationContext(), "wZEhDLxUjxe8P7ZelpHOqxYf", "lKC2aKjObcDLvGo4cjd9ZUV2nrKtz6Th");

        Density.setDensity(this,Density.DesignWit);

    }
}
