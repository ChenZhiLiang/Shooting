package com.tianfan.shooting.adapter;

import android.widget.Button;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.TaskEquipBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：器材模板
 * @Author：Chen
 * @Date：2020/4/22 1:20
 * 修改人：Chen
 * 修改时间：2020/4/22 1:20
 */
public class EquipModelAdapter extends BaseQuickAdapter<TaskEquipBean, BaseViewHolder> {


    public EquipModelAdapter(@Nullable List<TaskEquipBean> data) {
        super(R.layout.layout_equip_model, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskEquipBean item) {

        helper.addOnClickListener(R.id.img_add);
        helper.addOnClickListener(R.id.img_reduce);
        helper.addOnClickListener(R.id.btn_delete);
        helper.setText(R.id.tv_number,String.valueOf(helper.getAdapterPosition()+1));
        helper.setText(R.id.tv_equip_type,item.getEquip_type());
        helper.setText(R.id.tv_equip_name,item.getEquip_name());
        helper.setText(R.id.tv_equip_unit,item.getEquip_unit());
        helper.setText(R.id.tv_equip_count,String.valueOf(item.getEquip_count()));

    }
}
