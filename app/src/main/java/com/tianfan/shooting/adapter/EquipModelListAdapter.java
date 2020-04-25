package com.tianfan.shooting.adapter;

import android.view.View;
import android.widget.CheckBox;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.EquipModelBean;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * @Name：Shooting
 * @Description：模板列表适配
 * @Author：Chen
 * @Date：2020/4/25 0:09
 * 修改人：Chen
 * 修改时间：2020/4/25 0:09
 */
public class EquipModelListAdapter extends BaseQuickAdapter<EquipModelBean, BaseViewHolder> {

    private int mSelectedPos = -1;   //实现单选，保存当前选中的position

    public EquipModelListAdapter(@Nullable List<EquipModelBean> data) {
        super(R.layout.layout_equip_model_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EquipModelBean item) {
        int position = helper.getAdapterPosition();

        helper.addOnClickListener(R.id.layout_model);
        helper.setText(R.id.tv_model_number,String.valueOf(helper.getAdapterPosition()+1));
        helper.setText(R.id.tv_model_name,item.getEquip_model_type_name());
        helper.setText(R.id.tv_model_dec,item.getEquip_model_type_desc());
        CheckBox cb_select = helper.getView(R.id.check_model);
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
