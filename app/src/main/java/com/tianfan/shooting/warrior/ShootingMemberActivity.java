package com.tianfan.shooting.warrior;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.mvp.presenter.ShootingMemberPresenter;
import com.tianfan.shooting.admin.mvp.view.ShootingMemberView;
import com.tianfan.shooting.bean.CameraBean;
import com.tianfan.shooting.bean.CommandManageBean;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.bean.TaskPersonBean;
import com.tianfan.shooting.view.LoadingDialog;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hcnetsdk.HcnetUtils;

/**
 * @Name：Shooting
 * @Description：射击员
 * @Author：Chen
 * @Date：2020/5/26 22:09
 * 修改人：Chen
 * 修改时间：2020/5/26 22:09
 */
public class ShootingMemberActivity extends AppCompatActivity implements ShootingMemberView {

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.sur_player)
    SurfaceView surPlayer;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_person_row)
    TextView tv_person_row;
    @BindView(R.id.tv_10h_data)
    TextView tv_10h_data;
    @BindView(R.id.tv_9h_data)
    TextView tv_9h_data;
    @BindView(R.id.tv_8h_data)
    TextView tv_8h_data;
    @BindView(R.id.tv_7h_data)
    TextView tv_7h_data;
    @BindView(R.id.tv_6h_data)
    TextView tv_6h_data;
    @BindView(R.id.tv_5h_data)
    TextView tv_5h_data;
    @BindView(R.id.tv_allring_data)
    TextView tv_allring_data;
    @BindView(R.id.tv_all_fa_data)
    TextView tv_all_fa_data;
    @BindView(R.id.tv_status)
    TextView tv_status;

    public LoadingDialog mLoadingDialog;

    private ShootingMemberPresenter mShootingMemberPresenter;
    private CameraBean mCameraBean;
    private TaskInfoBean mTaskInfoBean;
    private HcnetUtils mHcnetUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooting_member);
        ButterKnife.bind(this);
        mLoadingDialog = new LoadingDialog(this, R.style.TransparentDialog);
        mLoadingDialog.setTitle("加载中");
        mLoadingDialog.setCancelable(false);
        initView();
        mHcnetUtils = new HcnetUtils(surPlayer);
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(3000);//休眠3秒
                    mHcnetUtils.startPlay();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                /**
                 * 要执行的操作
                 */
            }
        }.start();
    }

    private void initView() {
        mCameraBean = getIntent().getParcelableExtra("CameraBean");

        mShootingMemberPresenter = new ShootingMemberPresenter(this);
        mShootingMemberPresenter.findTaskInfo();
    }

    @OnClick({R.id.iv_back})
    public void onClick(View v){
        if (v==iv_back){
            finish();
        }
    }
    @Override
    public void FindTaskInfoResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<TaskInfoBean> mDatas = JSONArray.parseArray(datas, TaskInfoBean.class);
            List<TaskInfoBean> underwayTask = new ArrayList<>();

            if (mDatas.size() > 0) {
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).getTask_status().equals("1")) {
                        underwayTask.add(mDatas.get(i));
                    }
                }
                //判断是否已经有正在进行中的任务
                if (underwayTask.size() > 0) {
                     mTaskInfoBean = underwayTask.get(0);
                    tv_status.setText("进行中...");
                    tv_status.setTextColor(getResources().getColor(R.color.red));
                    mShootingMemberPresenter.findTaskPerson(mTaskInfoBean.getTask_id(),mTaskInfoBean.getTask_rows(),String.valueOf(mCameraBean.getCamera_col()));
                } else {
                    tv_status.setText("已结束");
                    tv_status.setTextColor(getResources().getColor(R.color.horizontal_toutiao));
                    showLoadFailMsg("暂无任务正在打靶");
                }
            }
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void FindTaskPersonResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<TaskPersonBean> mDatas = JSONArray.parseArray(datas, TaskPersonBean.class);
            if (mDatas.size()>0){
                tv_name.setText(mDatas.get(0).getPerson_name());
                tv_person_row.setText(mTaskInfoBean.getTask_rows());
                mShootingMemberPresenter.findTaskPersonScore(mDatas.get(0).getTask_id(),mTaskInfoBean.getTask_rounds(),mDatas.get(0).getPerson_id());
            }
        } else if (code==2){//表示该靶位无人 直接新增队员
            showLoadFailMsg("该分组的靶位查找不到队员！");

        }else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void FindTaskPersonScoreResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<CommandManageBean.CommandManageItem> mDatas = JSONArray.parseArray(datas, CommandManageBean.CommandManageItem.class);
            List<CommandManageBean.CommandManageItem.PersonScoreBean> mPersonScoreBean= mDatas.get(0).getPerson_score();
            if (mDatas.size()>0&&mPersonScoreBean!=null&&mPersonScoreBean.size()>0){
                for (int i=0;i<mPersonScoreBean.size();i++){
                    if (mPersonScoreBean.get(i).getRounds()==Integer.parseInt(mTaskInfoBean.getTask_rounds())){
                        tv_5h_data.setText(String.valueOf(mPersonScoreBean.get(i).getScore_5()));
                        tv_6h_data.setText(String.valueOf(mPersonScoreBean.get(i).getScore_6()));
                        tv_7h_data.setText(String.valueOf(mPersonScoreBean.get(i).getScore_7()));
                        tv_8h_data.setText(String.valueOf(mPersonScoreBean.get(i).getScore_8()));
                        tv_9h_data.setText(String.valueOf(mPersonScoreBean.get(i).getScore_9()));
                        tv_10h_data.setText(String.valueOf(mPersonScoreBean.get(i).getScore_10()));
                        tv_allring_data.setText(String.valueOf(mPersonScoreBean.get(i).getHit_score()));
                        tv_all_fa_data.setText(String.valueOf(mPersonScoreBean.get(i).getHit_count()));
                    }
                }
            }
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
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
