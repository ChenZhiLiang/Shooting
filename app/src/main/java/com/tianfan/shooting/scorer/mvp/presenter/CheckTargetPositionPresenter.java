package com.tianfan.shooting.scorer.mvp.presenter;

import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;
import com.tianfan.shooting.scorer.mvp.view.CheckTargetPositionView;

/**
 * @Author: chenzl
 * @ClassName: CheckTargetPositionPresenter
 * @Description: java类作用描述
 * @CreateDate: 2020/5/25 10:52
 */
public class CheckTargetPositionPresenter {

    private CheckTargetPositionView mCheckTargetPositionView;
    private BaseMode mBaseMode;

    public CheckTargetPositionPresenter(CheckTargetPositionView mCheckTargetPositionView) {
        this.mCheckTargetPositionView = mCheckTargetPositionView;
        this.mBaseMode = new BaseMode();
    }
    /**
     *  @author
     *  @time
     *  @describe 查找摄像头
     */
    public void findCameraCol(){
        String url  = ApiUrl.CameraApi.FindCameraCol;
        mCheckTargetPositionView.showProgress();
        RequestParams params = new RequestParams();

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mCheckTargetPositionView.FindCameraColResult(result);
                mCheckTargetPositionView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mCheckTargetPositionView.hideProgress();
                mCheckTargetPositionView.showLoadFailMsg(result.toString());
            }
        });
    }

}
