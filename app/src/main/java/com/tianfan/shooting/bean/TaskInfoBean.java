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
     * task_id : task_20200511002034889100
     * task_name : 任务名称1
     * task_site : 任务地点1
     * task_date : 2020-05-11
     * task_row_count : 20
     * task_row_persons : 4
     * task_target_type : 1
     * task_inuser : user_0001
     * task_intime : 1589156337000
     * task_equips : 0
     * task_rounds : 3
     * task_rounds_status : 1
     * task_status : 1
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
    private String task_equips;
    private String task_rounds;
    private String task_rounds_status;
    private String task_rows;
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

    public String getTask_equips() {
        return task_equips;
    }

    public void setTask_equips(String task_equips) {
        this.task_equips = task_equips;
    }

    public String getTask_rounds() {
        return task_rounds;
    }

    public void setTask_rounds(String task_rounds) {
        this.task_rounds = task_rounds;
    }

    public String getTask_rounds_status() {
        return task_rounds_status;
    }

    public void setTask_rounds_status(String task_rounds_status) {
        this.task_rounds_status = task_rounds_status;
    }

    public String getTask_status() {
        return task_status;
    }

    public void setTask_status(String task_status) {
        this.task_status = task_status;
    }

    public String getTask_rows() {
        return task_rows;
    }

    public void setTask_rows(String task_rows) {
        this.task_rows = task_rows;
    }
}
