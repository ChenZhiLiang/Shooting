package com.tianfan.shooting.utills;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-08-27 16:18
 * @Description String 转txt
 */
public class LogStringToTxt {
    public static void writeTxt(String str, String fileName) {
        //新建文件夹
        String folderName = "Shooting";
        File sdCardDir = new File(Environment.getExternalStoragePublicDirectory(
                "Log"), folderName);
        if (!sdCardDir.exists()) {
            if (!sdCardDir.mkdirs()) {
                try {
                    sdCardDir.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            //新建文件
            File saveFile = new File(sdCardDir, fileName+"Log.txt");
            saveFile.createNewFile();
            final FileOutputStream outStream = new FileOutputStream(saveFile);
            outStream.write(str.getBytes());
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeZhiJian(String str) {

//        //新建文件夹
//        String folderName = "ACJF";
//        File sdCardDir = new File(Environment.getExternalStoragePublicDirectory(
//                "BOCOLog"), folderName);
//        if (!sdCardDir.exists()) {
//            if (!sdCardDir.mkdirs()) {
//                try {
//                    sdCardDir.createNewFile();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        try {
//            //新建文件
//            File saveFile = new File(sdCardDir, "质检操作2"+".txt");
//            if (!saveFile.exists()){
//                saveFile.createNewFile();
//            }
//
//
//            StringBuilder sb = new StringBuilder();
//            if (Environment.getExternalStorageState().equals(
//                    Environment.MEDIA_MOUNTED)) {
//                String path = Environment.getExternalStorageDirectory()
//                        .getAbsolutePath() + "/BOCOLog/ACJF/";
//                String name = "质检操作2.txt";
//                File dir = new File(path);
//                if (!dir.exists()) {
//                    dir.mkdirs();
//                }
//
//                BufferedReader reader = null;
//                try {
//                    reader = new BufferedReader(new InputStreamReader(
//                            new FileInputStream(path + name)));
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                String line = null;
//                try {
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    try {
//                        reader.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            str = str+sb.toString()+"\n";
//            final FileOutputStream outStream = new FileOutputStream(saveFile);
//            outStream.write(str.getBytes());
//            outStream.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
