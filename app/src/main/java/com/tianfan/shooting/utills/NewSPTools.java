package com.tianfan.shooting.utills;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author liangxingfu
 * @time 2019/2/25 9:58 AM
 * @describe 轻量级数据工具类
 */
public class NewSPTools {
    Context mcontext;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

   public static NewSPTools newSPTools;

    public NewSPTools(Context context) {
        this.mcontext = context;
        sharedPreferences = context.getSharedPreferences("shoot_config", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (newSPTools==null){
            newSPTools = this;
        }
    }

    public static NewSPTools getNSP(){
        return newSPTools;
    }

    public void saveIP(String id) {
        editor.putString("ip", id);
        editor.commit();
    }

    public String getIp() {
        Log.e("取出的IP地址","------" +sharedPreferences.getString("ip", "192.168.88.136"));
        return  sharedPreferences.getString("ip", "192.168.88.136");
    }




}
