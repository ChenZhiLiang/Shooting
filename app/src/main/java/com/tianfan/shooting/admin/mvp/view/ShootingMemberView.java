package com.tianfan.shooting.admin.mvp.view;

import com.tianfan.shooting.base.BaseView;

/**
 * @Name：Shooting
 * @Description：描述信息
 * @Author：Chen
 * @Date：2020/5/26 22:21
 * 修改人：Chen
 * 修改时间：2020/5/26 22:21
 */
public interface ShootingMemberView extends BaseView {

    //查找任务
    void FindTaskInfoResult(Object result);
    //查找队员
    void FindTaskPersonResult(Object result);
    //查找分数
    void FindTaskPersonScoreResult(Object result);
}
