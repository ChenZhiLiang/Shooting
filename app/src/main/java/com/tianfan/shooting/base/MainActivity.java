package com.tianfan.shooting.base;

import android.content.Intent;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.alibaba.fastjson.JSONObject;
import com.apkfuns.logutils.LogUtils;
import com.tianfan.shooting.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.rosuh.filepicker.config.FilePickerManager;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.bt)
    Button bt;

//    @BindView(R.id.bt2)
//    Button sendCommand;

    //后台定时任务
    private Handler handler = new Handler();
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooting_main);
        ButterKnife.bind(this);
        bt.setOnClickListener(this);
//        initsocket();
    }




    @OnClick(R.id.bt)
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt) {

            FilePickerManager.INSTANCE.from(this)
                    .maxSelectable(1).forResult(1123);

//

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //文件选择返回
        if (requestCode == 1123) {
            LogUtils.e(JSONObject.toJSONString(FilePickerManager.INSTANCE.obtainData()));
        }
    }
}
