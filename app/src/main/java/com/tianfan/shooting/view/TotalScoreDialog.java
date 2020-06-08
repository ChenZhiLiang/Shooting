package com.tianfan.shooting.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.TotalScoreAdapter;
import com.tianfan.shooting.bean.CommandManageBean;

import java.util.List;

/**
 * 总成绩
 **/
public class TotalScoreDialog extends Dialog {

    private TextView tv_title;
    private TextView tv_task_rows;
    private TextView tv_all_10_data;
    private TextView tv_all_9_data;
    private TextView tv_all_8_data;
    private TextView tv_all_7_data;
    private TextView tv_all_6_data;
    private TextView tv_all_5_data;
    private TextView tv_all_hit_count_data;
    private TextView tv_all_hit_score_data;

    private RecyclerView recycler_scorer;
    private Context mContext;
    private TotalScoreAdapter mTotalScoreAdapter;

    private int camera_col;
    private String task_rows;
    private List<CommandManageBean.CommandManageItem> datas;

    public TotalScoreDialog(@NonNull Context context, String task_rows, int camera_col, List<CommandManageBean.CommandManageItem> datas) {
        super(context, R.style.alert_dialog);
        this.mContext = context;
        this.datas = datas;
        this.camera_col = camera_col;
        this.task_rows = task_rows;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_total_score);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(camera_col + "号靶位(射击员：" + datas.get(0).getPerson_name() + ")");
        tv_task_rows = findViewById(R.id.tv_task_rows);
        tv_task_rows.setText("第" + task_rows + "组");
        tv_all_10_data = findViewById(R.id.tv_all_10_data);
        tv_all_9_data = findViewById(R.id.tv_all_9_data);
        tv_all_8_data = findViewById(R.id.tv_all_8_data);
        tv_all_7_data = findViewById(R.id.tv_all_7_data);
        tv_all_6_data = findViewById(R.id.tv_all_6_data);
        tv_all_5_data = findViewById(R.id.tv_all_5_data);
        tv_all_hit_count_data = findViewById(R.id.tv_all_hit_count_data);
        tv_all_hit_score_data = findViewById(R.id.tv_all_hit_score_data);
        recycler_scorer = findViewById(R.id.recycler_scorer);
        recycler_scorer.setLayoutManager(new LinearLayoutManager(mContext));
        if (datas.get(0).getPerson_score()!=null&&datas.get(0).getPerson_score().size()>0){
            mTotalScoreAdapter = new TotalScoreAdapter(datas.get(0).getPerson_score());
            recycler_scorer.setAdapter(mTotalScoreAdapter);
            recycler_scorer.setNestedScrollingEnabled(false);
            for (int i = 0; i < datas.get(0).getPerson_score().size(); i++) {
                tv_all_10_data.setText(String.valueOf(Integer.parseInt(tv_all_10_data.getText().toString())+datas.get(0).getPerson_score().get(i).getScore_10()));
                tv_all_9_data.setText(String.valueOf(Integer.parseInt(tv_all_9_data.getText().toString())+datas.get(0).getPerson_score().get(i).getScore_9()));
                tv_all_8_data.setText(String.valueOf(Integer.parseInt(tv_all_8_data.getText().toString())+datas.get(0).getPerson_score().get(i).getScore_8()));
                tv_all_7_data.setText(String.valueOf(Integer.parseInt(tv_all_7_data.getText().toString())+datas.get(0).getPerson_score().get(i).getScore_7()));
                tv_all_6_data.setText(String.valueOf(Integer.parseInt(tv_all_6_data.getText().toString())+datas.get(0).getPerson_score().get(i).getScore_6()));
                tv_all_5_data.setText(String.valueOf(Integer.parseInt(tv_all_5_data.getText().toString())+datas.get(0).getPerson_score().get(i).getScore_5()));
                tv_all_hit_count_data.setText(String.valueOf(Integer.parseInt(tv_all_hit_count_data.getText().toString())+datas.get(0).getPerson_score().get(i).getHit_count()));
                tv_all_hit_score_data.setText(String.valueOf(Integer.parseInt(tv_all_hit_score_data.getText().toString())+datas.get(0).getPerson_score().get(i).getHit_score()));
            }
        }
    }
}
