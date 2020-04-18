package com.tianfan.shooting.admin.mvp.presenter;

import com.tianfan.shooting.admin.mvp.view.TaskView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

/**
 * @Name：Shooting
 * @Description：任务管理
 * @Author：Chen
 * @Date：2020/4/9 22:34
 * 修改人：Chen
 * 修改时间：2020/4/9 22:34
 */
public class TaskPresenter {

    private TaskView mTaskViewMode;
    private BaseMode mBaseMode;

    public TaskPresenter(TaskView mTaskViewMode) {
        this.mTaskViewMode = mTaskViewMode;
        mBaseMode = new BaseMode();
    }

    /**
     * 查询任务信息列表
     *  @author
     *  @time
     *  @describe
     */
    public void findTaskInfo(){
        mTaskViewMode.showProgress();
        String url = ApiUrl.TaskApi.FindTaskInfo;
        RequestParams params = new RequestParams();
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskViewMode.hideProgress();
                mTaskViewMode.FindTaskInfoResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskViewMode.hideProgress();
                mTaskViewMode.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     * 添加任务
     *  @author
     *  @time
     *  @describe
     */
    public void addTaskInfo(RequestParams params){
        mTaskViewMode.showProgress();
        String url = ApiUrl.TaskApi.AddTaskInfo;
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskViewMode.hideProgress();
                mTaskViewMode.AddTaskInfoResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskViewMode.hideProgress();
                mTaskViewMode.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     * 修改任务
     *  @author
     *  @time
     *  @describe
     */
    public void editTaskInfo(RequestParams params){
        mTaskViewMode.showProgress();
        String url = ApiUrl.TaskApi.EditTaskInfo;
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskViewMode.hideProgress();
                mTaskViewMode.EditTaskInfoResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskViewMode.hideProgress();
                mTaskViewMode.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     * 删除任务
     *  @author
     *  @time
     *  @describe
     */
    public void deleteTaskInfo(String task_id){
        mTaskViewMode.showProgress();
        String url = ApiUrl.TaskApi.RemoveTaskInfo;

        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskViewMode.hideProgress();
                mTaskViewMode.RemoveTaskInfoResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskViewMode.hideProgress();
                mTaskViewMode.showLoadFailMsg(result.toString());
            }
        });
    }
}
