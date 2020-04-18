package com.tianfan.shooting.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.CameraBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：摄像头列表适配
 * @Author：Chen
 * @Date：2020/4/15 22:29
 * 修改人：Chen
 * 修改时间：2020/4/15 22:29
 */
public class CameraListAdapter extends BaseQuickAdapter<CameraBean, BaseViewHolder> {


    public CameraListAdapter(@Nullable List<CameraBean> data) {
        super(R.layout.layout_camera_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CameraBean item) {

        helper.setText(R.id.tv_number,String.valueOf(helper.getAdapterPosition()+1));
    }
}
