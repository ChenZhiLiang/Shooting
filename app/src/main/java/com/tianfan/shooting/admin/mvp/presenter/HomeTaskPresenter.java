package com.tianfan.shooting.admin.mvp.presenter;

import com.alibaba.fastjson.JSONObject;
import com.tianfan.shooting.admin.mvp.model.HomeTaskService;
import com.tianfan.shooting.admin.mvp.view.IHomeService;
import com.tianfan.shooting.admin.mvp.view.VIewHomeFrg;


/**
 * @CreateBy liangxingfu
 * @Blog https://www.allenliang.cn/
 * @Email liangxingfu@boco.com.cn、allenliang1995@gmail.com
 * @CreateTime 2019-07-12 10:54
 * @Description 功能描述
 */
public class HomeTaskPresenter {
    VIewHomeFrg homeView;
    HomeTaskService homeservice = new HomeTaskService();

    public HomeTaskPresenter(VIewHomeFrg chooseTask) {
        this.homeView = chooseTask;
    }

    public void getUnDoData(String page) {
        homeservice.getUnDoTask(page, new IHomeService.HomeTaskResult() {
            @Override
            public void ok(String data) {
                homeView.ok(data);
            }

            @Override
            public void bullshit(String msg) {
                homeView.bullshit(msg);
            }
        });
    }

    public void getUnFinshData(String page) {
        homeservice.startGetFinish(page, new IHomeService.HomeTaskResult() {
            @Override
            public void ok(String data) {
                homeView.ok(data);
            }

            @Override
            public void bullshit(String msg) {
                homeView.bullshit(msg);
            }
        });
    }

    //开始射击任务
    public void startTask(String id) {
        homeservice.startTask(id, new IHomeService.HomeTaskResult() {
            @Override
            public void ok(String data) {
                homeView.startTask();
            }

            @Override
            public void bullshit(String msg) {
                homeView.bullshit(msg);
            }
        });
    }

    //结束整个射击任务
    public void endTask(String id) {
        homeservice.finshTask(id, new IHomeService.HomeTaskResult() {
            @Override
            public void ok(String data) {
                homeView.endTask();
            }

            @Override
            public void bullshit(String msg) {
                homeView.bullshit(msg);
            }
        });
    }

    //开始某一轮的射击
    public void startTimesTask(String timesID, String groupID,JSONObject jsonObject) {
            homeservice.startTimesTask(timesID, groupID,jsonObject, new IHomeService.HomeTaskResult() {
                @Override
                public void ok(String data) {
                    homeView.startTimesTask();
                }

                @Override
                public void bullshit(String msg) {
                    homeView.bullshit(msg);
                }
            });
    }

    //结束某一轮的射击
    public void endTimesTask(String timesID) {
        homeservice.endTimesTask(timesID, new IHomeService.HomeTaskResult() {
            @Override
            public void ok(String data) {
                homeView.endTimesTask();
            }

            @Override
            public void bullshit(String msg) {
                    homeView.bullshit(msg);
            }
        });
    }

}
