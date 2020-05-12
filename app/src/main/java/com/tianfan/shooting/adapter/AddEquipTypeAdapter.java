package com.tianfan.shooting.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.EquipTypeBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/5/5 18:44
 * 修改人：Chen
 * 修改时间：2020/5/5 18:44
 */
public class AddEquipTypeAdapter extends BaseQuickAdapter<EquipTypeBean, BaseViewHolder> {
    public AddEquipTypeAdapter(@Nullable List<EquipTypeBean> data) {
        super(R.layout.layout_task_rounds_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EquipTypeBean item) {
        helper.addOnClickListener(R.id.ll_model_item);
        helper.setText(R.id.tv_task_round,item.getEquip_type_name());
    }
}
