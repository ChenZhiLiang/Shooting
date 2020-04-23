package com.tianfan.shooting.admin.mvp.view;

import com.tianfan.shooting.base.BaseView;

/**
 * @Name：Shooting
 * @Description：器材管理
 * @Author：Chen
 * @Date：2020/4/12 22:16
 * 修改人：Chen
 * 修改时间：2020/4/12 22:16
 */
public interface TaskEquipView extends BaseView {

    //查询任务信息
    void findTaskInfoResult(Object result);
    //查找器材模板
    void findEquipModelTypeResult(Object result);
    //查找任务器材
    void findTaskEquipResult(Object result);

    //生成任务器材
    void createTaskEquip(Object result);

    //添加任务器材
    void addTaskEquipResult(Object result);
    // 改变任务器材数量
    void changeTaskEquipCountResult(Object result);

    //修改任务（任务器材流程）
    void editTaskInfoResult(Object result);
}
