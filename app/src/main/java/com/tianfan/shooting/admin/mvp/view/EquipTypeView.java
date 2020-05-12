package com.tianfan.shooting.admin.mvp.view;

import com.tianfan.shooting.base.BaseView;

/**
 * @Name：Shooting
 * @Description：器材类型
 * @Author：Chen
 * @Date：2020/4/22 0:27
 * 修改人：Chen
 * 修改时间：2020/4/22 0:27
 */
public interface EquipTypeView extends BaseView {
    void findEquipTypeResult(Object result);

    void addEquipTypeResult(Object result);

    void editEquipTypeResult(Object result);

    void removeEquipTypeResult(Object result);
}
