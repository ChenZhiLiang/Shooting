package com.tianfan.shooting.admin.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.evenmessage.UserSelectEvent;
import com.tianfan.shooting.admin.mvp.view.ViewSignActivity;
import com.tianfan.shooting.admin.ui.evendata.UserSelectCalBack;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.touch.OnItemMoveListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.leefeng.promptlibrary.PromptDialog;

/**
 * @program: Shooting
 * @description:
 * @author: lxf
 * @create: 2019-11-11 15:51
 **/
public class SelectUserActivity extends AppCompatActivity implements View.OnClickListener, ViewSignActivity {


    @BindView(R.id.list_shoot_group)
    RecyclerView recyclerViewGroup;
    @BindView(R.id.list_shoot_user)
    SwipeRecyclerView recyclerViewUser;


    Activity activity = this;
    JSONArray jsonArrayForUser = new JSONArray();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        initListData();
        startActivity(new Intent(this, UserDataList.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
    AdapForUser adapForUser;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void selectResult(UserSelectEvent userSelectEvent){
        Log.e("回调的数据","----》》》"+userSelectEvent.getData());
        JSONArray jsonArray = JSONArray.parseArray(userSelectEvent.getData().getString("task_data"));
        jsonArrayForUser.clear();
        for (int i=0;i<jsonArray.size();i++){
            Log.e("取余的结果---"+i,"-------》"+i%10);
            jsonArrayForUser.add(jsonArray.getJSONObject(i));
        }
        Log.e("test----","---->"+jsonArrayForUser);

        JSONArray arrayGruo = new JSONArray();

        for (int i=0;i<50;i++){
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", "第"+(i+1)+"组");
            jsonObject.put("grupNumer", (i+1)+"");
            arrayGruo.add(jsonObject);

        }

        recyclerViewGroup.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewGroup.setAdapter(new AdapForGroup(arrayGruo,getApplicationContext()));


         adapForUser = new AdapForUser(jsonArrayForUser, getApplicationContext(), new QueQinCallBack() {
            @Override
            public void QueCallBack(int position) {
                jsonArrayForUser.remove(position);
                adapForUser.notifyDataSetChanged();
            }
        });
        recyclerViewUser.setLongPressDragEnabled(true);
        recyclerViewUser.setAdapter(adapForUser);
        OnItemMoveListener mItemMoveListener = new OnItemMoveListener() {
            @Override
            public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
                int fromPosition = srcHolder.getAdapterPosition();
                int toPosition = targetHolder.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(jsonArrayForUser, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(jsonArrayForUser, i, i - 1);
                    }
                }
                adapForUser.notifyItemMoved(fromPosition, toPosition);
                return true;
            }
            @Override
            public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
                int position = srcHolder.getAdapterPosition();
                jsonArrayForUser.remove(position);
                adapForUser.notifyItemRemoved(position);
            }
        };
        recyclerViewUser.setOnItemMoveListener(mItemMoveListener);
    }

    public interface QueQinCallBack{
        void QueCallBack(int position);
    }

    class AdapForUser extends RecyclerView.Adapter  {
        JSONArray jsonArray;
        Context context;
        QueQinCallBack queQinCallBack;
        public AdapForUser(JSONArray jsonArray, Context context,QueQinCallBack callBack) {
            this.jsonArray = jsonArray;
            this.context = context;
            this.queQinCallBack = callBack;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_chil,parent,false);
            VHListChil vhListChil = new VHListChil(view);
            return vhListChil;
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof VHListChil) {
                holder.setIsRecyclable(false);
                ((VHListChil) holder).title.setText(jsonArray.getJSONObject(position).getString("name"));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.e("click","--->"+holder.getAdapterPosition());
                        DialogShowUser dialogShowUser = new DialogShowUser(activity, jsonArrayForUser.getJSONObject(holder.getAdapterPosition()), new UserSignI() {
                            @Override
                            public void result(boolean result) {
                                if (result){
                                }else{
                                    //如果是缺勤的，放到列表最后，他的后一个补上
                                    ((VHListChil) holder).title.setBackgroundColor(Color.parseColor("#ef2243"));
                                    queQinCallBack.QueCallBack(holder.getAdapterPosition());
                                }
                            }
                        });
                        dialogShowUser.show();
                    }
                });
            }
        }
        @Override
        public int getItemCount() {
            return jsonArray.size();
        }
    }
    public interface UserSignI{
        public void result(boolean result);
    }
    class DialogShowUser extends Dialog{
        JSONObject jsonObject;
        UserSignI userSignI;
        public DialogShowUser(@NonNull Context context,JSONObject jsonObject,UserSignI userSignI) {
            super(context);
            this.jsonObject = jsonObject;
            this.userSignI = userSignI;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.item_dianlog_show_user);
            TextView name = findViewById(R.id.tv_name);
            name.setText("姓名:"+jsonObject.getString("name"));
            TextView tv_post = findViewById(R.id.tv_post);
            tv_post.setText("职务："+jsonObject.getString("post"));
            TextView tv_idNumber = findViewById(R.id.tv_idNumber);
            tv_idNumber.setText("身份证号："+jsonObject.getString("idNumber"));
            TextView tv_tell = findViewById(R.id.tv_tell);
            tv_tell.setText("联系电话："+jsonObject.getString("tell"));
            TextView tv_mtservice = findViewById(R.id.tv_mtservice);
            tv_mtservice.setText("服兵役情况："+jsonObject.getString("militaryService"));
            TextView tv_work_unit = findViewById(R.id.tv_work_unit);
            tv_work_unit.setText("工作单位："+jsonObject.getString("WorkUnit"));
            TextView tv_remark = findViewById(R.id.tv_remark);
            tv_remark.setText("备注："+jsonObject.getString("remakr"));

            TextView tv_is_come = findViewById(R.id.tv_is_come);
            TextView tv_not_come = findViewById(R.id.tv_not_come);

            tv_is_come.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userSignI.result(true);
                    dismiss();
                }
            });


            tv_not_come.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    userSignI.result(false);
                    dismiss();
                }
            });


        }
    }

    class AdapForGroup extends RecyclerView.Adapter  {
        JSONArray jsonArray;
        Context context;
        public AdapForGroup(JSONArray jsonArray, Context context) {
            this.jsonArray = jsonArray;
            this.context = context;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group,parent,false);
            VHListChil vhListChil = new VHListChil(view);
            return vhListChil;
        }
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof VHListChil) {
                ((VHListChil) holder).title.setText(jsonArray.getJSONObject(position).getString("name"));
                ((VHListChil) holder).number.setText(jsonArray.getJSONObject(position).getString("grupNumer"));
            }
        }
        @Override
        public int getItemCount() {
            return jsonArray.size();
        }
    }
    class VHListChil extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView number;

        public VHListChil(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_user_name);
            number = itemView.findViewById(R.id.gr_id);
        }
    }
    private void  initListData() {
        recyclerViewUser.setLayoutManager(new GridLayoutManager(getApplicationContext(),10));

    }
    @OnClick({R.id.tv_comfir})
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_comfir){
            int tmp = 0;
            int gruop =1;
            for (int i =0;i<jsonArrayForUser.size();i++){
                int shootPision = i%10;
                tmp = tmp+1;

                jsonArrayForUser.getJSONObject(i).put("gruop",""+gruop);
                jsonArrayForUser.getJSONObject(i).put("position",""+(shootPision+1));
                if (tmp==10){
                    tmp =0;
                    gruop+=1;
                }
                Log.e("-----","--->"+jsonArrayForUser.getJSONObject(i));
                Log.e("--------------------","------------------>");
            }
            UserSelectCalBack userSelectCalBack = new UserSelectCalBack();
            userSelectCalBack.setData(jsonArrayForUser);
            EventBus.getDefault().post(userSelectCalBack);
            finish();
        }
        //点击完成创建
    }

    @Override
    public void itemClic() {

    }

}
