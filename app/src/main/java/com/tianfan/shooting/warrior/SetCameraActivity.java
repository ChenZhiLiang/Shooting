package com.tianfan.shooting.warrior;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tianfan.shooting.R;
import com.tianfan.shooting.tools.NewSPTools;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-11 08:51
 * @Description 设置摄像头
 */
public class SetCameraActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.sp_set_camera_sel_shoot_position)
    MaterialSpinner spinner;
    String [] spStrs = {"一号靶位","二号靶位","三号靶位","四号靶位","五号靶位","六号靶位","七号靶位","八号靶位","九号靶位","十号靶位"};
    String shootPosition ="1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_camera);
        ButterKnife.bind(this);
        spinner.setItems(spStrs);
        NewSPTools.getInstance().setContext(getApplicationContext());
        if (!NewSPTools.getInstance().getShootingPosition().equals("")){
            startActivity(new Intent(this, WarriorActivity.class));
            finish();
        }

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                shootiingPosition(position);
            }
        });
    }

    void shootiingPosition(int position){
        if (position==0){
            shootPosition = "1";
        }else if (position==1){
            shootPosition = "2";
        }else if (position==2){
            shootPosition = "3";
        }else if (position==3){
            shootPosition = "4";
        }else if (position==4){
            shootPosition = "5";
        }else if (position==5){
            shootPosition = "6";
        }else if (position==6){
            shootPosition = "7";
        }else if (position==7){
            shootPosition = "8";
        }else if (position==7){
            shootPosition = "9";
        }else if (position==9){
            shootPosition = "10";
        }
    }

    //点击事件监听
    @OnClick({R.id.set_camera_bt})
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.set_camera_bt){
            if (!shootPosition.equals("")){
                JSONObject jsonObject = new JSONObject();
                NewSPTools.getInstance().setShootingPosition(shootPosition);
//                NewSPTools.getInstance().setCamera(jsonObject);
                startActivity(new Intent(this, WarriorActivity.class));
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "请选择需要关联的靶位!", Toast.LENGTH_SHORT).show();
            }

        }
    }
}