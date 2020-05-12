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
     * 查询器材类型
     *  @author
     *  @time
     *  @describe
     */
    public void findEquipType(){
        mTaskEquipView.showProgress();
        String url = ApiUrl.EquipTypeApi.FindEquipType;
        mBaseMode.GetRequest(url, new RequestParams(), new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipView.findEquipTypeResult(result);
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
     *  @author
     *  @time
     *  @describe 查询任务信息
     */
    public void findTaskInfo(String task_id){
        String url = ApiUrl.TaskApi.FindTaskInfo;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipView.findTaskInfoResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskEquipView.showLoadFailMsg(result.toString());
            }
        });

    }
    /**
     * 查询器材模板类型
     *  @author
     *  @time
     *  @describe
     */
    public void findEquipModelType(){
        String url = ApiUrl.EquipApi.FindEquipModelType;
        mBaseMode.GetRequest(url, new RequestParams(), new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipView.findEquipModelTypeResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskEquipView.showLoadFailMsg(result.toString());
            }
        });
    }
    /**
     *  查找任务器材
     *  @author
     *  @time
     *  @describe
     */
    public void findTaskEquip(String task_id){
        String url = ApiUrl.TaskEquipApi.FindTaskEquip;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipView.findTaskEquipResult(result);
            }

            @Override
            public void onFailure(Object result) {
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
     *  @author
     *  @time
     *  @describe 添加任务器材
     */
    public void addTaskEquip(String task_id,String equip_model_item_id,String equip_type,String equip_name,String equip_unit,String equip_count){
        mTaskEquipView.showProgress();
        String url = ApiUrl.TaskEquipApi.AddTaskEquip;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("equip_model_type_id",equip_model_item_id);
        params.put("equip_type",equip_type);
        params.put("equip_name",equip_name);
        params.put("equip_unit",equip_unit);
        params.put("equip_count",equip_count);
        params.put("equip_status",String.valueOf(0));
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipView.addTaskEquipResult(result);
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
    public void changeTaskEquipCount(String task_id,String equip_model_item_id,int equip_count){
        String url = ApiUrl.TaskEquipApi.ChangeTaskEquipCount;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("equip_model_item_id",equip_model_item_id);
        params.put("equip_count",String.valueOf(equip_count));
        params.put("equip_status",String.valueOf(0));

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipView.changeTaskEquipCountResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskEquipView.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     *  修改任务器材状态
     *  @author
     *  @time
     *  @describe
     */
    public void editTaskInfo(String task_id,String task_equips){

        mTaskEquipView.showProgress();
        String url = ApiUrl.TaskEquipApi.EditTaskInfo;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("task_equips",task_equips);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipView.editTaskInfoResult(result);
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
