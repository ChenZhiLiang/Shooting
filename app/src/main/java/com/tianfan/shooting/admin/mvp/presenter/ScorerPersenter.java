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

    /**
     * @author: czl
     * @description 计分
     * @date: 2020/5/25 14:10
     * @param person_row  队员分组（行）
     * @param person_col  队员靶位（列）
     */
    public void recordTaskPersonScore(String task_id,String person_row,String person_col,String rounds,String score_0, String score_5,String score_6,String score_7,String score_8,String score_9,String score_10){
        mScorerView.showProgress();
        String url = ApiUrl.ScoreApi.RecordTaskPersonScore;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("inuser","user_0001");
        params.put("person_row",person_row);
        params.put("person_col",person_col);
        params.put("rounds",rounds);
        params.put("score_0",TextUtils.isEmpty(score_0)?"0":score_0);
        params.put("score_5",TextUtils.isEmpty(score_5)?"0":score_5);
        params.put("score_6",TextUtils.isEmpty(score_6)?"0":score_6);
        params.put("score_7",TextUtils.isEmpty(score_7)?"0":score_7);
        params.put("score_8",TextUtils.isEmpty(score_8)?"0":score_8);
        params.put("score_9",TextUtils.isEmpty(score_9)?"0":score_9);
        params.put("score_10",TextUtils.isEmpty(score_10)?"0":score_10);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {

                mScorerView.RecordTaskPersonScoreResult(result);
                mScorerView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mScorerView.hideProgress();
                mScorerView.showLoadFailMsg(result.toString());
            }
        });

    }
}
