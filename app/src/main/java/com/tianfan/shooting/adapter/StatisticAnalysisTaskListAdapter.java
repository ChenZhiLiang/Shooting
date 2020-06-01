package com.tianfan.shooting.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.TaskInfoBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/6/2 0:29
 * 修改人：Chen
 * 修改时间：2020/6/2 0:29
 */
public class StatisticAnalysisTaskListAdapter extends BaseQuickAdapter<TaskInfoBean,BaseViewHolder> {

    public StatisticAnalysisTaskListAdapter(@Nullable List<TaskInfoBean> data) {
        super(R.layout.layout_statistic_analysis_task, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskInfoBean item) {
        helper.setText(R.id.tv_data_number, (helper.getAdapterPosition() + 1) + "");
        helper.setText(R.id.tv_task_name, item.getTask_name());
        helper.setText(R.id.tv_adress, item.getTask_site());
        helper.setText(R.id.tv_time, item.getTask_date());
        if (item.getTask_target_type().equals("1")) {
            helper.setText(R.id.tv_type, "胸环靶");
        } else {
            helper.setText(R.id.tv_type, "人形靶");
        }
    }
}
