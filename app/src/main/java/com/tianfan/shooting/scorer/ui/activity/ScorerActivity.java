package com.tianfan.shooting.scorer.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.tianfan.shooting.BuildConfig;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.CameraListActivity;
import com.tianfan.shooting.admin.mvp.presenter.ScorerPersenter;
import com.tianfan.shooting.admin.mvp.view.ScorerView;
import com.tianfan.shooting.admin.ui.evendata.CameraSelectEvent;
import com.tianfan.shooting.bean.CameraBean;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.net.FileUpLoadTools;
import com.tianfan.shooting.net.GetResult;
import com.tianfan.shooting.net.RequestTools;
import com.tianfan.shooting.net.RetrofitUtils;
import com.tianfan.shooting.tools.PicSaveTools;
import com.tianfan.shooting.view.LoadingDialog;


import org.easydarwin.video.Client;

import org.easydarwin.video.EasyPlayerClient;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;
import ua.polohalo.zoomabletextureview.ZoomableTextureView;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-25 08:45
 * @Description 记分员角色
 */
public class ScorerActivity extends AppCompatActivity implements View.OnClickListener, ScorerView {
    @BindView(R.id.cm_view)
    ZoomableTextureView textureView;
    public JSONObject userJson = new JSONObject();
    //名字
    private PromptDialog promptDialog;

    @BindView(R.id.iv_back)
    ImageView iv_back;
    @BindView(R.id.tv_positon)
    TextView tvPosition;

    @BindView(R.id.tv_ring_lb)
    TextView tv_ring_lb;

    @BindView(R.id.tv_10_h_lb)
    TextView tv_10_h_lb;

    @BindView(R.id.tv_9_h_lb)
    TextView tv_9_h_lb;

    @BindView(R.id.tv_8_h_lb)
    TextView tv_8_h_lb;

    @BindView(R.id.tv_7_h_lb)
    TextView tv_7_h_lb;

    @BindView(R.id.tv_6_h_lb)
    TextView tv_6_h_lb;

    @BindView(R.id.tv_5_h_lb)
    TextView tv_5_h_lb;

    @BindView(R.id.tv_previos_times)
    TextView tv_previos_times;

    @BindView(R.id.tv_next_time)
    TextView tv_next_time;

    @BindView(R.id.tv_reset)
    TextView tv_reset;

    @BindView(R.id.tv_switch)
    TextView tv_switch;

    @BindView(R.id.tv_save)
    TextView tv_save;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_now_times_data)
    TextView tv_now_times_data;

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
    @BindView(R.id.tv_0h_data)
    TextView tv_0h_data;

    @BindView(R.id.tv_allring_data)
    TextView tv_allring_data;
    @BindView(R.id.tv_all_fa_data)
    TextView tv_all_fa_data;
    @BindView(R.id.tv_all_data)
    TextView tv_all_data;
    //    private String nowPosision = "";
    @BindView(R.id.title_cm)
    TextView title_cm;
    String name = "";
    String Times = "";
    String taskType = "";
    String tasKey = "";
    private EasyPlayerClient client;

    private List<String> scoreList = new ArrayList<>();
    private List<Bitmap> scoreBitmaps = new ArrayList<>();

    //    private int checkTarget;
    private CameraBean mCameraBean;

    private ScorerPersenter mScorerPersenter;
    public LoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorer);
        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
        promptDialog = new PromptDialog(this);
//        textureView = findViewById(R.id.cm_view);
        client = new EasyPlayerClient(this, BuildConfig.RTSP_KEY, textureView, null, null);
        try {
            doInit();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void doInit() {
//        checkTarget = getIntent().getIntExtra("checkTarget",0);
        mCameraBean = getIntent().getParcelableExtra("CameraBean");

        title_cm.setText(mCameraBean.getCamera_col() + "号靶位实时影像");
        mLoadingDialog = new LoadingDialog(this, R.style.TransparentDialog);
        mLoadingDialog.setTitle("加载中");
        mLoadingDialog.setCancelable(false);
        mScorerPersenter = new ScorerPersenter(this);
//        getFuckData();
        initListen();
//        client.start("rtsp://192.168.1.6/vod/mp4://BigBuckBunny_175k.mov", Client.TRANSTYPE_UDP, Client.TRANSTYPE_UDP,
//                Client.EASY_SDK_VIDEO_FRAME_FLAG
//                        | Client.EASY_SDK_AUDIO_FRAME_FLAG, "", "", null);
//        initCameraList();
    }

    private Bitmap revertBitMap;
    private Bitmap readBitMap;
    ImageView icon_show;
    int showWid = 0;
    int showHight = 0;

    @SuppressLint("ClickableViewAccessibility")
    void initListen() {
        ImageView imageView = findViewById(R.id.iv_ai_pic);
        icon_show = findViewById(R.id.iv_ai_show);
        Bitmap ShoInput = ((BitmapDrawable) icon_show.getDrawable()).getBitmap();
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        icon_show.measure(w, h);
        showHight = icon_show.getMeasuredHeight();
        showWid = icon_show.getMeasuredWidth();


        Log.e("icon_show", "icshow>>>>XXXX>>>>" + showWid);
        Log.e("icon_show", "View>>>YYYY>>>>>>>>" + showHight);
        Bitmap bitMapShow = zoomImg(ShoInput, 909, 909);
        readBitMap = zoomImg(((BitmapDrawable) imageView.getDrawable()).getBitmap(), 909, 909);
        imageView.setImageBitmap(readBitMap);
        revertBitMap = bitMapShow;
        icon_show.setImageBitmap(bitMapShow);
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.e("点击", "View>>>>XXXX>>>>" + v.getWidth());
                    Log.e("点击", "View>>>YYYY>>>>>>>>" + v.getHeight());
                    Log.e("点击", "ev点击的位置>>>>X>>>>" + event.getX());
                    Log.e("点击", "ev点击的位置>>>Y>>>>>" + event.getY());
                    Bitmap imageInput = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    Bitmap cvMap = zoomImg(imageInput, v.getWidth(), v.getHeight());
                    int color = cvMap.getPixel((int) event.getX(), (int) event.getY());
                    int r = Color.red(color);
                    int g = Color.green(color);
                    int b = Color.blue(color);
                    Log.e("点击", "----------------RBG>>>>>>>>>>>>" + convertRGBToHex(r, g, b));

                    if (convertRGBToHex(r, g, b).equals("#CC0000")) {
                        Log.e("点击", "----------------十环");

                        startCalculation("10");
                    } else if (convertRGBToHex(r, g, b).equals("#990099")) {
                        Log.e("点击", "----------------九环");
                        startCalculation("9");
                    } else if (convertRGBToHex(r, g, b).equals("#8AB975")) {
                        Log.e("点击", "----------------八环");
                        startCalculation("8");
                    } else if (convertRGBToHex(r, g, b).equals("#FFFF00")) {
                        Log.e("点击", "----------------七环");
                        startCalculation("7");
                    } else if (convertRGBToHex(r, g, b).equals("#003300")) {
                        Log.e("点击", "----------------六环");
                        startCalculation("6");
                    } else if (convertRGBToHex(r, g, b).equals("#0000CC")) {
                        Log.e("点击", "----------------五环");
                        startCalculation("5");
                    } else {
                        startCalculation("4");
                    }
//                   else  (convertRGBToHex(r, g, b).equals("#CECECE")) {
//                        Log.e("点击", "----------------四环");
//                        startCalculation("4");
//                    }
                    Bitmap imageInput2 = ((BitmapDrawable) icon_show.getDrawable()).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
                    Bitmap cvMap2 = zoomImg(imageInput2, 909, 909);
                    Canvas canvas = new Canvas(cvMap2);
                    Paint paint = new Paint();
                    paint.setColor(Color.WHITE);
                    paint.setStrokeWidth(7);
                    canvas.drawCircle(909 * (event.getX() / v.getWidth()), 909 * (event.getY() / v.getHeight()), 12, paint);
                    paint.setColor(Color.BLACK);
                    paint.setStrokeWidth(5);
                    canvas.drawCircle(909 * (event.getX() / v.getWidth()), 909 * (event.getY() / v.getHeight()), 10, paint);
                    icon_show.setImageBitmap(cvMap2);
                    scoreBitmaps.add(cvMap2);
//                    开始将图片保存到本地
////                    JSONObject map = new JSONObject();
////                    map.put("position", "" + checkTarget);
////                    FileUpLoadTools.doUpLoadPic(new FileUpLoadTools.FileUpCallBack() {
////                        @Override
////                        public void result(boolean result, String msg) {
////                            Log.e("照片上传回调", msg);
////                        }
////                    }, PicSaveTools.savePhotoToSDCard(icon_show), map);
////                    Pic
////                    开始计算落点位置、得出比例，传到服务端，射击角色按比例画出
////                    float xUp = event.getX() / v.getWidth();
////                    float yUp = event.getY() / v.getHeight();
////                    JSONObject jsonObject = new JSONObject();
////                    jsonObject.put("x", "" + xUp);
////                    jsonObject.put("y", "" + yUp);
////                    upXY(jsonObject);
////                    Log.e("坐标比例计算结果", "----------------X" + xUp + ",yyy---" + yUp);
                }
                return false;
            }
        });
    }


    /**
     * @param type 判断类型，对应的数据自增1
     */
    private void startCalculation(String type) {
        scoreList.add(type);
        if (type.equals("10")) {
            if (!tv_10h_data.getText().toString().equals("")) {
                tv_10h_data.setText((Integer.parseInt(tv_10h_data.getText().toString()) + 1) + "");
            } else {
                tv_10h_data.setText("1");
            }
        } else if (type.equals("9")) {

            if (!tv_9h_data.getText().toString().equals("")) {
                tv_9h_data.setText((Integer.parseInt(tv_9h_data.getText().toString()) + 1) + "");
            } else {
                tv_9h_data.setText("1");
            }

        } else if (type.equals("8")) {
            if (!tv_8h_data.getText().toString().equals("")) {
                tv_8h_data.setText((Integer.parseInt(tv_8h_data.getText().toString()) + 1) + "");
            } else {
                tv_8h_data.setText("1");
            }
        } else if (type.equals("7")) {

            if (!tv_7h_data.getText().toString().equals("")) {
                tv_7h_data.setText((Integer.parseInt(tv_7h_data.getText().toString()) + 1) + "");
            } else {
                tv_7h_data.setText("1");
            }
        } else if (type.equals("6")) {
            if (!tv_6h_data.getText().toString().equals("")) {
                tv_6h_data.setText((Integer.parseInt(tv_6h_data.getText().toString()) + 1) + "");
            } else {
                tv_6h_data.setText("1");
            }
        } else if (type.equals("5")) {
            if (!tv_5h_data.getText().toString().equals("")) {
                tv_5h_data.setText((Integer.parseInt(tv_5h_data.getText().toString()) + 1) + "");
            } else {
                tv_5h_data.setText("1");
            }
        } else {
            if (!tv_0h_data.getText().toString().equals("")) {
                tv_0h_data.setText((Integer.parseInt(tv_0h_data.getText().toString()) + 1) + "");
            } else {
                tv_0h_data.setText("1");
            }
        }

        calculationRingAndFa(false);
    }

    private void calculationRingAndFa(boolean isRevocation) {
        int allRingCa = 0;
        int allFaCa = 0;
        if (!tv_10h_data.getText().toString().equals("")) {
            allRingCa = allRingCa + (10 * Integer.parseInt(tv_10h_data.getText().toString()));
            allFaCa = allFaCa + Integer.parseInt(tv_10h_data.getText().toString());
        }
        if (!tv_9h_data.getText().toString().equals("")) {
            allRingCa = allRingCa + (9 * Integer.parseInt(tv_9h_data.getText().toString()));
            allFaCa = allFaCa + Integer.parseInt(tv_9h_data.getText().toString());
        }
        if (!tv_8h_data.getText().toString().equals("")) {
            allRingCa = allRingCa + (8 * Integer.parseInt(tv_8h_data.getText().toString()));
            allFaCa = allFaCa + Integer.parseInt(tv_8h_data.getText().toString());
        }
        if (!tv_7h_data.getText().toString().equals("")) {
            allRingCa = allRingCa + (7 * Integer.parseInt(tv_7h_data.getText().toString()));
            allFaCa = allFaCa + Integer.parseInt(tv_7h_data.getText().toString());
        }
        if (!tv_6h_data.getText().toString().equals("")) {
            allRingCa = allRingCa + (6 * Integer.parseInt(tv_6h_data.getText().toString()));
            allFaCa = allFaCa + Integer.parseInt(tv_6h_data.getText().toString());
        }
        if (!tv_5h_data.getText().toString().equals("")) {
            allRingCa = allRingCa + (5 * Integer.parseInt(tv_5h_data.getText().toString()));
            allFaCa = allFaCa + Integer.parseInt(tv_5h_data.getText().toString());
        }
        //判断是否是撤销
        if (isRevocation){
            if (Integer.parseInt(tv_all_data.getText().toString())>0) {
                tv_all_data.setText(String.valueOf(Integer.parseInt(tv_all_data.getText().toString()) - 1));
            } else {
                tv_all_data.setText("");
            }
        }else {
            if (!TextUtils.isEmpty(tv_all_data.getText().toString())) {
                tv_all_data.setText(String.valueOf(Integer.parseInt(tv_all_data.getText().toString()) + 1));
            } else {
                tv_all_data.setText(String.valueOf(1));
            }
        }

        tv_allring_data.setText(allRingCa == 0 ? "" : String.valueOf(allRingCa));
        tv_all_fa_data.setText(allFaCa == 0 ? "" : String.valueOf(allFaCa));

    }


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


    //**将rgb色彩值转成16进制代码**
    public String convertRGBToHex(int r, int g, int b) {
        String rFString, rSString, gFString, gSString,
                bFString, bSString, result;
        int red, green, blue;
        int rred, rgreen, rblue;
        red = r / 16;
        rred = r % 16;
        if (red == 10) {
            rFString = "A";
        } else if (red == 11) {
            rFString = "B";
        } else if (red == 12) {
            rFString = "C";
        } else if (red == 13) {
            rFString = "D";
        } else if (red == 14) {
            rFString = "E";
        } else if (red == 15) {
            rFString = "F";
        } else {
            rFString = String.valueOf(red);
        }

        if (rred == 10) {
            rSString = "A";
        } else if (rred == 11) {
            rSString = "B";
        } else if (rred == 12) {
            rSString = "C";
        } else if (rred == 13) {
            rSString = "D";
        } else if (rred == 14) {
            rSString = "E";
        } else if (rred == 15) {
            rSString = "F";
        } else {
            rSString = String.valueOf(rred);
        }

        rFString = rFString + rSString;

        green = g / 16;
        rgreen = g % 16;

        if (green == 10) {
            gFString = "A";
        } else if (green == 11) {
            gFString = "B";
        } else if (green == 12) {
            gFString = "C";
        } else if (green == 13) {
            gFString = "D";
        } else if (green == 14) {
            gFString = "E";
        } else if (green == 15) {
            gFString = "F";
        } else {
            gFString = String.valueOf(green);
        }

        if (rgreen == 10) {
            gSString = "A";
        } else if (rgreen == 11) {
            gSString = "B";
        } else if (rgreen == 12) {
            gSString = "C";
        } else if (rgreen == 13) {
            gSString = "D";
        } else if (rgreen == 14) {
            gSString = "E";
        } else if (rgreen == 15) {
            gSString = "F";
        } else {
            gSString = String.valueOf(rgreen);
        }

        gFString = gFString + gSString;

        blue = b / 16;
        rblue = b % 16;

        if (blue == 10) {
            bFString = "A";
        } else if (blue == 11) {
            bFString = "B";
        } else if (blue == 12) {
            bFString = "C";
        } else if (blue == 13) {
            bFString = "D";
        } else if (blue == 14) {
            bFString = "E";
        } else if (blue == 15) {
            bFString = "F";
        } else {
            bFString = String.valueOf(blue);
        }

        if (rblue == 10) {
            bSString = "A";
        } else if (rblue == 11) {
            bSString = "B";
        } else if (rblue == 12) {
            bSString = "C";
        } else if (rblue == 13) {
            bSString = "D";
        } else if (rblue == 14) {
            bSString = "E";
        } else if (rblue == 15) {
            bSString = "F";
        } else {
            bSString = String.valueOf(rblue);
        }
        bFString = bFString + bSString;
        result = "#" + rFString + gFString + bFString;
        return result;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().unregister(this);
//        }
//        if (disposable!=null){
//            disposable.dispose();
//        }

    }


//    List<String> CameraURLList = new ArrayList<>();
//    List<String> spStrLIst = new ArrayList<>();
//    List<String> positonList = new ArrayList<>();

 /*   void initCameraList() {
        RequestTools.doAction().getData(RetrofitUtils.getService().getCameraList(new HashMap<>()), new GetResult<String>() {
            @Override
            public void fail(String msg) {
            }

            @Override
            public void ok(String o) {
                CameraURLList.clear();
                spStrLIst.clear();
                positonList.clear();
                JSONArray jsonArray = JSONArray.parseArray(o);
                for (int i = 0; i < jsonArray.size(); i++) {
                    Log.e("摄像机回调", "---》" + o);
                    CameraURLList.add(jsonArray.getJSONObject(i).getString("camera_add"));
                    spStrLIst.add(jsonArray.getJSONObject(i).getString("position") + "号位");
                    positonList.add(jsonArray.getJSONObject(i).getString("position"));
                }

                client.start("rtsp://192.168.1.6/vod/mp4://BigBuckBunny_175k.mov", Client.TRANSTYPE_UDP, Client.TRANSTYPE_UDP,
                        Client.EASY_SDK_VIDEO_FRAME_FLAG
                                | Client.EASY_SDK_AUDIO_FRAME_FLAG, "", "", null);

            }
        });
//        nowPosision = "1";
//        title_cm.setText(checkTarget + "号靶位实时影像");

    }*/

  /*  @Subscribe(threadMode = ThreadMode.MAIN)
    public void SelectCallback(CameraSelectEvent selectCalBack) {
        try {
            Toast.makeText(getApplicationContext(), selectCalBack.getResult(), Toast.LENGTH_SHORT).show();
            client.stop();
            client.start(selectCalBack.getResult(), Client.TRANSTYPE_UDP, Client.TRANSTYPE_UDP,
                    Client.EASY_SDK_VIDEO_FRAME_FLAG
                            | Client.EASY_SDK_AUDIO_FRAME_FLAG, "", "", null);
//            nowPosision = selectCalBack.getPositon();
//            title_cm.setText(nowPosision + "号靶位实时影像");
            resetData();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "" + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }*/


    //点击事件监听
    @OnClick({R.id.iv_back, R.id.tv_switch, R.id.iv_ai_pic, R.id.tv_reset, R.id.tv_save})
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.iv_back) {
            finish();
        } else if (v.getId() == R.id.tv_switch) {
            if (scoreList != null && scoreList.size() > 0) {
                revocationData(scoreList.get(scoreList.size() - 1));
            }
//            startActivity(new Intent(getApplicationContext(), CameraListActivity.class));
        } else if (v.getId() == R.id.tv_reset) {
            promptDialog.showAlertSheet("是否重置？", true, new PromptButton("取消", new PromptButtonListener() {
                @Override
                public void onClick(PromptButton button) {
                }
            }), new PromptButton("确定", new PromptButtonListener() {
                @Override
                public void onClick(PromptButton button) {
                    resetData();
                }
            }));

        } else if (v.getId() == R.id.tv_save) {

            if (!TextUtils.isEmpty(tv_all_data.getText().toString())&&Integer.parseInt(tv_all_data.getText().toString())>0){
                promptDialog.showAlertSheet("是否保存提交？", true, new PromptButton("取消", new PromptButtonListener() {
                    @Override


                    public void onClick(PromptButton button) {
                    }
                }), new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        mScorerPersenter.findTaskInfo();
                    }
                }));
            }else {
                showLoadFailMsg("打靶未计分，请点击计分图计分");
            }
        }

    }

/*
    private void doUp() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("data5", tv_10h_data.getText().toString());
        jsonObject.put("data6", tv_9h_data.getText().toString());
        jsonObject.put("data7", tv_8h_data.getText().toString());
        jsonObject.put("data8", tv_7h_data.getText().toString());
        jsonObject.put("data9", tv_6h_data.getText().toString());
        jsonObject.put("data10", tv_5h_data.getText().toString());
        jsonObject.put("dataAllRing", tv_allring_data.getText().toString());
        jsonObject.put("dataAllFa", tv_all_fa_data.getText().toString());
        uploadataForYHXB(jsonObject);
    }
*/

    /**
     * @author
     * @time
     * @describe 撤销
     */
    private void revocationData(String score) {
        scoreList.remove(scoreList.size() - 1);
        scoreBitmaps.remove(scoreBitmaps.size()-1);
        icon_show.setImageBitmap(revertBitMap);
        for (int i = 0;i<scoreBitmaps.size();i++){
            icon_show.setImageBitmap(scoreBitmaps.get(i));
        }
        if (score.equals("10")) {
            if (tv_10h_data.getText().toString().equals("1")) {
                tv_10h_data.setText("");
            } else {
                tv_10h_data.setText((Integer.parseInt(tv_10h_data.getText().toString()) - 1) + "");
            }
        }
        if (score.equals("9")) {
            if (tv_9h_data.getText().toString().equals("1")) {
                tv_9h_data.setText("");
            } else {
                tv_9h_data.setText((Integer.parseInt(tv_9h_data.getText().toString()) - 1) + "");
            }

        }
        if (score.equals("8")) {
            if (tv_8h_data.getText().toString().equals("1")) {
                tv_8h_data.setText("");
            } else {
                tv_8h_data.setText((Integer.parseInt(tv_8h_data.getText().toString()) - 1) + "");
            }
        }
        if (score.equals("7")) {
            if (tv_7h_data.getText().toString().equals("1")) {
                tv_7h_data.setText("");
            } else {
                tv_7h_data.setText((Integer.parseInt(tv_7h_data.getText().toString()) - 1) + "");
            }
        }
        if (score.equals("6")) {
            if (tv_6h_data.getText().toString().equals("1")) {
                tv_6h_data.setText("");
            } else {
                tv_6h_data.setText((Integer.parseInt(tv_6h_data.getText().toString()) - 1) + "");
            }
        }
        if (score.equals("5")) {
            if (tv_5h_data.getText().toString().equals("1")) {
                tv_5h_data.setText("");
            } else {
                tv_5h_data.setText((Integer.parseInt(tv_5h_data.getText().toString()) - 1) + "");
            }
        }
        if (score.equals("4")){
            if (tv_0h_data.getText().toString().equals("1")) {
                tv_0h_data.setText("");
            } else {
                tv_0h_data.setText((Integer.parseInt(tv_0h_data.getText().toString()) - 1) + "");
            }
        }

        calculationRingAndFa(true);
    }

    private void resetData() {
        tv_10h_data.setText("");
        tv_9h_data.setText("");
        tv_8h_data.setText("");
        tv_7h_data.setText("");
        tv_6h_data.setText("");
        tv_5h_data.setText("");
        tv_allring_data.setText("");
        tv_all_fa_data.setText("");
        tv_0h_data.setText("");
        tv_all_data.setText("0");
        icon_show.setImageBitmap(revertBitMap);
    }

//    int count = 0;
//    Disposable disposable;

  /*  private void getFuckData() {
        disposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        count = count + 1;
                        Log.e("MainActivity", "----------RxJava 定时轮询任务----------" + count);
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
                });
    }
*/
//    String timeFlag = "";
//    String nameFlag = "";
//    String nowGroupFlag = "";


    /*private void dealFuckData(String s) {
        Log.e("deal", "-----start--->");
        JSONObject jsonObjectResPon = JSONObject.parseObject(s);
        String nowGruop = jsonObjectResPon.getString("now_group");
        if (!nowGroupFlag.equals(nowGruop)) {
            nowGroupFlag = nowGruop;
            resetData();
        }
        tv_ring_lb.setText("第" + nowGruop + "组");
        Times = jsonObjectResPon.getString("now_times");
        taskType = jsonObjectResPon.getString("task_type");
        tasKey = jsonObjectResPon.getString("task_key");
        if (!Times.equals(timeFlag)) {
            timeFlag = Times;
            tv_now_times_data.setText("第" + Times + "轮");
            resetData();
        }
        JSONArray jsonArrayForUser = JSONArray.parseArray((JSONObject.parseObject(jsonObjectResPon.getString("task_data"))).getString("userData"));
        for (int i = 0; i < jsonArrayForUser.size(); i++) {
            JSONObject user = jsonArrayForUser.getJSONObject(i);
            if (user.getString("gruop").equals(nowGruop) & user.getString("position").equals(checkTarget+"")) {
                userJson = user;
                name = user.getString("user_name");
                if (!name.equals(nameFlag)) {
                    tv_name.setText("" + user.getString("user_name"));
                    nameFlag = name;
                }
            }
        }
    }*/

    //上传数据
  /*  private void uploadataForYHXB(JSONObject jsonObject) {
        Log.e("取得的数据", "---》" + jsonObject);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < 6; i++) {
            JSONObject type = new JSONObject();
            if (i == 0) {
                type.put("type", "5");
                type.put("result", jsonObject.getString("data5"));
            } else if (i == 1) {
                type.put("type", "6");
                type.put("result", jsonObject.getString("data6"));
            } else if (i == 2) {
                type.put("type", "7");
                type.put("result", jsonObject.getString("data7"));
            } else if (i == 3) {
                type.put("type", "8");
                type.put("result", jsonObject.getString("data8"));
            } else if (i == 4) {
                type.put("type", "9");
                type.put("result", jsonObject.getString("data9"));
            } else if (i == 5) {
                type.put("type", "10");
                type.put("result", jsonObject.getString("data10"));
            }
            jsonArray.add(type);
        }

        Map map = new HashMap();
        map.put("data", "" + jsonArray);
        map.put("userInfo", "" + userJson);
        map.put("nowTimes", Times);
        map.put("type", taskType);
        map.put("key", tasKey);
        map.put("dataAllRing", jsonObject.getString("dataAllRing"));
        map.put("dataAllFa", jsonObject.getString("dataAllFa"));
        Log.e("sendDate--->", "--->" + map);
        RequestTools.doAction().getData(RetrofitUtils.getService().upShootData(map),
                new GetResult<String>() {
                    @Override
                    public void fail(String msg) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void ok(String s) {
                        Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
                    }
                });
    }
*/
  /*  private void upXY(JSONObject data) {
        JSONObject jsonObject = new JSONObject();
        data.put("data5", tv_10h_data.getText().toString());
        data.put("data6", tv_9h_data.getText().toString());
        data.put("data7", tv_8h_data.getText().toString());
        data.put("data8", tv_7h_data.getText().toString());
        data.put("data9", tv_6h_data.getText().toString());
        data.put("data10", tv_5h_data.getText().toString());
        data.put("dataAllRing", tv_allring_data.getText().toString());
        data.put("dataAllFa", tv_all_fa_data.getText().toString());
        data.put("nowTimes", Times);
        data.put("taskKey", tasKey);
        data.put("userName", "" + name);

        Map map = new HashMap();
        map.put("postion", "" + checkTarget);
        map.put("xyData", "" + data);
        map.put("shootData", "" + jsonObject);

        RequestTools.doAction().getData(RetrofitUtils.getService().upXY(map),
                new GetResult<String>() {
                    @Override
                    public void fail(String msg) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void ok(String s) {
                        Log.e("xy上传", ">>>>>>>>>>>>." + s);
                    }
                });
        doUp();

    }*/


    //上传数据
  /*  private void uploadataForWHXB(String result) {
        Map map = new HashMap();
        map.put("data", "" + result);
        map.put("userInfo", "" + userJson);
        map.put("nowTimes", Times);
        map.put("type", taskType);
        map.put("key", tasKey);
        Log.e("sendDate--->", "--->" + map);
        RequestTools.doAction().getData(RetrofitUtils.getService().upShootData(map),
                new GetResult<String>() {
                    @Override
                    public void fail(String msg) {
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void ok(String s) {
                        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                    }
                });

    }
*/
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
                    TaskInfoBean mTaskInfoBean = underwayTask.get(0);
                    mScorerPersenter.recordTaskPersonScore(mTaskInfoBean.getTask_id(),mTaskInfoBean.getTask_rows(),String.valueOf(mCameraBean.getCamera_col()),mTaskInfoBean.getTask_rounds(),
                            tv_0h_data.getText().toString(),tv_5h_data.getText().toString(),tv_6h_data.getText().toString(),tv_7h_data.getText().toString(),tv_8h_data.getText().toString(),
                            tv_9h_data.getText().toString(),tv_10h_data.getText().toString());
                } else {
                    showLoadFailMsg("暂无任务正在打靶");
                }
            }
        } else {
            showLoadFailMsg(jsonObject.getString("message"));
        }
    }

    @Override
    public void RecordTaskPersonScoreResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            resetData();
        }
        showLoadFailMsg(jsonObject.getString("message"));
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
