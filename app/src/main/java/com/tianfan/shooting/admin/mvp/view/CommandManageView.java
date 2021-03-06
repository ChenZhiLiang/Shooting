package com.tianfan.shooting.admin.mvp.view;

import com.tianfan.shooting.base.BaseView;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/4/25 23:07
 * 修改人：Chen
 * 修改时间：2020/4/25 23:07
 */
public interface CommandManageView extends BaseView {

    void FindTaskInfoResult(Object result);
    void EditTaskInfoResult(Object result);

    void ChangeTaskRoundsResult(Object result,int task_rounds_status);

    void FindTaskPersonScoreResult(Object result);


    void ChangeGroupResult(Object result);
}
