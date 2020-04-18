package com.tianfan.shooting.admin.taskdata;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.newnettools.NewNetTools;
import com.tianfan.shooting.utills.GlideEngine;


import java.io.File;
import java.util.List;

/**
 * CreateBy：lxf
 * CreateTime： 2020-03-21 19:15
 */
public class DialogEDUser  extends Dialog {
    public ImageView imageView;
    private String picURI = "";
    public DialogEDUser(Context context, Activity activity, JSONObject shos, FraDuiyuan.AddUserCB cb) {
        super(context);
        setContentView(R.layout.dialog_ed_user);
        getWindow().setGravity(Gravity.TOP);
        findViewById(R.id.id_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        imageView = findViewById(R.id.iv_user_icon);
        findViewById(R.id.iv_user_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(activity)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .loadImageEngine(GlideEngine.createGlideEngine())
//                            / 请参考Demo GlideEngine.java
                        .forResult(new OnResultCallbackListener() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                if (result.size()>0){
                                    LocalMedia media = result.get(0);
                                    picURI = media.getPath();
                                    File file = new File(picURI);
                                    RequestOptions options = new RequestOptions().error(R.drawable.user_icon).bitmapTransform(new RoundedCorners(30));//图片圆角为30
                                    Glide.with(getContext()).load(Uri.fromFile(file)).override(120,120).apply(options).into(imageView);
                                }
                            }
                            @Override
                            public void onCancel() {
                            }
                        })
                ;
            }
        });
        findViewById(R.id.tv_takepic_user_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PictureSelector.create(activity)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .forResult(new OnResultCallbackListener() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                if (result.size()>0){
                                    LocalMedia media = result.get(0);
                                    picURI = media.getPath();
                                    File file = new File(picURI);
                                    RequestOptions options = new RequestOptions().error(R.drawable.user_icon).bitmapTransform(new RoundedCorners(30));//图片圆角为30
                                    Glide.with(getContext()).load(Uri.fromFile(file)).override(120,120).apply(options).into(imageView);
                                }                                }
                            @Override
                            public void onCancel() {
                            }
                        })
                ;
            }
        });
        ((EditText) findViewById(R.id.ed_name)).setText(shos.getString("person_name"));
        ((EditText) findViewById(R.id.ed_id_number)).setText(shos.getString("person_idno"));
        ((EditText) findViewById(R.id.ed_work_unit)).setText(shos.getString("person_orga"));
        ((EditText) findViewById(R.id.ed_user_type)).setText(shos.getString("person_role"));
        ((EditText) findViewById(R.id.ed_user_group)).setText(shos.getString("person_row"));
        ((EditText) findViewById(R.id.ed_user_positon)).setText(shos.getString("person_col"));
        String Pid= shos.getString("person_id");
        Log.e("编辑DIalog","----Pid-----"+Pid);
        if (shos.getString("person_head")!=null){
            String picUri = NewNetTools.userIconAdd+shos.getString("person_head");
            Log.e("当前头像地址","》》》》》-----"+picUri);
            RequestOptions options = new RequestOptions().error(R.drawable.user_icon).bitmapTransform(new RoundedCorners(30));//图片圆角为30
            Glide.with(getContext()).load(picUri).override(120,120).apply(options).into(imageView);
        }

        findViewById(R.id.comfir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((EditText) findViewById(R.id.ed_name)).getText().toString().equals("")) {
                    Toast.makeText(getContext(), "请输入队员名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (((EditText) findViewById(R.id.ed_id_number)).getText().toString().equals("")) {
                    Toast.makeText(getContext(), "请输入身份证号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (((EditText) findViewById(R.id.ed_work_unit)).getText().toString().equals("请选择")) {
                    Toast.makeText(getContext(), "请输入工作单位", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (((EditText) findViewById(R.id.ed_user_type)).getText().toString().equals("")) {
                    Toast.makeText(getContext(), "请输入角色类型", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (((EditText) findViewById(R.id.ed_user_group)).getText().toString().equals("")) {
                    Toast.makeText(getContext(), "请输入队员所在分组", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (((EditText) findViewById(R.id.ed_user_positon)).getText().toString().equals("")) {
                    Toast.makeText(getContext(), "请输入队员所在靶位", Toast.LENGTH_SHORT).show();
                    return;
                }
                com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
                jsonObject.put("ed_name", ((EditText) findViewById(R.id.ed_name)).getText().toString());
                jsonObject.put("ed_id_number", ((EditText) findViewById(R.id.ed_id_number)).getText().toString());
                jsonObject.put("ed_work_unit", ((EditText) findViewById(R.id.ed_work_unit)).getText().toString());
                jsonObject.put("ed_user_type", ((EditText) findViewById(R.id.ed_user_type)).getText().toString());
                jsonObject.put("ed_user_group", ((EditText) findViewById(R.id.ed_user_group)).getText().toString());
                jsonObject.put("ed_user_positon", ((EditText) findViewById(R.id.ed_user_positon)).getText().toString());
                jsonObject.put("ed_user_pic", picURI);
                jsonObject.put("pseron_id", Pid);
                Log.e("编辑DIalog","----即将回调的数据-----"+jsonObject);

                cb.cbData(jsonObject);
                dismiss();
            }
        });


    }
}
