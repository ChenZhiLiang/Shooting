package com.tianfan.shooting.view;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tianfan.shooting.R;
import com.tianfan.shooting.admin.mvp.presenter.TaskTeamPresenter;
import com.tianfan.shooting.bean.TaskPersonBean;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.utills.GlideEngine;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * @Name：Shooting
 * @Description：修改队员弹窗
 * @Author：Chen
 * @Date：2020/4/11 14:16
 * 修改人：Chen
 * 修改时间：2020/4/11 14:16
 */
public class EditTaskPersonDialog extends Dialog implements View.OnClickListener {

    private TextView tv_title;
    private ImageView img_delete;
    private EditText ed_name;
    private EditText ed_id_number;
    private EditText ed_work_unit;
    private EditText ed_user_type;
    private EditText ed_group;
    private EditText ed_position;
    private RoundedImageView iv_user_icon;
    private TextView tv_takepic_user_icon;
    private TextView id_cancle;
    private TextView comfir;
    private TextView tv_status;
    private Button btn_status;
    private Activity mActivity;

    private String picUri; //头像
    private TaskTeamPresenter mTaskTeamPresenter;
    private TaskPersonBean mTaskPersonBean;

    public EditTaskPersonDialog(@NonNull Activity activity, TaskPersonBean mTaskPersonBean, TaskTeamPresenter mTaskTeamPresenter) {
        super(activity, R.style.alert_dialog);
        this.mActivity = activity;
        this.mTaskPersonBean = mTaskPersonBean;
        this.mTaskTeamPresenter = mTaskTeamPresenter;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_task_person);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        img_delete = findViewById(R.id.img_delete);
        ed_name = findViewById(R.id.ed_name);
        ed_id_number = findViewById(R.id.ed_id_number);
        ed_work_unit = findViewById(R.id.ed_work_unit);
        ed_user_type = findViewById(R.id.ed_user_type);
        ed_group = findViewById(R.id.ed_group);
        ed_position = findViewById(R.id.ed_position);
        iv_user_icon = findViewById(R.id.iv_user_icon);
        tv_takepic_user_icon = findViewById(R.id.tv_takepic_user_icon);
        id_cancle = findViewById(R.id.id_cancle);
        comfir = findViewById(R.id.comfir);
        tv_status = findViewById(R.id.tv_status);
        btn_status = findViewById(R.id.btn_status);

        tv_title.setText("修改队员");
        img_delete.setVisibility(View.VISIBLE);
        ed_name.setText(mTaskPersonBean.getPerson_name());
        ed_id_number.setText(mTaskPersonBean.getPerson_idno());
        ed_work_unit.setText(mTaskPersonBean.getPerson_orga());
        ed_user_type.setText(mTaskPersonBean.getPerson_role());
        ed_group.setText(String.valueOf(mTaskPersonBean.getPerson_row()));
        ed_position.setText(String.valueOf(mTaskPersonBean.getPerson_col()));
        if (mTaskPersonBean.getPerson_status().equals("1")){
            tv_status.setText("(正常)");
            btn_status.setText("缺勤");
        }else if (mTaskPersonBean.getPerson_status().equals("0")){
            tv_status.setText("(缺勤)");
            btn_status.setText("报道");
        }
        Glide.with(mActivity).load(ApiUrl.HOST_HEAD + mTaskPersonBean.getPerson_head()).error(R.drawable.user_icon).into(iv_user_icon);

        tv_takepic_user_icon.setOnClickListener(this);
        id_cancle.setOnClickListener(this);
        comfir.setOnClickListener(this);
        img_delete.setOnClickListener(this);
        btn_status.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_takepic_user_icon:
                PictureSelector.create(mActivity)
                        .openGallery(PictureMimeType.ofImage())
                        .maxSelectNum(1)
                        .loadImageEngine(GlideEngine.createGlideEngine())
                        .forResult(new OnResultCallbackListener() {
                            @Override
                            public void onResult(List<LocalMedia> result) {
                                if (result.size() > 0) {
                                    LocalMedia media = result.get(0);
                                    picUri = media.getPath();
                                    File file = new File(picUri);
                                    if (file.exists()) {
                                        mTaskTeamPresenter.uploadTaskPersonHead(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(), file);

                                    }
                                    RequestOptions options = new RequestOptions().error(R.drawable.user_icon);//图片圆角为30
                                    Glide.with(mActivity).load(Uri.fromFile(file)).apply(options).into(iv_user_icon);
                                }
                            }

                            @Override
                            public void onCancel() {
                            }
                        });
                break;
            case R.id.comfir:
                if (TextUtils.isEmpty(ed_name.getText().toString())) {
                    Toast.makeText(mActivity, "请输入名字", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ed_id_number.getText().toString())) {
                    Toast.makeText(mActivity, "请输入身份证号", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ed_work_unit.getText().toString())) {
                    Toast.makeText(mActivity, "请输入工作单位", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ed_user_type.getText().toString())) {
                    Toast.makeText(mActivity, "请输入角色", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ed_group.getText().toString())) {
                    Toast.makeText(mActivity, "请输入分组", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(ed_position.getText().toString())) {
                    Toast.makeText(mActivity, "请输入靶位", Toast.LENGTH_SHORT).show();
                } else {
                    //判断分组和靶位是否改变  没有改变就直接修改队员 有改变就得先去查询该分组靶位是否有人
                    if (mTaskPersonBean.getPerson_col() == Integer.parseInt(ed_position.getText().toString())
                            && mTaskPersonBean.getPerson_row() == Integer.parseInt(ed_group.getText().toString())) {
                        mTaskTeamPresenter.editTaskPerson(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(),
                                ed_id_number.getText().toString(), ed_name.getText().toString()
                                , ed_work_unit.getText().toString(), ed_user_type.getText().toString(),
                                ed_group.getText().toString(), ed_position.getText().toString());
                    } else {
                        mTaskTeamPresenter.queryTaskPerson(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(), ed_group.getText().toString(), ed_position.getText().toString(), new ResultCallback() {
                            @Override
                            public void onSuccess(Object result) {
                                JSONObject jsonObject = JSONObject.parseObject(result.toString());
                                int code = jsonObject.getIntValue("code");
                                if (code == 1) {
                                    String datas = jsonObject.getString("datas");
                                    List<TaskPersonBean> mDatas = JSONArray.parseArray(datas, TaskPersonBean.class);
                                    if (mDatas.size() > 0) {

                                        SweetAlertDialogTools.ShowDialog(getContext(), "该分组靶位已经有人，是否要互换位置？", new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                sweetAlertDialog.dismiss();
                                                mTaskTeamPresenter.changeTaskPersonRowcol(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(), ed_group.getText().toString(), ed_position.getText().toString(), new ResultCallback() {
                                                    @Override
                                                    public void onSuccess(Object result) {
                                                        JSONObject jsonObject = JSONObject.parseObject(result.toString());
                                                        int code = jsonObject.getIntValue("code");
                                                        if (code == 1) {
                                                            mTaskTeamPresenter.editTaskPerson(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(),
                                                                    ed_id_number.getText().toString(), ed_name.getText().toString()
                                                                    , ed_work_unit.getText().toString(), ed_user_type.getText().toString(),
                                                                    ed_group.getText().toString(), ed_position.getText().toString());
                                                        } else {
                                                            Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Object result) {
                                                        Toast.makeText(mActivity, result.toString(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });                                            }
                                        });

                                    } else {
                                        mTaskTeamPresenter.editTaskPerson(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(),
                                                ed_id_number.getText().toString(), ed_name.getText().toString()
                                                , ed_work_unit.getText().toString(), ed_user_type.getText().toString(),
                                                ed_group.getText().toString(), ed_position.getText().toString());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Object result) {
                                Toast.makeText(mActivity, result.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }

                break;
            case R.id.id_cancle:
                dismiss();
                break;
            case R.id.img_delete:

                SweetAlertDialogTools.ShowDialog(mActivity, "确定删除该队员吗？", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismiss();
                        mTaskTeamPresenter.removeTaskPerson(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id());
                    }
                });

                break;
            case R.id.btn_status:
                if (mTaskPersonBean.getPerson_status().equals("0")){
                    mTaskTeamPresenter.setTaskPersonStatus(mTaskPersonBean.getTask_id(),mTaskPersonBean.getPerson_id(),"1",mTaskPersonBean.getPerson_row(),mTaskPersonBean.getPerson_col());
                    mTaskPersonBean.setPerson_status("1");
                    tv_status.setText("(正常)");
                    btn_status.setText("缺勤");
                }else if (mTaskPersonBean.getPerson_status().equals("1")){
                    mTaskTeamPresenter.setTaskPersonStatus(mTaskPersonBean.getTask_id(),mTaskPersonBean.getPerson_id(),"0",mTaskPersonBean.getPerson_row(),mTaskPersonBean.getPerson_col());
                    mTaskPersonBean.setPerson_status("0");
                    tv_status.setText("(缺勤)");
                    btn_status.setText("报道");
                }
                break;
        }
    }

}
