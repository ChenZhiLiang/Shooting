package com.tianfan.shooting.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.CameraBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/5/24 21:52
 * 修改人：Chen
 * 修改时间：2020/5/24 21:52
 */
public class CheckTargetPositionAdapter extends BaseQuickAdapter<CameraBean, BaseViewHolder> {
    public CheckTargetPositionAdapter(@Nullable List<CameraBean> data) {
        super(R.layout.layout_check_target_position, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CameraBean item) {
        helper.addOnClickListener(R.id.layout_main);
        helper.setText(R.id.tv_number,String.valueOf(helper.getAdapterPosition()+1));
        helper.setText(R.id.tv_camera_id,item.getCamera_id());
        helper.setText(R.id.tv_camera_name,item.getCamera_name());
        helper.setText(R.id.tv_camera_col,String.valueOf(item.getCamera_col()));
    }
}
