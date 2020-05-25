package com.tianfan.shooting.scorer.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.CheckTargetPositionAdapter;
import com.tianfan.shooting.bean.CameraBean;
import com.tianfan.shooting.scorer.mvp.presenter.CheckTargetPositionPresenter;
import com.tianfan.shooting.scorer.mvp.view.CheckTargetPositionView;
import com.tianfan.shooting.view.LoadingDialog;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Author: chenzl
 * @ClassName: CheckTargetPositionActivity
 * @Description: java类作用描述
 * @CreateDate: 2020/5/25 10:23
 */
public class CheckTargetPositionActivity extends AppCompatActivity implements CheckTargetPositionView {

    @BindView(R.id.iv_return_home)
    ImageView iv_return_home;

    @BindView(R.id.recycler_camera)
    RecyclerView recycler_camera;
    private CheckTargetPositionAdapter mCheckTargetPositionAdapter;
    private CheckTargetPositionPresenter mCheckTargetPositionPresenter;
    private List<CameraBean> mCameraListDatas = new ArrayList<>();
    public LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_target_position);
        ButterKnife.bind(this);
        mLoadingDialog = new LoadingDialog(this, R.style.TransparentDialog);
        mLoadingDialog.setTitle("加载中");
        mLoadingDialog.setCancelable(false);
        initView();
    }

    private void initView(){
        recycler_camera.setLayoutManager(new LinearLayoutManager(this));
        mCheckTargetPositionAdapter = new CheckTargetPositionAdapter(mCameraListDatas);
        mCheckTargetPositionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                CameraBean cameraBean = mCameraListDatas.get(position);
                startActivity(new Intent(CheckTargetPositionActivity.this, ScorerActivity.class).putExtra("CameraBean",cameraBean));

            }
        });
        recycler_camera.setAdapter(mCheckTargetPositionAdapter);
        mCheckTargetPositionPresenter = new CheckTargetPositionPresenter(this);
        mCheckTargetPositionPresenter.findCameraCol();
    }

    @Override
    public void FindCameraColResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<CameraBean> mEquipTypeDatas = JSONArray.parseArray(datas,CameraBean.class);
            if (mCameraListDatas.size()>0){
                mCameraListDatas.clear();
            }
            mCameraListDatas.addAll(mEquipTypeDatas);
            mCheckTargetPositionAdapter.notifyDataSetChanged();
        }else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @OnClick({R.id.iv_return_home})
    public void onClick(View v){
        if (v==iv_return_home){
            finish();
        }
    }
    @Override
    public void showProgress() {
        mLoadingDialog.show();
    }

    @Override
    public void hideProgress() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showLoadFailMsg(String err) {
        Toast.makeText(this,err,Toast.LENGTH_SHORT).show();
    }
}
