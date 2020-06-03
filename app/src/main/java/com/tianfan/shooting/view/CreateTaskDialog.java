package com.tianfan.shooting.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.ui.activity.StatisticAnalysisActivity;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.network.okhttp.request.RequestParams;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import butterknife.OnClick;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/10 0:18
 * 修改人：Chen
 * 修改时间：2020/4/10 0:18
 */
public class CreateTaskDialog extends Dialog implements View.OnClickListener {

    private String type = "1";

    private EditText ed_task_name;
    private EditText ed_task_addr;

    private TextView ed_task_time;

//    private ImageView image_add;
//    private TextView tv_number;
//    private ImageView image_reduce;
//
//    private ImageView image_row_add;
//    private TextView tv_row_count;
//    private ImageView image_row_reduce;

    private CreateCallBack mCreateCallBack;

    private TaskInfoBean mTaskInfos;//创建时为空  、编辑时不为空
    private Context context;
    public CreateTaskDialog(@NonNull Context context, CreateCallBack mCreateCallBack) {
        super(context,R.style.alert_dialog);
        this.mCreateCallBack = mCreateCallBack;
        this.context = context;
    }

    public CreateTaskDialog(Context context, TaskInfoBean mTaskInfos, CreateCallBack mCreateCallBack){
        super(context,R.style.alert_dialog);
        this.context = context;

        this.mTaskInfos = mTaskInfos;
        this.mCreateCallBack = mCreateCallBack;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_create_task);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();

    }

    public void chageType(CheckBox cb_xhb) {
        if (cb_xhb.isChecked()) {
            type = "1";
        } else {
            type = "2";
        }
    }

    private void initView() {

        findViewById(R.id.id_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        ed_task_name = findViewById(R.id.ed_task_name);
        ed_task_addr = findViewById(R.id.ed_task_addr);
        ed_task_time = findViewById(R.id.ed_task_time);
//        image_add = findViewById(R.id.image_add);
//        image_reduce = findViewById(R.id.image_reduce);
//        tv_number = findViewById(R.id.tv_number);
//        image_row_add = findViewById(R.id.image_row_add);
//        image_row_reduce = findViewById(R.id.image_row_reduce);
//        tv_row_count = findViewById(R.id.tv_row_count);
        CheckBox cb_xhb = findViewById(R.id.cb_xhb);
        cb_xhb.setChecked(true);
        CheckBox cbxbb = findViewById(R.id.cbxbb);
//        image_add.setOnClickListener(this);
//        image_reduce.setOnClickListener(this);
//        image_row_add.setOnClickListener(this);
//        image_row_reduce.setOnClickListener(this);
        cb_xhb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_xhb.isChecked()) {
                    cbxbb.setChecked(false);
                } else {
                    cbxbb.setChecked(true);
                }
                chageType(cb_xhb);
            }
        });

        cbxbb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbxbb.isChecked()) {
                    cb_xhb.setChecked(false);
                } else {
                    cb_xhb.setChecked(true);
                }
                chageType(cb_xhb);
            }
        });

        findViewById(R.id.ed_task_time).setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                DatePickerDialog datePicker = new DatePickerDialog(context);
                datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String result = "" + year + "-" + (month + 1) + "-" + dayOfMonth;
                        ((TextView) findViewById(R.id.ed_task_time)).setText(result);
                    }
                });
                datePicker.show();
            }
        });

        findViewById(R.id.comfir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(ed_task_name.getText().toString())) {
                    Toast.makeText(getContext(), "请输入任务名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ed_task_addr.getText().toString())) {
                    Toast.makeText(getContext(), "请输入任务地址", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(ed_task_time.getText().toString())) {
                    Toast.makeText(getContext(), "请选择任务时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                RequestParams params = new RequestParams();
                params.put("task_name",ed_task_name.getText().toString());
                params.put("task_site",ed_task_addr.getText().toString());
                params.put("task_date",ed_task_time.getText().toString());
                if (mTaskInfos==null){
                    params.put("task_row_count",String.valueOf(100));
                    params.put("task_row_persons",String.valueOf(10));
                    params.put("task_target_type",type);
                    params.put("task_inuser","admin");
                }else {
                    params.put("task_id",mTaskInfos.getTask_id());
                }
                mCreateCallBack.result(params);
                dismiss();
            }
        });

        if (mTaskInfos!=null){
            ed_task_name.setText(mTaskInfos.getTask_name());
            ed_task_addr.setText(mTaskInfos.getTask_site());
            ed_task_time.setText(mTaskInfos.getTask_date());
//            tv_number.setText(String.valueOf(mTaskInfos.getTask_row_persons()));
//            tv_row_count.setText(String.valueOf(mTaskInfos.getTask_row_count()));
            if (mTaskInfos.getTask_target_type().equals("1")){
                cb_xhb.setChecked(true);
                cbxbb.setChecked(false);
            }else {
                cb_xhb.setChecked(false);
                cbxbb.setChecked(true);
            }

//            image_add.setOnClickListener(this);
//            image_reduce.setOnClickListener(this);
//            image_row_add.setOnClickListener(this);
//            image_row_reduce.setOnClickListener(this);
//            image_add.setEnabled(false);
//            image_reduce.setEnabled(false);
//            image_row_add.setEnabled(false);
//            image_row_reduce.setEnabled(false);
            cb_xhb.setEnabled(false);
            cbxbb.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {

//        int number = Integer.parseInt(tv_number.getText().toString());
//        int row = Integer.parseInt(tv_row_count.getText().toString());
//        if (v==image_add){
//            number++;
//            tv_number.setText(String.valueOf(number));
//        }else if (v==image_reduce){
//            if (number==1){
//                Toast.makeText(getContext(),"列队数量不能小于1",Toast.LENGTH_SHORT).show();
//            }else {


//                number--;
//                tv_number.setText(String.valueOf(number));
//            }
//        }else if (v==image_row_add){
//            row++;
//            tv_row_count.setText(String.valueOf(row));
//
//        }else if (v==image_row_reduce){
//            if (row==1){
//                Toast.makeText(getContext(),"队员数量不能小于1",Toast.LENGTH_SHORT).show();
//            }else {
//                number--;
//                tv_row_count.setText(String.valueOf(number));
//            }
//        }
    }

    public interface CreateCallBack {
        void result(RequestParams params);
    }
}
