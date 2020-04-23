package com.tianfan.shooting.admin.ui.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;


import com.kelin.scrollablepanel.library.PanelAdapter;
import com.rmondjone.locktableview.LockTableView;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.ui.SyncHorizontalScrollView;
import com.tianfan.shooting.net.GetResult;
import com.tianfan.shooting.net.RequestTools;
import com.tianfan.shooting.net.RetrofitUtils;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-17 15:52
 * @Description 历史数据详情页
 */
public class HistoryDetals extends AppCompatActivity implements View.OnClickListener {


    JSONArray recordList = new JSONArray();
    //    List<ShootDetalsBean>
    List<Map> xiuxiuList = new ArrayList<>();
    JSONObject data;
    JSONArray jsonArrayALLUser;

    SweetAlertDialog pDialog;


    @BindView(R.id.lv_shows_table)
    LinearLayout tableContent;

    LockTableView lockTableView;


    @BindView(R.id.lv_column)
    LinearLayout lv_column;

    @BindView(R.id.lv_name)
    LinearLayout lv_name;

    @BindView(R.id.hv_content)
    SyncHorizontalScrollView hv_content;

    @BindView(R.id.hv_title)
    SyncHorizontalScrollView hv_title;
//


    private int mhight;
    private int mwidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detials);
        ButterKnife.bind(this);
//        getWindow().setBackgroundDrawable(null);
        Bundle bundle = getIntent().getBundleExtra("bd");
        data = JSONObject.parseObject(bundle.getString("data"));
        JSONObject fuckData = JSONObject.parseObject(data.getString("user_data"));
        jsonArrayALLUser = JSONArray.parseArray(fuckData.getString("userData"));

        DisplayMetrics dm = getResources().getDisplayMetrics();
        mhight = dm.heightPixels;
        mwidth = dm.widthPixels;
//
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("加载中");
        pDialog.setCancelable(false);
        pDialog.show();

        hv_content.setScrollView(hv_title);
        hv_title.setScrollView(hv_content);


//        getFuckData();
//
        if (data.getString("type").equals("1")) {
            HashMap map = new HashMap();
            map.put("key", data.getString("task_key"));
            int totlalTimes = Integer.parseInt(data.getString("total_times"));
            Log.e("详情页--", "》》》》》》》》》》》》》start");
            RequestTools.doAction().getData(RetrofitUtils.getService().getRecordData(map),
                    new GetResult<String>() {
                        @Override
                        public void fail(String msg) {
                            pDialog.dismiss();
                        }

                        @Override
                        public void ok(String s) {
                            Log.e("详情页--", "开始查询记录--回调->>>>>" + s);
                            List showList = new ArrayList();
                            recordList = JSONArray.parseArray(s);
                            if (recordList != null) {
                                dealFuckType1(recordList);
                            } else {
//                                pDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "网络请求出错", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        } else if (data.getString("type").equals("2")) {
            HashMap map = new HashMap();
            map.put("key", data.getString("task_key"));
            Log.e("详情页--", "开始查询记录");
            RequestTools.doAction().getData(RetrofitUtils.getService().getRecordData(map),
                    new GetResult<String>() {
                        @Override
                        public void fail(String msg) {
//                            pDialog.dismiss();
                            dealFuckType2(recordList);
                        }
                        @Override
                        public void ok(String s) {
                            Log.e("详情页--", "开始查询记录--回调");
//                            pDialog.dismiss();
                            recordList = JSONArray.parseArray(s);
                        }
                    });
        }

    }

    private void dealFuckType1(JSONArray jsonArray) {
        if (jsonArray.size() > 0) {
            Log.e("有记录", ">>>>>>>");
            int cont = 0;
//            统计出现最多的Times
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray shootList = JSONArray.parseArray(jsonObject.getString("shoot_data"));
                for (int j = 0; j < shootList.size(); j++) {
                    JSONObject record = shootList.getJSONObject(j);
                    int temp = Integer.parseInt(record.getString("times"));
                    if (temp > cont) {
                        cont = temp;
                    }
                }
            }
            dealType1Data(jsonArrayALLUser, jsonArray, cont);
            Log.e("最大的值", ">>>>>>>" + cont);
        }
        if (jsonArray.size() < 1) {
            Log.e("无记录", ">>>>>>>");
        }
    }

    private void dealFuckType2(JSONArray jsonArray) {
        if (jsonArray.size() > 0) {
            Log.e("TYpe2有记录", ">>>>>>>");
            int cont = 0;
//            统计出现最多的Times
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                JSONArray shootList = JSONArray.parseArray(jsonObject.getString("shoot_data"));
                for (int j = 0; j < shootList.size(); j++) {
                    JSONObject record = shootList.getJSONObject(j);
                    int temp = Integer.parseInt(record.getString("times"));
                    if (temp > cont) {
                        cont = temp;
                    }
                }
            }
//            dealType1Data(jsonArrayALLUser, jsonArray, cont);
            Log.e("Type2最大的值", ">>>>>>>" + cont);
        }
        if (jsonArray.size() < 1) {
            Log.e("无记录", ">>>>>>>");
        }
    }


    List<List<JSONObject>> myFuckList = new ArrayList<>();
    List<JSONObject> dataTypeList = new ArrayList<>();

    private void dealType1Data(JSONArray userList, JSONArray recordList, int times) {
        for (int i = 0; i < userList.size(); i++) {
            JSONObject jsonObject = userList.getJSONObject(i);
            List<JSONObject> listByRecyler = new ArrayList<>();
            JSONObject userName = new JSONObject();
            userName.put("data", jsonObject.getString("user_name"));
            userName.put("type", "1");
            listByRecyler.add(userName);
            if (checkHasRecord(jsonObject, recordList)) {
                JSONObject rec = getRecord(jsonObject, recordList);
                int userALl = 0;
                for (int k = 1; k <= times; k++) {
                    JSONArray fuckList = JSONArray.parseArray(rec.getString("shoot_data"));
                    boolean macflag = false;
                    for (int m = 0; m < fuckList.size(); m++) {
                        JSONObject woyaosile = fuckList.getJSONObject(m);
                        if (k == Integer.parseInt(woyaosile.getString("times"))) {
                            macflag = true;
                            JSONArray shalewoBaList = JSONObject.parseArray(woyaosile.getString("data"));
                            String details = "";
                            int timesAllNumber = 0;
                            for (int p = 0; p < shalewoBaList.size(); p++) {
                                if (!shalewoBaList.getJSONObject(p).getString("result").equals("")) {
                                    timesAllNumber = timesAllNumber + Integer.parseInt(shalewoBaList.getJSONObject(p).getString("result"));
                                    details = details + shalewoBaList.getJSONObject(p).getString("type") + "环" + shalewoBaList.getJSONObject(p).getString("result") + "发、";
                                }
                            }
                            userALl = userALl + timesAllNumber;
                            if (i == 0) {
                                JSONObject datype= new JSONObject();
                                datype.put("type","1");
                                datype.put("data","第"+k+"轮命中");
                                dataTypeList.add(datype);
                                datype = new JSONObject();
                                datype.put("type","1");
                                datype.put("data","环数");
                                dataTypeList.add(datype);
                            }
                            JSONObject data1 = new JSONObject();
                            data1.put("data", "" + timesAllNumber + "发");
                            data1.put("type", "1");
                            JSONObject data2 = new JSONObject();
                            data2.put("data", "" + shalewoBaList);
                            data2.put("type", "2");
                            listByRecyler.add(data1);
                            listByRecyler.add(data2);
                        }
                    }

                    if (!macflag) {
                        if (i == 0) {
                            JSONObject datype= new JSONObject();
                            datype.put("type","1");
                            datype.put("data","第"+k+"轮命中");
                            dataTypeList.add(datype);
                            datype = new JSONObject();
                            datype.put("type","1");
                            datype.put("data","环数");
                            dataTypeList.add(datype);
                        }

                        JSONObject data1 = new JSONObject();
                        data1.put("data", "");
                        data1.put("type", "1");
                        JSONObject data2 = new JSONObject();
                        data2.put("data", "");
                        data2.put("type", "2");
                        listByRecyler.add(data1);
                        listByRecyler.add(data2);
                    }
                }
                JSONObject data1 = new JSONObject();
                data1.put("data", userALl);
                data1.put("type", "1");
                listByRecyler.add(data1);

                if (i == 0) {
                    JSONObject datype= new JSONObject();
                    datype.put("type","2");
                    datype.put("data","总分");
                    dataTypeList.add(datype);
                }

            } else {
                int userALl = 0;
                for (int k = 1; k <= times; k++) {
                    if (i == 0) {
                        JSONObject datype= new JSONObject();
                        datype.put("type","1");
                        datype.put("data","第"+k+"轮命中");
                        dataTypeList.add(datype);
                        datype = new JSONObject();
                        datype.put("type","1");
                        datype.put("data","环数");
                        dataTypeList.add(datype);
                    }
                    JSONObject data1 = new JSONObject();
                    data1.put("data", "");
                    data1.put("type", "1");
                    JSONObject data2 = new JSONObject();
                    data2.put("data", "");
                    data2.put("type", "2");
                    listByRecyler.add(data1);
                    listByRecyler.add(data2);
                }
                JSONObject data1 = new JSONObject();
                data1.put("data", userALl);
                data1.put("type", "1");
                listByRecyler.add(data1);
                if (i == 0) {
                    JSONObject datype= new JSONObject();
                    datype.put("type","2");
                    datype.put("data","总分");
                    dataTypeList.add(datype);
                }
            }
            if (i == 0) {

            }
            myFuckList.add(listByRecyler);
        }

        List<List<JSONObject>> LastList = new ArrayList<>();
        //内容重新排序，总分放到前面，你说气人不气人
        for (int i =0;i<myFuckList.size();i++){
            List<JSONObject> dalist = myFuckList.get(i);
            List<JSONObject> addList = new ArrayList<>();
            addList.add(dalist.get(0));
            addList.add(dalist.get(dalist.size()-1));
            LastList.add(addList);
        }

        for (int i =0;i<myFuckList.size();i++){
            List<JSONObject> dalist = myFuckList.get(i);
           for (int j=0;j<dalist.size();j++){
               if (j!=dalist.size()-1&&j!=0){
                   LastList.get(i).add(dalist.get(j));
               }
           }
        }

        myFuckList = LastList;
//        加载内容和列表头
        for (int i = 0; i < myFuckList.size(); i++) {
            View viewForLv = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_test_lv, null, false);
            LinearLayout lv = viewForLv.findViewById(R.id.lv_test);
            for (int j = 0; j < myFuckList.get(i).size(); j++) {
                JSONObject jsonObject = myFuckList.get(i).get(j);
                if (j != 0) {
                    if (jsonObject.getString("type").equals("1")) {
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_tabel_detials, null, false);
                        TextView textView = view.findViewById(R.id.tv_type_detials);
                        if (dataTypeList.size()<=8){
                            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) textView.getLayoutParams();
                            params.width=(((mwidth-findViewById(R.id.left_top).getWidth())/dataTypeList.size()));//设置当前控件布局的高度
                            textView.setLayoutParams(params);//将设置好的布局参数应用到控件中
                        }
                        textView.setBackgroundColor(Color.parseColor("#F5F5F5"));
                        textView.setText(jsonObject.getString("data"));
                        if (j==1){
                            textView.setBackgroundColor(Color.parseColor("#C8D8E5"));
                        }
//                        if ()
                        lv.addView(view);
                    } else {
                        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_tabel_detials, null, false);
                        TextView textView = view.findViewById(R.id.tv_type_detials);
                        if (dataTypeList.size()<=8){
                            LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) textView.getLayoutParams();
                            params.width=(((mwidth-findViewById(R.id.left_top).getWidth())/dataTypeList.size()));
                            textView.setLayoutParams(params);//将设置好的布局参数应用到控件中
                        }
                        textView.setBackgroundColor(Color.parseColor("#F0FFF0"));
                        if (!jsonObject.getString("data").equals("")) {
                            JSONArray jsonArray = JSONArray.parseArray(jsonObject.getString("data"));
                            int countToatalRing = 0;
                            for (int cc = 0;cc<jsonArray.size();cc++){
                                JSONObject cData = jsonArray.getJSONObject(cc);
                                if (!cData.getString("result").equals("")){
                                    countToatalRing = countToatalRing+(Integer.parseInt(cData.getString("result"))*Integer.parseInt(cData.getString("type")));
                                }
                            }
                            textView.setText(countToatalRing+"");
                            textView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
//                                    Toast.makeText(getApplicationContext(), jsonObject.getString("data"), Toast.LENGTH_SHORT).show();
                                    DialogSHowDetilas dialogSHowDetilas = new DialogSHowDetilas(mContext,jsonArray);
                                    dialogSHowDetilas.show();

                                }
                            });
                        }
                        lv.addView(view);
                    }
                }
            }
            tableContent.addView(viewForLv);
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_table_colum, null, false);
            TextView textView = view.findViewById(R.id.tv_type_detials);
            textView.setBackgroundColor(Color.parseColor("#87CEFA"));
            JSONObject jsonObject = myFuckList.get(i).get(0);
            textView.setText(jsonObject.getString("data"));
            lv_column.addView(view);
        }
//        表头  横向
        List<JSONObject> listByTBHead = new ArrayList<>();
        for (int i=0;i<dataTypeList.size();i++){
            if (i==0){
                listByTBHead.add(dataTypeList.get(dataTypeList.size()-1));
            }
        }

        for (int i=0;i<dataTypeList.size();i++){
            if (i!=dataTypeList.size()-1){
                listByTBHead.add(dataTypeList.get(i));
            }
        }

        dataTypeList = listByTBHead;

        boolean datyTypeFlag = true;
        for (int m = 0; m < dataTypeList.size(); m++) {
            JSONObject jsonObject = dataTypeList.get(m);
            View viewName = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_tabel_detials, null, false);
            TextView tvDaType = viewName.findViewById(R.id.tv_type_detials);
            tvDaType.setWidth(600);
            if (datyTypeFlag){
                tvDaType.setBackgroundColor(Color.parseColor("#3CB371"));
                if (m==0){

                    tvDaType.setBackgroundColor(Color.parseColor("#FFEFD5"));
                }
                datyTypeFlag = false;
            }else{
                tvDaType.setBackgroundColor(Color.parseColor("#F5FFFA"));
                datyTypeFlag = true;
            }
            tvDaType.setText(jsonObject.getString("data"));
            if (dataTypeList.size()<=8){
                LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) tvDaType.getLayoutParams();
                params.width=(((mwidth-findViewById(R.id.left_top).getWidth())/dataTypeList.size()));
                tvDaType.setLayoutParams(params);//将设置好的布局参数应用到控件中
            }
            lv_name.addView(viewName);
        }
        pDialog.dismiss();

    }

    private AppCompatActivity mContext = this;
    private void setViewWidth(TextView textView, int size) {

        if (size < 4) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            params.width = mwidth / size;//设置当前控件布局的高度
            textView.setLayoutParams(params);
        }

    }

    boolean checkHasRecord(JSONObject user, JSONArray recordList) {
        boolean result = false;
        for (int i = 0; i < recordList.size(); i++) {
            if (user.getString("user_name").equals(recordList.getJSONObject(i).getString("user_name"))) {
                result = true;
            }
        }
        return result;
    }

    JSONObject getRecord(JSONObject user, JSONArray recordList) {
        JSONObject result = new JSONObject();
        for (int i = 0; i < recordList.size(); i++) {
            if (user.getString("user_name").equals(recordList.getJSONObject(i).getString("user_name"))) {
                result = recordList.getJSONObject(i);
            }
        }
        return result;
    }

    //导出数据按钮
    @OnClick({R.id.bt_hs_details_out_data})
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_hs_details_out_data) {
//            promptDialog.showWarnAlert("是否导出Excel文档", new PromptButton("导出", new PromptButtonListener() {
//                @Override
//                public void onClick(PromptButton button) {
//                    createEcel();
//                }
//            }), new PromptButton("取消", new PromptButtonListener() {
//                @Override
//                public void onClick(PromptButton button) {
//
//                }
//            }));
        }
    }


    void createEcel() {
        DialogForInPut2 dialogForInPut2 = new DialogForInPut2(this, new DialogInpuerCallBackTow() {
            @Override
            public void result(String result) {
                if (result.equals("")) {
                    Toast.makeText(getApplicationContext(), "文件名不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < xiuxiuList.size(); i++) {
                    jsonArray.add(xiuxiuList.get(i));
                }
                Map map = new HashMap();
                map.put("fileName", result);
                map.put("data", jsonArray.toJSONString());
                RequestTools.doAction().getData(RetrofitUtils.getService().createFile(map),
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
        });
        dialogForInPut2.show();
    }

    public interface DialogInpuerCallBackTow {
        void result(String result);
    }

    public class DialogForInPut2 extends Dialog {
        DialogInpuerCallBackTow callBackTow;

        public DialogForInPut2(Context context, DialogInpuerCallBackTow dialogInpuerCallBackTow) {
            super(context);
            this.callBackTow = dialogInpuerCallBackTow;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_file_name_input);
            EditText editText = findViewById(R.id.ed_data);
            findViewById(R.id.bt_comfir).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    callBackTow.result(editText.getText().toString());
                    dismiss();
                }
            });
        }
    }


    public class TestPanelAdapter extends PanelAdapter {
        private List<List<String>> data;

        public TestPanelAdapter(List<List<String>> data) {
            this.data = data;
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return data.get(0).size();
        }

        @Override
        public int getItemViewType(int row, int column) {
            return super.getItemViewType(row, column);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int row, int column) {
            String title = data.get(row).get(column);
            TitleViewHolder titleViewHolder = (TitleViewHolder) holder;
            titleViewHolder.titleTextView.setText(title);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new TestPanelAdapter.TitleViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tabel_detials, parent, false));
        }

        private class TitleViewHolder extends RecyclerView.ViewHolder {
            public TextView titleTextView;

            public TitleViewHolder(View view) {
                super(view);
                this.titleTextView = (TextView) view.findViewById(R.id.tv_type_detials);
            }
        }


    }


    public class DialogSHowDetilas extends Dialog {
        JSONArray jsonArray;
        public DialogSHowDetilas(@NonNull Context context,JSONArray jsonArray) {
            super(context);
            this.jsonArray = jsonArray;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_show_fuck_reuslt);
            TextView tv_show = findViewById(R.id.tv_show);

            String showText = "";

            for (int cc = 0;cc<jsonArray.size();cc++){
                JSONObject cData = jsonArray.getJSONObject(cc);
                if (!cData.getString("result").equals("")){
                    showText = showText+ cData.getString("type")+"环,"+cData.getString("result") +"发"+"\n";
                }
            }
            tv_show.setText(showText);
        }

        //        tv_show
    }

}
