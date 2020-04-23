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
    //查询器材模板
    void findEquipModelTypeResult(Object result);

    //新增器材模板
    void addEquipModelTypeResult(Object result);

    //删除器材模板
    void removeEquipModelTypeResult(Object result);

    //添加器材模板项目
    void addEquipModelItemResult(Object result);

    //删除器材模板项目
    void removeEquipModelItemResult(Object result);

    //查询器材模板项目
    void findEquipModelItemResult(Object result);

    //修改器材模板项目
    void EditEquipModelItemResult(Object result);
}
