package com.tianfan.shooting.test;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.tianfan.shooting.BuildConfig;
import com.tianfan.shooting.R;

import org.easydarwin.video.Client;
import org.easydarwin.video.EasyPlayerClient;

import ua.polohalo.zoomabletextureview.ZoomableTextureView;

/**
 * CreateBy：lxf
 * CreateTime： 2020-02-23 19:50
 */
public class EasyRtspTest extends AppCompatActivity {
    private EasyPlayerClient client;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_easy_test);
        ZoomableTextureView zoomableTextureView = findViewById(R.id.sview);
        client = new EasyPlayerClient(this, BuildConfig.RTSP_KEY, zoomableTextureView, null, null);

        client.start("rtsp://admin:Abc1234567@192.168.88.176", Client.TRANSTYPE_UDP, Client.TRANSTYPE_UDP,
                        Client.EASY_SDK_VIDEO_FRAME_FLAG
                                | Client.EASY_SDK_AUDIO_FRAME_FLAG, "", "", null);
    }
}
