package com.tianfan.shooting.videotools;

import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * @program: Shooting
 * @description: 说明
 * @author: lxf
 * @create: 2019-11-21 00:51
 **/
public class VideoViewController {

    private ProgressDialog loadingDialog; // loading
    private VideoView videoView;
    private Context context;

    public VideoViewController(Context contxt, VideoView mVideoView) {

        videoView = mVideoView;
        context = contxt;
    }

    public void start(String videoUrl) {

        loadingDialog = new ProgressDialog(context);
        loadingDialog.setMessage("Loading...");
        loadingDialog.show();
        // loadingDialog.setCancelable(false);

        final MediaController controll = new MediaController(context);
        controll.setMediaPlayer(videoView);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("************", "call back after media file registing.");
                loadingDialog.dismiss();
            }
        });
        videoView.setVideoPath(videoUrl);
        videoView.setMediaController(controll);
        videoView.requestFocus();
        videoView.start();
        controll.show();
    }
}
