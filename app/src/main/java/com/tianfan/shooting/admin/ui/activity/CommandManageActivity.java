package com.tianfan.shooting.admin.ui.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.tianfan.shooting.R;
import com.tianfan.shooting.adapter.CommandManageAdapter;
import com.tianfan.shooting.adapter.TaskEquipModelAdapter;
import com.tianfan.shooting.adapter.TaskRoundsAdapter;
import com.tianfan.shooting.admin.mvp.presenter.CommandManagePersenter;
import com.tianfan.shooting.admin.mvp.view.CommandManageView;
import com.tianfan.shooting.bean.CommandManageBean;
import com.tianfan.shooting.bean.EquipModelBean;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.view.ChangeTaskPersonRowcolDialog;
import com.tianfan.shooting.view.LoadingDialog;
import com.tianfan.shooting.view.MyPopWindow;
import com.tianfan.shooting.view.SelectTaskDialog;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.tianfan.shooting.admin.ui.activity.CompleteNameActivity.CHANGE_LOCATION_TYPE;
import static com.tianfan.shooting.admin.ui.activity.CompleteNameActivity.COMPLETE_NAME_TYPE;

/**
 * @Name：Shooting
 * @Description：指挥管理
 * @Author：Chen
 * @Date：2020/4/25 18:11
 * 修改人：Chen
 * 修改时间：2020/4/25 18:11
 */
public class CommandManageActivity extends AppCompatActivity implements CommandManageView {

    @BindView(R.id.iv_return_home)
    ImageView iv_return_home;
    @BindView(R.id.layout_task_status)
    LinearLayout layout_task_status;
    @BindView(R.id.tv_task_name)
    TextView tv_task_name;
    @BindView(R.id.tv_task_state)
    TextView tv_task_state;
    @BindView(R.id.tv_complete_name)
    TextView tv_complete_name;
    @BindView(R.id.img_front_in)
    ImageView img_front_in;//前往射击阵地
    @BindView(R.id.img_drop_down)
    ImageView img_drop_down;//卧倒
    @BindView(R.id.img_kreload)
    ImageView img_kreload;//装弹
    @BindView(R.id.img_be_loaded)
    ImageView img_be_loaded;//上膛
    @BindView(R.id.img_open_insurance)
    ImageView img_open_insurance;//打开保险
    @BindView(R.id.img_shooting)
    ImageView img_shooting;//射击
    @BindView(R.id.img_close_insurance)
    ImageView img_close_insurance;//关闭保险
    @BindView(R.id.img_inspect_arms)
    ImageView img_inspect_arms;//验枪
    @BindView(R.id.img_unload)
    ImageView img_unload;
    @BindView(R.id.img_front_out)
    ImageView img_front_out;//退出射击阵地
    @BindView(R.id.tv_change_location)
    TextView tv_change_location;
    @BindView(R.id.tv_start_shooting_practice)
    TextView tv_start_shooting;
    @BindView(R.id.tv_task_nest)
    TextView tv_task_nest;
    @BindView(R.id.tv_task_finish)
    TextView tv_task_finish;
    @BindView(R.id.layout_round)
    LinearLayout layout_round;
    @BindView(R.id.tv_round)
    TextView tv_round;

    @BindView(R.id.hrecyclerview)
    RecyclerView mHrecyclerview;
    private CommandManageAdapter mCommandManageAdapter;
    private List<CommandManageBean> mCommandManageDatas = new ArrayList<>();
    List<CommandManageBean.CommandManageItem> personDatas = new ArrayList<>();

    private MediaPlayer player;
    public LoadingDialog mLoadingDialog;
    private CommandManagePersenter mCommandManagePersenter;
    private TaskInfoBean mTaskInfoBean;

    private int payStatus = 1;
    private int imgStatusPosition = 1;
    private View popupView_view;
    private RecyclerView recycler_model;
    private TaskRoundsAdapter mTaskRoundsAdapter;
    private List<String> mTaskRoundDatas = new ArrayList<>();
    private MyPopWindow window_equip_model;
    private int currentRounds = 1;//当前轮次

    private Handler mHandler = new Handler();
    private Runnable runnable;
    private boolean isFinshRun = false;//是否结束轮询
    private int currentGroup = 0;//当前组数
    //进行中的任务
    List<TaskInfoBean> underwayTask = new ArrayList<>();
    //未开始的任务
    List<TaskInfoBean> waitForTask = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_command_manage);
        ButterKnife.bind(this);
        mLoadingDialog = new LoadingDialog(this, R.style.TransparentDialog);
        mLoadingDialog.setTitle("加载中");
        mLoadingDialog.setCancelable(false);
        initView();
    }

    private void initView() {
        mHrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mCommandManageAdapter = new CommandManageAdapter(mCommandManageDatas, new CommandManageAdapter.onCheckPositionInterface() {
            @Override
            public void check(int postion) {
                SweetAlertDialogTools.ShowDialog(CommandManageActivity.this, "确定切换分组？", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        for (int i = 0; i < mCommandManageDatas.size(); i++) {
                            if (postion == i) {
                                mCommandManageDatas.get(i).setSelectPostion(true);
                            } else {
                                mCommandManageDatas.get(i).setSelectPostion(false);
                            }
                        }
                        currentGroup = postion;
                        mCommandManagePersenter.changeGroup(mTaskInfoBean.getTask_id(), String.valueOf(currentRounds));
//                        mCommandManageAdapter.setCurrentRounds(currentRounds);
                        mCommandManageAdapter.notifyDataSetChanged();
                    }
                });
            }

            @Override
            public void onClickItem(CommandManageBean.CommandManageItem item, int postion) {
                if (item != null &&  ((currentGroup + 1) == item.getPerson_row())) {
                    ChangeTaskPersonRowcolDialog dialog = new ChangeTaskPersonRowcolDialog(CommandManageActivity.this, personDatas,item,
                            currentGroup + 1, postion, new ChangeTaskPersonRowcolDialog.onResultInterface() {
                        @Override
                        public void onSuccess(Object result) {
                            JSONObject jsonObject = JSONObject.parseObject(result.toString());
                            int code = jsonObject.getIntValue("code");
                            if (code == 1) {
                                mCommandManagePersenter.findTaskPersonScore(mTaskInfoBean.getTask_id(), currentRounds, false);
                                showLoadFailMsg("调整成功");
                            } else {
                                showLoadFailMsg(jsonObject.getString("message"));
                            }
                        }

                        @Override
                        public void onFailure(Object result) {
                            showLoadFailMsg(result.toString());

                        }
                    });
                    dialog.show();
                }
            }
        });
        mHrecyclerview.setAdapter(mCommandManageAdapter);
        mCommandManagePersenter = new CommandManagePersenter(this);
        runnable = new Runnable() {
            @Override
            public void run() {
                if (mTaskInfoBean != null) {
                    mCommandManagePersenter.findTaskPersonScore(mTaskInfoBean.getTask_id(), currentRounds, false);
                }
                if (!isFinshRun) {//判断是否结束
                    mHandler.postDelayed(this, 10000);
                }
            }
        };
        //查找任务
        mCommandManagePersenter.findTaskInfo();
    }


    @OnClick({R.id.iv_return_home, R.id.tv_task_state, R.id.tv_complete_name, R.id.tv_task_nest,
            R.id.tv_task_finish, R.id.tv_task_name, R.id.layout_round, R.id.tv_change_location, R.id.tv_start_shooting_practice})
    public void onClick(View view) {
        if (view == iv_return_home) {
            finish();
            return;
        } else if (view == tv_task_name) {
            if (mTaskInfoBean == null) {
                SelectTaskDialog mSelectTaskDialog = new SelectTaskDialog(this, waitForTask, new SelectTaskDialog.onSeleteTaskInterface() {
                    @Override
                    public void result(TaskInfoBean taskInfo) {
                        mTaskInfoBean = taskInfo;
                        tv_task_name.setText(mTaskInfoBean.getTask_name());
                        tv_task_state.setVisibility(View.VISIBLE);
                        tv_complete_name.setVisibility(View.VISIBLE);
                        tv_task_nest.setVisibility(View.GONE);
                        tv_task_finish.setVisibility(View.GONE);
                        mTaskRoundDatas.add("第1轮");

                    }
                });
                mSelectTaskDialog.show();
            }
            return;
        }
        if (mTaskInfoBean == null) {
            showLoadFailMsg("请选择任务");
            return;
        }
        switch (view.getId()) {
            case R.id.layout_round:
                initPopMenu();
                break;
            case R.id.tv_task_state:
                if (TextUtils.equals("1", mTaskInfoBean.getTask_rounds())) {
                    startActivity(new Intent(this, CompleteNameActivity.class)
                            .putExtra("task_id", mTaskInfoBean.getTask_id())
                            .putExtra("task_name", mTaskInfoBean.getTask_name())
                            .putExtra("task_rounds", mTaskInfoBean.getTask_rounds())
                            .putExtra("type", COMPLETE_NAME_TYPE));
                }
                break;
            case R.id.tv_complete_name:
                if (TextUtils.equals("1", mTaskInfoBean.getTask_rounds())) {
                    SweetAlertDialogTools.ShowDialog(this, "确定完成点名？", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            tv_task_state.setVisibility(View.GONE);
                            tv_complete_name.setVisibility(View.GONE);
                            tv_start_shooting.setVisibility(View.VISIBLE);
                            tv_task_nest.setVisibility(View.GONE);
                            tv_task_finish.setVisibility(View.VISIBLE);
                        }
                    });
                }
                break;
            case R.id.tv_task_nest:
                SweetAlertDialogTools.ShowDialog(this, "确定下一轮？", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        currentRounds++;
                        mCommandManagePersenter.changeTaskRounds(mTaskInfoBean.getTask_id(), currentRounds, 0);
                    }
                });
                break;
            case R.id.tv_change_location:
                startActivity(new Intent(this, CompleteNameActivity.class)
                        .putExtra("task_id", mTaskInfoBean.getTask_id())
                        .putExtra("task_name", mTaskInfoBean.getTask_name())
                        .putExtra("task_rounds", mTaskInfoBean.getTask_rounds())
                        .putExtra("type", CHANGE_LOCATION_TYPE));

                break;
            case R.id.tv_start_shooting_practice:
                SweetAlertDialogTools.ShowDialog(this, "确定开始打靶？", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mCommandManagePersenter.changeTaskRounds(mTaskInfoBean.getTask_id(), currentRounds, 1);
                    }
                });

                break;
            case R.id.tv_task_finish:
                if (!TextUtils.equals("0", mTaskInfoBean.getTask_rounds())) {
                    SweetAlertDialogTools.ShowDialog(this, "确定结束打靶？", new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            tv_task_nest.setVisibility(View.GONE);
                            tv_task_finish.setText("已结束");
                            tv_task_finish.setEnabled(false);
                            isFinshRun = true;
                            payStatus = 1;
                            resetPayerImg();
                            mCommandManagePersenter.editTaskInfo(mTaskInfoBean.getTask_id(), currentRounds, 2);
                        }
                    });
                }
                break;

        }
    }

    @OnClick({R.id.img_front_in,
            R.id.img_drop_down, R.id.img_kreload, R.id.img_be_loaded,
            R.id.img_open_insurance, R.id.img_shooting, R.id.img_close_insurance,
            R.id.img_inspect_arms, R.id.img_unload, R.id.img_front_out})
    public void onPayClick(View view) {
        if (player != null && player.isPlaying()) {
            return;
        }
        switch (view.getId()) {
            case R.id.img_front_in:
                payStatus = 1;
                imgStatusPosition = 0;
                palyerMedia(R.raw.qianwangshejizhendi, imgStatusPosition);
                break;
            case R.id.img_drop_down:
                if (payStatus > 0) {
                    payStatus = payStatus > 2 ? payStatus : 2;
                    imgStatusPosition = 1;
                    img_drop_down.setBackgroundResource(R.mipmap.drop_down_select);
                    palyerMedia(R.raw.wodao, imgStatusPosition);
                }
                break;
            case R.id.img_kreload:
                if (payStatus > 1) {
                    payStatus = payStatus > 3 ? payStatus : 3;
                    imgStatusPosition = 2;
                    img_kreload.setBackgroundResource(R.mipmap.kreload_select);
                    palyerMedia(R.raw.zhuangdan, imgStatusPosition);
                }

                break;
            case R.id.img_be_loaded:
                if (payStatus > 2) {
                    payStatus = payStatus > 4 ? payStatus : 4;
                    imgStatusPosition = 3;
                    img_be_loaded.setBackgroundResource(R.mipmap.be_loaded_select);
                    palyerMedia(R.raw.shangtang, imgStatusPosition);
                }

                break;
            case R.id.img_open_insurance:
                if (payStatus > 3) {
                    payStatus = payStatus > 5 ? payStatus : 5;
                    imgStatusPosition = 4;
                    img_open_insurance.setBackgroundResource(R.mipmap.open_insurance_select);
                    palyerMedia(R.raw.dakaibaoxian, imgStatusPosition);
                }
                break;
            case R.id.img_shooting:
                if (payStatus > 4) {
                    payStatus = payStatus > 6 ? payStatus : 6;
                    imgStatusPosition = 5;
                    img_shooting.setBackgroundResource(R.mipmap.shooting_select);
                    palyerMedia(R.raw.sheji, imgStatusPosition);
                }
                break;
            case R.id.img_close_insurance:
                if (payStatus > 5) {
                    payStatus = payStatus > 7 ? payStatus : 7;
                    imgStatusPosition = 6;
                    img_close_insurance.setBackgroundResource(R.mipmap.close_insurance_select);
                    palyerMedia(R.raw.guanbibaoxian, imgStatusPosition);
                }

                break;
            case R.id.img_inspect_arms:
                if (payStatus > 6) {
                    payStatus = payStatus > 8 ? payStatus : 8;
                    imgStatusPosition = 7;
                    img_inspect_arms.setBackgroundResource(R.mipmap.inspect_arms_select);
                    palyerMedia(R.raw.yanqiang, imgStatusPosition);
                }

                break;
            case R.id.img_unload:
                if (payStatus > 7) {
                    payStatus = payStatus > 9 ? payStatus : 9;
                    imgStatusPosition = 8;
                    img_unload.setBackgroundResource(R.mipmap.unload_select);
                    palyerMedia(R.raw.xiedan, imgStatusPosition);
                }

                break;
            case R.id.img_front_out:
                payStatus = 1;
                imgStatusPosition = 9;
                palyerMedia(R.raw.tuichushejizhendi, imgStatusPosition);
                break;
        }
    }

    /**
     * @author
     * @time
     * @describe 播放语音
     */
    private void palyerMedia(int from, int imgStatusPosition) {
        if (player != null) {
            player.reset();
            player.release();
        }
        player = MediaPlayer.create(this, from);
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                switch (imgStatusPosition) {
                    case 1:
                        img_drop_down.setBackgroundResource(R.mipmap.drop_down_pause);
                        break;
                    case 2:
                        img_kreload.setBackgroundResource(R.mipmap.kreload_pause);
                        break;
                    case 3:
                        img_be_loaded.setBackgroundResource(R.mipmap.be_loaded_pause);
                        break;
                    case 4:
                        img_open_insurance.setBackgroundResource(R.mipmap.open_insurance_puash);
                        break;
                    case 5:
                        img_shooting.setBackgroundResource(R.mipmap.shooting_puash);
                        break;
                    case 6:
                        img_close_insurance.setBackgroundResource(R.mipmap.close_insurance_puash);
                        break;
                    case 7:
                        img_inspect_arms.setBackgroundResource(R.mipmap.inspect_arms_puash);
                        break;
                    case 8:
                        img_unload.setBackgroundResource(R.mipmap.unload_puash);
                        break;
                    case 9:
                        resetPayerImg();
                        break;
                }
            }
        });
        try {
            player.prepare();// 同步
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // //播放
        player.start();

    }

    private void resetPayerImg() {
        img_drop_down.setBackgroundResource(R.mipmap.drop_down_unselect);
        img_kreload.setBackgroundResource(R.mipmap.kreload_unselect);
        img_be_loaded.setBackgroundResource(R.mipmap.be_loaded_unselect);
        img_open_insurance.setBackgroundResource(R.mipmap.open_insurance_unselect);
        img_shooting.setBackgroundResource(R.mipmap.shooting_unselet);
        img_close_insurance.setBackgroundResource(R.mipmap.close_insurance_unselect);
        img_inspect_arms.setBackgroundResource(R.mipmap.inspect_arms_unselect);
        img_unload.setBackgroundResource(R.mipmap.unload_unselect);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
        isFinshRun = true;
    }

    private void initPopMenu() {
        popupView_view = getLayoutInflater().inflate(R.layout.layout_popupwindow_equip, null);
        recycler_model = popupView_view.findViewById(R.id.recycler_model);
        recycler_model.setLayoutManager(new LinearLayoutManager(this));
        mTaskRoundsAdapter = new TaskRoundsAdapter(mTaskRoundDatas);
        mTaskRoundsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (position + 1 == currentRounds) {
                    window_equip_model.dismiss();
                    return;
                }
                SweetAlertDialogTools.ShowDialog(CommandManageActivity.this, "确定切换轮次？", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        window_equip_model.dismiss();
                        currentRounds = position + 1;
                        tv_round.setText(mTaskRoundDatas.get(position));
                        mCommandManagePersenter.findTaskPersonScore(mTaskInfoBean.getTask_id(), currentRounds, false);
                        if (currentRounds == Integer.parseInt(mTaskInfoBean.getTask_rounds())) {
                            tv_task_nest.setVisibility(View.VISIBLE);
                            //开始轮询查询分数
                            isFinshRun = false;
                            if (mHandler != null) {
                                mHandler.postDelayed(runnable, 3000);
                            }
                        } else {
                            isFinshRun = true;//结束轮询
                            tv_task_nest.setVisibility(View.GONE);
                        }
//                        mTaskInfoBean.setTask_rounds(String.valueOf(currentRounds));

                    }
                });
            }
        });
        recycler_model.setAdapter(mTaskRoundsAdapter);
        // TODO: 2016/5/17 创建PopupWindow对象，指定宽度和高度
        window_equip_model = new MyPopWindow(popupView_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window_equip_model.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().setAttributes(lp);
        window_equip_model.showAsDropDown(layout_round, 0, 0);
    }

    @Override
    public void FindTaskInfoResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<TaskInfoBean> mDatas = JSONArray.parseArray(datas, TaskInfoBean.class);
            if (mDatas.size() > 0) {
                for (int i = 0; i < mDatas.size(); i++) {
                    if (mDatas.get(i).getTask_status().equals("0")) {
                        waitForTask.add(mDatas.get(i));
                    } else if (mDatas.get(i).getTask_status().equals("1")) {
                        underwayTask.add(mDatas.get(i));
                    }
                }

                //判断是否已经有正在进行中的任务
                if (underwayTask.size() > 0) {
                    mTaskInfoBean = underwayTask.get(0);
                    tv_task_name.setText(underwayTask.get(0).getTask_name());
                    tv_task_state.setVisibility(View.GONE);
                    tv_complete_name.setVisibility(View.GONE);
                    //该轮次还未开始打靶
                    if (underwayTask.get(0).getTask_rounds_status().equals("0")) {
                        tv_start_shooting.setVisibility(View.VISIBLE);
                        tv_task_nest.setVisibility(View.GONE);
                    } else {
                        tv_start_shooting.setVisibility(View.GONE);
                        tv_task_nest.setVisibility(View.VISIBLE);
                    }
                    tv_task_finish.setVisibility(View.VISIBLE);
                    currentRounds = Integer.parseInt(mTaskInfoBean.getTask_rounds());
                    for (int i = 0; i < currentRounds; i++) {
                        mTaskRoundDatas.add("第" + (i + 1) + "轮");
                    }
                    tv_round.setText(mTaskRoundDatas.get(currentRounds - 1));

                    mCommandManagePersenter.findTaskPersonScore(mTaskInfoBean.getTask_id(), currentRounds, true);
                    //开始轮询查询分数
                    isFinshRun = false;
                    if (mHandler != null) {
                        mHandler.postDelayed(runnable, 3000);
                    }
                }
            }

        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void EditTaskInfoResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            mTaskInfoBean.setTask_rounds(String.valueOf(currentRounds));
            mCommandManagePersenter.findTaskPersonScore(mTaskInfoBean.getTask_id(), currentRounds, true);
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void ChangeTaskRoundsResult(Object result, int task_rounds_status) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            if (task_rounds_status == 1) {//开始打靶
                mCommandManagePersenter.editTaskInfo(mTaskInfoBean.getTask_id(), currentRounds, 1);
                tv_change_location.setVisibility(View.GONE);
                tv_start_shooting.setVisibility(View.GONE);
                tv_task_nest.setVisibility(View.VISIBLE);
                mTaskInfoBean.setTask_rounds(String.valueOf(currentRounds));
                mTaskInfoBean.setTask_rounds_status(String.valueOf(task_rounds_status));
                //开始打靶  开始轮询查询分数
                isFinshRun = false;
                if (mHandler != null) {
                    mHandler.postDelayed(runnable, 3000);
                }
            } else {//切换下一轮次
                isFinshRun = true;
                tv_round.setText("第" + currentRounds + "轮");
                if (mTaskRoundDatas != null && mTaskRoundDatas.size() < currentRounds) {
                    mTaskRoundDatas.add("第" + currentRounds + "轮");
                }
                payStatus = 1;
                tv_change_location.setVisibility(View.VISIBLE);
                tv_start_shooting.setVisibility(View.VISIBLE);
                tv_task_nest.setVisibility(View.GONE);
                mTaskInfoBean.setTask_rounds(String.valueOf(currentRounds));
                mTaskInfoBean.setTask_rounds_status(String.valueOf(task_rounds_status));
                resetPayerImg();

            }

        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void FindTaskPersonScoreResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            JSONArray jsonArray = JSONArray.parseArray(datas);
            mCommandManageDatas.clear();
            personDatas.clear();
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject itemsObject = JSONObject.parseObject(jsonArray.get(i).toString());
                String items = itemsObject.getString(String.valueOf(i + 1));
                List<CommandManageBean.CommandManageItem> mDatas = JSONArray.parseArray(items, CommandManageBean.CommandManageItem.class);
                for (int j = 0; j < mDatas.size(); j++) {
                    if (!TextUtils.isEmpty(mDatas.get(j).getTask_id())) {
                        personDatas.add(mDatas.get(j));
                    }
                }
                //添加个空的 显示组数
                mDatas.add(0, new CommandManageBean.CommandManageItem());
                CommandManageBean mCommandManageBean = new CommandManageBean();
                mCommandManageBean.setDatas(mDatas);
                if (i == currentGroup) {
                    mCommandManageBean.setSelectPostion(true);
                }
                mCommandManageDatas.add(mCommandManageBean);
            }
//            mCommandManageAdapter.setCurrentRounds(currentRounds);
            mCommandManageAdapter.notifyDataSetChanged();
            Log.i("personDatas:", personDatas.size() + "");
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void ChangeGroupResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {

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

        Toast.makeText(this, err, Toast.LENGTH_SHORT).show();
    }
}
