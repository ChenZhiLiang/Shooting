package com.tianfan.shooting.warrior;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.bumptech.glide.Glide;
import com.tianfan.shooting.BuildConfig;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.CameraListActivity;
import com.tianfan.shooting.admin.ui.evendata.CameraSelectEvent;

import com.tianfan.shooting.net.GetResult;
import com.tianfan.shooting.net.RequestTools;
import com.tianfan.shooting.net.RetrofitUtils;
import com.tianfan.shooting.net.RxBaseService;
import com.tianfan.shooting.tools.NewSPTools;


import org.easydarwin.video.Client;
import org.easydarwin.video.EasyPlayerClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
//import org.videolan.libvlc.LibVLC;
//import org.videolan.libvlc.MediaPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ua.polohalo.zoomabletextureview.ZoomableTextureView;


/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-02 17:11
 * @Description 射击角色页面
 */
public class WarriorActivity extends AppCompatActivity implements View.OnClickListener {

    TextView shooting_show_position;
    public JSONObject userJson = new JSONObject();

    private boolean getPckFlag = true;
    private boolean upShootFlag = false;
    private String nowPosision = "6";
    String name = "";

    private EasyPlayerClient client;
    ZoomableTextureView zoomableTextureView;

    @BindView(R.id.title_cm)
    TextView title_cm;
    @BindView(R.id.iv_show)
    ImageView icon_show;
    private Bitmap revertBitMap;

    Bitmap bitMapShow;
    int height;
    int width;
    AppCompatActivity appCompatActivity = this;
    @BindView(R.id.title_right)
    TextView title_right;

    @BindView(R.id.tv_name)
    TextView tv_name;


//    SurfaceView srfc;
//    LibVLC libVLC = null;
//    ArrayList<String> options = new ArrayList<>();
//    MediaPlayer mediaPlayer;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shooting_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        NewSPTools.getInstance().setContext(getApplicationContext());
        NewSPTools.getInstance().setContext(getApplicationContext());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        zoomableTextureView = findViewById(R.id.cm_view);

//        Bitmap ShoInput = ((BitmapDrawable) icon_show.getDrawable()).getBitmap();
//        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
//        icon_show.measure(w, h);
//        height = icon_show.getMeasuredHeight();
//        width = icon_show.getMeasuredWidth();

//        bitMapShow = zoomImg(ShoInput, 909, 909).copy(Bitmap.Config.ARGB_8888, true);
//        ;
//        revertBitMap = zoomImg(ShoInput, 909, 909).copy(Bitmap.Config.ARGB_8888, true);
        icon_show.setImageBitmap(bitMapShow);
        Log.e("点击", "icshow>>>>XXXX>>>>" + width);
        Log.e("点击", "View>>>YYYY>>>>>>>>" + height);
        client = new EasyPlayerClient(this, BuildConfig.RTSP_KEY, zoomableTextureView, null, null);






        initCameraList();
        getFuckData();
    }

    List<String> CameraURLList = new ArrayList<>();

    public Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    void initCameraList() {
        findViewById(R.id.tv_switich).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CameraListActivity.class));
            }
        });
        RequestTools.doAction().getData(RetrofitUtils.getService().getCameraList(new HashMap<>()), new GetResult<String>() {
            @Override
            public void fail(String msg) {
            }

            @Override
            public void ok(String o) {
                CameraURLList.clear();

                JSONArray jsonArray = JSONArray.parseArray(o);
                for (int i = 0; i < jsonArray.size(); i++) {
                    Log.e("摄像机回调", "---》" + o);
                    CameraURLList.add(jsonArray.getJSONObject(i).getString("camera_add"));
                }
                client.start(CameraURLList.get(0), Client.TRANSTYPE_UDP, Client.TRANSTYPE_UDP,
                        Client.EASY_SDK_VIDEO_FRAME_FLAG
                                | Client.EASY_SDK_AUDIO_FRAME_FLAG, "", "", null);
            }
        });
        nowPosision = "1";
        title_cm.setText(nowPosision + "号靶实时影像");
        title_right.setText("1号靶位计分图");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void cmareSelectCallback(CameraSelectEvent selectCalBack) {

        client.stop();
        client.start(selectCalBack.getResult(), Client.TRANSTYPE_UDP, Client.TRANSTYPE_UDP,
                Client.EASY_SDK_VIDEO_FRAME_FLAG
                        | Client.EASY_SDK_AUDIO_FRAME_FLAG, "", "", null);
        nowPosision = selectCalBack.getPositon();
        title_cm.setText(nowPosision + "号靶实时影像");
        title_right.setText(nowPosision+"号靶位计分图");

        resetData();
//        Bitmap ShoInput = ((BitmapDrawable) icon_show.getDrawable()).getBitmap();
//        bitMapShow = zoomImg(ShoInput, 909, 909);
//        icon_show.setImageBitmap(bitMapShow);


    }

    int count = 0;
    Disposable disposable;

    private void getFuckData() {
        disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        count = count + 1;
                        Log.e("MainActivity", "----------RxJava 定时轮询任务----------" + count);
                        getStatues();
                        Map map = new HashMap();
                        RequestTools.doAction().getData(RetrofitUtils.getService().getXY(map),
                                new GetResult<String>() {
                                    @Override
                                    public void fail(String msg) {
                                    }
                                    @Override
                                    public void ok(String s) {
                                        Log.e("XY获取", "------------" + s);
//                                        开始画图
                                        JSONObject jsonObject = JSONObject.parseObject(s);
                                        if (jsonObject != null) {
                                            if (nowPosision.equals("1")) {
                                                if (jsonObject.getString("p_yi") != null) {
                                                    doDraw(JSONObject.parseObject(jsonObject.getString("p_yi")),jsonObject.getString("picSave"));
                                                }
                                            }
                                            if (nowPosision.equals("2")) {
                                                if (jsonObject.getString("p_er") != null) {
                                                    doDraw(JSONObject.parseObject(jsonObject.getString("p_er")),jsonObject.getString("picSave"));
                                                }
                                            }
                                            if (nowPosision.equals("3")) {
                                                if (jsonObject.getString("p_san") != null) {
                                                    doDraw(JSONObject.parseObject(jsonObject.getString("p_san")),jsonObject.getString("picSave"));
                                                }
                                            }
                                            if (nowPosision.equals("4")) {
                                                if (jsonObject.getString("p_si") != null) {
                                                    doDraw(JSONObject.parseObject(jsonObject.getString("p_si")),jsonObject.getString("picSave"));
                                                }
                                            }

                                            if (nowPosision.equals("5")) {
                                                if (jsonObject.getString("p_wu") != null) {
                                                    doDraw(JSONObject.parseObject(jsonObject.getString("p_wu")),jsonObject.getString("picSave"));
                                                }
                                            }

                                            if (nowPosision.equals("6")) {
                                                if (jsonObject.getString("p_liu") != null) {
                                                    doDraw(JSONObject.parseObject(jsonObject.getString("p_liu")),jsonObject.getString("picSave"));
                                                }
                                            }
                                            if (nowPosision.equals("7")) {
                                                if (jsonObject.getString("p_qi") != null) {
                                                    doDraw(JSONObject.parseObject(jsonObject.getString("p_qi")),jsonObject.getString("picSave"));
                                                }
                                            }

                                            if (nowPosision.equals("8")) {
                                                if (jsonObject.getString("p_ba") != null) {
                                                    doDraw(JSONObject.parseObject(jsonObject.getString("p_ba")),jsonObject.getString("picSave"));
                                                }
                                            }
                                            if (nowPosision.equals("9")) {
                                                if (jsonObject.getString("p_jiu") != null) {
                                                    doDraw(JSONObject.parseObject(jsonObject.getString("p_jiu")),jsonObject.getString("picSave"));
                                                }
                                            }
                                            if (nowPosision.equals("10")) {
                                                if (jsonObject.getString("p_shi") != null) {
                                                    doDraw(JSONObject.parseObject(jsonObject.getString("p_shi")),jsonObject.getString("picSave"));
                                                }
                                            }
                                        }
                                    }
                                });
                    }
                });
    }

    void getStatues(){
        Map map = new HashMap();
        map.put("position", "1");
        RequestTools.doAction().getData(RetrofitUtils.getService().getTaskStatus(map),
                new GetResult<String>() {
                    @Override
                    public void fail(String msg) {
                    }

                    @Override
                    public void ok(String s) {
                        dealFuckData(s);
                    }
                });
    }

    private float x = 10000000;
    private float y = 100000000;

    String timeFlag = "";
    String nameFlag = "";
    String nowGroupFlag = "";
    String Times = "";
    private void dealFuckData(String s) {
        Log.e("deal", "-----start--->");
        JSONObject jsonObjectResPon = JSONObject.parseObject(s);
        String nowGruop = jsonObjectResPon.getString("now_group");
        if (!nowGroupFlag.equals(nowGruop)){
            nowGroupFlag = nowGruop;
            resetData();
        }

        Times = jsonObjectResPon.getString("now_times");
        if (!Times.equals(timeFlag)) {
            timeFlag = Times;
            resetData();
        }
        JSONArray jsonArrayForUser = JSONArray.parseArray((JSONObject.parseObject(jsonObjectResPon.getString("task_data"))).getString("userData"));
        for (int i = 0; i < jsonArrayForUser.size(); i++) {
            JSONObject user = jsonArrayForUser.getJSONObject(i);
            if (user.getString("gruop").equals(nowGruop) & user.getString("position").equals(nowPosision)) {
                userJson = user;
                name = user.getString("user_name");
                if (!name.equals(nameFlag)) {
                    tv_name.setText("姓名:" + user.getString("user_name"));
                    nameFlag = name;
                    resetData();
                }

            }
        }

    }

    @BindView(R.id.tv_10h_times1)
    TextView tv_10h_times1;

    @BindView(R.id.tv_9h_times1)
    TextView tv_9h_times1;

    @BindView(R.id.tv_8h_times1)
    TextView tv_8h_times1;

    @BindView(R.id.tv_7h_times1)
    TextView tv_7h_times1;

    @BindView(R.id.tv_6h_times1)
    TextView tv_6h_times1;

    @BindView(R.id.tv_5h_times1)
    TextView tv_5h_times1;


    @BindView(R.id.tv_10h_times2)
    TextView tv_10h_times2;

    @BindView(R.id.tv_9h_times2)
    TextView tv_9h_times2;

    @BindView(R.id.tv_8h_times2)
    TextView tv_8h_times2;

    @BindView(R.id.tv_7h_times2)
    TextView tv_7h_times2;

    @BindView(R.id.tv_6h_times2)
    TextView tv_6h_times2;

    @BindView(R.id.tv_5h_times2)
    TextView tv_5h_times2;


    @BindView(R.id.tv_10h_times3)
    TextView tv_10h_times3;

    @BindView(R.id.tv_9h_times3)
    TextView tv_9h_times3;

    @BindView(R.id.tv_8h_times3)
    TextView tv_8h_times3;

    @BindView(R.id.tv_7h_times3)
    TextView tv_7h_times3;

    @BindView(R.id.tv_6h_times3)
    TextView tv_6h_times3;

    @BindView(R.id.tv_5h_times3)
    TextView tv_5h_times3;





    boolean isReset = false;
    private void resetData(){
//
        Log.e("XY计算", "------------" + "开始重置");
//        icon_show.setImageBitmap(revertBitMap);
//        bitMapShow = revertBitMap;
//        isReset = true;
        Log.e("XY计算", "------------" + "开始重置END");
        tv_10h_times1.setText("");
        tv_9h_times1.setText("");
        tv_8h_times1.setText("");
        tv_7h_times1.setText("");
        tv_6h_times1.setText("");
        tv_5h_times1.setText("");
        tv_10h_times2.setText("");
        tv_9h_times2.setText("");
        tv_8h_times2.setText("");
        tv_7h_times2.setText("");
        tv_6h_times2.setText("");
        tv_5h_times2.setText("");
        tv_10h_times3.setText("");
        tv_9h_times3.setText("");
        tv_8h_times3.setText("");
        tv_7h_times3.setText("");
        tv_6h_times3.setText("");
        tv_5h_times3.setText("");
    }

    String nowPic = "";
    private void doDraw(JSONObject jsonObject,String pic) {
        Log.e("XY计算", "------------" + jsonObject);
        if (jsonObject==null){
            return;
        }
        Log.e("XY计算", "------------" + pic);

        JSONArray jsonArray = JSONArray.parseArray(pic);
        for (int i=0;i<jsonArray.size();i++){
            JSONObject jsonObject1 = jsonArray.getJSONObject(i);
            if (jsonObject1.getString("pic_position").equals(nowPosision)){
                if (!jsonObject1.getString("pic_name").equals("")){
                    ivDefalut.setVisibility(View.GONE);
                    icon_show.setVisibility(View.VISIBLE);
                    String URI = RxBaseService.BaseURL+ "picDl/"+jsonObject1.getString("pic_name");
                    Log.e("图片判断", "-----图片非空-------");
                    Glide.with(this).load(URI).into(icon_show);
                    if (!nowPic.equals(jsonObject1.getString("pic_name"))){
                        nowPic =jsonObject1.getString("pic_name");
                    }
                }else{
                    Log.e("图片判断", "-----图片是空-------" + pic);
                    icon_show.setVisibility(View.GONE);
                    ivDefalut.setVisibility(View.VISIBLE);
                }
            }
        }


        Log.e("XY计算", "--------开始写入成绩----");
        NewSPTools.getInstance().saveNowShootData(jsonObject.getString("taskKey"),jsonObject.getString("nowTimes"),name,jsonObject+"");
        Map<String,String> map = NewSPTools.getInstance().getNowShootData(jsonObject.getString("taskKey"),name);
        int total =0;
        int ringALl =0;

        if (!map.get("times1").equals("")){
            JSONObject data = JSONObject.parseObject(map.get("times1"));
            tv_10h_times1.setText(data.getString("data5"));
            tv_9h_times1.setText(data.getString("data6"));
            tv_8h_times1.setText(data.getString("data7"));
            tv_7h_times1.setText(data.getString("data8"));
            tv_6h_times1.setText(data.getString("data9"));
            tv_5h_times1.setText(data.getString("data10"));
//            计算总成绩和环数

            if (!data.getString("data5").equals("")){
                total = total+Integer.parseInt(data.getString("data5"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data5"))*5));
            }
            if (!data.getString("data6").equals("")){
                total = total+Integer.parseInt(data.getString("data6"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data6"))*6));
            }
            if (!data.getString("data7").equals("")){
                total = total+Integer.parseInt(data.getString("data7"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data7"))*7));
            }
            if (!data.getString("data8").equals("")){
                total = total+Integer.parseInt(data.getString("data8"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data8"))*8));
            }
            if (!data.getString("data9").equals("")){
                total = total+Integer.parseInt(data.getString("data9"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data9"))*9));
            }
            if (!data.getString("data10").equals("")){
                total = total+Integer.parseInt(data.getString("data10"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data10"))*10));
            }

        }
        if (!map.get("times2").equals("")){
            JSONObject data = JSONObject.parseObject(map.get("times2"));
            tv_10h_times2.setText(data.getString("data5"));
            tv_9h_times2.setText(data.getString("data6"));
            tv_8h_times2.setText(data.getString("data7"));
            tv_7h_times2.setText(data.getString("data8"));
            tv_6h_times2.setText(data.getString("data9"));
            tv_5h_times2.setText(data.getString("data10"));


            if (!data.getString("data5").equals("")){
                total = total+Integer.parseInt(data.getString("data5"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data5"))*5));
            }
            if (!data.getString("data6").equals("")){
                total = total+Integer.parseInt(data.getString("data6"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data6"))*6));
            }
            if (!data.getString("data7").equals("")){
                total = total+Integer.parseInt(data.getString("data7"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data7"))*7));
            }
            if (!data.getString("data8").equals("")){
                total = total+Integer.parseInt(data.getString("data8"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data8"))*8));
            }
            if (!data.getString("data9").equals("")){
                total = total+Integer.parseInt(data.getString("data9"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data9"))*9));
            }
            if (!data.getString("data10").equals("")){
                total = total+Integer.parseInt(data.getString("data10"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data10"))*10));
            }

        }

        if (!map.get("times3").equals("")){
            JSONObject data = JSONObject.parseObject(map.get("times3"));
            tv_10h_times3.setText(data.getString("data5"));
            tv_9h_times3.setText(data.getString("data6"));
            tv_8h_times3.setText(data.getString("data7"));
            tv_7h_times3.setText(data.getString("data8"));
            tv_6h_times3.setText(data.getString("data9"));
            tv_5h_times3.setText(data.getString("data10"));
            if (!data.getString("data5").equals("")){
                total = total+Integer.parseInt(data.getString("data5"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data5"))*5));
            }
            if (!data.getString("data6").equals("")){
                total = total+Integer.parseInt(data.getString("data6"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data6"))*6));
            }
            if (!data.getString("data7").equals("")){
                total = total+Integer.parseInt(data.getString("data7"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data7"))*7));
            }
            if (!data.getString("data8").equals("")){
                total = total+Integer.parseInt(data.getString("data8"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data8"))*8));
            }
            if (!data.getString("data9").equals("")){
                total = total+Integer.parseInt(data.getString("data9"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data9"))*9));
            }
            if (!data.getString("data10").equals("")){
                total = total+Integer.parseInt(data.getString("data10"));
                ringALl = ringALl+((Integer.parseInt(data.getString("data10"))*10));
            }
        }
        tv_shoot_number.setText("命中发数:"+total+"发");
        tv_shoot_ring.setText("命中环数："+ringALl+"环");
    }

    @BindView(R.id.tv_shoot_number)
    TextView tv_shoot_number;
    @BindView(R.id.tv_shoot_ring)
    TextView tv_shoot_ring;
    @BindView(R.id.iv_bg)
    ImageView ivDefalut;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        disposable.dispose();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {

    }
}
