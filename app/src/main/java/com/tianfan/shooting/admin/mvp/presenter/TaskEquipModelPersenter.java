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
     *
        【查询器材模板项目】
     *  @author
     *  @time
     *  @describe
     */
    public void findEquipModelItem(String equip_model_type_id){
        String url = ApiUrl.EquipApi.FindEquipModelItem;
        RequestParams params = new RequestParams();
        params.put("equip_model_type_id",equip_model_type_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipModelView.findEquipModelItemResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskEquipModelView.showLoadFailMsg(result.toString());
            }
        });
    }


    /**
     * 修改器材模板项目
     *  @author
     *  @time
     *  @describe
     */
    public void editEquipModelItem(String equip_model_type_id,String equip_model_item_id,String equip_type,String equip_name ,String equip_unit,int equip_count,String equip_status){
        String url = ApiUrl.EquipApi.EditEquipModelItem;
        RequestParams params = new RequestParams();
        params.put("equip_model_type_id",equip_model_type_id);
        params.put("equip_model_item_id",equip_model_item_id);
        params.put("equip_type",equip_type);
        params.put("equip_name",equip_name);
        params.put("equip_unit",equip_unit);
        params.put("equip_count",String.valueOf(equip_count));
        params.put("equip_status",equip_status);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskEquipModelView.EditEquipModelItemResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskEquipModelView.showLoadFailMsg(result.toString());
            }
        });
    }
}
