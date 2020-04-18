package com.tianfan.shooting.admin.ui.fragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.fastjson.JSONArray;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.tianfan.shooting.R;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @program: Shooting
 * @description: 说明
 * @author: lxf
 * @create: 2019-12-22 16:59
 **/
public class ZhiHuiFrgment  extends Fragment {

    MediaPlayer player;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zhihui,container,false);
        ButterKnife.bind(this,view);
        fuckLisener(view);
        return view;
    }

    void fuckLisener(View view){
        view.findViewById(R.id.tv_qianjin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palyer(R.raw.xiangshejizhendiqianjn);
            }
        });
        view.findViewById(R.id.tv_baoxian_open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palyer(R.raw.dakaibaoxian);
            }
        });
        view.findViewById(R.id.tv_wodao).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palyer(R.raw.wodao);
            }
        });
        view.findViewById(R.id.tv_baoxian_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palyer(R.raw.guanbaoxian);
            }
        });
        view.findViewById(R.id.tv_zhuangdan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palyer(R.raw.zhuangdan);
            }
        });
        view.findViewById(R.id.tv_shangtang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palyer(R.raw.shangtang);
            }
        });
        view.findViewById(R.id.tv_sheji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palyer(R.raw.kaishisheji);
            }
        });
        view.findViewById(R.id.tv_yanqiang).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palyer(R.raw.yanqiang);
            }
        });
        view.findViewById(R.id.xiedan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                palyer(R.raw.xiedanjia);
            }
        });
    }

    void palyer(int from){
        if (player!=null){
            player.reset();
            player.release();
        }
        player = MediaPlayer.create(getContext(),from);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player!=null){
            player.release();
        }

    }
}
