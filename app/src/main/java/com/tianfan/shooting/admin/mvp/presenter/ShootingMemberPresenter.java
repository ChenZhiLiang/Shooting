package com.tianfan.shooting.admin.mvp.presenter;

import com.tianfan.shooting.admin.mvp.view.ShootingMemberView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/5/26 22:21
 * 修改人：Chen
 * 修改时间：2020/5/26 22:21
 */
public class ShootingMemberPresenter {
    private ShootingMemberView mShootingMemberView;
    private BaseMode mBaseMode;

    public ShootingMemberPresenter(ShootingMemberView mShootingMemberView) {
        this.mShootingMemberView = mShootingMemberView;
        mBaseMode = new BaseMode();
    }

    public void findTaskInfo(){
        mShootingMemberView.showProgress();
        String url = ApiUrl.TaskApi.FindTaskInfo;
        RequestParams params = new RequestParams();
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mShootingMemberView.FindTaskInfoResult(result);
                mShootingMemberView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mShootingMemberView.showLoadFailMsg(result.toString());
                mShootingMemberView.hideProgress();
            }
        });
    }

    /**
     *  @author
     *  @time
     *  @describe 根据靶位 组数 查找队员
     */
    public void findTaskPerson(String task_id,String person_row,String person_col){
        mShootingMemberView.showProgress();
        String url = ApiUrl.TaskPersonApi.FindTaskPerson;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("person_row",person_row);
        params.put("person_col",person_col);
        params.put("task_person_type","2");

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mShootingMemberView.FindTaskPersonResult(result);
                mShootingMemberView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mShootingMemberView.showLoadFailMsg(result.toString());
                mShootingMemberView.hideProgress();
            }
        });
    }

    /**
     *  @author
     *  @time
     *  @describe 查找队员分数
     */
    public void findTaskPersonScore(String task_id,String rounds,String person_id){
        mShootingMemberView.showProgress();
        String url = ApiUrl.ScoreApi.FindTaskPersonScore;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("rounds",rounds);
        params.put("person_id",person_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mShootingMemberView.FindTaskPersonScoreResult(result);
                mShootingMemberView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mShootingMemberView.showLoadFailMsg(result.toString());
                mShootingMemberView.hideProgress();
            }
        });

    }
}
