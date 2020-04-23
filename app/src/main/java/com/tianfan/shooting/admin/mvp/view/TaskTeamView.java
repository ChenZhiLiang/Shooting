package com.tianfan.shooting.admin.mvp.view;

import com.tianfan.shooting.base.BaseView;

/**
 * @Name：Shooting
 * @Description：任务队员
 * @Author：Chen
 * @Date：2020/4/11 11:14
 * 修改人：Chen
 * 修改时间：2020/4/11 11:14
 */
public interface TaskTeamView extends BaseView {
    /**
     *  @author
     *  @time
     *  @describe 查询
     */
    void FindTaskPersonResult(Object result);

    /**
     *  @author
     *  @time
     *  @describe 添加
     */
    void AddTaskPersonResult(Object result);

    /**
     *  @author
     *  @time
     *  @describe 修改
     */
    void EditTaskPersonResult(Object result);


    /**
     *  @author
     *  @time
     *  @describe 删除
     */
    void RemoveTaskPersonResult(Object result);


    /**
     *  @author
     *  @time
     *  @describe 上传头像
     */
    void UploadTaskPersonHeadResult(Object result);

    /**
     *  @author
     *  @time
     *  @describe 导入EXCEL
     */
    void ImportTaskPersonGroupResult(Object result);

    void SetTaskPersonStatusResult(Object result);

}
