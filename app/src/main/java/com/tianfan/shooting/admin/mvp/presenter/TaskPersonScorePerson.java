package com.tianfan.shooting.admin.mvp.presenter;

import com.tianfan.shooting.admin.mvp.view.TaskPersonScoreView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

public class TaskPersonScorePerson {

    private TaskPersonScoreView mTaskPersonScoreView;
    private BaseMode mBaseMode;
    public TaskPersonScorePerson(TaskPersonScoreView mTaskPersonScoreView) {
        this.mTaskPersonScoreView = mTaskPersonScoreView;
        mBaseMode = new BaseMode();
    }

    /**
     *  @author
     *  @time
     *  @describe  查询分数
     * @param task_id 任务id
     */
    public void findTaskPersonScore(String task_id,boolean isShow){
        if (isShow){
            mTaskPersonScoreView.showProgress();
        }
        String url = ApiUrl.ScoreApi.FindTaskPersonScore;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("person_id","0");
        params.put("rounds",String.valueOf(1));

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskPersonScoreView.hideProgress();
                mTaskPersonScoreView.FindTaskPersonScoreResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskPersonScoreView.hideProgress();
                mTaskPersonScoreView.showLoadFailMsg(result.toString());
            }
        });
    }
}
