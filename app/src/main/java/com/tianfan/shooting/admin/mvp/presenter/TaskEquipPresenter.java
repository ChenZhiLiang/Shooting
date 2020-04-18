package com.tianfan.shooting.admin.mvp.presenter;

import com.tianfan.shooting.admin.mvp.view.TaskEquipView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

/**
 * @Name：Shooting
 * @Description：器材管理
 * @Author：Chen
 * @Date：2020/4/12 22:16
 * 修改人：Chen
 * 修改时间：2020/4/12 22:16
 */
public class TaskEquipPresenter {

    private TaskEquipView mTaskEquipView;
    private BaseMode mBaseMode;

    public TaskEquipPresenter(TaskEquipView mTaskEquipView) {
        this.mTaskEquipView = mTaskEquipView;
        mBaseMode = new BaseMode();
    }

    /**
     *  查找任务器材
     *  @author
     *  @time
     *  @describe
     */
    public void findTaskEquip(String task_id){
        mTaskEquipView.showProgress();
        String url = ApiUrl.TaskEquipApi.FindTaskEquip;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipView.findTaskEquipResult(result);
                mTaskEquipView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mTaskEquipView.hideProgress();
                mTaskEquipView.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     * 生成任务器材
     *  @author
     *  @time
     *  @describe
     */
    public void createTaskEquip(String task_id,String equip_model_type_id){
        mTaskEquipView.showProgress();
        String url = ApiUrl.TaskEquipApi.CreateTaskEquip;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("equip_model_type_id",equip_model_type_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipView.createTaskEquip(result);
                mTaskEquipView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mTaskEquipView.hideProgress();
                mTaskEquipView.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     * 【调整任务器材数量】设置调整后的数量（加减数量）
     *  @author
     *  @time
     *  @describe
     */
    public void changeTaskEquipCount(String task_id,String equip_model_item_id,int equip_count,String equip_status){
        mTaskEquipView.showProgress();
        String url = ApiUrl.TaskEquipApi.ChangeTaskEquipCount;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("equip_model_item_id",equip_model_item_id);
        params.put("equip_count",String.valueOf(equip_count));
        params.put("equip_status",String.valueOf(equip_status));

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipView.changeTaskEquipCount(result);
                mTaskEquipView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mTaskEquipView.hideProgress();
                mTaskEquipView.showLoadFailMsg(result.toString());
            }
        });
    }
}
