package com.tianfan.shooting.admin.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.CameraListAdapter;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.bean.CameraBean;
import com.tianfan.shooting.utills.Utils;
import com.tianfan.shooting.view.CameraDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @Name：Shooting
 * @Description：摄像头管理
 * @Author：Chen
 * @Date：2020/4/15 0:24
 * 修改人：Chen
 * 修改时间：2020/4/15 0:24
 */
public class CameraFragment extends BaseFragment {

    @BindView(R.id.recycler_camera)
    RecyclerView recycler_camera;
    private String task_id;
    private CameraListAdapter mCameraListAdapter;

    private List<CameraBean> datas = new ArrayList<>();
    public static CameraFragment getInstance(String task_id) {
        CameraFragment hf = new CameraFragment();
        hf.task_id = task_id;
        return hf;
    }
    @Override
    public void loadingData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_camera;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {

        datas.clear();
        for (int i = 0; i < 5; i++) {
            CameraBean mCameraBean =new CameraBean();
            datas.add(mCameraBean);
        }
        recycler_camera.setLayoutManager(new LinearLayoutManager(getContext()));
        mCameraListAdapter = new CameraListAdapter(datas);
        recycler_camera.setAdapter(mCameraListAdapter);
    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.iv_return_home,R.id.iv_create,R.id.iv_editor,R.id.iv_delete})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_return_home:
                getActivity().finish();
                break;
            case R.id.iv_create:
                CameraDialog addCameraDialog = new CameraDialog(getContext());
                addCameraDialog.show();
                break;
            case R.id.iv_editor:
                CameraDialog editCameraDialog = new CameraDialog(getContext(),null);
                editCameraDialog.show();
                break;
            case R.id.iv_delete:
                new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("温馨提示")
                        .setContentText("确定删除选中的摄像头吗？")
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
