package com.tianfan.shooting.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.sql.Savepoint;
import java.util.HashMap;
import java.util.Map;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-11 09:04
 * @Description 储存类
 */

public class NewSPTools {
    Context mcontext;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public NewSPTools() {

    }
    public void setContext(Context context){
        this.mcontext = context;
        sharedPreferences = context.getSharedPreferences("shooting",Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    private static class SingletonHolder{
        private final static NewSPTools instance=new NewSPTools();
    }
    public static NewSPTools getInstance(){
        return SingletonHolder.instance;
    }


//    //保存摄像头信息
//    public void setCamera(JSONObject jsonObject){
//
//        editor.putString("CameraData",jsonObject+"");
//        editor.commit();
//
//
//    }
//
//    //检查摄像头信息
//    public boolean CheckCamera(){
//
//       boolean CheckResult = false;
//        String result = sharedPreferences.getString("CameraData","");
//        if (!result.equals("")){
//            try {
//               CheckResult = true;
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return CheckResult;
//    }


    //保存用户信息
    public void setUserType(String result){

        editor.putString("UserType",result+"");
        editor.commit();

    }

    public  String checkUPFileDate(){
        String result = sharedPreferences.getString("UPFIle","");
        return result;
    }

    public  void saveUPFileData(String file){
        editor.putString("UPFIle",file);
        editor.commit();
    }

    //检查用户信息
    public String CheckUserType(){
        String result = sharedPreferences.getString("UserType","");
        return result;
    }

    //保存用户信息
    public void setShootingPosition(String result){

        editor.putString("ShootingPosition",result+"");
        editor.commit();

    }


    //检查用户信息
    public String getShootingPosition(){
        String result = sharedPreferences.getString("ShootingPosition","");
        return result;
    }

    //设置记分员靶位
    public  void saveScorerePosition(String result){
        editor.putString("ScorerPosition",result+"");
        editor.commit();
    }

    //清除记分员员靶位
    public  void eraseScorerePosition(){
        editor.putString("ScorerPosition","");
        editor.commit();
    }

    //获取记分员员靶位
    public  String getScorerePosition(){
        return sharedPreferences.getString("ScorerPosition","");
    }

    //查询记分员员靶位
    public  boolean checktScorerePosition(){
        boolean flag = true;
        if (sharedPreferences.getString("ScorerPosition","").equals("")){
            flag = false;
        }
        return flag;
    }


    public void saveNowShootData(String key,String times,String name,String data){
        editor.putString(""+key+times+name,data+"");
        Log.e("取出的数据1-----"+key+times+name+"-------",">>>>>>>>>>>>"+data);
        editor.commit();

    }

    public Map<String,String> getNowShootData(String key, String name){
        Map map = new HashMap();
        String key1 = key+"1"+name;
        String key2 = key+"2"+name;
        String key3 = key+"3"+name;

        Log.e("取出的数据2",">>>>>>>>key1>>>>"+key1+">>>>key2>>>"+key2+">>>>key3>>>"+key3);

        String tiems1 = sharedPreferences.getString(key1,"");
        String tiems2 = sharedPreferences.getString(key2,"");
        String tiems3 = sharedPreferences.getString(key3,"");
        map.put("times1",tiems1);
        map.put("times2",tiems2);
        map.put("times3",tiems3);
        Log.e("取出的数据2",">>>>>>>>>>>>"+JSONObject.toJSONString(map));
       return map;

    }


}
