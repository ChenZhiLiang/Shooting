package com.tianfan.shooting.adapter;

import android.content.Context;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.TaskRankBean;
import com.tianfan.shooting.view.hrecycler.CommonAdapter;
import com.tianfan.shooting.view.hrecycler.CommonViewHolder;
import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Name：Shooting
 * @Description：列队适配
 * @Author：Chen
 * @Date：2020/4/11 22:04
 * 修改人：Chen
 * 修改时间：2020/4/11 22:04
 */
public class TaskRankListAdapter extends CommonAdapter<TaskRankBean> {

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;
    public TaskRankListAdapter(Context context, List<TaskRankBean> dataList, OnItemClickListener mOnItemClickListener) {
        super(context, dataList, R.layout.layout_task_rank);
        this.mContext = context;
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public void bindData(CommonViewHolder holder, TaskRankBean data) {
        int position = holder.getAdapterPosition() + 1;
        holder.setText(R.id.gr_id, String.valueOf(position));
//        if (holder.getAdapterPosition()==0){
//            holder.getView(R.id.img).setVisibility(View.GONE);
//        }else {
//            holder.getView(R.id.img).setVisibility(View.GONE);
//
//        }
        holder.setText(R.id.tv_user_name, "第" + position + "组");
        RecyclerView mRecycleRank = holder.getView(R.id.recycler_rank_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleRank.setLayoutManager(layoutManager);
        mRecycleRank.setNestedScrollingEnabled(false);
        TaskRanItemAdapter mTaskRanItemAdapter = new TaskRanItemAdapter(data.getDatas());
        mRecycleRank.setAdapter(mTaskRanItemAdapter);
        mTaskRanItemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mOnItemClickListener.onItemChildClick(holder.getAdapterPosition(),position);
            }
        });

    }

    public interface OnItemClickListener{
        void onItemChildClick(int parentPostion, int childPosition);
    }
}
