package com.tianfan.shooting.admin.mvp.presenter;

import android.text.TextUtils;

import com.tianfan.shooting.admin.mvp.view.ScorerView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/5/24 23:32
 * 修改人：Chen
 * 修改时间：2020/5/24 23:32
 */
public class ScorerPersenter {

    private ScorerView mScorerView;
    private BaseMode mBaseMode;

    public ScorerPersenter(ScorerView mScorerView) {
        this.mScorerView = mScorerView;
        mBaseMode = new BaseMode();
    }

    /**
     *  @author
     *  @time
     *  @describe 查询任务
     */
    public void findTaskInfo(){
        mScorerView.showProgress();
        String url = ApiUrl.TaskApi.FindTaskInfo;
        RequestParams params = new RequestParams();
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mScorerView.FindTaskInfoResult(result);
                mScorerView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mScorerView.showLoadFailMsg(result.toString());
                mScorerView.hideProgress();
            }
        });
    }
}
