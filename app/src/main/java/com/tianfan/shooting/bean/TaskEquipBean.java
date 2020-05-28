package com.tianfan.shooting.bean;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/12 21:45
 * 修改人：Chen
 * 修改时间：2020/4/12 21:45
 */
public class TaskEquipBean {

    /**
     * task_id : task_20200316221424504475
     * equip_model_type_id : equip_model_type_20200321222550078761
     * equip_model_item_id : equip_model_item_20200322004706039202
     * equip_type : 器材类型11
     * equip_name : 器材名称11
     * equip_unit : 器材单位11
     * equip_count : 111
     * equip_status : 1
     */

    private String task_id;
    private String equip_model_type_id;
    private String equip_model_item_id;
    private String equip_type;
    private String equip_name;
    private String equip_unit;
    private int equip_count;
    private int equip_count_take;
    private String equip_status;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getEquip_model_type_id() {
        return equip_model_type_id;
    }

    public void setEquip_model_type_id(String equip_model_type_id) {
        this.equip_model_type_id = equip_model_type_id;
    }

    public String getEquip_model_item_id() {
        return equip_model_item_id;
    }

    public void setEquip_model_item_id(String equip_model_item_id) {
        this.equip_model_item_id = equip_model_item_id;
    }

    public String getEquip_type() {
        return equip_type;
    }

    public void setEquip_type(String equip_type) {
        this.equip_type = equip_type;
    }

    public String getEquip_name() {
        return equip_name;
    }

    public void setEquip_name(String equip_name) {
        this.equip_name = equip_name;
    }

    public String getEquip_unit() {
        return equip_unit;
    }

    public void setEquip_unit(String equip_unit) {
        this.equip_unit = equip_unit;
    }

    public int getEquip_count() {
        return equip_count;
    }

    public void setEquip_count(int equip_count) {
        this.equip_count = equip_count;
    }

    public String getEquip_status() {
        return equip_status;
    }

    public void setEquip_status(String equip_status) {
        this.equip_status = equip_status;
    }

    public int getEquip_count_take() {
        return equip_count_take;
    }

    public void setEquip_count_take(int equip_count_take) {
        this.equip_count_take = equip_count_take;
    }
}
