package com.tianfan.shooting.admin.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.tianfan.shooting.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @program: Shooting
 * @description: 任务待办
 * @author: lxf
 * @create: 2019-11-14 16:16
 **/
public class FrgForUnDoList  extends Fragment implements View.OnClickListener {
//    list_undolist_view


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_undolist,container,false);
        ButterKnife.bind(this,view);
        return view;
    }
    @OnClick({R.id.tv_right})
    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.tv_right){
            Toast.makeText(getContext(),"创建新的任务",Toast.LENGTH_SHORT).show();
        }
    }
}
