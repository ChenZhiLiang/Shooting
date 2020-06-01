package com.tianfan.shooting.admin.mvp.presenter;

import com.tianfan.shooting.admin.mvp.view.StatisticAnalysisView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

/**
 * @Name：Shooting
 * @Description：统计分析
 * @Author：Chen
 * @Date：2020/6/2 0:22
 * 修改人：Chen
 * 修改时间：2020/6/2 0:22
 */
public class StatisticAnalysisPersenter {


    private StatisticAnalysisView mStatisticAnalysisView;
    private BaseMode mBaseMode;

    public StatisticAnalysisPersenter(StatisticAnalysisView mStatisticAnalysisView) {
        this.mStatisticAnalysisView = mStatisticAnalysisView;
        mBaseMode = new BaseMode();
    }

    /**
     * 查询已完成的任务列表
     *  @author
     *  @time
     *  @describe
     */
    public void findTaskInfo(){
        mStatisticAnalysisView.showProgress();
        String url = ApiUrl.TaskApi.FindTaskInfo;
        RequestParams params = new RequestParams();
        params.put("task_status",String.valueOf(2));
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mStatisticAnalysisView.hideProgress();
                mStatisticAnalysisView.FindTaskInfoResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mStatisticAnalysisView.hideProgress();
                mStatisticAnalysisView.showLoadFailMsg(result.toString());
            }
        });
    }
}
