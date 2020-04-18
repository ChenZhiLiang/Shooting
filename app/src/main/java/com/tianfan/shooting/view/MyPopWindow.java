package com.tianfan.shooting.view;

import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.widget.PopupWindow;

import com.tianfan.shooting.R;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/12 22:42
 * 修改人：Chen
 * 修改时间：2020/4/12 22:42
 */
public class MyPopWindow extends PopupWindow {

    public MyPopWindow(View contentView, int width, int height) {
        // 设置SelectPicPopupWindow的View
        this.setContentView(contentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(width);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(height);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setBackgroundDrawable(new BitmapDrawable());
        // TODO: 2016/5/17 设置可以获取焦点
        this.setFocusable(true);
        // TODO: 2016/5/17 设置可以触摸弹出框以外的区域
        this.setOutsideTouchable(true);
        // TODO: 2016/5/17 设置动画
        this.setAnimationStyle(R.style.popwin_anim_style);
        // TODO：更新popupwindow的状态
        this.update();
    }
}

