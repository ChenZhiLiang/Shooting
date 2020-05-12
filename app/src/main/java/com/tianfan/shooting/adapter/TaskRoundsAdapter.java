package com.tianfan.shooting.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/5/2 15:04
 * 修改人：Chen
 * 修改时间：2020/5/2 15:04
 */
public class TaskRoundsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public TaskRoundsAdapter(@Nullable List<String> data) {
        super(R.layout.layout_task_rounds_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.addOnClickListener(R.id.ll_model_item);
        helper.setText(R.id.tv_task_round,item);
    }
}
