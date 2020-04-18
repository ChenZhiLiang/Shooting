package com.tianfan.shooting.admin.ui.fragment;

import android.os.Bundle;
import android.view.View;

import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.EquipTypeAdapter;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.bean.CameraBean;
import com.tianfan.shooting.bean.EquipTypeBean;
import com.tianfan.shooting.view.CameraDialog;
import com.tianfan.shooting.view.EquipTypeDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * @Name：Shooting
 * @Description：器材类型管理
 * @Author：Chen
 * @Date：2020/4/15 0:24
 * 修改人：Chen
 * 修改时间：2020/4/15 0:24
 */
public class EquipTypelFragment extends BaseFragment {

    @BindView(R.id.recycler_equip_mode)
    RecyclerView recycler_equip_mode;
    private String task_id;
    private EquipTypeAdapter mEquipTypeAdapter;
    private List<EquipTypeBean> mDatas = new ArrayList<>();
    public static EquipTypelFragment getInstance(String task_id) {
        EquipTypelFragment hf = new EquipTypelFragment();
        hf.task_id = task_id;
        return hf;
    }
    @Override
    public void loadingData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_equip_type;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        mDatas.clear();
        for (int i = 0; i < 5; i++) {
            EquipTypeBean mEquipTypeBean = new EquipTypeBean();
            mDatas.add(mEquipTypeBean);
        }
        recycler_equip_mode.setLayoutManager(new LinearLayoutManager(getContext()));
        mEquipTypeAdapter = new EquipTypeAdapter(mDatas);
        recycler_equip_mode.setAdapter(mEquipTypeAdapter);
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
                EquipTypeDialog addEquipTypeDialog = new EquipTypeDialog(getContext());
                addEquipTypeDialog.show();
                break;
            case R.id.iv_editor:
                EquipTypeDialog editEquipTypeDialog = new EquipTypeDialog(getContext());
                editEquipTypeDialog.show();
                break;
            case R.id.iv_delete:
                new SweetAlertDialog(mActivity, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("温馨提示")
                        .setContentText("确定删除选中的器材吗？")
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
