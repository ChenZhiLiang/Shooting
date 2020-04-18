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

    void findTaskEquipResult(Object result);

    void createTaskEquip(Object result);

    void changeTaskEquipCount(Object result);

}
