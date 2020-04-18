package com.tianfan.shooting.tools;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * @program: ViewSaveDemo
 * @description: 说明
 * @author: lxf
 * @create: 2020-01-07 08:59
 **/
public class PicSaveTools {
    /**
     * View转换为Bitmap图片
     *
     * @param view
     * @return Bitmap
     */
    private static Bitmap convertViewToBitmap(View view) {
        //创建Bitmap,最后一个参数代表图片的质量.
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        //创建Canvas，并传入Bitmap.
        Canvas canvas = new Canvas(bitmap);
        //View把内容绘制到canvas上，同时保存在bitmap.
        view.draw(canvas);
        return bitmap;
    }

    public static File savePhotoToSDCard(View view) {
        String path = "/sdcard/testPicSave";
        String photoName = UUID.randomUUID().toString().replaceAll("-", "");
        Bitmap photoBitmap = convertViewToBitmap(view);
        File photoFile=null;
        if (checkSDCardAvailable()) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }

             photoFile = new File(path, photoName + ".png");
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(photoFile);
                if (photoBitmap != null) {
                    if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                        fileOutputStream.flush();
                    }
                }
            } catch (FileNotFoundException e) {
                photoFile.delete();
                e.printStackTrace();
            } catch (IOException e) {
                photoFile.delete();
                e.printStackTrace();
            } finally {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return photoFile;
    }

    private static boolean checkSDCardAvailable() {
        return android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    }
}
