package com.tianfan.shooting.adapter;

import android.text.TextUtils;

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
 * @Date：2020/4/25 22:04
 * 修改人：Chen
 * 修改时间：2020/4/25 22:04
 */
public class SelectTaskAdapter extends BaseQuickAdapter<TaskInfoBean, BaseViewHolder> {


    public SelectTaskAdapter(@Nullable List<TaskInfoBean> data) {
        super(R.layout.layout_select_task_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskInfoBean item) {
        helper.addOnClickListener(R.id.layout_task_item);
        helper.setText(R.id.tv_task_name,item.getTask_name());
        int rounds = Integer.parseInt(item.getTask_rounds());
        if (rounds==0){
            helper.setText(R.id.tv_task_round,"未点名");
        }else{
            helper.setText(R.id.tv_task_round,"第"+rounds+"轮");
        }
        int status = Integer.parseInt(item.getTask_status());
        if (status==0){
            helper.setText(R.id.tv_task_state,"未开始");
        }else if (status==1){
            helper.setText(R.id.tv_task_state,"进行中");
        }else {
            helper.setText(R.id.tv_task_state,"已结束");
        }
    }
}
