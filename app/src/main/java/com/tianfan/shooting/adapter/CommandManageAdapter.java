package com.tianfan.shooting.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.CommandManageBean;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/27 0:01
 * 修改人：Chen
 * 修改时间：2020/4/27 0:01
 */
public class CommandManageAdapter extends BaseQuickAdapter<CommandManageBean, BaseViewHolder> {

    private List<CommandManageBean> data;
    private onCheckPositionInterface mOnCheckPositionInterface;
    public CommandManageAdapter(@Nullable List<CommandManageBean> data,onCheckPositionInterface mOnCheckPositionInterface) {
        super(R.layout.layout_command_manage, data);
        this.data = data;
        this.mOnCheckPositionInterface =mOnCheckPositionInterface;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommandManageBean item) {
        RecyclerView mRecycleRank = helper.getView(R.id.recycler_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecycleRank.setLayoutManager(layoutManager);
        mRecycleRank.setNestedScrollingEnabled(false);
        CommandManageItemAdapter mCommandManageItemAdapter = new CommandManageItemAdapter(item.isSelectPostion(),helper.getAdapterPosition()+1,item.getDatas());
        mRecycleRank.setAdapter(mCommandManageItemAdapter);
        mCommandManageItemAdapter.setOnItemChildClickListener(new OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId()==R.id.layout_number_item){
                    mOnCheckPositionInterface.check(helper.getAdapterPosition());
                }
            }
        });
    }

    public interface onCheckPositionInterface{
        void check(int postion);
    }
}
