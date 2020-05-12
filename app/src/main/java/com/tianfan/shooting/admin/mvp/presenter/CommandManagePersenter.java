package com.tianfan.shooting.admin.mvp.presenter;

import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.admin.mvp.view.CommandManageView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

import java.util.List;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/25 23:07
 * 修改人：Chen
 * 修改时间：2020/4/25 23:07
 */
public class CommandManagePersenter {

    private CommandManageView mCommandManageView;
    private BaseMode mBaseMode;

    public CommandManagePersenter(CommandManageView mCommandManageView) {
        this.mCommandManageView = mCommandManageView;
        mBaseMode = new BaseMode();
    }


    public void findTaskInfo(){
        findTaskInfo("");
    }
    /**
     *  @author
     *  @time
     *  @describe 查询任务
     */
    public void findTaskInfo(String task_date){
        mCommandManageView.showProgress();
        String url = ApiUrl.TaskApi.FindTaskInfo;
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(task_date)){
            params.put("task_date",task_date);
        }
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mCommandManageView.FindTaskInfoResult(result);
                mCommandManageView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mCommandManageView.showLoadFailMsg(result.toString());
                mCommandManageView.hideProgress();
            }
        });
    }


    /**
     *  @author
     *  @time
     *  @describe 修改任务
     * @param task_rounds 任务射击 0点名  >=1 具体轮次
     * @param task_status 任务状态 0未开始 1进行中 2 已结束
     */
    public void editTaskInfo(String task_id,int task_rounds,int task_status){
        mCommandManageView.showProgress();
        String url = ApiUrl.TaskApi.EditTaskInfo;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("task_rounds",String.valueOf(task_rounds));
        params.put("task_status",String.valueOf(task_status));

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mCommandManageView.EditTaskInfoResult(result);
                mCommandManageView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mCommandManageView.showLoadFailMsg(result.toString());
                mCommandManageView.hideProgress();
            }
        });
    }
    /**
     *  @author
     *  @time
     *  @describe  查询分数
     * @param task_id 任务id
     * @param rounds 计分轮次
     */
    public void findTaskPersonScore(String task_id,int rounds,boolean isShow){
        if (isShow){
            mCommandManageView.showProgress();

        }
        String url = ApiUrl.ScoreApi.FindTaskPersonScore;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("person_id","0");
        params.put("rounds",String.valueOf(rounds));

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mCommandManageView.hideProgress();
                mCommandManageView.FindTaskPersonScoreResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mCommandManageView.hideProgress();
                mCommandManageView.showLoadFailMsg(result.toString());
            }
        });

    }
}
