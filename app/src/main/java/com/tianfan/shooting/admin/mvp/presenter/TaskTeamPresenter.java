package com.tianfan.shooting.admin.mvp.presenter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.admin.mvp.view.TaskTeamView;
import com.tianfan.shooting.base.BaseMode;
import com.tianfan.shooting.bean.TaskPersonBean;
import com.tianfan.shooting.network.api.ApiUrl;
import com.tianfan.shooting.network.okhttp.callback.ResultCallback;
import com.tianfan.shooting.network.okhttp.request.RequestParams;

import java.io.File;
import java.util.List;

/**
 * @Name：Shooting
 * @Description：任务队员管理
 * @Author：Chen
 * @Date：2020/4/11 11:19
 * 修改人：Chen
 * 修改时间：2020/4/11 11:19
 */
public class TaskTeamPresenter {

    private TaskTeamView mTaskTeamView;
    private BaseMode mBaseMode;
    boolean isHavePerson = false;

    public TaskTeamPresenter(TaskTeamView mTaskTeamView) {
        this.mTaskTeamView = mTaskTeamView;
        this.mBaseMode = new BaseMode();
    }

    /**
     * 查询靶位和分组是否有人
     *  @author
     *  @time
     *  @describe
     */
    public void queryTaskPerson(String task_id,String person_id,String person_row,String person_col,ResultCallback mResultCallback){
        String url = ApiUrl.TaskPersonApi.FindTaskPerson;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("person_row",String.valueOf(person_row));
        params.put("person_col",String.valueOf(person_col));
//        params.put("person_id",person_id);
        params.put("task_person_type",String.valueOf(2));
        mBaseMode.GetRequest(url, params, mResultCallback);
    }

    /**
     * 查找任务队员列表
     *  @author
     *  @time
     *  @describe
     * @param task_id 任务id
     * task_person_type 任务队员类型 1集合 2 队列
     */

    public void findTaskPerson(String task_id,String task_rounds,boolean isShow){

        if (isShow){
            mTaskTeamView.showProgress();
        }
        String url = ApiUrl.TaskPersonApi.FindTaskPerson;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        //task_person_type 任务队员类型 1集合 2 队列
        params.put("task_person_type",String.valueOf(2));
        params.put("task_rounds",task_rounds);

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskTeamView.FindTaskPersonResult(result);
                mTaskTeamView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mTaskTeamView.hideProgress();
                mTaskTeamView.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     * 添加任务队员
     *  @author
     *  @time
     *  @describe
     * @param task_id 任务id
     * @param person_idno 身份证号
     * @param person_name 姓名
     * @param person_orga 工作单位
     * @param person_role 角色
     * @param person_row 行数
     * @param person_col 列数
     * task_person_type 任务队员类型 1集合 2 队列
     */
    public void addTaskPerson(String task_id,String person_idno ,String person_name,String person_orga
            ,String person_role,String person_row,String person_col){

        mTaskTeamView.showProgress();
        String url = ApiUrl.TaskPersonApi.AddTaskPerson;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("person_idno",person_idno);
        params.put("person_name",person_name);
        params.put("person_orga",person_orga);
        params.put("person_role",person_role);
        params.put("person_row",person_row);
        params.put("person_col",person_col);
        //task_person_type 任务队员类型 1集合 2 队列
        params.put("task_person_type",String.valueOf(2));

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskTeamView.AddTaskPersonResult(result);
                mTaskTeamView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mTaskTeamView.hideProgress();
                mTaskTeamView.showLoadFailMsg(result.toString());
            }
        });
    }


    /**
     * 修改任务队员
     *  @author
     *  @time
     *  @describe
     * @param task_id 任务id
     * @param person_id 队员id
     * @param person_idno 身份证号
     * @param person_name 姓名
     * @param person_orga 工作单位
     * @param person_role 角色
     * @param person_row 行数
     * @param person_col 列数
     * task_person_type 任务队员类型 1集合 2 队列
     * */
    public void editTaskPerson(String task_id,String person_id,String person_idno ,String person_name,String person_orga
            ,String person_role,String person_row,String person_col){

        mTaskTeamView.showProgress();
        String url = ApiUrl.TaskPersonApi.EditTaskPerson;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("person_id",person_id);
        params.put("person_idno",person_idno);
        params.put("person_name",person_name);
        params.put("person_orga",person_orga);
        params.put("person_role",person_role);
        params.put("person_row",String.valueOf(person_row));
        params.put("person_col",String.valueOf(person_col));
        //task_person_type 任务队员类型 1集合 2 队列
        params.put("task_person_type",String.valueOf(2));

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskTeamView.EditTaskPersonResult(result);
                mTaskTeamView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mTaskTeamView.hideProgress();
                mTaskTeamView.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     * 删除队员
     *  @author
     *  @time
     *  @describe
     * @param task_id 任务id
     * @param person_id 队员id 支持同时删除多个队员person_id用英文逗号,分隔
     */
    public void removeTaskPerson(String task_id,String person_id){
        mTaskTeamView.showProgress();
        String url = ApiUrl.TaskPersonApi.RemoveTaskPerson;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("person_id",person_id);
        //task_person_type 任务队员类型 1集合 2 队列
        params.put("task_person_type",String.valueOf(2));
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskTeamView.RemoveTaskPersonResult(result);
                mTaskTeamView.hideProgress();

            }

            @Override
            public void onFailure(Object result) {
                mTaskTeamView.hideProgress();
                mTaskTeamView.showLoadFailMsg(result.toString());
            }
        });

    }

    /**
     * 上传头像
     *  @author
     *  @time
     *  @describe
     */
    public void uploadTaskPersonHead(String task_id, String person_id,File file){
        mTaskTeamView.showProgress();

        String url = ApiUrl.TaskPersonApi.UploadTaskPersonHead;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("person_id",person_id);
        params.put("task_person_type",String.valueOf(2));
        params.fileParams.put("file",file);
        mBaseMode.MultiPostRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskTeamView.UploadTaskPersonHeadResult(result);
                mTaskTeamView.hideProgress();

            }

            @Override
            public void onFailure(Object result) {
                mTaskTeamView.showLoadFailMsg(result.toString());
                mTaskTeamView.hideProgress();
            }
        });
    }

    /**
     * 导入EXCEL
     *  @author
     *  @time
     *  @describe
     */
    public void importTaskPersonGroup(String task_id,File file){
        mTaskTeamView.showProgress();

        String url = ApiUrl.TaskPersonApi.ImportTaskPerson;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("task_person_type",String.valueOf(2));
        params.fileParams.put("file",file);
        mBaseMode.MultiPostRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                if (file.exists()){
                    file.delete();
                }
                mTaskTeamView.ImportTaskPersonGroupResult(result);
                mTaskTeamView.hideProgress();

            }

            @Override
            public void onFailure(Object result) {
                mTaskTeamView.showLoadFailMsg(result.toString());
                mTaskTeamView.hideProgress();
            }
        });
    }

    /**
     * 队列查询
     *  @author
     *  @time
     *  @describe
     */
    public void recordTaskPersonScore(String task_id,String task_rounds,boolean isShow){
        if (isShow){
            mTaskTeamView.showProgress();
        }
        String url = ApiUrl.TaskPersonApi.FindTaskPersonRowcol;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("task_rounds",task_rounds);
        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {
                mTaskTeamView.FindTaskPersonResult(result);
                mTaskTeamView.hideProgress();
            }

            @Override
            public void onFailure(Object result) {
                mTaskTeamView.hideProgress();
                mTaskTeamView.showLoadFailMsg(result.toString());
            }
        });
    }

    /**
     *  调换组员与靶位
     *  @author
     *  @time
     *  @describe
     */
    public void exChangeTaskPersonRowcol(String task_id,String person_id,String person_row,String person_col,String task_rounds,ResultCallback mResultCallback){
        String url = ApiUrl.TaskPersonApi.ExChangeTaskPersonRowcol;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("person_id",person_id);
        params.put("person_row",person_row);
        params.put("person_col",person_col);
        params.put("task_rounds",task_rounds);
        mBaseMode.GetRequest(url, params, mResultCallback);
    }


    /**
     * 设置队员状态（缺勤、报道）
     *  @author
     *  @time
     *  @describe
     * @param person_status  0 缺勤 1报道
     */
    public void setTaskPersonStatus(String task_id,String person_id,String person_status,int person_row,int person_col){
        String url = ApiUrl.TaskPersonApi.EditTaskPerson;
        RequestParams params = new RequestParams();
        params.put("task_id",task_id);
        params.put("person_id",person_id);
        params.put("person_status",person_status);
        params.put("person_row",String.valueOf(person_row));
        params.put("person_col",String.valueOf(person_col));
        params.put("task_person_type",String.valueOf(2));

        mBaseMode.GetRequest(url, params, new ResultCallback() {
            @Override
            public void onSuccess(Object result) {

                mTaskTeamView.SetTaskPersonStatusResult(result);
            }

            @Override
            public void onFailure(Object result) {
                mTaskTeamView.showLoadFailMsg(result.toString());
            }
        });
    }
}
