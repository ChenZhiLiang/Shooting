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
import com.tianfan.shooting.bean.CommandManageBean;
import com.tianfan.shooting.network.api.ApiUrl;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/27 0:11
 * 修改人：Chen
 * 修改时间：2020/4/27 0:11
 */
public class CommandManageItemAdapter extends BaseQuickAdapter<CommandManageBean.CommandManageItem, BaseViewHolder> {


    private int number;
    private boolean selectPostion;
    private int currentRounds = -1;//当前轮次

    public CommandManageItemAdapter(boolean selectPostion,int number, @Nullable List<CommandManageBean.CommandManageItem> data) {
        super(R.layout.layout_command_manage_item, data);
        this.number = number;
        this.selectPostion =selectPostion;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommandManageBean.CommandManageItem item) {
        helper.addOnClickListener(R.id.lv);
        helper.addOnClickListener(R.id.layout_number_item);

        RelativeLayout layout_number_item = helper.getView(R.id.layout_number_item);

        TextView tv_number_position = helper.getView(R.id.tv_number_position);
        TextView tv_number_name = helper.getView(R.id.tv_number_name);
        TextView tv_score = helper.getView(R.id.tv_score);
        if (selectPostion) {
            helper.getView(R.id.image_translucence).setVisibility(View.GONE);
            helper.getView(R.id.image_number_translucence).setVisibility(View.GONE);
            tv_score.setVisibility(View.VISIBLE);
            if (item.getPerson_score()!=null&&item.getPerson_score().size()>0){
                for (int i = 0;i<item.getPerson_score().size();i++){
                    if (Integer.parseInt(item.getTask_rounds())==item.getPerson_score().get(i).getRounds()){
                        tv_score.setText(item.getPerson_score().get(i).getHit_count()+"/"+item.getPerson_score().get(i).getHit_score());
                    }
                }
            }else {
                tv_score.setText("0/0");
            }
        } else {
            helper.getView(R.id.image_translucence).setVisibility(View.VISIBLE);
            helper.getView(R.id.image_number_translucence).setVisibility(View.VISIBLE);
            tv_score.setVisibility(View.INVISIBLE);

        }
        RelativeLayout lv = helper.getView(R.id.lv);
        if (helper.getAdapterPosition() == 0) {
            layout_number_item.setVisibility(View.VISIBLE);
            lv.setVisibility(View.GONE);
            tv_number_position.setText(String.valueOf(number));
            tv_number_name.setText("第" + number + "组");
            tv_score.setVisibility(View.INVISIBLE);
        } else {
            layout_number_item.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
            RoundedImageView iv_user_icon = helper.getView(R.id.iv);
            LinearLayout linear_status_show = helper.getView(R.id.linear_status_show);
            ImageView img_add = helper.getView(R.id.img_add);
            TextView tv_user_name = helper.getView(R.id.tv_user_name);
            if (item != null && !TextUtils.isEmpty(item.getTask_id())) {
                lv.setBackgroundResource(R.drawable.backgroup_command_manage);
                iv_user_icon.setVisibility(View.VISIBLE);
                tv_user_name.setVisibility(View.VISIBLE);
                img_add.setVisibility(View.INVISIBLE);
                tv_user_name.setText(item.getPerson_name());
                if (item.getPerson_status().equals("0")) {
                    linear_status_show.setVisibility(View.VISIBLE);
                    iv_user_icon.setVisibility(View.GONE);
                } else {
                    linear_status_show.setVisibility(View.GONE);
                    iv_user_icon.setVisibility(View.VISIBLE);
                    if (TextUtils.isEmpty(item.getPerson_head())) {
                        Glide.with(mContext).load(R.drawable.user_icon).into(iv_user_icon);
                    } else {
                        Glide.with(mContext).load(ApiUrl.HOST_HEAD + item.getPerson_head()).error(R.drawable.user_icon).into(iv_user_icon);
                    }
                }
            } else {
                lv.setBackgroundResource(R.drawable.backgroup_command_manage_green);
                iv_user_icon.setVisibility(View.INVISIBLE);
                tv_user_name.setVisibility(View.INVISIBLE);
                linear_status_show.setVisibility(View.INVISIBLE);
                img_add.setVisibility(View.VISIBLE);
                tv_score.setVisibility(View.INVISIBLE);

            }
        }
    }

    public void setCurrentRounds(int currentRounds) {
        this.currentRounds = currentRounds;
    }
}
