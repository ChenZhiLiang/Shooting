package com.tianfan.shooting;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tianfan.shooting.admin.ui.activity.AdminActivity;
import com.tianfan.shooting.base.IPSetUpActivity;
import com.tianfan.shooting.scorer.ui.activity.ScorerActivity;
import com.tianfan.shooting.tools.RXPermissionTools;
import com.tianfan.shooting.utills.NewSPTools;
import com.tianfan.shooting.view.CheckTargetPositionDialog;
import com.tianfan.shooting.warrior.WarriorActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @program: Shooting
 * @description: 说明
 * @author: lxf
 * @create: 2019-12-19 16:40
 **/
public class Login extends AppCompatActivity {
    public static String nowIP = "192.168.88.136";
    AppCompatActivity activity = this;
    String p = "0";
    @BindView(R.id.tv_user_type)
    TextView tv_user_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        findViewById(R.id.lv3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogTypeSel dialogTypeSel = new DialogTypeSel(activity);
                dialogTypeSel.show();
            }
        });
        dologin();
       new  RXPermissionTools(this).applyPermissionNew(new RXPermissionTools.APPlyCBack() {
           @Override
           public void result(boolean result) {
               if (!result){
                   Toast.makeText(getApplicationContext(),"必要权限尚未授权、请授权后使用",Toast.LENGTH_SHORT).show();
                   finish();
               }
           }
       });
    }

    void dologin(){
        nowIP= new NewSPTools(getApplicationContext()).getIp();

        findViewById(R.id.tv_admin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), AdminActivity.class));
                return;
            }
        });
        findViewById(R.id.tv_sheji).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), WarriorActivity.class));
                return;
            }
        });
        findViewById(R.id.tv_jifen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckTargetPositionDialog dialog = new CheckTargetPositionDialog(Login.this);
                dialog.show();
//                startActivity(new Intent(getApplicationContext(), ScorerActivity.class));
                return;
            }
        });


    }



    class DialogTypeSel extends Dialog {
        public DialogTypeSel(@NonNull Context context) {
            super(context);
        }
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.dialog_user_type);
            findViewById(R.id.tv_type_1).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p = "0";
                    tv_user_type.setText("管理员");
                    dismiss();
                }
            });
            findViewById(R.id.tv_type_2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p = "1";
                    tv_user_type.setText("记分员");
                    dismiss();
                }
            });
            findViewById(R.id.tv_type_3).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    p = "2";
                    tv_user_type.setText("射击员");
                    dismiss();
                }
            });

            findViewById(R.id.tv_type_4).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    跳转到设置IP的位置
                    startActivity(new Intent(getApplicationContext(), IPSetUpActivity.class));
                    dismiss();
                }
            });
        }
    }
}
