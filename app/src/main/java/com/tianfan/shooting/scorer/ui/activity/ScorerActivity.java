package com.tianfan.shooting.scorer.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.mvp.presenter.ScorerPersenter;
import com.tianfan.shooting.admin.mvp.view.ScorerView;
import com.tianfan.shooting.bean.CameraBean;
import com.tianfan.shooting.bean.CommandManageBean;
import com.tianfan.shooting.bean.TaskInfoBean;
import com.tianfan.shooting.bean.TaskPersonBean;
import com.tianfan.shooting.view.LoadingDialog;
import com.tianfan.shooting.view.TotalScoreDialog;

import org.easydarwin.video.EasyPlayerClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hcnetsdk.HcnetUtils;
import me.leefeng.promptlibrary.PromptButton;
import me.leefeng.promptlibrary.PromptButtonListener;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-25 08:45
 * @Description 记分员角色
 */
public class ScorerActivity extends AppCompatActivity implements View.OnClickListener, ScorerView {

    @BindView(R.id.sur_player)
    SurfaceView surPlayer;
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

    @BindView(R.id.tv_total_points)
    TextView tv_total_points;//总成绩
    @BindView(R.id.tv_allring_data)
    TextView tv_allring_data;
    @BindView(R.id.tv_all_fa_data)
    TextView tv_all_fa_data;
    @BindView(R.id.tv_all_data)
    TextView tv_all_data;
    @BindView(R.id.title_cm)
    TextView title_cm;

    private List<String> scoreList = new ArrayList<>();
    private List<Bitmap> scoreBitmaps = new ArrayList<>();

    //    private int checkTarget;
    private CameraBean mCameraBean;

    private ScorerPersenter mScorerPersenter;
    public LoadingDialog mLoadingDialog;

    private HcnetUtils mHcnetUtils;
    private TaskInfoBean mTaskInfoBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scorer);
        ButterKnife.bind(this);
//        EventBus.getDefault().register(this);
        promptDialog = new PromptDialog(this);
//        textureView = findViewById(R.id.cm_view);
//        client = new EasyPlayerClient(this, BuildConfig.RTSP_KEY, textureView, null, null);
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
//        client.start("rtsp://admin:Abc1234567@192.168.10.113:554", Client.TRANSTYPE_UDP, Client.TRANSTYPE_UDP,
//                Client.EASY_SDK_VIDEO_FRAME_FLAG
//                        | Client.EASY_SDK_AUDIO_FRAME_FLAG, "", "", null);
//        initCameraList();

        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    mHcnetUtils = new HcnetUtils(surPlayer);
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
        if (mHcnetUtils!=null){
            mHcnetUtils.stopPlay();
        }
    }
    //点击事件监听
    @OnClick({R.id.iv_back, R.id.tv_switch, R.id.iv_ai_pic, R.id.tv_reset, R.id.tv_total_points,R.id.tv_save})
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

        } else if (v.getId()==R.id.tv_total_points){//总成绩
            mScorerPersenter.findTaskInfo(1);

//            TotalScoreDialog dialog  = new TotalScoreDialog(this);
//            dialog.show();
        }else if (v.getId() == R.id.tv_save) {

            if (!TextUtils.isEmpty(tv_all_data.getText().toString())&&Integer.parseInt(tv_all_data.getText().toString())>0){
                promptDialog.showAlertSheet("是否保存提交？", true, new PromptButton("取消", new PromptButtonListener() {
                    @Override


                    public void onClick(PromptButton button) {
                    }
                }), new PromptButton("确定", new PromptButtonListener() {
                    @Override
                    public void onClick(PromptButton button) {
                        mScorerPersenter.findTaskInfo(0);
                    }
                }));
            }else {
                showLoadFailMsg("打靶未计分，请点击计分图计分");
            }
        }

    }

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
        scoreBitmaps.clear();
        if (scoreList!=null&&scoreList.size()>0){
            scoreList.clear();
        }
    }
    @Override
    public void FindTaskInfoResult(Object result,int type) {
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
                    if (type==0){//提交成绩
                        mScorerPersenter.recordTaskPersonScore(mTaskInfoBean.getTask_id(),mTaskInfoBean.getTask_rows(),String.valueOf(mCameraBean.getCamera_col()),mTaskInfoBean.getTask_rounds(),
                                tv_0h_data.getText().toString(),tv_5h_data.getText().toString(),tv_6h_data.getText().toString(),tv_7h_data.getText().toString(),tv_8h_data.getText().toString(),
                                tv_9h_data.getText().toString(),tv_10h_data.getText().toString());
                    }else {//根据靶位查队员询
                        mScorerPersenter.findTaskPerson(mTaskInfoBean.getTask_id(),mTaskInfoBean.getTask_rows(),String.valueOf(mCameraBean.getCamera_col()));
                    }

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
    public void FindTaskPersonResult(Object result) {
        JSONObject jsonObject = JSONObject.parseObject(result.toString());
        int code = jsonObject.getIntValue("code");
        if (code == 1) {
            String datas = jsonObject.getString("datas");
            List<TaskPersonBean> mDatas = JSONArray.parseArray(datas, TaskPersonBean.class);
            if (mDatas.size()>0){
                if (mTaskInfoBean!=null){
                    //查询某个队员的成绩
                    mScorerPersenter.findTaskPersonScore(mDatas.get(0).getTask_id(),mTaskInfoBean.getTask_rounds(),mDatas.get(0).getPerson_id());
                }
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
//            List<CommandManageBean.CommandManageItem.PersonScoreBean> mPersonScoreBean= mDatas.get(0).getPerson_score();
            TotalScoreDialog dialog  = new TotalScoreDialog(this,mTaskInfoBean.getTask_rows(),mCameraBean.getCamera_col(),mDatas);
            dialog.show();
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
