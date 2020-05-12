package com.tianfan.shooting.admin.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.EquipTypeAdapter;
import com.tianfan.shooting.admin.mvp.presenter.EquipTypePersenter;
import com.tianfan.shooting.admin.mvp.view.EquipTypeView;
import com.tianfan.shooting.base.BaseFragment;
import com.tianfan.shooting.bean.CameraBean;
import com.tianfan.shooting.bean.EquipTypeBean;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.view.CameraDialog;
import com.tianfan.shooting.view.EquipTypeDialog;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Name：Shooting
 * @Description：器材类型管理
 * @Author：Chen
 * @Date：2020/4/15 0:24
 * 修改人：Chen
 * 修改时间：2020/4/15 0:24
 */
public class EquipTypelFragment extends BaseFragment implements EquipTypeView {

    @BindView(R.id.recycler_equip_mode)
    RecyclerView recycler_equip_mode;
    private EquipTypeAdapter mEquipTypeAdapter;
    private List<EquipTypeBean> mDatas = new ArrayList<>();

    private EquipTypePersenter mEquipTypePersenter;
    public static EquipTypelFragment getInstance() {
        EquipTypelFragment hf = new EquipTypelFragment();
        return hf;
    }
    @Override
    public void loadingData() {

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_equip_type;
    }

    @Override
    public void initView(View view, Bundle savedInstanceState) {
        recycler_equip_mode.setLayoutManager(new LinearLayoutManager(getContext()));
        mEquipTypeAdapter = new EquipTypeAdapter(mDatas);
        recycler_equip_mode.setAdapter(mEquipTypeAdapter);

        mEquipTypePersenter = new EquipTypePersenter(this);
    }

    @Override
    public void initData() {
        mEquipTypePersenter.findEquipType();
    }

    @OnClick({R.id.iv_return_home,R.id.iv_create,R.id.iv_editor,R.id.iv_delete})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_return_home:
                getActivity().finish();
                break;
            case R.id.iv_create:
                EquipTypeDialog addEquipTypeDialog = new EquipTypeDialog(getContext(), new EquipTypeDialog.onClickInterface() {
                    @Override
                    public void onClick(String name, String desc, String equip_name, String equip_unit) {
                        mEquipTypePersenter.addEquipType(name,desc,equip_name,equip_unit);

                    }
                });
                addEquipTypeDialog.show();
                break;
            case R.id.iv_editor:
                if (mEquipTypeAdapter.getSelectedPos() == -1) {
                    Toast.makeText(getContext(), "请选择要编辑的器材类型", Toast.LENGTH_SHORT).show();
                }else {

                    EquipTypeDialog editEquipTypeDialog = new EquipTypeDialog(getContext(), mDatas.get(mEquipTypeAdapter.getSelectedPos()),new EquipTypeDialog.onClickInterface() {
                        @Override
                        public void onClick(String name, String desc, String equip_name, String equip_unit) {
                            mEquipTypePersenter.editEquipType(mDatas.get(mEquipTypeAdapter.getSelectedPos()).getEquip_type_id(),name,desc,equip_name,equip_unit);

                        }
                    });
                    editEquipTypeDialog.show();
                }
                break;
            case R.id.iv_delete:
                if (mEquipTypeAdapter.getSelectedPos() == -1) {
                    Toast.makeText(getContext(), "请选择要删除的器材类型", Toast.LENGTH_SHORT).show();
                }else {
                    SweetAlertDialogTools.ShowDialog(mActivity, "确定删除选中的器材类型吗？", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            mEquipTypePersenter.removeEquipType(mDatas.get(mEquipTypeAdapter.getSelectedPos()).getEquip_type_id());
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void findEquipTypeResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<EquipTypeBean> mEquipTypeDatas = JSONArray.parseArray(datas,EquipTypeBean.class);
            if (mDatas.size()>0){
                mDatas.clear();
            }
            mDatas.addAll(mEquipTypeDatas);
            mEquipTypeAdapter.notifyDataSetChanged();

        }else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void addEquipTypeResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code==1){
            showLoadFailMsg("添加成功");
            initData();
        }else {
            showLoadFailMsg(jsonObject.getString("message"));

        }
    }

    @Override
    public void editEquipTypeResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code==1){
            showLoadFailMsg("修改成功");
            initData();
        }else {
            showLoadFailMsg(jsonObject.getString("message"));

        }
    }

    @Override
    public void removeEquipTypeResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code==1){
            showLoadFailMsg("删除成功");
            mEquipTypeAdapter.setSelectedPos(-1);
            initData();
        }else {
            showLoadFailMsg(jsonObject.getString("message"));

        }
    }

    @Override
    public void showProgress() {
        mLoadingDialog.show();
    }

    @Override
    public void hideProgress() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showLoadFailMsg(String err) {
        Toast.makeText(mActivity,err,Toast.LENGTH_SHORT).show();
    }
}
