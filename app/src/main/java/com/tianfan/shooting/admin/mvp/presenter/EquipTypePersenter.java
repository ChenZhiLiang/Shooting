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
     * 查询器材类型
     *  @author
     *  @time
     *  @describe
     */
    public void findEquipType(){
        mEquipTypeView.showProgress();
        String url = ApiUrl.EquipTypeApi.FindEquipType;
        mBaseMode.GetRequest(url, new RequestParams(), new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipTypeView.findEquipTypeResult(result);
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
     * 添加器材类型
     *  @author
     *  @time
     *  @describe
     */
    public void addEquipType(String equip_type_name,String equip_type_desc,String equip_name,String equip_unit){
        mEquipTypeView.showProgress();
        String url = ApiUrl.EquipTypeApi.AddEquipType;
        RequestParams params = new RequestParams();
        params.put("equip_type_name",equip_type_name);
        params.put("equip_type_desc",equip_type_desc);
        params.put("equip_name",equip_name);
        params.put("equip_unit",equip_unit);
        params.put("equip_type_status","1");
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipTypeView.addEquipTypeResult(result);
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
     *  @describe  修改器材类型
     */
    public void editEquipType(String equip_type_id,String equip_type_name,String equip_type_desc,String equip_name,String equip_unit){
        mEquipTypeView.showProgress();
        String url = ApiUrl.EquipTypeApi.EditEquipType;
        RequestParams params = new RequestParams();
        params.put("equip_type_id",equip_type_id);
        params.put("equip_type_name",equip_type_name);
        params.put("equip_type_desc",equip_type_desc);
        params.put("equip_name",equip_name);
        params.put("equip_unit",equip_unit);
        params.put("equip_type_status","1");
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipTypeView.editEquipTypeResult(result);
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
     *  @describe  删除器材类型
     */
    public void removeEquipType(String equip_type_id){
        mEquipTypeView.showProgress();
        String url = ApiUrl.EquipTypeApi.RemoveEquipType;
        RequestParams params = new RequestParams();
        params.put("equip_type_id",equip_type_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipTypeView.removeEquipTypeResult(result);
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
