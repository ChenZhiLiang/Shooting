package com.tianfan.shooting.utills;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.UUID;

import static com.alibaba.fastjson.util.IOUtils.close;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/11 15:28
 * 修改人：Chen
 * 修改时间：2020/4/11 15:28
 */
public class Utils {

    public static String listToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i < list.size() - 1) {
                    sb.append(list.get(i) + ",");
                } else {
                    sb.append(list.get(i));
                }
            }
        }
        return sb.toString();
    }

    public  static File nioTransferCopy(File source) {
        FileChannel in = null;
        FileChannel out = null;
        FileInputStream inStream = null;
        FileOutputStream outStream = null;
        File temFuck = null;
        try {
            inStream = new FileInputStream(source);
            String newFileName = source.getName().substring(source.getName().lastIndexOf("."), source.getName().length());
            String fuckFIle = source.getParent()+"/" + UUID.randomUUID() + newFileName;
            temFuck = new File(fuckFIle);
            outStream = new FileOutputStream(temFuck);
            in = inStream.getChannel();
            out = outStream.getChannel();
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(inStream);
            close(in);
            close(outStream);
            close(out);
        }
        return temFuck;
    }
}
