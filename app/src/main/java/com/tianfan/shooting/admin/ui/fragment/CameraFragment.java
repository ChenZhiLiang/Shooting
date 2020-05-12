package com.tianfan.shooting.admin.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.CameraListAdapter;
import com.tianfan.shooting.admin.mvp.presenter.CameraPersenter;
import com.tianfan.shooting.admin.mvp.view.CameraView;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.bean.CameraBean;
import com.tianfan.shooting.bean.EquipTypeBean;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.utills.Utils;
import com.tianfan.shooting.view.CameraDialog;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Name：Shooting
 * @Description：摄像头管理
 * @Author：Chen
 * @Date：2020/4/15 0:24
 * 修改人：Chen
 * 修改时间：2020/4/15 0:24
 */
public class CameraFragment extends BaseFragment implements CameraView {

    @BindView(R.id.recycler_camera)
    RecyclerView recycler_camera;
    private CameraListAdapter mCameraListAdapter;
    private CameraPersenter mCameraPersenter;

    private List<CameraBean> mCameraListDatas = new ArrayList<>();
    public static CameraFragment getInstance() {
        CameraFragment hf = new CameraFragment();
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

        recycler_camera.setLayoutManager(new LinearLayoutManager(getContext()));
        mCameraListAdapter = new CameraListAdapter(mCameraListDatas);
        recycler_camera.setAdapter(mCameraListAdapter);
        mCameraPersenter = new CameraPersenter(this);
    }

    @Override
    public void initData() {
        mCameraPersenter.findCameraCol();
    }

    @OnClick({R.id.iv_return_home,R.id.iv_create,R.id.iv_editor,R.id.iv_delete})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_return_home:
                getActivity().finish();
                break;
            case R.id.iv_create:
                CameraDialog addCameraDialog = new CameraDialog(getContext(), new CameraDialog.onClickInterface() {
                    @Override
                    public void onClick(String camera_id, String camera_name, String camera_col) {
                        mCameraPersenter.addCameraCol(camera_id,camera_name,camera_col);
                    }
                });
                addCameraDialog.show();
                break;
            case R.id.iv_editor:
                if (mCameraListAdapter.getSelectedPos() == -1) {
                    Toast.makeText(getContext(), "请选择要编辑的摄像头", Toast.LENGTH_SHORT).show();
                }else {
                    CameraDialog editCameraDialog = new CameraDialog(getContext(), mCameraListDatas.get(mCameraListAdapter.getSelectedPos()), new CameraDialog.onClickInterface() {
                        @Override
                        public void onClick(String camera_id, String camera_name, String camera_col) {
                            mCameraPersenter.editCameraCol(camera_id,camera_name,camera_col);
                        }
                    });
                    editCameraDialog.show();
                }

                break;
            case R.id.iv_delete:

                if (mCameraListAdapter.getSelectedPos() == -1) {
                    Toast.makeText(getContext(), "请选择要删除的摄像头", Toast.LENGTH_SHORT).show();
                }else {
                    SweetAlertDialogTools.ShowDialog(mActivity, "确定删除选中的摄像头吗？", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            mCameraPersenter.removeCameraCol(mCameraListDatas.get(mCameraListAdapter.getSelectedPos()).getCamera_id());
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void FindCameraColResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<CameraBean> mEquipTypeDatas = JSONArray.parseArray(datas,CameraBean.class);
            if (mCameraListDatas.size()>0){
                mCameraListDatas.clear();
            }
            mCameraListDatas.addAll(mEquipTypeDatas);
            mCameraListAdapter.notifyDataSetChanged();

        }else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void AddCameraColResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code==1){
            showLoadFailMsg("添加成功");
            initData();
        }else {
            showLoadFailMsg(jsonObject.getString("message"));

        }
    }

    @Override
    public void EditCameraColResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code==1){
            showLoadFailMsg("修改成功");
            initData();
        }else {
            showLoadFailMsg(jsonObject.getString("message"));

        }
    }

    @Override
    public void RemoveCameraColResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code==1){
            showLoadFailMsg("删除成功");
            mCameraListAdapter.setSelectedPos(-1);
            initData();
        }else {
            showLoadFailMsg(jsonObject.getString("message"));

        }
    }

    @Override
    public void showProgress() {

        mLoadingDialog.show();
    }

    @Override
    public void hideProgress() {
        mLoadingDialog.hide();
    }

    @Override
    public void showLoadFailMsg(String err) {

        Toast.makeText(mActivity,err,Toast.LENGTH_SHORT).show();
    }
}
