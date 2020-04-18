package com.tianfan.shooting.adapter;

import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.TaskPersonBean;

import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：队员适配
 * @Author：Chen
 * @Date：2020/4/11 11:48
 * 修改人：Chen
 * 修改时间：2020/4/11 11:48
 */
public class TaskPersonListAdapter extends BaseQuickAdapter<TaskPersonBean, BaseViewHolder> {


    public TaskPersonListAdapter(@Nullable List<TaskPersonBean> data) {
        super(R.layout.item_duiuyuan_content, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskPersonBean item) {

        helper.addOnClickListener(R.id.cbox);
        int position = helper.getAdapterPosition();
        helper.setText(R.id.tv_item_duiyuan_number, String.valueOf(position + 1));
        helper.setText(R.id.tv_item_duiyuan_group,String.valueOf(item.getPerson_row()));
        helper.setText(R.id.tv_item_duiyuan_user_type,item.getPerson_role());
        helper.setText(R.id.tv_item_duiyuan_name,item.getPerson_name());
        helper.setText(R.id.tv_item_duiyuan_work_unit,item.getPerson_orga());
        helper.setText(R.id.tv_item_duiyuan_id_number,item.getPerson_idno());
        helper.setText(R.id.tv_item_duiyuan_postion,String.valueOf(item.getPerson_col()));
        CheckBox cbox = helper.getView(R.id.cbox);
        cbox.setChecked(item.isSelect());

    }
}
