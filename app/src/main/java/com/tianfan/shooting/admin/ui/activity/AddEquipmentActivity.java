package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tianfan.shooting.R;
import com.tianfan.shooting.net.BaseService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-08 01:21
 * @Description 增加装备页面
 */
public class AddEquipmentActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.ae_sub_bt)
    Button subBt;
    @BindView(R.id.ae_ed)
    EditText inpuTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_equipment);
        ButterKnife.bind(this);
        subBt.setOnClickListener(this);
    }
    //点击事件监听
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ae_sub_bt) {
            if (inpuTV.getText().length() > 0) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BaseService.BaseURL)
                        .build();
                BaseService service = retrofit.create(BaseService.class);
                Map map = new HashMap();
                map.put("data", "" + inpuTV.getText());
                Call<ResponseBody> gitHubBeanCall = service.addEquipment(map);
                gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result = response.body().string();
                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call call, Throwable t) {
                    }
                });
            } else {
                Toast.makeText(getApplicationContext(), "请输入装备名字", Toast.LENGTH_LONG).show();
            }
        }
    }
}