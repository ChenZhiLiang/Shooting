package com.tianfan.shooting.admin.ui.mdziliao;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tianfan.shooting.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * CreateBy：lxf
 * CreateTime： 2020-02-24 11:10
 */
public class ZiLiaoMD extends AppCompatActivity implements View.OnClickListener {
    private String bgSlectColor = "#3c533d";
    private String corlorSlectStr = "#FFd966";
    private String bgNormalColor = "#1E3824";
    private String colorNormalStr = "#ffffff";
    @BindView(R.id.zlmd_rwgl)
    TextView RenWu;
    @BindView(R.id.zlmd_dygl)
    TextView DuiYuan;
    @BindView(R.id.zlmd_ldgl)
    TextView LieDui;
    @BindView(R.id.zlmd_qcgl)
    TextView QiCai;
    TextView tvSelct;
    List<TextView> list = new ArrayList<>();
    FraRenwu fraRenwu;
    private FragmentManager fragmentManager;
    FragmentTransaction ft;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ziliao_md);
        ButterKnife.bind(this);
        tvSelct = RenWu;
        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();
        fraRenwu = new FraRenwu();
        ft.add(R.id.zl_frag_layout, fraRenwu);
        ft.commit();
    }

    @OnClick({R.id.zlmd_rwgl, R.id.zlmd_dygl, R.id.zlmd_ldgl, R.id.zlmd_qcgl})
    @Override
    public void onClick(View v) {
        ft = fragmentManager.beginTransaction();
        if (v.getId() == R.id.zlmd_rwgl) {
            RenWu.setTextColor(Color.parseColor(corlorSlectStr));
            RenWu.setBackgroundColor(Color.parseColor(bgSlectColor));
            changeSelect(0);
            ft.replace(R.id.zl_frag_layout, fraRenwu);
            ft.commit();
        }
    }

    private void changeSelect(int p) {
        for (int i = 0; i < list.size(); i++) {
            if (i != p) {
                list.get(i).setBackgroundColor(Color.parseColor(bgNormalColor));
                list.get(i).setTextColor(Color.parseColor(colorNormalStr));
            }
        }
    }
}
