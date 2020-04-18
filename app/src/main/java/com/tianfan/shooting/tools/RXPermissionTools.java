package com.tianfan.shooting.tools;

import android.Manifest;
import android.app.Activity;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

/**
 * @author liangxingfu
 * @time 2019/1/17 1:56 PM
 * @describe 权限检查，申请工具
 */

//在oncreate方法中调用
public class RXPermissionTools {
    RxPermissions rxPermissions;

    public RXPermissionTools(Activity activity) {
        rxPermissions = new RxPermissions(activity);
    }


    int apConnut = 0;
    boolean WRITE_EXTERNAL_STORAGE = false;
    boolean CAMERA = false;
    boolean READ_EXTERNAL_STORAGE = false;
    boolean READ_PHONE_STATE = false;
    public void applyPermissionNew(final APPlyCBack apPlyCBack) {
        rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_PHONE_STATE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        {
                            apConnut = apConnut + 1;
                            if (permission.granted) {
                                if (permission.name.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                    WRITE_EXTERNAL_STORAGE = true;
                                }
                                if (permission.name.equals(Manifest.permission.CAMERA)) {
                                    CAMERA = true;
                                }
                                if (permission.name.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                    READ_EXTERNAL_STORAGE = true;
                                }
                                if (permission.name.equals(Manifest.permission.READ_PHONE_STATE)) {
                                    READ_PHONE_STATE = true;
                                }
                            }
                            if (apConnut == 4) {
                                if (!(WRITE_EXTERNAL_STORAGE && CAMERA && READ_EXTERNAL_STORAGE && READ_PHONE_STATE)) {
                                    apPlyCBack.result(false);
                                } else {
                                    apPlyCBack.result(true);
                                }
                            }
                        }
                    }
                });
    }

    public interface PermissCallback {
        void result(Permission result);
    }

    public interface APPlyCBack {
        void result(boolean result);
    }

}
