package com.tianfan.shooting.admin.mvp.presenter;

import com.tianfan.shooting.admin.mvp.view.EquipModeDetailView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

import java.io.File;

/**
 * @Name：Shooting
 * @Description器材模板详情
 * @Author：Chen
 * @Date：2020/4/25 17:39
 * 修改人：Chen
 * 修改时间：2020/4/25 17:39
 */
public class EquipModeDetailPersenter {

    private EquipModeDetailView mEquipModeDetailView;
    private BaseMode  mBaseMode;

    public EquipModeDetailPersenter(EquipModeDetailView mEquipModeDetailView) {
        this.mEquipModeDetailView = mEquipModeDetailView;
        mBaseMode = new BaseMode();
    }
    /**
     * 查询器材类型
     *  @author
     *  @time
     *  @describe
     */
    public void findEquipType(){
        String url = ApiUrl.EquipTypeApi.FindEquipType;
        mBaseMode.GetRequest(url, new RequestParams(), new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipModeDetailView.findEquipTypeResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mEquipModeDetailView.showLoadFailMsg(result.toString());
            }
        });
    }

     /**
     *【查询器材模板项目】
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
                mEquipModeDetailView.findEquipModelItemResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mEquipModeDetailView.showLoadFailMsg(result.toString());
            }
        });
    }


    /**
     *  @author
     *  @time
     *  @describe 生成器材模板项目
      */
    public void addEquipModelItem(String equip_model_type_id,String equip_type,String equip_name,String equip_unit,String equip_count,String equip_count_take){
        mEquipModeDetailView.showProgress();
        String url = ApiUrl.EquipApi.AddEquipModelItem;
        RequestParams params = new RequestParams();
        params.put("equip_model_type_id",equip_model_type_id);
        params.put("equip_type",equip_type);
        params.put("equip_name",equip_name);
        params.put("equip_unit",equip_unit);
        params.put("equip_count",equip_count);
        params.put("equip_count_take",equip_count_take);

        params.put("equip_status","1");
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipModeDetailView.addEquipModelItemResult(result);
                mEquipModeDetailView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mEquipModeDetailView.showLoadFailMsg(result.toString());
                mEquipModeDetailView.hideProgress();
            }
        });
    }

    /***
     *  @author
     *  @time
     *  @describe 删除器材模板项
     */
    public void removeEquipModelItem(String equip_model_item_id){
        mEquipModeDetailView.showProgress();
        String url = ApiUrl.EquipApi.RemoveEquipModelItem;
        RequestParams params = new RequestParams();
        params.put("equip_model_item_id",equip_model_item_id);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipModeDetailView.removeEquipModelItemResult(result);
                mEquipModeDetailView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mEquipModeDetailView.showLoadFailMsg(result.toString());
                mEquipModeDetailView.hideProgress();
            }
        });

    }
    /**
     * 修改器材模板项目
     *  @author
     *  @time
     *  @describe
     */
    public void editEquipModelItem(String equip_model_type_id,String equip_model_item_id,String equip_type,String equip_name ,String equip_unit,int equip_count,int equip_count_take,String equip_status){
        String url = ApiUrl.EquipApi.EditEquipModelItem;
        RequestParams params = new RequestParams();
        params.put("equip_model_type_id",equip_model_type_id);
        params.put("equip_model_item_id",equip_model_item_id);
        params.put("equip_type",equip_type);
        params.put("equip_name",equip_name);
        params.put("equip_unit",equip_unit);
        params.put("equip_count",String.valueOf(equip_count));
        params.put("equip_count_take",String.valueOf(equip_count_take));

        params.put("equip_status",equip_status);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mEquipModeDetailView.EditEquipModelItemResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mEquipModeDetailView.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     *  @author
     *  @time
     *  @describe 导入器材模板项目
     */
    public void importEquipModelItem(String equip_model_type_id, File file){

        mEquipModeDetailView.showProgress();
        String url = ApiUrl.EquipApi.ImportEquipModelItem;
        RequestParams params = new RequestParams();
        params.put("equip_model_type_id",equip_model_type_id);
        params.fileParams.put("file",file);

        mBaseMode.MultiPostRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                if (file.exists()){
                    file.delete();
                }
                mEquipModeDetailView.ImportEquipModelItemResult(result);
                mEquipModeDetailView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mEquipModeDetailView.showLoadFailMsg(result.toString());
                mEquipModeDetailView.hideProgress();
            }
        });
    }
}
