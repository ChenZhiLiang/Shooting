package com.tianfan.shooting.admin.newnettools;

import android.annotation.SuppressLint;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.admin.taskdata.FraDuiyuan;
import com.tianfan.shooting.utills.NewSPTools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import retrofit2.Retrofit;

import static com.alibaba.fastjson.util.IOUtils.close;

/**
 * CreateBy：lxf
 * CreateTime： 2020-03-16 22:39
 */
public class NewNetTools {
    @SuppressLint("CheckResult")
    public static void getTastList(NetCallBack netCallBack){
//         Observable.
//        new Observable()
        Observable.create(new ObservableOnSubscribe<JSONObject>() {
            @Override
            public void subscribe(ObservableEmitter<JSONObject> emitter) throws Exception {
                String url = "http://120.79.192.60:8181/anb_shot/task/findTaskInfo.action";
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).get().build();
                Call call = client.newCall(request);
                //异步调用并设置回调函数
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("任务列表回调onFailure","------"+ JSONObject.toJSONString(e));
//                        netCallBack.netFail( JSONObject.toJSONString(e));
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result","fail");
                        jsonObject.put("msg",JSONObject.toJSONString(e));
                        emitter.onNext(jsonObject);
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject rsp = new JSONObject();
                        final String responseStr = response.body().string();
                        if (response.code() == 200) {//请求和获取数据成功
                            JSONObject jsonObject = JSONObject.parseObject(responseStr);
                            if (jsonObject.getJSONArray("datas")!=null){
                                if (jsonObject.getJSONArray("datas").size()>0){
                                    rsp.put("result","ok");
                                    rsp.put("data",jsonObject.getJSONArray("datas").toJSONString());
                                }else{
                                    rsp.put("result","fail");
                                    rsp.put("msg","暂无数据");
                                }
                            }
                            Log.e("请求和获取数据成功","------"+ responseStr);

                        } else {
                            JSONObject jsonObject = JSONObject.parseObject(responseStr);
                            rsp.put("result","fail");
                            rsp.put("msg", jsonObject.getString("message"));
                        }
                        emitter.onNext(rsp);
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(JSONObject jsonObject) throws Exception {
                        if (jsonObject.getString("result").equals("ok")){
                            netCallBack.netOk(jsonObject.getString("data"));
                        }else{
                            netCallBack.netFail(jsonObject.getString("msg"));
                        }
                    }

                });

    }

    @SuppressLint("CheckResult")
    public static void dobaseGet(String baseURL, String actionURL, String prameter, NetCallBack netCallBack){
        Observable.create(new ObservableOnSubscribe<JSONObject>() {
            @Override
            public void subscribe(ObservableEmitter<JSONObject> emitter) throws Exception {
                String url =baseURL+actionURL+prameter;
                Log.e("请求URL--->>>",url+"\n");
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).get().build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result","fail");
                        jsonObject.put("msg",JSONObject.toJSONString(e));
                        emitter.onNext(jsonObject);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject rsp = new JSONObject();
                        final String responseStr = response.body().string();
                        Log.e("服务器回调---",responseStr+"\n");
                        if (response.code() == 200) {//请求和获取数据成功
                            Log.e("","");
                            rsp.put("result","ok");
                            rsp.put("data",responseStr);
                        } else {
                            JSONObject jsonObject = JSONObject.parseObject(responseStr);
                            rsp.put("result","fail");
                            rsp.put("msg", jsonObject.getString("message"));
                        }
                        emitter.onNext(rsp);
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Consumer<JSONObject>() {
            @Override
            public void accept(JSONObject jsonObject) throws Exception {
                if (jsonObject.getString("result").equals("ok")){
                    netCallBack.netOk(jsonObject.getString("data"));
                }else{
                    netCallBack.netFail(jsonObject.getString("msg"));
                }
            }
        });

    }


    @SuppressLint("CheckResult")
    public static void dobasePost(String baseURL, String actionURL, HashMap<String,String> map, NetCallBack netCallBack){
        Observable.create(new ObservableOnSubscribe<JSONObject>() {
            @Override
            public void subscribe(ObservableEmitter<JSONObject> emitter) throws Exception {
                String url =baseURL+actionURL;
                Log.e("Post请求URL--->>>",url+"\n");
                Log.e("Post请求入参--->>>",JSONObject.toJSONString(map)+"\n");
                FormBody.Builder builder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    builder.add(entry.getKey(),entry.getValue());
                }
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url(url).post(builder.build()).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result","fail");
                        jsonObject.put("msg",JSONObject.toJSONString(e));
                        emitter.onNext(jsonObject);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        JSONObject rsp = new JSONObject();
                        final String responseStr = response.body().string();
                        Log.e("Post服务器回调---",responseStr+"\n");
                        if (response.code() == 200) {//请求和获取数据成功
                            Log.e("","");
                            JSONObject rspData = JSONObject.parseObject(responseStr);

                            if (rspData.getString("code").equals("1")){
                                rsp.put("result","ok");
                                rsp.put("data",responseStr);
                            }else{
                                rsp.put("result","fail");
                                rsp.put("msg",rspData.getString("message"));
                            }
                        } else {
                            JSONObject jsonObject = JSONObject.parseObject(responseStr);
                            rsp.put("result","fail");
                            rsp.put("msg", jsonObject.getString("message"));
                        }
                        emitter.onNext(rsp);
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(JSONObject jsonObject) throws Exception {
                        if (jsonObject.getString("result").equals("ok")){
                            netCallBack.netOk(jsonObject.getString("data"));
                        }else{
                            netCallBack.netFail(jsonObject.getString("msg"));
                        }
                    }
                });

    }



    @SuppressLint("CheckResult")
    public static void doBaseUpFile(String url, File file, HashMap<String,String> map, NetCallBack netCallBack){
        Observable.create(new ObservableOnSubscribe<JSONObject>() {
            @Override
            public void subscribe(ObservableEmitter<JSONObject> emitter) throws Exception {
                Log.e("通用文件上传----","入参-----"+map);
                Log.e("通用文件上传----","文件名字-----"+file.getName());
                Log.e("通用文件上传----","文件路径-----"+file.getParent());
                Log.e("通用文件上传----","图片大小-----"+file.getTotalSpace());
                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder .setType(MultipartBody.FORM);
                builder.addFormDataPart("file",file.getName(),RequestBody.create(MediaType.parse("multipart/form-data"), file));
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    builder.addFormDataPart(entry.getKey(),entry.getValue());
                }
                RequestBody  requestBody = builder.build();
                Request request = new Request.Builder()
                        .header("Authorization", "Client-ID " + UUID.randomUUID())
                        .post(requestBody)
                        .url(url)
                        .build();
                new OkHttpClient().newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("通用文件上传回调----",""+e.getMessage());
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result","fail");
                        jsonObject.put("msg",JSONObject.toJSONString(e));
                        emitter.onNext(jsonObject);
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        final String responseStr = response.body().string();
                        Log.e("通用文件上传回调----onResponse","--------"+responseStr);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("result","ok");
                        jsonObject.put("data",responseStr);
                        emitter.onNext(jsonObject);
                    }
                });
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<JSONObject>() {
                    @Override
                    public void accept(JSONObject jsonObject) throws Exception {
                        if (jsonObject.getString("result").equals("ok")){
                            netCallBack.netOk(jsonObject.getString("data"));
                        }else{
                            netCallBack.netFail(jsonObject.getString("msg"));
                        }
                    }
                });

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

    public interface FileCVCB {
        void doCallBck();
    }



    private static final MediaType FROM_DATA = MediaType.parse("multipart/form-data");
    public static String BaseURL = "http://120.79.192.60:8181/anb_shot/";
    public static String addTask = "task/addTaskInfo.action";
    public static String editTask = "task/editTaskInfo.action";
    public static String deleteTask = "task/removeTaskInfo.action";
    public static String getDYList = "task/findTaskPerson.action";
    public static String addUser = "task/addTaskPerson.action";
    public static String upExcelForUser = "task/importTaskPersonGroup.action";
    public static String upUserIcon = "task/uploadTaskPersonHead.action";
    public static String deleUser = "task/removeTaskPerson.action";
    public static String userIconAdd = "http://120.79.192.60:8181/shot/task_person_head/";
    public static String edUserInfo = "task/editTaskPerson.action";
    //队列管理
    public static String dlGetList = "task/findTaskPerson.action";
    public static String dlCreate = "task/createTaskPersonRowcol.action";

    public static String changeTaskPersonRowcol="task/changeTaskPersonRowcol.action";



//    removeTaskPerson


//    【队员管理-查询】
//http://120.79.192.60:8181/anb_shot/task/findTaskPerson.action?task_id=task_20200316221424504475&task_person_type=1


    public static String TestURL = "http://192.168.88.136:8971/ShootingService/";
    public static String TestAction = "TestOKHttp/laowang";
    public static String TestFileUp = "Task/uploadTest";


    public interface NetCallBack{
        void netOk(String data);
        void netFail(String msg);
    }
}
