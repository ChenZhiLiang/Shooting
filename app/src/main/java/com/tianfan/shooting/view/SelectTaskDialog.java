package com.tianfan.shooting.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.SelectTaskAdapter;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;
import java.util.List;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @Name：Shooting
 * @Description：选择任务
 * @Author：Chen
 * @Date：2020/4/25 21:50
 * 修改人：Chen
 * 修改时间：2020/4/25 21:50
 */
public class SelectTaskDialog extends Dialog implements View.OnClickListener {

    private ImageView image_back;
    private Button btn_task_select;
    private RecyclerView recycler_task;
    private SelectTaskAdapter mSelectTaskAdapter;
    List<TaskInfoBean> mTaskInfos;
    private onSeleteTaskInterface mOnSeleteTaskInterface;

    public SelectTaskDialog(@NonNull Context context,List<TaskInfoBean> mTaskInfos,onSeleteTaskInterface mOnSeleteTaskInterface) {
        super(context, R.style.alert_dialog);
        this.mTaskInfos = mTaskInfos;
        this.mOnSeleteTaskInterface = mOnSeleteTaskInterface;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_task);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
        image_back = findViewById(R.id.image_back);
        btn_task_select = findViewById(R.id.btn_task_select);
        recycler_task = findViewById(R.id.recycler_task);
        recycler_task.setLayoutManager(new LinearLayoutManager(getContext()));
        mSelectTaskAdapter = new SelectTaskAdapter(mTaskInfos);
        mSelectTaskAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                mOnSeleteTaskInterface.result(mTaskInfos.get(position));
                dismiss();
            }
        });
        recycler_task.setAdapter(mSelectTaskAdapter);

        image_back.setOnClickListener(this);
        btn_task_select.setOnClickListener(this);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        if (v==image_back){
            dismiss();
        }else if (v==btn_task_select){
            DatePickerDialog datePicker = new DatePickerDialog(getContext());
            datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    String result = year + "-" + (month + 1) + "-" + dayOfMonth;
                    btn_task_select.setText(result);
                    loadData(result);
                }
            });
            datePicker.show();
        }
    }


    private void loadData(String task_date){
        String url = ApiUrl.TaskApi.FindTaskInfo;
        RequestParams params = new RequestParams();
        params.put("task_date",task_date);
        params.put("task_status","0");

        new BaseMode().GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                JSONObject jsonObject = JSONObject.parseObject(result.toString());
                int code = jsonObject.getIntValue("code");
                if (code == 1) {
                    String datas = jsonObject.getString("datas");
                    List<TaskInfoBean> mDatas = JSONArray.parseArray(datas, TaskInfoBean.class);
                    if (mTaskInfos.size()>0){
                        mTaskInfos.clear();
                    }
                    mTaskInfos.addAll(mDatas);
                    mSelectTaskAdapter.notifyDataSetChanged();
                } else if (code==2){//无数据

                }else {
                    Toast.makeText(getContext(),jsonObject.getString("message"),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Object result) {
               Toast.makeText(getContext(),result.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public interface onSeleteTaskInterface{
        void result(TaskInfoBean mTaskInfoBean);
    }
}
