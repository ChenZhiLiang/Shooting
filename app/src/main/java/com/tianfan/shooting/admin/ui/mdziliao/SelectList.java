package com.tianfan.shooting.admin.ui.mdziliao;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;
import com.tianfan.shooting.net.GetResult;
import com.tianfan.shooting.net.RequestTools;
import com.tianfan.shooting.net.RetrofitUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * CreateBy：lxf
 * CreateTime： 2020-03-03 11:52
 */
public class SelectList  extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.listview)
    RecyclerView recyclerView;
    List<JSONObject> list = new ArrayList<>();
    DuiYuanListAdapter duiYuanListAdapter;
    @BindView(R.id.tv_select_all)
    TextView tv_select_all;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_select_for_liedui);
        ButterKnife.bind(this);
        tv_select_all.setText("全选");
        findViewById(R.id.tv_select_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (allSelectFlag){
                    allSelectFlag = false;
                    doALlSelect();
                }else{
                    allSelectFlag = true;
                    doALlUnSelect();
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        duiYuanListAdapter = new DuiYuanListAdapter(R.layout.item_duiuyuan_content, list, new CBoxCB() {
            @Override
            public void result(int position, String reuslt) {
                list.get(position).put("hasSelect",reuslt);
            }
        });
        recyclerView.setAdapter(duiYuanListAdapter);
        getData();
    }

    @OnClick({R.id.tv_return,R.id.tv_save})
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_save){
            List<JSONObject> cbList = new ArrayList<>();
            for (JSONObject obj:list){
                if (obj.getString("hasSelect").equals("1")){
                    cbList.add(obj);
                }
            }
            if (cbList.size()>0){
                Log.e("当前选择的对象",""+cbList);
                SelectDLEven dlEven = new SelectDLEven();
                dlEven.setList(cbList);
                EventBus.getDefault().post(dlEven);
                finish();
            }else{
                Toast.makeText(getApplicationContext(),"至少选择一个对象",Toast.LENGTH_SHORT).show();
            }
        }else{
            finish();
        }
    }

    void doALlSelect(){
        for (int i=0;i<list.size();i++){
            list.get(i).put("hasSelect","1");
        }
        duiYuanListAdapter.notifyDataSetChanged();
    }
    void doALlUnSelect(){
        for (int i=0;i<list.size();i++){
            list.get(i).put("hasSelect","");
        }
        duiYuanListAdapter.notifyDataSetChanged();
    }

    boolean allSelectFlag = false;

    void getData(){
        RequestTools.doAction().getData(RetrofitUtils.getService().getUserList(new HashMap<>()), new GetResult<String>() {
            @Override
            public void fail(String msg) {
                Log.e("网络请求错误","------"+msg);
            }
            @Override
            public void ok(String result) {
                Log.e("回调","------"+result);
                JSONArray jsonArray = JSONArray.parseArray(result);
                for (int i=0;i<jsonArray.size();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    jsonObject1.put("hasSelect","");
                    list.add(jsonObject1);
                }
                duiYuanListAdapter.notifyDataSetChanged();
            }
        });
    }


    interface CBoxCB{
        void result(int position,String reuslt);
    }

    class DuiYuanListAdapter extends BaseQuickAdapter<JSONObject, BaseViewHolder> {
        CBoxCB callB;
        public DuiYuanListAdapter(int layoutResId, @Nullable List<JSONObject> data,CBoxCB cBoxCB) {
            super(layoutResId, data);
            this.callB = cBoxCB;
        }
        @Override
        protected void convert(BaseViewHolder helper, JSONObject item) {
            helper.setIsRecyclable(false);
            helper.setText(R.id.tv_item_duiyuan_number,(helper.getAdapterPosition()+1)+"");
            helper.setText(R.id.tv_item_duiyuan_name,item.getString("user_name"));
            helper.setText(R.id.tv_item_duiyuan_user_type,item.getString("group_type"));
            helper.setText(R.id.tv_item_duiyuan_group,item.getString("group"));
            helper.setText(R.id.tv_item_duiyuan_work_unit,item.getString("work_unit"));
            helper.setText(R.id.tv_item_duiyuan_id_number,item.getString("user_id_card"));
            helper.setText(R.id.tv_item_duiyuan_postion,item.getString("position"));
            CheckBox checkBox = helper.itemView.findViewById(R.id.cbox);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()){
                        callB.result(helper.getAdapterPosition(),"1");
                    }else{
                        callB.result(helper.getAdapterPosition(),"");
                    }
                }
            });

            if (item.getString("hasSelect").equals("1")){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
        }
    }
}
