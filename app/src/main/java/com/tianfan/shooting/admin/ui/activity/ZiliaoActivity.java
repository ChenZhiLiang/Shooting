package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.ui.fragment.CheckFragment;
import com.tianfan.shooting.admin.ui.fragment.HistoryFragment;
import com.tianfan.shooting.admin.ui.fragment.HomeFragment;
import com.tianfan.shooting.admin.ui.fragment.SetUpFragment;
import com.tianfan.shooting.admin.ui.fragment.ZhiHuiFrgment;

/**
 * @program: Shooting
 * @description: 显示被选中的activity
 * @author: lxf
 * @create: 2019-11-22 14:34
 **/
public class ZiliaoActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_frag);
        fragmentManager = getSupportFragmentManager();
        String type = getIntent().getStringExtra("type");
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (type.equals("shoot")){
            ft.add(R.id.rv_show_frag,new HomeFragment());
        }else if (type.equals("ziliao")){
            ft.add(R.id.rv_show_frag,new CheckFragment());
        }else if (type.equals("zhihui")){
            ft.add(R.id.rv_show_frag,new ZhiHuiFrgment());
        }else if (type.equals("fenxi")){
            ft.add(R.id.rv_show_frag,new HistoryFragment());
        }else if (type.equals("setup")){
            ft.add(R.id.rv_show_frag,new SetUpFragment());
        }
        ft.commit();

    }
}
