package com.tianfan.shooting.adapter;

import androidx.annotation.Nullable;

import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.mvp.view.ViewHistory;

import java.util.List;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-12 10:18
 * @Description 功能描述
 */
public class ChooseTaskAdapter extends BaseQuickAdapter<JSONObject,BaseViewHolder> {

    public interface ClickHisCallbak{
        void click (JSONObject jsonObject);
    }
    ClickHisCallbak clickHisCallbak;
    public ChooseTaskAdapter(int layoutResId, @Nullable List<JSONObject> data, ClickHisCallbak callbak) {
        super(layoutResId, data);
        this.clickHisCallbak = callbak;
    }

    @Override
    protected void convert(BaseViewHolder helper, final JSONObject item) {
        helper.setText(R.id.item_task_name,""+item.getString("name"));
        helper.setText(R.id.item_task_create_time,""+item.getString("fnish_time"));
        helper.getView(R.id.history_item_root).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickHisCallbak.click(item);
            }
        });
    }
}
