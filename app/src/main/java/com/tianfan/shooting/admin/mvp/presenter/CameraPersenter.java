package com.tianfan.shooting.admin.mvp.presenter;

import com.tianfan.shooting.admin.mvp.view.CameraView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

/**
 * @Name：Shooting
 * @Description：摄像头管理
 * @Author：Chen
 * @Date：2020/5/5 16:36
 * 修改人：Chen
 * 修改时间：2020/5/5 16:36
 */
public class CameraPersenter {
    private CameraView mCameraView;
    private BaseMode mBaseMode;

    public CameraPersenter(CameraView mCameraView) {
        this.mCameraView = mCameraView;
        this.mBaseMode = new BaseMode();
    }

    /**
     *  @author
     *  @time
     *  @describe 查找摄像头
     */
    public void findCameraCol(){
        String url  = ApiUrl.CameraApi.FindCameraCol;
        mCameraView.showProgress();
        RequestParams params = new RequestParams();

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mCameraView.FindCameraColResult(result);
                mCameraView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mCameraView.hideProgress();
                mCameraView.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     *  @author
     *  @time
     *  @describe 添加摄像头
     */
    public void addCameraCol(String camera_id,String camera_name,String camera_col){
        String url  = ApiUrl.CameraApi.AddCameraCol;
        mCameraView.showProgress();
        RequestParams params = new RequestParams();
        params.put("camera_id",camera_id);
        params.put("camera_name",camera_name);
        params.put("camera_col",camera_col);
        params.put("camera_status",String.valueOf(1));
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mCameraView.AddCameraColResult(result);
                mCameraView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mCameraView.hideProgress();
                mCameraView.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     *  @author
     *  @time
     *  @describe 修改摄像头
     */
    public void editCameraCol(String camera_id,String camera_name,String camera_col){
        String url  = ApiUrl.CameraApi.EditCameraCol;
        mCameraView.showProgress();
        RequestParams params = new RequestParams();
        params.put("camera_id",camera_id);
        params.put("camera_name",camera_name);
        params.put("camera_col",camera_col);
        params.put("camera_status",String.valueOf(1));
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mCameraView.EditCameraColResult(result);
                mCameraView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mCameraView.hideProgress();
                mCameraView.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     *  @author
     *  @time
     *  @describe 删除摄像头
     */
    public void removeCameraCol(String camera_id){
        String url  = ApiUrl.CameraApi.RemoveCameraCol;
        mCameraView.showProgress();
        RequestParams params = new RequestParams();
        params.put("camera_id",camera_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mCameraView.RemoveCameraColResult(result);
                mCameraView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mCameraView.hideProgress();
                mCameraView.showLoadFailMsg(result.toString());
            }
        });
    }
}
