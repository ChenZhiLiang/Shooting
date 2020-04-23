package com.tianfan.shooting.admin.mvp.presenter;

import com.tianfan.shooting.admin.mvp.view.EquipTypeView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

/**
 * @Name：Shooting
 * @Description：器材类型
 * @Author：Chen
 * @Date：2020/4/22 0:27
 * 修改人：Chen
 * 修改时间：2020/4/22 0:27
 */
public class EquipTypePersenter {

    private EquipTypeView mEquipTypeView;
    private BaseMode mBaseMode;

    public EquipTypePersenter(EquipTypeView mEquipTypeView) {
        this.mEquipTypeView = mEquipTypeView;
        mBaseMode = new BaseMode();
    }

    /**
     * 查询器材模板类型
     *  @author
     *  @time
     *  @describe
     */
    public void findEquipModelType(){
        mEquipTypeView.showProgress();
        String url = ApiUrl.EquipApi.FindEquipModelType;
        mBaseMode.GetRequest(url, new RequestParams(), new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipTypeView.findEquipModelTypeResult(result);
                mEquipTypeView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mEquipTypeView.showLoadFailMsg(result.toString());
                mEquipTypeView.hideProgress();
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
        mEquipTypeView.showProgress();

        String url = ApiUrl.EquipApi.AddEquipModelType;
        RequestParams params = new RequestParams();
        params.put("equip_model_type_name",equip_model_type_name);
        params.put("equip_model_type_desc",equip_model_type_desc);
        params.put("equip_model_type_status","1");
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipTypeView.addEquipModelTypeResult(result);
                mEquipTypeView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mEquipTypeView.showLoadFailMsg(result.toString());
                mEquipTypeView.hideProgress();
            }
        });
    }

    /**
     *  @author
     *  @time
     *  @describe  修改器材模板类型
     */
    public void editEquipModelType(String equip_model_type_id,String equip_model_type_name,String equip_model_type_desc){
        mEquipTypeView.showProgress();

        String url = ApiUrl.EquipApi.EditEquipModelType;
        RequestParams params = new RequestParams();
        params.put("equip_model_type_id",equip_model_type_id);
        params.put("equip_model_type_name",equip_model_type_name);
        params.put("equip_model_type_desc",equip_model_type_desc);
        params.put("equip_model_type_status","1");
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipTypeView.editEquipModelTypeResult(result);
                mEquipTypeView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mEquipTypeView.showLoadFailMsg(result.toString());
                mEquipTypeView.hideProgress();
            }
        });

    }

    /**
     *  @author
     *  @time
     *  @describe  删除器材模板类型
     */
    public void removeEquipModelType(String equip_model_type_id){
        mEquipTypeView.showProgress();
        String url = ApiUrl.EquipApi.RemoveEquipModelType;
        RequestParams params = new RequestParams();
        params.put("equip_model_type_id",equip_model_type_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipTypeView.removeEquipModelTypeResult(result);
                mEquipTypeView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mEquipTypeView.showLoadFailMsg(result.toString());
                mEquipTypeView.hideProgress();
            }
        });

    }
}
