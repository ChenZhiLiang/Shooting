package com.tianfan.shooting.admin.mvp.presenter;

import com.tianfan.shooting.admin.mvp.view.TaskRankView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

/**
 * @Name：Shooting
 * @Description：队列管理
 * @Author：Chen
 * @Date：2020/4/12 18:03
 * 修改人：Chen
 * 修改时间：2020/4/12 18:03
 */
public class TaskRankPresenter {

    private TaskRankView mTaskRankView;
    private BaseMode mBaseMode;

    public TaskRankPresenter(TaskRankView mTaskRankView) {
        this.mTaskRankView = mTaskRankView;
        mBaseMode = new BaseMode();
    }


    /**
     *
     【队列管理-生成初始化】从队员管理中取数据，添加到队列管理
     *  @author
     *  @time
     *  @describe
     */

    public void createTaskPersonRowcol(String task_id){

        mTaskRankView.showProgress();
        String url = ApiUrl.TaskPersonApi.CreateTaskPersonRowcol;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskRankView.createTaskPersonRowcolResult(result);
                mTaskRankView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mTaskRankView.hideProgress();
                mTaskRankView.showLoadFailMsg(result.toString());
            }
        });
    }
}
