package com.tianfan.shooting.bean;

/**
 * @Name：Shooting
 * @Description：任务列表信息
 * @Author：Chen
 * @Date：2020/4/9 23:07
 * 修改人：Chen
 * 修改时间：2020/4/9 23:07
 */
public class TaskInfoBean {

    /**
     * task_id : task_20200407222557125376
     * task_name : 啦啦啦啦
     * task_site : 啦啦啦
     * task_date : 2020-04-07
     * task_row_count : 3
     * task_row_persons : 10
     * task_target_type : 1
     * task_inuser : admin
     * task_intime : 1586298357000
     * task_status : 0
     */

    private String task_id;
    private String task_name;
    private String task_site;
    private String task_date;
    private int task_row_count;
    private int task_row_persons;
    private String task_target_type;
    private String task_inuser;
    private long task_intime;
    private String task_status;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_site() {
        return task_site;
    }

    public void setTask_site(String task_site) {
        this.task_site = task_site;
    }

    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }

    public int getTask_row_count() {
        return task_row_count;
    }

    public void setTask_row_count(int task_row_count) {
        this.task_row_count = task_row_count;
    }

    public int getTask_row_persons() {
        return task_row_persons;
    }

    public void setTask_row_persons(int task_row_persons) {
        this.task_row_persons = task_row_persons;
    }

    public String getTask_target_type() {
        return task_target_type;
    }

    public void setTask_target_type(String task_target_type) {
        this.task_target_type = task_target_type;
    }

    public String getTask_inuser() {
        return task_inuser;
    }

    public void setTask_inuser(String task_inuser) {
        this.task_inuser = task_inuser;
    }

    public long getTask_intime() {
        return task_intime;
    }

    public void setTask_intime(long task_intime) {
        this.task_intime = task_intime;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }
}
