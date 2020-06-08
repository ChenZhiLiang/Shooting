package com.tianfan.shooting.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.CommandManageBean;
import com.tianfan.shooting.scorer.mvp.presenter.ScorerPresenter;

import java.util.List;

public class TotalScoreAdapter extends BaseQuickAdapter<CommandManageBean.CommandManageItem.PersonScoreBean, BaseViewHolder> {


    public TotalScoreAdapter(@Nullable List<CommandManageBean.CommandManageItem.PersonScoreBean> data) {
        super(R.layout.layout_total_score, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommandManageBean.CommandManageItem.PersonScoreBean item) {

        int position = helper.getAdapterPosition()+1;
        helper.setText(R.id.tv_rounds,"第"+position+"轮");
        helper.setText(R.id.tv_10h_data,String.valueOf(item.getScore_10()));
        helper.setText(R.id.tv_9h_data,String.valueOf(item.getScore_9()));
        helper.setText(R.id.tv_8h_data,String.valueOf(item.getScore_8()));
        helper.setText(R.id.tv_7h_data,String.valueOf(item.getScore_7()));
        helper.setText(R.id.tv_6h_data,String.valueOf(item.getScore_6()));
        helper.setText(R.id.tv_5h_data,String.valueOf(item.getScore_5()));
        helper.setText(R.id.tv_hit_count,String.valueOf(item.getHit_count()));
        helper.setText(R.id.tv_hit_score,String.valueOf(item.getHit_score()));

    }
}
