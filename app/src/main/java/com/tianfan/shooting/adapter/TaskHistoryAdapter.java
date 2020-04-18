package com.tianfan.shooting.adapter;

import android.view.View;
import android.widget.CheckBox;
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
 * @Date：2020/4/9 23:10
 * 修改人：Chen
 * 修改时间：2020/4/9 23:10
 */
public class TaskHistoryAdapter extends BaseQuickAdapter<TaskInfoBean, BaseViewHolder> {

    private int mSelectedPos = -1;   //实现单选，保存当前选中的position

    public TaskHistoryAdapter(@Nullable List<TaskInfoBean> data) {
        super(R.layout.item_renwu_list_content, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskInfoBean item) {
        helper.addOnClickListener(R.id.layout_task_item);
        int position = helper.getAdapterPosition();
        helper.setText(R.id.tv_data_number, (helper.getAdapterPosition() + 1) + "");
        helper.setText(R.id.tv_task_name, item.getTask_name());
        helper.setText(R.id.tv_adress, item.getTask_site());
        helper.setText(R.id.tv_time, item.getTask_date());
        helper.setText(R.id.tv_ld_nmuber, String.valueOf(item.getTask_row_persons()));
        helper.setText(R.id.tv_dy_number, String.valueOf(item.getTask_row_count()));
        CheckBox checkBox = helper.itemView.findViewById(R.id.cb_select);
        if (mSelectedPos == position){
            checkBox.setChecked(true);
        }else {
            checkBox.setChecked(false);
        }
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()){
                    mSelectedPos = position;
                }else {
                    mSelectedPos = -1;
                }
                notifyDataSetChanged();
            }
        });
        if (item.getTask_target_type().equals("1")) {
            helper.setText(R.id.tv_type, "胸环靶");
        } else {
            helper.setText(R.id.tv_type, "人形靶");
        }
        if (item.getTask_status().equals("0")) {
            helper.setText(R.id.tv_status, "未开始");
        } else if (item.getTask_status().equals("1")){
            helper.setText(R.id.tv_status, "进行中");
        }else if (item.getTask_status().equals("2")){
            helper.setText(R.id.tv_status, "已完成");

        }else {
            helper.setText(R.id.tv_status, "已删除");
        }
    }

    public int getSelectedPos() {
        return mSelectedPos;
    }

    public void setSelectedPos(int mSelectedPos) {
        this.mSelectedPos = mSelectedPos;
    }
}

