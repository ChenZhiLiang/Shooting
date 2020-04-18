package com.tianfan.shooting.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.EquipTypeBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：器材类型
 * @Author：Chen
 * @Date：2020/4/15 23:08
 * 修改人：Chen
 * 修改时间：2020/4/15 23:08
 */
public class EquipTypeAdapter extends BaseQuickAdapter<EquipTypeBean, BaseViewHolder> {


    public EquipTypeAdapter(@Nullable List<EquipTypeBean> data) {
        super(R.layout.layout_equip_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EquipTypeBean item) {

        helper.setText(R.id.tv_number,String.valueOf(helper.getAdapterPosition()+1));
    }
}
