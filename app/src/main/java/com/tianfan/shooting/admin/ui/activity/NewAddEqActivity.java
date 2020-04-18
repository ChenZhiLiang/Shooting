package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;

import com.tianfan.shooting.admin.mvp.view.SubEQDialogView;
import com.tianfan.shooting.admin.ui.dialog.SumitQEDialog;
import com.tianfan.shooting.net.BaseService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-24 10:28
 * @Description 增加装备页面
 */
public class NewAddEqActivity extends AppCompatActivity implements View.OnClickListener, SubEQDialogView {


    @BindView(R.id.list_add_qe_activity)
    RecyclerView recyclerView;
    List<JSONObject> list = new ArrayList<>();
    AddPicActivityAdapter addPicActivityAdapter;
    @BindView(R.id.ed_name)
    EditText name;
    @BindView(R.id.ed_number)
    EditText number;
    @BindView(R.id.ed_type)
    EditText type;

    //    ed_type
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newadd_eq);
        ButterKnife.bind(this);
        addPicActivityAdapter = new AddPicActivityAdapter(R.layout.item_add_qe_activity, list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(addPicActivityAdapter);

    }
    void delete(int positon){
        list.remove(positon);
        addPicActivityAdapter.notifyDataSetChanged();
    }

    @Override
    public void submitClick(String result) {
        Log.e("提交回调---开始发送任务请求","-----"+result);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.BaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data",JSONObject.toJSONString(list));
        jsonObject.put("name",result);
        Map map = new HashMap();
        map.put("data", "" + jsonObject);

        Call<ResponseBody> gitHubBeanCall = service.addEquipment(map);
        gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String result = response.body().string();
                    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });


    }

    class AddPicActivityAdapter extends BaseQuickAdapter<JSONObject, BaseViewHolder> {

        public AddPicActivityAdapter(int layoutResId, @Nullable List<JSONObject> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(final BaseViewHolder helper, JSONObject item) {
            helper.setText(R.id.name, item.getString("name"));
            helper.setText(R.id.number, item.getString("number")+"/"+item.getString("type"));
            helper.getView(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    delete(helper.getPosition());

                }
            });
        }
    }

    @OnClick({R.id.add_bt,R.id.summit})
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_bt) {
            if (name.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "装备名称不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (number.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "装备数量不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            if (type.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "计量单位不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", name.getText().toString());
            jsonObject.put("number", number.getText().toString());
            jsonObject.put("type", type.getText().toString());
            list.add(jsonObject);
            addPicActivityAdapter.notifyDataSetChanged();
        }else if (v.getId()==R.id.summit){
            if (list.size()<1){
                Toast.makeText(getApplicationContext(),"数据不能为空",Toast.LENGTH_SHORT).show();
                return;
            }

            SumitQEDialog sumitQEDialog = new SumitQEDialog(this,this);
            sumitQEDialog.show();
        }
    }

}

