package com.tianfan.shooting.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.EquipModelBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/12 22:58
 * 修改人：Chen
 * 修改时间：2020/4/12 22:58
 */
public class TaskEquipModelAdapter extends BaseQuickAdapter<EquipModelBean, BaseViewHolder> {

    public TaskEquipModelAdapter(@Nullable List<EquipModelBean> data) {
        super(R.layout.layout_task_equip_model, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EquipModelBean item) {
        helper.addOnClickListener(R.id.ll_model_item);
        helper.setText(R.id.tv_modle_name,item.getEquip_model_type_name());
    }
}
