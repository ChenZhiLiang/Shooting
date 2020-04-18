package com.tianfan.shooting.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.TaskRankItemBean;
import com.tianfan.shooting.network.api.ApiUrl;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/11 23:57
 * 修改人：Chen
 * 修改时间：2020/4/11 23:57
 */
public class TaskRanItemAdapter extends BaseQuickAdapter<TaskRankItemBean, BaseViewHolder> {

    public TaskRanItemAdapter(@Nullable List<TaskRankItemBean> data) {
        super(R.layout.layout_task_ran_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TaskRankItemBean item) {
        helper.addOnClickListener(R.id.lv);
        RelativeLayout lv = helper.getView(R.id.lv);
        RoundedImageView iv_user_icon = helper.getView(R.id.iv);
        ImageView img_add = helper.getView(R.id.img_add);
        TextView tv_user_name = helper.getView(R.id.tv_user_name);

        if (item!=null&& !TextUtils.isEmpty(item.getTask_id())){
            lv.setBackgroundResource(R.drawable.view_item_bg);
            iv_user_icon.setVisibility(View.VISIBLE);
            tv_user_name.setVisibility(View.VISIBLE);
            img_add.setVisibility(View.INVISIBLE);
            tv_user_name.setText(item.getPerson_name());
            if (TextUtils.isEmpty(item.getPerson_head())) {
                Glide.with(mContext).load(R.drawable.user_icon).into(iv_user_icon);
            } else {
                Glide.with(mContext).load(ApiUrl.HOST_HEAD + item.getPerson_head()).error(R.drawable.user_icon).into(iv_user_icon);
            }
        }else {
            lv.setBackgroundResource(R.drawable.view_liedui_user_grenn_bg);
            iv_user_icon.setVisibility(View.INVISIBLE);
            tv_user_name.setVisibility(View.INVISIBLE);
            img_add.setVisibility(View.VISIBLE);
        }
    }
}
