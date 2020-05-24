package com.tianfan.shooting.admin.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.ui.fragment.CameraFragment;
import com.tianfan.shooting.admin.ui.fragment.EquipModelFragment;
import com.tianfan.shooting.admin.ui.fragment.EquipTypelFragment;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/15 0:13
 * 修改人：Chen
 * 修改时间：2020/4/15 0:13
 */
public class SystemManageActivity extends AppCompatActivity {
    @BindView(R.id.tv_sxt)
    TextView tv_sxt;//摄像头
    @BindView(R.id.tv_qclx)
    TextView tv_qclx;//器材类型
    @BindView(R.id.tv_qcmb)
    TextView tv_qcmb;//器材模板

    List<TextView> list = new ArrayList<>();
    CameraFragment mCameraFragment;
    EquipTypelFragment mEquipTypelFragment;
    EquipModelFragment mEquipModelFragment;
    private FragmentManager fragmentManager;
    FragmentTransaction ft;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_manage);
        ButterKnife.bind(this);
        list.add(tv_sxt);
        list.add(tv_qclx);
        list.add(tv_qcmb);
        fragmentManager = getSupportFragmentManager();
        ft = fragmentManager.beginTransaction();


        mCameraFragment = CameraFragment.getInstance();
        mEquipTypelFragment = EquipTypelFragment.getInstance();
        mEquipModelFragment = EquipModelFragment.getInstance();
        ft.add(R.id.system_frag_layout, mCameraFragment);
        ft.commit();
        tv_sxt.setTextColor(getResources().getColor(R.color.corlorSlectStr));
        tv_sxt.setBackgroundColor(getResources().getColor(R.color.bgSlectColor));
        changeSelect(0);
    }

    @OnClick({R.id.tv_sxt, R.id.tv_qclx, R.id.tv_qcmb})
    public void onClick(View v) {
        ft = fragmentManager.beginTransaction();
        if (v.getId() == R.id.tv_sxt) {
            tv_sxt.setTextColor(getResources().getColor(R.color.corlorSlectStr));
            tv_sxt.setBackgroundColor(getResources().getColor(R.color.bgSlectColor));
            changeSelect(0);
            ft.replace(R.id.system_frag_layout, mCameraFragment);
            ft.commit();
        }
        if (v.getId() == R.id.tv_qclx) {
            tv_qclx.setTextColor(getResources().getColor(R.color.corlorSlectStr));
            tv_qclx.setBackgroundColor(getResources().getColor(R.color.bgSlectColor));
            changeSelect(1);
            ft.replace(R.id.system_frag_layout, mEquipTypelFragment);
            ft.commit();
        }
        if (v.getId() == R.id.tv_qcmb) {
            tv_qcmb.setTextColor(getResources().getColor(R.color.corlorSlectStr));
            tv_qcmb.setBackgroundColor(getResources().getColor(R.color.bgSlectColor));
            changeSelect(2);
            ft.replace(R.id.system_frag_layout, mEquipModelFragment);
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
