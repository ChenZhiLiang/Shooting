package com.tianfan.shooting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.CommandManageBean;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/6/2 17:54
 * 修改人：Chen
 * 修改时间：2020/6/2 17:54
 */
public class TaskPersonScoreItemAdapter extends RecyclerView.Adapter<TaskPersonScoreItemAdapter.ScrollViewHolder> {


    private Context context;
    private List<CommandManageBean.CommandManageItem.PersonScoreBean> rightDatas;

    public TaskPersonScoreItemAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<CommandManageBean.CommandManageItem.PersonScoreBean> rightDatas) {
        this.rightDatas = rightDatas;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ScrollViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layotu_person_score_item, viewGroup, false);
        return new ScrollViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScrollViewHolder scrollViewHolder, int i) {
//        scrollViewHolder.tv_score.setText(rightDatas.get(i).getHit_score()+"");
//        scrollViewHolder.tv_all_score.setText(rightDatas.get(i).getAll_count()+"");

    }

    @Override
    public int getItemCount() {
        return null == rightDatas ? 0 : rightDatas.size();
    }

    class ScrollViewHolder extends RecyclerView.ViewHolder {

        TextView tv_score;
        TextView tv_all_score;

        public ScrollViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_score = itemView.findViewById(R.id.tv_score);
            tv_all_score = itemView.findViewById(R.id.tv_all_score);

        }
    }
}
