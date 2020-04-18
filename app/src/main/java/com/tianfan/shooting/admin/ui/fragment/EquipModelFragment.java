package com.tianfan.shooting.admin.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.tianfan.shooting.R;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.view.EquipModelDialog;

import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @Name：Shooting
 * @Description：器材模板
 * @Author：Chen
 * @Date：2020/4/15 0:23
 * 修改人：Chen
 * 修改时间：2020/4/15 0:23
 */
public class EquipModelFragment extends BaseFragment {

    private String task_id;
    public static EquipModelFragment getInstance(String task_id) {
        EquipModelFragment hf = new EquipModelFragment();
        hf.task_id = task_id;
        return hf;
    }
    @Override
    public void loadingData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_equip_model;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

    }

    @Override
    public void initData() {

    }
    @OnClick({R.id.iv_return_home,R.id.iv_create,R.id.iv_delete})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_return_home:
                getActivity().finish();
                break;
            case R.id.iv_create:
                EquipModelDialog mEquipModelDialog = new EquipModelDialog(getContext());
                mEquipModelDialog.show();
                break;
            case R.id.iv_delete:
                new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("温馨提示")
                        .setContentText("确定删除该模板？")
                        .setConfirmText("确定")
                        .setCancelText("取消")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                            }
                        }).show();
                break;
        }
    }

}
