package com.tianfan.shooting.view;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.listener.OnResultCallbackListener;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tianfan.shooting.R;
import com.tianfan.shooting.utills.GlideEngine;
import java.io.File;
import java.util.List;
import androidx.annotation.NonNull;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/11 12:11
 * 修改人：Chen
 * 修改时间：2020/4/11 12:11
 */
public class AddTaskPersonDialog extends Dialog implements View.OnClickListener {

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
    private Activity mActivity;

    private String picUri; //头像
    private ClickConfirInterface mClickConfirInterface;
    // task_person_type 任务队员类型 1集合 2 队列
    private int task_person_type = 1;
    private int person_row ;//行数
    private int person_col ;//列数
    /**
     * 集合调用
     *  @author
     *  @time
     *  @describe
     */
    public AddTaskPersonDialog(@NonNull Activity activity,ClickConfirInterface mClickConfirInterface) {
        super(activity, R.style.alert_dialog);
        this.mActivity = activity;
        this.mClickConfirInterface = mClickConfirInterface;
    }

    /**
     * 列队调用
     *  @author
     *  @time
     *  @describe
     */
    public AddTaskPersonDialog(@NonNull Activity activity, int task_person_type,int person_row, int person_col,ClickConfirInterface mClickConfirInterface) {
        super(activity, R.style.alert_dialog);
        this.mActivity = activity;
        this.mClickConfirInterface = mClickConfirInterface;
        this.task_person_type = task_person_type;
        this.person_col = person_col;
        this.person_row = person_row;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_task_person);
        getWindow().setGravity(Gravity.CENTER);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){
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
        tv_takepic_user_icon.setOnClickListener(this);
        id_cancle.setOnClickListener(this);
        comfir.setOnClickListener(this);

        if (task_person_type==2){
            ed_position.setText(String.valueOf(person_col));
            ed_group.setText(String.valueOf(person_row));
            ed_group.setEnabled(false);
            ed_position.setEnabled(false);
        }

        Glide.with(mActivity).load(R.drawable.user_icon).into(iv_user_icon);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
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
                if (TextUtils.isEmpty(ed_name.getText().toString())){
                    Toast.makeText(mActivity,"请输入名字",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(ed_id_number.getText().toString())){
                    Toast.makeText(mActivity,"请输入身份证号",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(ed_work_unit.getText().toString())){
                    Toast.makeText(mActivity,"请输入工作单位",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(ed_user_type.getText().toString())){
                    Toast.makeText(mActivity,"请输入角色",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(ed_group.getText().toString())){
                    Toast.makeText(mActivity,"请输入分组",Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(ed_position.getText().toString())){
                    Toast.makeText(mActivity,"请输入靶位",Toast.LENGTH_SHORT).show();
                }else {
                    mClickConfirInterface.onResult(ed_name.getText().toString(),ed_id_number.getText().toString()
                            ,ed_work_unit.getText().toString(),ed_user_type.getText().toString()
                            ,ed_group.getText().toString(),ed_position.getText().toString(),picUri);
                }

                break;
            case R.id.id_cancle:
                dismiss();
                break;
        }
    }

    public interface ClickConfirInterface{
        void onResult(String person_name,String person_idno ,String person_orga,String person_role,String person_row,String person_col,String picUri);
    }
}
