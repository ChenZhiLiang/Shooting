package com.tianfan.shooting.adapter;

import android.text.TextUtils;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.TaskEquipBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：任务器材管理
 * @Author：Chen
 * @Date：2020/4/12 21:44
 * 修改人：Chen
 * 修改时间：2020/4/12 21:44
 */
public class TaskEquipListAdapter extends BaseQuickAdapter<TaskEquipBean, BaseViewHolder> {


    public TaskEquipListAdapter(@Nullable List<TaskEquipBean> data) {
        super(R.layout.layout_task_equip_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskEquipBean item) {

        helper.addOnClickListener(R.id.img_add);
        helper.addOnClickListener(R.id.img_reduce);
        helper.addOnClickListener(R.id.img_add_take_count);
        helper.addOnClickListener(R.id.img_reduce_take_count);
        helper.addOnClickListener(R.id.tv_equip_take_count);
        helper.addOnClickListener(R.id.tv_equip_count);

//        helper.addOnClickListener(R.id.check_equip_status);

        helper.setText(R.id.tv_number,String.valueOf(helper.getAdapterPosition()+1));
        helper.setText(R.id.tv_equip_type,item.getEquip_type());
        helper.setText(R.id.tv_equip_name,item.getEquip_name());
        helper.setText(R.id.tv_equip_unit,item.getEquip_unit());
        helper.setText(R.id.tv_equip_count,String.valueOf(item.getEquip_count()));
        helper.setText(R.id.tv_equip_take_count,String.valueOf(item.getEquip_count_take()));

//        CheckBox checkBox = helper.getView(R.id.check_equip_status);
//        if (!TextUtils.isEmpty(item.getEquip_status())&&Integer.parseInt(item.getEquip_status())==1){
//            checkBox.setChecked(true);
//        }else {
//            checkBox.setChecked(false);
//        }

    }
}
