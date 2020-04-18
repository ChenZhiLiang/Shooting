package com.tianfan.shooting.base;

import android.os.Bundle;
import android.text.InputType;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tianfan.shooting.Login;
import com.tianfan.shooting.R;
import com.tianfan.shooting.utills.NewSPTools;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * CreateBy：lxf
 * CreateTime： 2020-03-12 15:20
 */
public class IPSetUpActivity extends AppCompatActivity {

    @BindView(R.id.ed_ip)
    EditText editText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_ip_setup);
        ButterKnife.bind(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        String digits = "0123456789.";
        editText.setKeyListener(DigitsKeyListener.getInstance(digits));
        findViewById(R.id.comfir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new NewSPTools(getApplicationContext()).saveIP(editText.getText().toString().trim());
                Login.nowIP = editText.getText().toString().trim();
                finish();
            }
        });

    }
}