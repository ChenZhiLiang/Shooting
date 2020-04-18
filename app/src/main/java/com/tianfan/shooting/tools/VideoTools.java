package com.tianfan.shooting.tools;

import android.os.Looper;

import java.lang.ref.WeakReference;

import tv.danmaku.ijk.media.player.IjkLibLoader;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

import static tv.danmaku.ijk.media.player.IjkMediaPlayer.loadLibrariesOnce;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-09 09:13
 * @Description 视频播放工具
 */
public class VideoTools {
//    private void initPlayer(IjkLibLoader libLoader) {
//        loadLibrariesOnce(libLoader);
//        initNativeOnce();
//
//        Looper looper;
//        if ((looper = Looper.myLooper()) != null) {
//            mEventHandler = new EventHandler(this, looper);
//        } else if ((looper = Looper.getMainLooper()) != null) {
//            mEventHandler = new EventHandler(this, looper);
//        } else {
//            mEventHandler = null;
//        }
//
//        /*
//         * Native setup requires a weak reference to our object. It's easier to
//         * create it here than in C++.
//         */
//        native_setup(new WeakReference<IjkMediaPlayer>(this));
//    }
}
