package com.tianfan.shooting.admin.ui.mdziliao;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * CreateBy：lxf
 * CreateTime： 2020-03-04 07:22
 */
public class AddUser  extends AppCompatActivity {
   @BindView(R.id.ed_user_name)
    EditText ed_user_name;

    @BindView(R.id.ed_user_bingyi)
     EditText ed_user_bingyi;

    @BindView(R.id.ed_user_zhiwu)
    EditText ed_user_zhiwu;

    @BindView(R.id.ed_user_type)
    EditText ed_user_type;

    @BindView(R.id.ed_user_work_unit)
    EditText ed_user_work_unit;

    @BindView(R.id.ed_user_id_number)
    EditText ed_user_id_number;

    @BindView(R.id.ed_user_tell)
    EditText ed_user_tell;

    @BindView(R.id.ed_user_position)
    EditText ed_user_position;


    @BindView(R.id.ed_user_group)
    EditText ed_user_group;

    @BindView(R.id.ed_user_remark)
    EditText ed_user_remark;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_add_user);
        ButterKnife.bind(this);

        findViewById(R.id.id_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.comfir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               docallBack();
            }
        });
    }

    void docallBack(){
        if (ed_user_name.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"用户名不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed_user_bingyi.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"兵役情况不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed_user_zhiwu.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"职务不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed_user_type.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"角色组不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed_user_work_unit.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"工作单位不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed_user_id_number.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"身份证号码不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed_user_tell.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"联系方式不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed_user_group.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"所在组别不能为空",Toast.LENGTH_SHORT).show();
            return;
        }

//        @BindView(R.id.ed_user_group)
//        EditText ed_user_group;
//
//        @BindView(R.id.ed_user_remark)
//        EditText ed_user_remark;





        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name",ed_user_name.getText().toString());
        jsonObject.put("user_id_card",ed_user_id_number.getText().toString());
        jsonObject.put("position",ed_user_position.getText().toString());
        jsonObject.put("work_unit",ed_user_work_unit.getText().toString());
        jsonObject.put("group_type",ed_user_type.getText().toString());
        jsonObject.put("zhiwu",ed_user_zhiwu.getText().toString());
        jsonObject.put("bingyi",ed_user_bingyi.getText().toString());
        jsonObject.put("tell",ed_user_tell.getText().toString());
        jsonObject.put("group",ed_user_group.getText().toString());
        jsonObject.put("remark",ed_user_remark.getText().toString());
        EventBus.getDefault().post(new AddUserEven(jsonObject));
        finish();
    }
}
