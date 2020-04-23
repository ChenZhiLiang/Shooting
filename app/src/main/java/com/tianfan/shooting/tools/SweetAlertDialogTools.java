package com.tianfan.shooting.tools;

import android.app.Activity;
import android.content.Context;

import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import androidx.appcompat.app.AppCompatActivity;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/21 23:55
 * 修改人：Chen
 * 修改时间：2020/4/21 23:55
 */
public class SweetAlertDialogTools {

    public static void ShowDialog(AppCompatActivity activity, String str, SweetAlertDialog.OnSweetClickListener mOnSweetClickListener){
        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("温馨提示")
                .setContentText(str)
                .setCancelText("取消")
                .setConfirmText("确定")
                .setConfirmClickListener(mOnSweetClickListener)
                .show();
    }

    public static void ShowDialog(Activity activity, String str, SweetAlertDialog.OnSweetClickListener mOnSweetClickListener){
        new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("温馨提示")
                .setContentText(str)
                .setCancelText("取消")
                .setConfirmText("确定")
                .setConfirmClickListener(mOnSweetClickListener)
                .show();
    }

    public static void ShowDialog(Context context, String str, SweetAlertDialog.OnSweetClickListener mOnSweetClickListener){
        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("温馨提示")
                .setContentText(str)
                .setCancelText("取消")
                .setConfirmText("确定")
                .setConfirmClickListener(mOnSweetClickListener)
                .show();
    }
}
