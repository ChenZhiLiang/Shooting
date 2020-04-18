package com.tianfan.shooting.admin.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tianfan.shooting.BuildConfig;
import com.tianfan.shooting.Login;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.CameraListActivity;
import com.tianfan.shooting.admin.NetAction;
import com.tianfan.shooting.admin.NewFuckHttpTools;
import com.tianfan.shooting.admin.mvp.presenter.HomeTaskPresenter;
import com.tianfan.shooting.admin.mvp.view.VIewHomeFrg;
import com.tianfan.shooting.admin.mvp.view.FileUPLoadView;
import com.tianfan.shooting.admin.mvp.presenter.FileUpLoadPresenter;
import com.tianfan.shooting.admin.FuckRequestCallBack;

import com.tianfan.shooting.admin.ui.evendata.CameraSelectEvent;
import com.tianfan.shooting.admin.ui.evendata.SelectUserForTaskEven;
import com.tianfan.shooting.admin.ui.evendata.UserSelectCalBack;

import com.tianfan.shooting.admin.ui.mdziliao.ZiLiaoMD;
import com.tianfan.shooting.net.GetResult;
import com.tianfan.shooting.net.RequestTools;
import com.tianfan.shooting.net.RetrofitUtils;
import com.vise.log.ViseLog;

import org.easydarwin.video.Client;
import org.easydarwin.video.EasyPlayerClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import me.rosuh.filepicker.config.FilePickerManager;


/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-12 18:14
 * @Description 功能描述
 */
public class HomeFragment extends Fragment implements View.OnClickListener, FileUPLoadView, VIewHomeFrg {
    VIewHomeFrg vIewHomeFrg = this;
    PromptDialog promptDialog;
    @BindView(R.id.sp_home_sle_camera)
    TextView tcSlectCm;
    @BindView(R.id.home_input_bt)
    Button inputData;
    @BindView(R.id.sp_home_order_sel)
    MaterialSpinner spOrderSel;

    String[] spOrderSts = {"列队集合", "向射击阵地前进", "卧倒", "装弹",
            "打开保险", "上膛", "开始射击", "验枪", "关保险", "卸弹夹",};

    @BindView(R.id.sp_slect_task_type)
    MaterialSpinner spTaskType;
    String[] spTaskTypeSts = {"胸环靶", "胸靶靶"};


    JSONArray jsonArrayForTask = new JSONArray();
    boolean hashTaskDoing = false;
    JSONObject nowSelectTaskJson;

    @BindView(R.id.home_sp_selct_group)
    MaterialSpinner spSelecGroup;
    List<String> spGroupStr;
    @BindView(R.id.home_sp_selct_times)
    MaterialSpinner spSelecTimes;
    List<String> spSelecTimesStr = new ArrayList<>();

    @BindView(R.id.bt_home_start_task)
    Button btStartTask;
    @BindView(R.id.bt_home_finish_task)
    Button btEndTask;
    @BindView(R.id.bt_home_start_current_times)
    Button startCurrentTimes;
    @BindView(R.id.bt_home_finsh_current_times)
    Button endCurrentTimes;
    TextureView textureView;

    private EasyPlayerClient client;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_command_home, container, false);
        ButterKnife.bind(this, view);
        EventBus.getDefault().register(this);
        spListen();
        spOrderSel.setItems(spOrderSts);
//        spSelecTimes.setItems(spSelecTimesStr);
        spTaskType.setItems(spTaskTypeSts);
        spTaskType.setSelectedIndex(0);
        spTaskType.setEnabled(false);
        promptDialog = new PromptDialog(getActivity());
        textureView = view.findViewById(R.id.tr_view_cm);
        initCamera();
        client = new EasyPlayerClient(getActivity(), BuildConfig.RTSP_KEY, textureView, null, null);
//        查询当前是否有正在进行的任务
        client = new EasyPlayerClient(getContext(), BuildConfig.RTSP_KEY, textureView, null, null);

        for (int i = 1; i <= 20; i++) {
                    spSelecTimesStr.add("第" + i + "轮");
                }
        spSelecTimes.setItems(spSelecTimesStr);
        spSelecTimes.setEnabled(false);
        checkTask();
        return view;

    }
    @BindView(R.id.tv_now_times)
    TextView tv_now_times;


    private void checkTask() {
        RequestTools.doAction().getData(RetrofitUtils.getService().getNowShoot(new HashMap<>()),
                new GetResult<String>() {
                    @Override
                    public void fail(String msg) {

                    }
                    @Override
                    public void ok(String s) {
                        setState(s);

                    }
                });
    }

    private void setState(String s) {
//
        spSelecTimes.setEnabled(true);
        JSONObject jsonObject = JSONObject.parseObject(s);
        tv_now_times.setText("当前进行：第"+jsonObject.getString("now_group")+"组第" + jsonObject.getString("now_times") + "轮射击");
        JSONObject jsonObject1 = JSONObject.parseObject(jsonObject.getString("task_data"));
        JSONArray userData = JSONArray.parseArray(jsonObject1.getString("userData"));
        spGroupStr = new ArrayList<>();
        spGroupStr.clear();
        for (int i = 1; i <= userData.size() / 10; i++) {
            spGroupStr.add("第" + i + "组");
        }
        Log.e("数据回调test", "--" + spGroupStr);
        spSelecGroup.setItems(spGroupStr);
        spSelecGroup.setSelectedIndex(Integer.parseInt(jsonObject.getString("now_times")) - 1);


        inputData.setText("结束任务");


    }

    JSONArray startShootData = new JSONArray();

    //    选择射击对象回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SelectCallback(UserSelectCalBack selectCalBack) {
        if (!inputData.getText().toString().equals("新建任务")) {
            Toast.makeText(getActivity(), "当前有任务进行中", Toast.LENGTH_SHORT).show();
            return;
        } else {
            startShootData = selectCalBack.getData();
            startNewTask();
            spGroupStr = new ArrayList<>();
            spGroupStr.clear();
            for (int i = 1; i <= selectCalBack.getData().size() / 10; i++) {
                spGroupStr.add("第" + i + "组");
            }
            Log.e("数据回调test", "--" + spGroupStr);
            spSelecGroup.setItems(spGroupStr);
        }

    }

    void spListen() {
        spSelecTimes.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                startCurrentTimes.setText("开始" + spGroupStr.get(spSelecGroup.getSelectedIndex()) + "第" + spSelecTimesStr.get(spSelecTimes.getSelectedIndex()) + "射击");
            }
        });

        spSelecGroup.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                startCurrentTimes.setText("开始" + spGroupStr.get(spSelecGroup.getSelectedIndex()) + "第" + spSelecTimesStr.get(spSelecTimes.getSelectedIndex()) + "射击");
            }
        });

//        CameraNativeHelper.init(getActivity(), OCR.getInstance(getActivity()).getLicense(),
//                new CameraNativeHelper.CameraNativeInitCallback() {
//                    @Override
//                    public void onError(int errorCode, Throwable e) {
//                        String msg;
//                        switch (errorCode) {
//                            case CameraView.NATIVE_SOLOAD_FAIL:
//                                msg = "加载so失败，请确保apk中存在ui部分的so";
//                                break;
//                            case CameraView.NATIVE_AUTH_FAIL:
//                                msg = "授权本地质量控制token获取失败";
//                                break;
//                            case CameraView.NATIVE_INIT_FAIL:
//                                msg = "本地质量控制";
//                                break;
//                            default:
//                                msg = String.valueOf(errorCode);
//                        }
//                    }
//                });

    }

    void finshNowShoot() {
        RequestTools.doAction().getData(RetrofitUtils.getService().finishNowShoot(new HashMap<>()),
                new GetResult<String>() {
                    @Override
                    public void fail(String msg) {
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void ok(String s) {
                        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                        inputData.setText("新建任务");
                        spSelecTimes.setEnabled(false);
                        tv_now_times.setText("当前进行：");
                    }
                });
    }

    @OnClick({R.id.home_input_bt, R.id.home_humen_qd, R.id.bt_home_start_task,
            R.id.bt_home_finish_task, R.id.bt_home_start_current_times
            , R.id.bt_home_finsh_current_times, R.id.home_input_data, R.id.sp_home_sle_camera})
    @Override
    public void onClick(View v) {
        //数据导入
        if (v.getId() == R.id.home_input_data) {

            FilePickerManager.INSTANCE.from(this)
                    .maxSelectable(1).forResult(1123);
//
            //人员签到
        }
        //新建任务
        if (v.getId() == R.id.home_input_bt) {
            if (inputData.getText().toString().equals("结束任务")) {
                promptDialog.showAlertSheet("是否结束任务", true, new PromptButton("取消", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                    }
                }), new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        finshNowShoot();
                    }
                }));
            } else {
                startActivity(new Intent(getContext(), ZiLiaoMD.class));
            }

            //人员签到
        } else if (v.getId() == R.id.home_humen_qd) {
            if (nowSelectTaskJson != null) {
//                Intent intent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putString("data", nowSelectTaskJson + "");
//                intent.putExtra("data", bundle);
//                intent.setClass(getContext(), SignActivity.class);
//                startActivity(intent);
            }
            //开始任务
        } else if (v.getId() == R.id.bt_home_start_task) {
//            startNewTask();
        } else if (v.getId() == R.id.bt_home_finish_task) {
            promptDialog.showAlertSheet("是否结束任务", true, new PromptButton("取消", new PromptButtonListener() {
                @Override
                public void onClick(PromptButton button) {
                }
            }), new PromptButton("确定", new PromptButtonListener() {
                @Override
                public void onClick(PromptButton button) {
//
//                        spSelectTask.setEnabled(true);
                    finshNowShoot();
                }
            }));
        } else if (v.getId() == R.id.bt_home_start_current_times) {
//                开始某一轮射击
            if (inputData.getText().toString().equals("新建任务")) {
                Toast.makeText(getActivity(), "当前没有进行进行中的任务", Toast.LENGTH_SHORT).show();
                return;
            }
            Map map = new HashMap();
            map.put("group", ""+(spSelecGroup.getSelectedIndex()+1));
            map.put("times", ""+(spSelecTimes.getSelectedIndex()+1));

            RequestTools.doAction().getData(RetrofitUtils.getService().upNowShoot(map),
                    new GetResult<String>() {
                        @Override
                        public void fail(String msg) {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void ok(String s) {
                            Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
                            tv_now_times.setText("当前进行：第"+(spSelecGroup.getSelectedIndex()+1)+"组第"+(spSelecTimes.getSelectedIndex()+1)+"轮射击");

                        }
                    });

        } else if (v.getId() == R.id.bt_home_finsh_current_times) {
            getActivity().startActivity(new Intent(getActivity().getApplicationContext(), Login.class));
            getActivity().finish();
        } else if (v.getId() == R.id.sp_home_sle_camera) {
            startActivity(new Intent(getContext(), CameraListActivity.class));
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cmareSelectCallback(CameraSelectEvent selectCalBack) {

        client.stop();
        client.start(selectCalBack.getResult(), Client.TRANSTYPE_UDP, Client.TRANSTYPE_UDP,
                Client.EASY_SDK_VIDEO_FRAME_FLAG
                        | Client.EASY_SDK_AUDIO_FRAME_FLAG, "", "", null);

//        Bitmap ShoInput = ((BitmapDrawable) icon_show.getDrawable()).getBitmap();
//        bitMapShow = zoomImg(ShoInput, 909, 909);
//        icon_show.setImageBitmap(bitMapShow);


    }

    private void startNewTask() {
        DialogTitle dialong = new DialogTitle(getActivity(), new TimesCallBack() {
            @Override
            public void result(String result) {
                JSONObject requestData = new JSONObject();
                requestData.put("type", spTaskType.getSelectedIndex() + 1);
                requestData.put("userData", startShootData + "");
                requestData.put("times", result + "");
                requestData.put("task_name",result);
                Log.e("开始新任务", "---》开始请求---->");
                new NewFuckHttpTools().startNewTask(requestData, new FuckRequestCallBack() {
                    @Override
                    public void result(boolean flag, JSONObject result, String msg) {
                        if (flag) {
                            Toast.makeText(getContext(), result + "", Toast.LENGTH_SHORT).show();
                            Log.e("网络回调数据", result + "");
                            inputData.setText("结束任务");
                            tv_now_times.setText("当前进行：第1组第" + 1 + "轮射击");

                            spSelecTimes.setEnabled(true);
                        } else {
                            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                            Log.e("网络回调数据", msg);
                        }
                    }
                }, NetAction.START_TASK, result, (spTaskType.getSelectedIndex() + 1) + "");
            }
        });
        dialong.show();

//
    }

    public class DialogTitle extends Dialog {
        TimesCallBack callBack;

        public DialogTitle(@NonNull Context context, TimesCallBack callBack) {
            super(context);
            this.callBack = callBack;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_times_input);
            EditText editText = findViewById(R.id.ed_data);
            findViewById(R.id.bt_comfir).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editText.getText().toString().equals("")) {
                        Toast.makeText(getContext(), "此处不能为空！", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    callBack.result(editText.getText().toString());
                    dismiss();
                }
            });
        }
    }

    public interface TimesCallBack {
        void result(String result);
    }

    void initCamera() {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SelectCallback(CameraSelectEvent selectCalBack) {
    }

    //数据选择返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void SelectDataResult(SelectUserForTaskEven selectUserForTaskEven) {
        Log.e("Test-----", "---数据回调--》" + JSONObject.toJSONString(selectUserForTaskEven));
//        数据回调，设置总组别


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //文件选择返回
        if (requestCode == 1123) {
            if (FilePickerManager.INSTANCE.obtainData().size() > 0) {
                String result = FilePickerManager.INSTANCE.obtainData().get(0);
//                XLog.e("选中文件------>："+result);
                int position = result.lastIndexOf(".");
                if (position != -1) {
                    String check = result.substring(position + 1);
                    if (check.equals("xls")) {
                        promptDialog.showLoading("正在上传");
                        new FileUpLoadPresenter(this).doUP(result);
                    } else {
                        promptDialog.showWarnAlert("只支持XLS格式的Excel文档", new PromptButton("确定", new PromptButtonListener() {
                            @Override
                            public void onClick(PromptButton button) {
                            }
                        }));
                    }
                } else {

                    promptDialog.showWarnAlert("不支持的文件类型", new PromptButton("确定", new PromptButtonListener() {
                        @Override
                        public void onClick(PromptButton button) {
                        }
                    }));
                }
            }
        }
    }

    //文件上传成功
    @Override
    public void FileUpSuccess() {
        promptDialog.dismiss();
        new HomeTaskPresenter(this).getUnDoData("1");
    }

    //文件上传失败
    @Override
    public void FileUpError(String reason) {
        promptDialog.showWarnAlert(reason, new PromptButton("确定", new PromptButtonListener() {
            @Override
            public void onClick(PromptButton button) {
            }
        }));
    }

    @Override
    public void onDestroy() {
        // 释放本地质量控制模型
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
//        CameraNativeHelper.release();
        super.onDestroy();
    }


    //获取任务回调
    @Override
    public void ok(String data) {
        promptDialog.dismiss();
        jsonArrayForTask = JSONArray.parseArray(data);
        List<String> spTaskStr = new ArrayList<>();
        for (int i = 0; i < jsonArrayForTask.size(); i++) {
            JSONObject jsonObject = jsonArrayForTask.getJSONObject(i);
            spTaskStr.add(jsonObject.getString("name"));
        }

        if (jsonArrayForTask.size() > 0) {
            Log.e("获取数据回调的结果", "---" + data);
        }

    }


    //获取任务失败
    @Override
    public void bullshit(String data) {

    }

    //开始当前任务
    @Override
    public void startTask() {
        ViseLog.e("开始任务回调：--------");
    }

    //结束当前任务
    @Override
    public void endTask() {
        ViseLog.e("结束任务回调：--------");
        new HomeTaskPresenter(this).getUnDoData("1");
    }

    //开始某一轮的射击
    @Override
    public void startTimesTask() {
        ViseLog.e("开始某一轮回调：--------");
    }

    //结束某一轮的射击
    @Override
    public void endTimesTask() {
        ViseLog.e("结束某一轮任务回调：--------");
    }
}