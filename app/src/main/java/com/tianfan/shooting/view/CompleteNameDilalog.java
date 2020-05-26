package com.tianfan.shooting.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.tianfan.shooting.bean.TaskRankBean;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.tools.SweetAlertDialogTools;
import com.tianfan.shooting.utills.GlideEngine;
import com.tianfan.shooting.view.sweetalert.SweetAlertDialog;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;

import static com.tianfan.shooting.admin.ui.activity.CompleteNameActivity.COMPLETE_NAME_TYPE;

/**
 * @Name：Shooting
 * @Description：完成点名弹窗
 * @Author：Chen
 * @Date：2020/5/14 22:30
 * 修改人：Chen
 * 修改时间：2020/5/14 22:30
 */
public class CompleteNameDilalog extends Dialog implements View.OnClickListener {

    private TextView tv_title;
    private ImageView img_delete;
    private EditText ed_name;
    private EditText ed_id_number;
    private EditText ed_work_unit;
    private EditText ed_user_type;
    private TextView tv_number_row;
    private ImageView image_add_row;
    private ImageView image_reduce_row;
    private ImageView image_add_position;
    private TextView tv_number_position;
    private ImageView image_reduce_position;
    private RoundedImageView iv_user_icon;
    private TextView tv_takepic_user_icon;

    private TextView id_cancle;
    private TextView comfir;
    private TextView tv_last;
    private TextView tv_next;
    private TextView tv_status;
    private Button btn_status;
    private Activity mActivity;

    private TaskTeamPresenter mTaskTeamPresenter;
    private TaskPersonBean mTaskPersonBean;
    private String task_rounds;
    private List<TaskRankBean> mTaskRankDatas;
    private int parentPostion;
    private int childPosition;
    private int type;
    private String picUri; //头像

    public CompleteNameDilalog(@NonNull Activity activity, List<TaskRankBean> mTaskRankDatas, int parentPostion, int childPosition,String task_rounds,int type, TaskTeamPresenter mTaskTeamPresenter) {
        super(activity, R.style.alert_dialog);
        this.mActivity = activity;
        this.mTaskRankDatas = mTaskRankDatas;
        this.parentPostion = parentPostion;
        this.childPosition = childPosition;
        this.mTaskTeamPresenter = mTaskTeamPresenter;
        this.task_rounds = task_rounds;
        this.type = type;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_complete_name);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();
        initData(parentPostion,childPosition);
    }
    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        img_delete = findViewById(R.id.img_delete);
        ed_name = findViewById(R.id.ed_name);
        ed_id_number = findViewById(R.id.ed_id_number);
        ed_work_unit = findViewById(R.id.ed_work_unit);
        ed_user_type = findViewById(R.id.ed_user_type);

        tv_number_row = findViewById(R.id.tv_number_row);
        image_add_row = findViewById(R.id.image_add_row);
        image_reduce_row = findViewById(R.id.image_reduce_row);

        image_add_position = findViewById(R.id.image_add_position);
        tv_number_position = findViewById(R.id.tv_number_position);
        image_reduce_position = findViewById(R.id.image_reduce_position);

        image_add_row.setOnClickListener(this);
        image_reduce_row.setOnClickListener(this);
        image_add_position.setOnClickListener(this);
        image_reduce_position.setOnClickListener(this);


        iv_user_icon = findViewById(R.id.iv_user_icon);
        tv_takepic_user_icon = findViewById(R.id.tv_takepic_user_icon);

        id_cancle = findViewById(R.id.id_cancle);
        comfir = findViewById(R.id.comfir);
        tv_last = findViewById(R.id.tv_last);
        tv_next = findViewById(R.id.tv_next);
        tv_status = findViewById(R.id.tv_status);
        btn_status = findViewById(R.id.btn_status);

        tv_title.setText("队员信息");
        img_delete.setVisibility(View.VISIBLE);

        tv_takepic_user_icon.setOnClickListener(this);

        id_cancle.setOnClickListener(this);
        comfir.setOnClickListener(this);
        tv_last.setOnClickListener(this);
        tv_next.setOnClickListener(this);
        img_delete.setOnClickListener(this);
        btn_status.setOnClickListener(this);
    }

    private void initData(int parentPostion,int childPosition){
        this.mTaskPersonBean = mTaskRankDatas.get(parentPostion).getDatas().get(childPosition);
        ed_name.setText(mTaskPersonBean.getPerson_name());
        ed_id_number.setText(mTaskPersonBean.getPerson_idno());
        ed_work_unit.setText(mTaskPersonBean.getPerson_orga());
        ed_user_type.setText(mTaskPersonBean.getPerson_role());
        tv_number_row.setText(String.valueOf(mTaskPersonBean.getPerson_row()));
        tv_number_position.setText(String.valueOf(mTaskPersonBean.getPerson_col()));
        if (TextUtils.isEmpty(mTaskPersonBean.getTask_id())){
            tv_status.setText("(无)");
            btn_status.setVisibility(View.GONE);
        }else {
            btn_status.setVisibility(View.VISIBLE);
            if (mTaskPersonBean.getPerson_status().equals("1")) {
                tv_status.setText("(正常)");
                if (type==COMPLETE_NAME_TYPE){
                    btn_status.setText("缺勤");
                }else {
                    btn_status.setText("退出打靶");
                }
            } else if (mTaskPersonBean.getPerson_status().equals("0")) {
                tv_status.setText("(缺勤)");
                btn_status.setText("报道");
            }else if (mTaskPersonBean.getPerson_status().equals("-1")){
                tv_status.setText("(已退出)");
                btn_status.setText("进入打靶");
            }
            Glide.with(mActivity).load(ApiUrl.HOST_HEAD + mTaskPersonBean.getPerson_head()).error(R.drawable.user_icon).into(iv_user_icon);
        }
    }
    private int row;
    private int position;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_takepic_user_icon:
                openPhoto();
                break;
            case R.id.image_add_row://添加分组
                row = Integer.parseInt(tv_number_row.getText().toString());
                row++;
                tv_number_row.setText(String.valueOf(row));
                break;
            case R.id.image_reduce_row://减少分组
                row = Integer.parseInt(tv_number_row.getText().toString());
                if (row==1){
                    Toast.makeText(mActivity,"分组数不能小于1",Toast.LENGTH_SHORT).show();
                }else {
                    row--;
                    tv_number_row.setText(String.valueOf(row));
                }
                break;
            case R.id.image_add_position://添加靶位
                position = Integer.parseInt(tv_number_position.getText().toString());
                position++;
                tv_number_position.setText(String.valueOf(position));
                break;
            case R.id.image_reduce_position://删除靶位
                position = Integer.parseInt(tv_number_position.getText().toString());
                if (position==1){
                    Toast.makeText(mActivity,"靶位不能小于1",Toast.LENGTH_SHORT).show();
                }else {
                    position--;
                    tv_number_position.setText(String.valueOf(position));
                }
                break;
            case R.id.comfir:
                onComfir();
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
                if (mTaskPersonBean.getPerson_status().equals("0")) {
                    mTaskTeamPresenter.setTaskPersonStatus(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(), "1", mTaskPersonBean.getPerson_row(), mTaskPersonBean.getPerson_col());
                    mTaskPersonBean.setPerson_status("1");
                    tv_status.setText("(正常)");
                    btn_status.setText("缺勤");

                } else if (mTaskPersonBean.getPerson_status().equals("1")) {
                    if (type==COMPLETE_NAME_TYPE){
                        mTaskTeamPresenter.setTaskPersonStatus(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(), "0", mTaskPersonBean.getPerson_row(), mTaskPersonBean.getPerson_col());
                        mTaskPersonBean.setPerson_status("0");
                        tv_status.setText("(缺勤)");
                        btn_status.setText("报道");
                    }else {
                        mTaskTeamPresenter.setTaskPersonStatus(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(), "-1", mTaskPersonBean.getPerson_row(), mTaskPersonBean.getPerson_col());
                        mTaskPersonBean.setPerson_status("-1");
                        tv_status.setText("(已退出)");
                        btn_status.setText("进入打靶");
                    }
                }else if (mTaskPersonBean.getPerson_status().equals("-1")){
                    mTaskTeamPresenter.setTaskPersonStatus(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(), "1", mTaskPersonBean.getPerson_row(), mTaskPersonBean.getPerson_col());
                    mTaskPersonBean.setPerson_status("-1");
                    tv_status.setText("(正常)");
                    btn_status.setText("退出打靶");
                }
                break;
            case R.id.tv_last://上一个
                if (childPosition==0){
                    if (parentPostion==0){
                        Toast.makeText(mActivity,"已经是第一个",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        parentPostion--;
                        childPosition = mTaskRankDatas.get(parentPostion).getDatas().size()-1;
                    }
                }else {
                    childPosition--;
                }
                initData(parentPostion,childPosition);
                break;
            case R.id.tv_next://下一个
                if (childPosition==mTaskRankDatas.get(parentPostion).getDatas().size()-1){
                    if (parentPostion==mTaskRankDatas.size()-1){
                        Toast.makeText(mActivity,"已经是最后一个",Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        //切换到下一组
                        parentPostion++;
                        childPosition = 0;
                    }
                }else {
                    childPosition++;
                }
                initData(parentPostion,childPosition);
                break;
        }
    }
    private void openPhoto() {
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
    }
    private void onComfir() {
        if (TextUtils.isEmpty(ed_name.getText().toString())) {
            Toast.makeText(mActivity, "请输入名字", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ed_id_number.getText().toString())) {
            Toast.makeText(mActivity, "请输入身份证号", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ed_work_unit.getText().toString())) {
            Toast.makeText(mActivity, "请输入工作单位", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(ed_user_type.getText().toString())) {
            Toast.makeText(mActivity, "请输入角色", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(tv_number_row.getText().toString())) {
            Toast.makeText(mActivity, "请输入分组", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(tv_number_position.getText().toString())) {
            Toast.makeText(mActivity, "请输入靶位", Toast.LENGTH_SHORT).show();
        } else {
            //判断分组和靶位是否改变  没有改变就直接修改队员 有改变就得先去查询该分组靶位是否有人
            if (mTaskPersonBean.getPerson_col() == Integer.parseInt(tv_number_position.getText().toString())
                    && mTaskPersonBean.getPerson_row() == Integer.parseInt(tv_number_row.getText().toString())) {
                mTaskTeamPresenter.editTaskPerson(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(),
                        ed_id_number.getText().toString(), ed_name.getText().toString()
                        , ed_work_unit.getText().toString(), ed_user_type.getText().toString(),
                        tv_number_row.getText().toString(), tv_number_position.getText().toString());
            } else {
                mTaskTeamPresenter.queryTaskPerson(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(), tv_number_row.getText().toString(), tv_number_position.getText().toString(), new ResultCallback() {
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
                                        mTaskTeamPresenter.exChangeTaskPersonRowcol(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(), tv_number_row.getText().toString(), tv_number_position.getText().toString(), task_rounds,new ResultCallback() {
                                            @Override
                                            public void onSuccess(Object result) {
                                                JSONObject jsonObject = JSONObject.parseObject(result.toString());
                                                int code = jsonObject.getIntValue("code");
                                                if (code == 1) {
                                                    mTaskTeamPresenter.editTaskPerson(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(),
                                                            ed_id_number.getText().toString(), ed_name.getText().toString()
                                                            , ed_work_unit.getText().toString(), ed_user_type.getText().toString(),
                                                            tv_number_row.getText().toString(), tv_number_position.getText().toString());
                                                } else {
                                                    Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                                                }
                                            }

                                            @Override
                                            public void onFailure(Object result) {
                                                Toast.makeText(mActivity, result.toString(), Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });

                            } else {
                                mTaskTeamPresenter.editTaskPerson(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(),
                                        ed_id_number.getText().toString(), ed_name.getText().toString()
                                        , ed_work_unit.getText().toString(), ed_user_type.getText().toString(),
                                        tv_number_row.getText().toString(), tv_number_position.getText().toString());
                            }
                        } else if (code == 2) {//查询靶位无数据
                            mTaskTeamPresenter.editTaskPerson(mTaskPersonBean.getTask_id(), mTaskPersonBean.getPerson_id(),
                                    ed_id_number.getText().toString(), ed_name.getText().toString()
                                    , ed_work_unit.getText().toString(), ed_user_type.getText().toString(),
                                    tv_number_row.getText().toString(), tv_number_position.getText().toString());
                        } else {
                            Toast.makeText(mActivity, result.toString(), Toast.LENGTH_SHORT).show();

                        }
                    }

                    @Override
                    public void onFailure(Object result) {
                        Toast.makeText(mActivity, result.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

}
