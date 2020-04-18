package com.tianfan.shooting.admin.mvp.view;

import com.tianfan.shooting.base.BaseView;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/9 22:35
 * 修改人：Chen
 * 修改时间：2020/4/9 22:35
 */
public interface TaskView extends BaseView {


    void FindTaskInfoResult(Object result);

    void AddTaskInfoResult(Object result);

    void EditTaskInfoResult(Object result);

    void RemoveTaskInfoResult(Object result);

}
