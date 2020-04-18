package com.tianfan.shooting.admin.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.tianfan.shooting.R;

import com.tianfan.shooting.admin.ui.activity.CheckDetials;
import com.tianfan.shooting.admin.ui.activity.NewAddEqActivity;
import com.tianfan.shooting.net.BaseService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-12 18:14
 * @Description 功能描述
 */
public class CheckFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.list_eq)
    RecyclerView recyclerView;
    List<JSONObject> list = new ArrayList<>();
    CheckListAdapter checkListAdapter;
    @Nullable
    @Override

    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eq_check,container,false);
        ButterKnife.bind(this,view);
        checkListAdapter = new CheckListAdapter(R.layout.item_check_list,list);
        checkListAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), CheckDetials.class);
                Bundle bundle = new Bundle();
                bundle.putString("data",list.get(position)+"");
                intent.putExtra("data",bundle);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(checkListAdapter);
        return view;
    }

    @OnClick({R.id.bt_eq_add,R.id.bt_eq_editor})
    @Override
    public void onClick(View v) {
        //增加设备
        if (v.getId()==R.id.bt_eq_add){
            startActivity(new Intent(getContext(), NewAddEqActivity.class));
            //检查设备
        }else if (v.getId()==R.id.bt_eq_editor){

        }
    }

    class CheckListAdapter extends BaseQuickAdapter<JSONObject, BaseViewHolder> {

        public CheckListAdapter(int layoutResId, @Nullable List<JSONObject> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, JSONObject item) {
            JSONObject jsonObject = item.getJSONObject("data");
            helper.setText(R.id.name,jsonObject.getString("name"));
            helper.setText(R.id.check_create_time,item.getString("create_time"));

        }
    }

    //回到页面，刷新数据
    @Override
    public void onResume() {
        super.onResume();
        //
        Log.e("提交回调---获取所有的装备数据","-----");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseService.BaseURL)
                .build();
        BaseService service = retrofit.create(BaseService.class);

        Map map = new HashMap();


        Call<ResponseBody> gitHubBeanCall = service.getAllEquipment(map);
        gitHubBeanCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body()!=null){
                        String result = response.body().string();
//                    Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
                        JSONArray jsonArray = JSONArray.parseArray(result);
                        list.clear();
                        for (Object object:jsonArray){
                            list.add((JSONObject) object);
                        }
                        checkListAdapter.notifyDataSetChanged();
                        Log.e("装备数据","-----"+result);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
            }
        });
    }
}
