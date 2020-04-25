package com.tianfan.shooting.network.api;

/**
 * Created by Administrator on 2017-11-02.
 * 对外接口url链接
 */

public final class ApiUrl {

    // dev = 0 测试 dev=1 生产
    public static final int dev = 0;
    public static final String HOST = dev == 0 ? "http://120.79.192.60:8181/" : "http://120.79.192.60:8181/";

    //头像
    public static final String HOST_HEAD = "http://120.79.192.60:8181/shot/task_person_head/";

    /**
     * 任务管理
     *
     * @author
     * @time
     * @describe
     */
    public static class TaskApi {
        //任务查询
        public static final String FindTaskInfo = HOST + "anb_shot/task/findTaskInfo.action";
        //任务添加
        public static final String AddTaskInfo = HOST + "anb_shot/task/addTaskInfo.action";
        //任务修改
        public static final String EditTaskInfo = HOST + "anb_shot/task/editTaskInfo.action";
        //任务删除
        public static final String RemoveTaskInfo = HOST + "anb_shot/task/removeTaskInfo.action";

    }

    /**
     * 队员/列队管理
     *
     * @author
     * @time
     * @describe
     */
    public static class TaskPersonApi {

        //队员管理-查询
        public static final String FindTaskPerson = HOST + "anb_shot/task/findTaskPerson.action";
        //队列查询
        public static final String FindTaskPersonRowcol = HOST+"anb_shot/task/findTaskPersonRowcol.action";
        //队员管理-添加
        public static final String AddTaskPerson = HOST + "anb_shot/task/addTaskPerson.action";

        //队员管理-修改
        public static final String EditTaskPerson = HOST + "anb_shot/task/editTaskPerson.action";

        //队员管理-删除 支持同时删除多个队员person_id用英文逗号,分隔
        public static final String RemoveTaskPerson = HOST + "anb_shot/task/removeTaskPerson.action";

        //队员管理-上传头像
        public static final String UploadTaskPersonHead = HOST + "anb_shot/task/uploadTaskPersonHead.action";

        //队员管理-导入EXCEL
        public static final String ImportTaskPerson = HOST + "anb_shot/task/importTaskPerson.action";

        //【队列管理-生成初始化】从队员管理中取数据，添加到队列管理
        public static final String CreateTaskPersonRowcol = HOST + "anb_shot/task/createTaskPersonRowcol.action";
        //【队列管理-调整队列】更新队员的分组及靶位
//        public static final String ChangeTaskPersonRowcol = HOST + "anb_shot/task/changeTaskPersonRowcol.action";
        public static final String ChangeTaskPersonRowcol = HOST + "anb_shot/task/exchangeTaskPersonRowcol.action";

    }


    /**
     *  器材管理
     *  @author
     *  @time
     *  @describe
     */
    public static class TaskEquipApi{

        //修改器材流程状态
        public static final String EditTaskInfo = HOST +"anb_shot/task/editTaskInfo.action";
        //查询任务器材
        public static final String FindTaskEquip = HOST + "anb_shot/task/findTaskEquip.action";

        //添加任务器材项
        public static final String AddTaskEquip = HOST+"anb_shot/task/addTaskEquip.action";

        //生成任务器材(先删除原任务器材数据，再通过器材模板生成新数据)
        public static final String CreateTaskEquip = HOST + "anb_shot/task/createTaskEquip.action";

        //【查询任务器材】设置调整后的数量（加减数量）
        public static final String ChangeTaskEquipCount = HOST + "anb_shot/task/changeTaskEquipCount.action";

    }

    /**
     *  器材模板管理
     *  @author
     *  @time
     *  @describe
     */
    public static class EquipApi{
        //查询器材模板类型
        public static final String FindEquipModelType = HOST+"anb_shot/equip/findEquipModelType.action";

        //添加器材模板类型
        public static final String AddEquipModelType = HOST+"anb_shot/equip/addEquipModelType.action";

        //修改器材模板类型
        public static final String EditEquipModelType = HOST+"anb_shot/equip/editEquipModelType.action";

        //删除器材模板类型
        public static final String RemoveEquipModelType = HOST+"anb_shot/equip/removeEquipModelType.action";

        //查询器材模板项目
        public static final String FindEquipModelItem = HOST+"anb_shot/equip/findEquipModelItem.action";

        //添加器材模板项目
        public static final String AddEquipModelItem = HOST+"anb_shot/equip/addEquipModelItem.action";

        //修改器材模板项目
        public static final String EditEquipModelItem = HOST+"anb_shot/equip/editEquipModelItem.action";

        //删除器材模板项目
        public static final String RemoveEquipModelItem = HOST+"anb_shot/equip/removeEquipModelItem.action";
    }

    /**
     * 分数
     *
     * @author
     * @time
     * @describe
     */
    public static class ScoreApi {
        //打靶记分（单人）】系统自动判断添加or修改
        public static final String RecordTaskPersonScore = HOST + "anb_shot/score/recordTaskPersonScore.action";

        //【打靶记分查询（单人）】
        public static final String FindTaskPersonScore = HOST + "anb_shot/score/findTaskPersonScore.action";

    }
}
