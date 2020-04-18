package com.tianfan.shooting.base;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/9 22:33
 * 修改人：Chen
 * 修改时间：2020/4/9 22:33
 */
public interface BaseView {
    void showProgress();

    void hideProgress();

    void showLoadFailMsg(String err);
}
