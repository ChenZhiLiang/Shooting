package com.tianfan.shooting.bean;

import java.util.List;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/11 23:58
 * 修改人：Chen
 * 修改时间：2020/4/11 23:58
 */
public class TaskRankItemBean {

    /**
     * task_id : task_20200410010656232665
     * person_id : task_person_20200411160926226463
     * person_idno : 450100199901010001
     * person_name : 队员1
     * person_orga : 工作单位1
     * person_role : 角色1
     * person_head : null
     * person_row : 1
     * person_col : 1
     * person_score : []
     */

    private String task_id;
    private String person_id;
    private String person_idno;
    private String person_name;
    private String person_orga;
    private String person_role;
    private String person_head;
    private int person_row;
    private int person_col;
    private List<?> person_score;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getPerson_id() {
        return person_id;
    }

    public void setPerson_id(String person_id) {
        this.person_id = person_id;
    }

    public String getPerson_idno() {
        return person_idno;
    }

    public void setPerson_idno(String person_idno) {
        this.person_idno = person_idno;
    }

    public String getPerson_name() {
        return person_name;
    }

    public void setPerson_name(String person_name) {
        this.person_name = person_name;
    }

    public String getPerson_orga() {
        return person_orga;
    }

    public void setPerson_orga(String person_orga) {
        this.person_orga = person_orga;
    }

    public String getPerson_role() {
        return person_role;
    }

    public void setPerson_role(String person_role) {
        this.person_role = person_role;
    }

    public String getPerson_head() {
        return person_head;
    }

    public void setPerson_head(String person_head) {
        this.person_head = person_head;
    }

    public int getPerson_row() {
        return person_row;
    }

    public void setPerson_row(int person_row) {
        this.person_row = person_row;
    }

    public int getPerson_col() {
        return person_col;
    }

    public void setPerson_col(int person_col) {
        this.person_col = person_col;
    }

    public List<?> getPerson_score() {
        return person_score;
    }

    public void setPerson_score(List<?> person_score) {
        this.person_score = person_score;
    }
}
