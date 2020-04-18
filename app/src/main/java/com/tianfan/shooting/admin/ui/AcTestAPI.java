package com.tianfan.shooting.admin.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.newnettools.NewNetTools;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.RequestBody;

/**
 * CreateBy：lxf
 * CreateTime： 2020-03-17 14:08
 */
public class AcTestAPI extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_api);

        findViewById(R.id.tv_01).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String parameter = "?"+"task_name="+"测试建立"
                        +"&task_site="+"测试地点"
                        +"&task_date="+"2020-03-20"
                        +"&task_row_count="+"10"
                        +"&task_row_persons="+"6"
                        +"&task_target_type="+"1"
                        +"&task_inuser="+"user_002";

//
//                FormBody.Builder builder = new FormBody.Builder();
//                builder.add("task_name","新建测试Post");
//                builder.add("task_site","测试地点嘿嘿");
//                builder.add("task_date","2020-03-20");
//                builder.add("task_row_count","8");
//                builder.add("task_row_persons","10");
//                builder.add("task_target_type","1");
//                builder.add("task_inuser","user_003");
//
//                RequestBody formBody = builder.build();


        NewNetTools.getTastList(new NewNetTools.NetCallBack() {
            @Override
            public void netOk(String data) {
                Log.e("成功回调","-------->"+data);
            }
            @Override
            public void netFail(String msg) {
                Toast.makeText(getApplicationContext(),""+msg,Toast.LENGTH_SHORT).show();
            }
        });
            }
        });
        findViewById(R.id.tv_02).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String actionDada = "?task_id="+"task_20200317010725179462"+
                        "&task_name="+"修改测试"+
                        "&task_site="+"地址修改测试"+
                        "&task_date="+"2028-01-16";

                NewNetTools.dobaseGet(NewNetTools.BaseURL, NewNetTools.editTask, actionDada, new NewNetTools.NetCallBack() {
                    @Override
                    public void netOk(String data) {
                        Log.e("修改成功回调","-------->"+data);
                    }

                    @Override
                    public void netFail(String msg) {
                        Log.e("修改netFail回调","-------->"+msg);
                    }
                });

            }
        });


        findViewById(R.id.tv_03).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actionDada = "?task_id="+"task_20200317010410358897";
                NewNetTools.dobaseGet(NewNetTools.BaseURL, NewNetTools.deleteTask, actionDada, new NewNetTools.NetCallBack() {
                    @Override
                    public void netOk(String data) {
                        Log.e("删除成功回调","-------->"+data);
                    }

                    @Override
                    public void netFail(String msg) {
                        Log.e("删除netFail回调","-------->"+msg);
                    }
                });

            }
        });

        findViewById(R.id.tv_04).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HashMap<String,String> map = new HashMap<>();
//                map.put("task_name","新建测试Post");
//                map.put("task_site","测试地点嘿嘿");
//                map.put("task_date","2020-03-20");
//                map.put("task_row_count","8");
//                map.put("task_row_persons","10");
//                map.put("task_target_type","1");
//                map.put("task_inuser","user_003");
//                NewNetTools.dobasePost(NewNetTools.BaseURL, NewNetTools.addTask,map, new NewNetTools.NetCallBack() {
//                    @Override
//                    public void netOk(String data) {
//                        Log.e("Post成功回调","-------->"+data);
//                    }
//                    @Override
//                    public void netFail(String msg) {
//                        Log.e("PostnetFail回调","-------->"+msg);
//                    }
//                });

            }
        });

    }

    public class TestPost{
        String name;
        String add;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAdd() {
            return add;
        }

        public void setAdd(String add) {
            this.add = add;
        }
    }
}
