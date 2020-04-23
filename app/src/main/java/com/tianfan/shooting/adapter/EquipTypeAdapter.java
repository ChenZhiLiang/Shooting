package com.tianfan.shooting.adapter;

import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.EquipTypeBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：器材类型
 * @Author：Chen
 * @Date：2020/4/15 23:08
 * 修改人：Chen
 * 修改时间：2020/4/15 23:08
 */
public class EquipTypeAdapter extends BaseQuickAdapter<EquipTypeBean, BaseViewHolder> {
    private int mSelectedPos = -1;   //实现单选，保存当前选中的position


    public EquipTypeAdapter(@Nullable List<EquipTypeBean> data) {
        super(R.layout.layout_equip_type, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EquipTypeBean item) {
        int position = helper.getAdapterPosition();

        helper.setText(R.id.tv_number,String.valueOf(helper.getAdapterPosition()+1));
        helper.setText(R.id.tv_name,item.getEquip_model_type_name());
        helper.setText(R.id.tv_dec,item.getEquip_model_type_desc());
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
}
