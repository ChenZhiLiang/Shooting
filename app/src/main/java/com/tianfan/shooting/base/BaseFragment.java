package com.tianfan.shooting.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tianfan.shooting.R;
import com.tianfan.shooting.view.LoadingDialog;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Administrator on 2017/1/11.
 * Fragment基类
 */

public abstract class BaseFragment extends Fragment implements BaseFragmentInterface {

    protected Activity mActivity;
    protected boolean isViewInitiated;// 初始化view
    protected boolean isVisibleToUser;//fragment是否可见
    protected boolean isDataInitiated;//加载数据
    public LoadingDialog mLoadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = getActivity();
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        // 通过注解绑定控件
        ButterKnife.bind(mActivity);
        initView(view, savedInstanceState);
        initSweetAlertDialog();
        initData();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //查看这个fragment的可见状态
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData();
    }

    //在这个方法中写网络请求
    public abstract void loadingData();

    public boolean prepareFetchData() {
        return prepareFetchData(false);
    }

    public boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated && (!isDataInitiated || forceUpdate)) {
            loadingData();
            isDataInitiated = true;
            return true;
        }
        return false;
    }

    /**
     * 初始化提示
     */
    private void initSweetAlertDialog() {
        mLoadingDialog = new LoadingDialog(getActivity(), R.style.TransparentDialog);
        mLoadingDialog.setTitle("加载中");
        mLoadingDialog.setCancelable(false);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
