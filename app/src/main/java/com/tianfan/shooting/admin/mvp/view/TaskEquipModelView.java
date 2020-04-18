package com.tianfan.shooting.admin.mvp.view;

import com.tianfan.shooting.base.BaseView;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/12 23:04
 * 修改人：Chen
 * 修改时间：2020/4/12 23:04
 */
public interface TaskEquipModelView extends BaseView {
    void findEquipModelTypeResult(Object result);
    void findEquipModelItemResult(Object result);
    void EditEquipModelItemResult(Object result);
}
