package com.tianfan.shooting.admin.mvp.presenter;

import android.text.TextUtils;

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

    public void findTaskInfo(String task_name,String task_site,int task_target_type,String task_date){
        mStatisticAnalysisView.showProgress();
        String url = ApiUrl.TaskApi.FindTaskInfo;
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(task_name)){
            params.put("task_name",task_name);
        }
        if (!TextUtils.isEmpty(task_site)){
            params.put("task_site",task_site);
        }
        if (!TextUtils.isEmpty(task_date)){
            params.put("task_date",task_date);
        }
        if (task_target_type!=0){
            params.put("task_target_type",String.valueOf(task_target_type));
        }
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
