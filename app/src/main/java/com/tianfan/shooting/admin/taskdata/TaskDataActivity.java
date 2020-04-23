package com.tianfan.shooting.admin.taskdata;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * CreateBy：lxf
 * CreateTime： 2020-03-19 9:37
 */
public class TaskDataActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.zlmd_dygl)
    TextView DuiYuan;
    @BindView(R.id.zlmd_ldgl)
    TextView LieDui;
    @BindView(R.id.zlmd_qcgl)
    TextView QiCai;
    List<TextView> list = new ArrayList<>();
    FraDuiyuan fraDuiyuan;
    FragLieDui fragLieDui;
    FragQiCai fragQiCai;
    private FragmentManager fragmentManager;
    FragmentTransaction ft;
    private String task_id;
    private String task_name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_task_ed_main);
        ButterKnife.bind(this);
        task_id = getIntent().getStringExtra("task_id");
        task_name = getIntent().getStringExtra("task_name");
        list.add(DuiYuan);
        list.add(LieDui);
        list.add(QiCai);
        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();
        fraDuiyuan = FraDuiyuan.getInstance(task_id,task_name);
        fragLieDui = FragLieDui.getInstance(task_id, task_name);
        fragQiCai = FragQiCai.getInstance(task_id,task_name);
        ft.add(R.id.zl_frag_layout, fraDuiyuan);
        ft.commit();
        DuiYuan.setTextColor(getResources().getColor(R.color.corlorSlectStr));
        DuiYuan.setBackgroundColor(getResources().getColor(R.color.bgSlectColor));
        changeSelect(0);

    }

    @OnClick({R.id.zlmd_dygl, R.id.zlmd_ldgl, R.id.zlmd_qcgl})
    @Override
    public void onClick(View v) {
        ft = fragmentManager.beginTransaction();
        if (v.getId() == R.id.zlmd_dygl) {
            DuiYuan.setTextColor(getResources().getColor(R.color.corlorSlectStr));
            DuiYuan.setBackgroundColor(getResources().getColor(R.color.bgSlectColor));
            changeSelect(0);
            ft.replace(R.id.zl_frag_layout, fraDuiyuan);
            ft.commit();
        }
        if (v.getId() == R.id.zlmd_ldgl) {
            LieDui.setTextColor(getResources().getColor(R.color.corlorSlectStr));
            LieDui.setBackgroundColor(getResources().getColor(R.color.bgSlectColor));
            changeSelect(1);
            ft.replace(R.id.zl_frag_layout, fragLieDui);
            ft.commit();
        }
        if (v.getId() == R.id.zlmd_qcgl) {
            QiCai.setTextColor(getResources().getColor(R.color.corlorSlectStr));
            QiCai.setBackgroundColor(getResources().getColor(R.color.bgSlectColor));
            changeSelect(2);
            ft.replace(R.id.zl_frag_layout, fragQiCai);
            ft.commit();
        }
    }

    private void changeSelect(int p) {
        for (int i = 0; i < list.size(); i++) {
            if (i != p) {
                list.get(i).setBackgroundColor(getResources().getColor(R.color.bgNormalColor));
                list.get(i).setTextColor(getResources().getColor(R.color.colorNormalStr));

            }
        }
    }
}
