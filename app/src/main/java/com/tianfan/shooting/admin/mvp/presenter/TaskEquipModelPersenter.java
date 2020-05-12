package com.tianfan.shooting.admin.mvp.presenter;

import com.tianfan.shooting.admin.mvp.view.TaskEquipModelView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/12 23:04
 * 修改人：Chen
 * 修改时间：2020/4/12 23:04
 */
public class TaskEquipModelPersenter {

    private TaskEquipModelView mTaskEquipModelView;
    private BaseMode mBaseMode;

    public TaskEquipModelPersenter(TaskEquipModelView mTaskEquipModelView) {
        this.mTaskEquipModelView = mTaskEquipModelView;
        mBaseMode = new BaseMode();
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
                mTaskEquipModelView.findEquipModelTypeResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskEquipModelView.showLoadFailMsg(result.toString());
            }
        });
    }
    /**
     * 添加器材模板类型
     *  @author
     *  @time
     *  @describe
     */
    public void addEquipModelType(String equip_model_type_name,String equip_model_type_desc){
        mTaskEquipModelView.showProgress();

        String url = ApiUrl.EquipApi.AddEquipModelType;
        RequestParams params = new RequestParams();
        params.put("equip_model_type_name",equip_model_type_name);
        params.put("equip_model_type_desc",equip_model_type_desc);
        params.put("equip_model_type_status","1");
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipModelView.addEquipModelTypeResult(result);
                mTaskEquipModelView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mTaskEquipModelView.showLoadFailMsg(result.toString());
                mTaskEquipModelView.hideProgress();
            }
        });
    }
    /**
     *  @author
     *  @time
     *  @describe 删除器材模板类型
     */
    public void removeEquipModelType(String equip_model_type_id){
        mTaskEquipModelView.showProgress();
        String url = ApiUrl.EquipApi.RemoveEquipModelType;
        RequestParams params = new RequestParams();
        params.put("equip_model_type_id",equip_model_type_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipModelView.removeEquipModelTypeResult(result);
                mTaskEquipModelView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mTaskEquipModelView.showLoadFailMsg(result.toString());
                mTaskEquipModelView.hideProgress();
            }
        });
    }


}
