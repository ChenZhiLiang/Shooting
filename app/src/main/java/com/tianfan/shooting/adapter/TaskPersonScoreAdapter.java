package com.tianfan.shooting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.tianfan.shooting.R;
import com.tianfan.shooting.bean.CommandManageBean;
import com.tianfan.shooting.view.CustomHorizontalScrollView;
import com.tianfan.shooting.view.hrecycler.CommonAdapter;
import com.tianfan.shooting.view.hrecycler.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/6/2 17:35
 * 修改人：Chen
 * 修改时间：2020/6/2 17:35
 */
public class TaskPersonScoreAdapter extends RecyclerView.Adapter<TaskPersonScoreAdapter.ItemViewHolder> {


    private Context context;
    private List<CommandManageBean.CommandManageItem> datas;
    private List<ItemViewHolder> mViewHolderList = new ArrayList<>();
    private int offestX = 0;
    private OnContentScrollListener onContentScrollListener;

    public interface OnContentScrollListener {
        void onScroll(int offestX);
    }

    public void setOnContentScrollListener(OnContentScrollListener onContentScrollListener) {
        this.onContentScrollListener = onContentScrollListener;
    }


    public TaskPersonScoreAdapter(Context context) {
        this.context = context;
    }

    public void setDatas(List<CommandManageBean.CommandManageItem> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_task_person_score, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder itemViewHolder, int i) {
        itemViewHolder.tvNumber.setText(String.valueOf(i+1));
        itemViewHolder.tvName.setText(datas.get(i).getPerson_name());
        //右边滑动部分
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        itemViewHolder.rvItemRight.setLayoutManager(linearLayoutManager);
        itemViewHolder.rvItemRight.setHasFixedSize(true);
        TaskPersonScoreItemAdapter rightScrollAdapter = new TaskPersonScoreItemAdapter(context);
        List<CommandManageBean.CommandManageItem.PersonScoreBean> PersonScoreBeans =new ArrayList<>();
        if (datas.get(i).getPerson_score().size()==0){
            //添加一个总成绩item
            PersonScoreBeans.add(new CommandManageBean.CommandManageItem.PersonScoreBean());
//            PersonScoreBeans.add(new CommandManageBean.CommandManageItem.PersonScoreBean());
        }else {
            CommandManageBean.CommandManageItem.PersonScoreBean mPersonScoreBean = new CommandManageBean.CommandManageItem.PersonScoreBean();
            int score = 0;
            int allScore = 0;
            for (int j =0;j<datas.get(i).getPerson_score().size();j++){
                score = score+datas.get(i).getPerson_score().get(j).getHit_count();
                allScore = allScore+datas.get(i).getPerson_score().get(j).getHit_score();
            }
            mPersonScoreBean.setHit_count(score);
            mPersonScoreBean.setHit_score(allScore);
            PersonScoreBeans.add(mPersonScoreBean);
            PersonScoreBeans.addAll(datas.get(i).getPerson_score());
        }
        rightScrollAdapter.setDatas(PersonScoreBeans);
        itemViewHolder.rvItemRight.setAdapter(rightScrollAdapter);
        //缓存当前holder
        if (!mViewHolderList.contains(itemViewHolder)) {
            mViewHolderList.add(itemViewHolder);
        }
        //滑动单个ytem的rv时,右侧整个区域的联动
        itemViewHolder.horItemScrollview.setOnCustomScrollChangeListener(new CustomHorizontalScrollView.OnCustomScrollChangeListener() {
            @Override
            public void onCustomScrollChange(CustomHorizontalScrollView listener, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                for (int i = 0; i < mViewHolderList.size(); i++) {
                    ItemViewHolder touchViewHolder = mViewHolderList.get(i);
                    if (touchViewHolder != itemViewHolder) {
                        touchViewHolder.horItemScrollview.scrollTo(scrollX, 0);
                    }
                }
                //记录滑动距离,便于处理下拉刷新之后的还原操作
                if (null != onContentScrollListener) onContentScrollListener.onScroll(scrollX);
                offestX = scrollX;
            }
        });
        //由于viewHolder的缓存,在1级缓存取出来是2个viewholder,并且不会被重新赋值,所以这里需要处理缓存的viewholder的位移
        itemViewHolder.horItemScrollview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!itemViewHolder.isLayoutFinish()) {
                    itemViewHolder.horItemScrollview.scrollTo(offestX, 0);
                    itemViewHolder.setLayoutFinish(true);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return null == datas ? 0 : datas.size();
    }

    public List<ItemViewHolder> getViewHolderCacheList() {
        return mViewHolderList;
    }

    public int getOffestX() {
        return offestX;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_name)
        TextView tvName;
//        @BindView(R.id.tv_score)
//        TextView tvScore;
//        @BindView(R.id.tv_all_score)
//        TextView tvAllScore;
        @BindView(R.id.recycler_score_list)
        RecyclerView rvItemRight;
        @BindView(R.id.hor_item_scrollview)
        public CustomHorizontalScrollView horItemScrollview;
        private boolean isLayoutFinish;//自定义字段,用于标记layout

        public boolean isLayoutFinish() {
            return isLayoutFinish;
        }

        public void setLayoutFinish(boolean layoutFinish) {
            isLayoutFinish = layoutFinish;
        }

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
