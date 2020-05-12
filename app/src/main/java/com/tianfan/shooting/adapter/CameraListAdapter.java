package com.tianfan.shooting.adapter;

import android.view.View;
import android.widget.CheckBox;

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

    private int mSelectedPos = -1;   //实现单选，保存当前选中的position

    public CameraListAdapter(@Nullable List<CameraBean> data) {
        super(R.layout.layout_camera_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CameraBean item) {
        int position = helper.getAdapterPosition();

        helper.setText(R.id.tv_number,String.valueOf(helper.getAdapterPosition()+1));
        helper.setText(R.id.tv_camera_id,item.getCamera_id());
        helper.setText(R.id.tv_camera_name,item.getCamera_name());
        helper.setText(R.id.tv_camera_col,String.valueOf(item.getCamera_col()));
        CheckBox cb_select = helper.getView(R.id.cb_select);
        if (mSelectedPos == position){
            cb_select.setChecked(true);
        }else {
            cb_select.setChecked(false);
        }
        cb_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_select.isChecked()){
                    mSelectedPos = position;
                }else {
                    mSelectedPos = -1;
                }
                notifyDataSetChanged();
            }
        });
    }
    public int getSelectedPos() {
        return mSelectedPos;
    }

    public void setSelectedPos(int mSelectedPos) {
        this.mSelectedPos = mSelectedPos;
    }
}
