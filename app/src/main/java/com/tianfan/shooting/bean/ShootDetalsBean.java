package com.tianfan.shooting.bean;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-18 08:40
 * @Description 射击详情类
 */
@SmartTable(name = "射击详情")
public class ShootDetalsBean {
    public String getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(String tableNumber) {
        this.tableNumber = tableNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAllHasShootIn() {
        return allHasShootIn;
    }

    public void setAllHasShootIn(String allHasShootIn) {
        this.allHasShootIn = allHasShootIn;
    }

    public String getAllShootNull() {
        return allShootNull;
    }

    public void setAllShootNull(String allShootNull) {
        this.allShootNull = allShootNull;
    }

    public String getShootConut_1() {
        return ShootConut_1;
    }

    public void setShootConut_1(String shootConut_1) {
        ShootConut_1 = shootConut_1;
    }

    public String getFirstShoot_1D_etals() {
        return firstShoot_1D_etals;
    }

    public void setFirstShoot_1D_etals(String firstShoot_1D_etals) {
        this.firstShoot_1D_etals = firstShoot_1D_etals;
    }

    public String getShoot_2_Conut() {
        return Shoot_2_Conut;
    }

    public void setShoot_2_Conut(String shoot_2_Conut) {
        Shoot_2_Conut = shoot_2_Conut;
    }

    public String getSecondShoot_2_Details() {
        return secondShoot_2_Details;
    }

    public void setSecondShoot_2_Details(String secondShoot_2_Details) {
        this.secondShoot_2_Details = secondShoot_2_Details;
    }

    @SmartColumn(id = 0, name = "序号", autoMerge = true)
    String tableNumber;
    @SmartColumn(id = 1, name = "姓名")
    String name;
    @SmartColumn(id = 2, name = "总击中")
    String allHasShootIn;
    @SmartColumn(id = 3, name = "总环")
    String allShootNull;
    @SmartColumn(id = 4, name = "第一轮击中/发")
    String ShootConut_1;
    @SmartColumn(id = 5, name = "第一轮击中详情")
    String firstShoot_1D_etals;
    @SmartColumn(id = 6, name = "第二轮击中/发")
    String Shoot_2_Conut;
    @SmartColumn(id = 7, name = "第二轮击中详情")
    String secondShoot_2_Details;
//    @SmartColumn(id = 8, name = "第三轮击中/发")
//    String Shoot_3_Conut;
//    @SmartColumn(id = 9, name = "第三轮/击中详情")
//    String secondShoot_3_Details;
//    @SmartColumn(id = 1, name = "序号")
//    String Shoot_4_Conut;
//    @SmartColumn(id = 1, name = "序号")
//    String secondShoot_4_Details;
//    @SmartColumn(id = 1, name = "序号")
//    String Shoot_5_Conut;
//    @SmartColumn(id = 1, name = "序号")
//    String secondShoot_5_Details;
//    @SmartColumn(id = 1, name = "序号")
//    String Shoot_6_Conut;
//    @SmartColumn(id = 1, name = "序号")
//    String secondShoot_6_Details;
//    @SmartColumn(id = 1, name = "序号")
//    String Shoot_7_Conut;
//    String secondShoot_7_Details;
//    String Shoot_8_Conut;
//    String secondShoot_8_Details;
//    String Shoot_9_Conut;
//    String secondShoot_9_Details;
//    String Shoot_10_Conut;
//    String secondShoot_10_Details;

}
